import pickle

from scipy.optimize import linprog
import matplotlib.pyplot as plt
import numpy as np
from cvxopt import matrix
from cvxopt.modeling import variable
import cvxopt.modeling as mdl
import cvxpy as cp
import math

E = np.array([[0.09322392, 0.85534588, 0.25612082, 0.94439766, 0.42633067,
               0.06536948, 0.97064134, 0.59605372, 0.73525182, 0.6842462],
              [0.59525336, 0.68711493, 0.03954775, 0.04914643, 0.65077308,
               0.6972211, 0.27565094, 0.14625013, 0.68853873, 0.77251991],
              [0.99642384, 0.75885728, 0.40030778, 0.99131035, 0.5572867,
               0.86532668, 0.68350056, 0.56119457, 0.23819451, 0.75646121],
              [0.36966957, 0.72678763, 0.03385621, 0.36965023, 0.13353987,
               0.84237176, 0.52036681, 0.79644833, 0.6651488, 0.67698353],
              [0.90387539, 0.06230747, 0.70200091, 0.7969424, 0.25194996,
               0.61642119, 0.06693794, 0.07618512, 0.69921153, 0.54750388],
              [0.86363595, 0.32146419, 0.0538844, 0.79550811, 0.05269641,
               0.74829417, 0.06550841, 0.13173606, 0.52734283, 0.7313928],
              [0.14138556, 0.1544018, 0.25522385, 0.61533455, 0.10194901,
               0.27765385, 0.17338946, 0.67337989, 0.66310896, 0.04249519],
              [0.11971362, 0.89440945, 0.65327154, 0.38979769, 0.17711902,
               0.98706249, 0.66220248, 0.08215935, 0.4150711, 0.93698716],
              [0.55060447, 0.04984511, 0.82146392, 0.06386616, 0.43599684,
               0.651046, 0.33011703, 0.14584935, 0.80864122, 0.59623953],
              [0.87751508, 0.87257602, 0.34282294, 0.16543855, 0.1490381,
               0.01083776, 0.11252519, 0.88497765, 0.89220508, 0.65476338],
              [0.88474709, 0.64899125, 0.4145622, 0.18785098, 0.87002669,
               0.11738859, 0.93081107, 0.06898975, 0.9947384, 0.90889475],
              [0.12673796, 0.50994447, 0.0393742, 0.82807936, 0.35737611,
               0.90229723, 0.80676721, 0.18839114, 0.34463645, 0.0203787],
              [0.82367711, 0.62758946, 0.04367413, 0.50509919, 0.61292846,
               0.12903638, 0.4805433, 0.2130376, 0.86437851, 0.966469],
              [0.92312281, 0.11720124, 0.89030006, 0.57245876, 0.53105455,
               0.78812732, 0.44469728, 0.89138532, 0.28709828, 0.86433261],
              [0.79873612, 0.81824918, 0.49721493, 0.67818938, 0.89710259,
               0.5383191, 0.74146958, 0.88833569, 0.49509096, 0.09571412]])  # Error matrix
number_of_encoders = len(E)  # number of encoders
n = len(E[0])  # number of channels
reward = np.array([.7, .6, .9, .7, .6, .9, .7, .6, .9, .7, .6, .9, .7, .6, .9])  # minimising in scipy
unknown_gamma = np.array([0.2049165, 0.04770875, 0.0470002, 0.0064904, 0.2276362, 0.11604395,
                          0.15613963, 0.01551385, 0.00297475, 0.17557578])
tol = [.2]  # tolerance min [.16]tol = [.2]  # tolerance min [.16]
bound = tol[0]
good_bound = tol[0]
bad_bound = 1
learn_rate = tol[0] / 10
taken = False
prev = 0
rateUP = False
rateUpCount = 0
sign = 1

timeslots = 500
lam_estimation_distance = []
rolling_sum_error = []
rolling_sum_reward = []

T = 0

lam_optimal = np.zeros(number_of_encoders)

numbers = np.ones(number_of_encoders * 2).reshape(2, number_of_encoders)


def project(v):  # https://eng.ucmerced.edu/people/wwang5/papers/SimplexProj.pdf
    u = np.sort(v)[::-1]  # sort the vector in descending order
    s = 0
    r = len(v)
    for i in range(len(v)):

        s = s + u[i]
        if u[i] + ((1 - s) / float(i + 1)) <= 0:
            r = i
            break
        i = i + 1
    g = (1 - sum(u[0:r])) / float(r)

    v = v + g
    v[v < 0] = 0
    return v


def simulate(lam, result_blowback):
    gamma = unknown_gamma
    i = np.random.choice(np.arange(len(lam)), p=lam)  # encoder
    #   print(i, '  Encoder selected')
    j = np.random.choice(np.arange(len(gamma)), p=gamma)  # channel by adversary
    #  print(j, '  Channel selected by adversary')
    result = np.random.choice([1, 0], p=[E[i, j], 1 - E[i, j]])
    result_blowback.append(result)
    # performing Bernoulli trial of transmission of packet ^
    # print(lam)
    numbers[result, i] = numbers[result, i] + 1
    if len(rolling_sum_error) == 0:
        rolling_sum_error.append(result)
    else:
        rolling_sum_error.append(rolling_sum_error[-1] + result)
    if len(rolling_sum_reward) == 0:
        rolling_sum_reward.append(reward[i] * lam[i])
    else:
        rolling_sum_reward.append(rolling_sum_reward[-1] + 1 * reward[i])


def grad(gam, result):
    tmp = np.copy(E)
    for i in range(number_of_encoders):
        div = np.dot(gam, E[i])
        for j in range(n):
            tmp[i, j] = numbers[1, i] * E[i, j] / float(div) - numbers[0, i] * E[i, j] / (1 - float(div))
    return tmp.sum(axis=0)


def innitialize_lam_optimal(gamma):
    global lam_optimal
    const = []  # store the constraints coefficient
    for i in range(number_of_encoders):
        const.append(np.dot(gamma, E[i]))
    lam_optimal = np.array(
        linprog(c=reward, A_ub=[const], b_ub=[[bound]], method='simplex', A_eq=[np.ones(number_of_encoders)],
                b_eq=[[1]]).x)


def solve_qcqp(gamma):
    beta = -10
    a = np.concatenate([reward, -2 * beta * np.array(unknown_gamma)])
    dim = a.shape[0]
    A = np.zeros(dim ** 2).reshape(dim, dim)
    A[dim - number_of_encoders:dim, dim - number_of_encoders:dim] = np.zeros(number_of_encoders ** 2).reshape(
        number_of_encoders, number_of_encoders)
    B = np.zeros(dim ** 2).reshape(dim, dim)
    B[0:E.shape[0], dim - E.shape[1]:dim] = E
    z = cp.Variable(dim)
    t = dim + 1
    Z = cp.Variable((dim + 1, dim + 1), PSD=True)

    # print(A.shape, z.shape, a.shape)
    objective = cp.Maximize(Z[:, -1][:dim].T @ a - 1 * beta * cp.trace(Z[0:dim, 0:dim] @ A))
    constraints = [cp.trace(Z[0:dim, 0:dim] @ B) <= tol[0], Z[dim, dim] == 1,
                   sum(Z[:, -1][:number_of_encoders]) == 1,
                   sum(Z[:, -1][number_of_encoders:number_of_encoders+n]) == 1, Z >> 0]

    for i in range(dim):
        constraints += [
            Z[:, -1][i] >= 0
        ]
    prob = cp.Problem(objective, constraints)
    result = prob.solve(cp.CVXOPT)
    print(project(Z.value[:, -1][number_of_encoders:number_of_encoders+n]))
    print(unknown_gamma)
    # print(project(Z.value[:, -1][:number_of_encoders]), 'gg')
    return project(Z.value[:, -1][:number_of_encoders])


def solve_qcqp1(gamma):
    z = np.zeros(number_of_encoders ** 2).reshape(number_of_encoders, number_of_encoders)
    v = np.concatenate([z, z])
    v = np.concatenate([z, z])
    v2 = np.concatenate([z, np.identity(n)])
    A = matrix(np.concatenate([v, v2], axis=1))

    v2 = np.concatenate([E, z])
    B = matrix(np.concatenate([v, v2], axis=1))
    beta = .51
    a = matrix(np.concatenate([reward, -2 * beta * np.array(gamma)]))
    z = variable(1, 'a')
    sci = variable(1, 'b')
    z = matrix(1, (2 * number_of_encoders, 1))
    # print(z.T * a)
    # print
    g = matrix([z.T * a, - beta * z.T * A * z])
    sci.value = z.T * B * z
    # print(sci[0])
    mdl.op(mdl.max(g), [(sci[0] <= 1)]).solve()

    N = 1


def descent():
    gam = np.ones(n) / float(n)  # randomly assumed gamma
    lambda_estimated = np.ones(number_of_encoders) / float(number_of_encoders)  # ranomly assumed l
    lambda_estimated = np.array([0, 1, 0])
    simulation_result = []
    for T in range(timeslots):
        simulate(lambda_estimated, simulation_result)
        s = np.zeros(n)
        for t in range(1, 200):
            g = grad(gam=gam, result=np.array(simulation_result))
            s = gam
            gam = gam + g / float(t + 10000000)  # t+1000000000
            gam = project(gam)
        lambda_estimated = solve_qcqp(gamma=gam)
        print(np.linalg.norm(lam_optimal - lambda_estimated), '- best lambda')
        lam_estimation_distance.append(np.linalg.norm(lam_optimal - lambda_estimated))


innitialize_lam_optimal(unknown_gamma)
descent()

fig = plt.figure()

t = np.arange(1, timeslots + 1)

desc = 'QCQP , with known gamma'
plot_data = {'time': t, 'error': rolling_sum_error / t, 'rate': rolling_sum_reward / t,
             'lamda': lam_estimation_distance, 'description': desc}
plt.plot(plot_data['time'], plot_data['error'], color='black', linewidth=2, markersize=2,
         label="Rolling Avg. Empirical Error")
plt.plot(plot_data['time'], plot_data['rate'], color='red', linewidth=2, markersize=12,
         label="Rolling Avg. Empirical Rate")
plt.plot(plot_data['time'], plot_data['lamda'])

plt.xlabel('T')
plt.ylabel('Avg. Number of Error; Rate*lam * 10 till now')

with open('QCQP2.pickle', 'wb') as handle:
    pickle.dump(plot_data, handle, protocol=pickle.HIGHEST_PROTOCOL)

plt.legend(loc="upper right")

plt.show()
