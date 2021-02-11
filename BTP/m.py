import numpy as np
from scipy.optimize import linprog
import matplotlib.pyplot as plt

import numpy as np

packets = 100  # number of packets to transmit
E = np.array([[.34, .003, .34, .3], [.4, .4, .33, .2], [.3, .36, .3, .2], [.01, .3, .01, .01],
              [.03, .003, .1, .03]])  # Error matrix
h = len(E)  # number of encoders
n = len(E[0])  # number of channels
R = -1 * np.array([.9, .8, .7, .3, .4])  # minimising in scipy
real_gamma = np.array([0, .9, .1, 0])
tol = .01  # tolerance
timeslots = 100

emp_err = []
emp_rate = []
graph_rate_scale_up = 10

m0 = 0
m1 = 0


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


def simulate(lam):
    gamma = real_gamma
    i = np.random.choice(np.arange(len(lam)), p=lam)  # encoder
    print(i, '  Encoder selected')
    emp_rate.append(-1 * R[i] * graph_rate_scale_up)
    j = np.random.choice(np.arange(len(gamma)), p=gamma)  # channel by adversary
    print(j, '  Channel selected by adversary')
    trans = []  # records if communication has error(1) or is error free (0).
    for k in range(packets):
        trans.append(np.random.choice([1, 0], p=[E[i, j], 1 - E[i, j]]))
        # performing Bernoulli trial of transmission of packet ^
    return np.array(trans)


def grad(gam, result):
    one = result.sum(axis=0)
    zero = packets - one
    tmp = np.copy(E)
    for i in range(h):
        div = np.dot(gam, E[i])
        for j in range(n):
            tmp[i, j] = one * E[i, j] / float(div) - zero * E[i, j] / (1 - float(div))
    return tmp.sum(axis=0)


def solve_lp(gamma):
    const = []  # store the constraints coefficient
    b = []
    for i in range(h):
        const.append(np.dot(gamma, E[i]))
        b.append((0, float("inf")))
    opt = linprog(c=R, A_ub=[const], b_ub=[[tol]], A_eq=[np.ones(h)], b_eq=[[1]], method="simplex")
    print(project(opt.x))
    return project(opt.x)


def descent(gamma):
    gam = np.ones(n) / float(n)  # randomly assumed gamma

    lam_assumed = np.ones(h) / float(h)  # ranomly assumed l
    for T in range(timeslots):
        res = simulate(lam=lam_assumed)
        emp_err.append(res.sum(axis=0))
        s = np.zeros(n)
        for t in range(1, 100):
            # print(grad(gam=gam, result=res))
            g = grad(gam=gam, result=res)
            s = gam
            gam = gam + g * float(t * 10000000)
            gam = project(gam)

            if np.linalg.norm(gam) > 1:
                pass
                # gamma=gamma/np.linalg.norm(gamma)

        print(gam)
        lam_assumed = solve_lp(gamma=gam)
        print(lam_assumed)


project(np.array([-9.56476832, -7.07240545, -14.01279326]))
descent(gamma=project(np.array([.2, .2, .34])))
# project(np.array([.2, .2, .34, .5]))
# simulate(gamma=project(np.array([.2, .2, .34])), lam=np.ones(n) / float(n))
# simulate(gamma=np.array([.1, .5, .4]), lam=np.ones(n) / float(n))
plt.plot(range(1, timeslots + 1), emp_err, color='black', linewidth=2, markersize=2, label="Empirical Error")
plt.plot(range(1, timeslots + 1), emp_rate, color='red', linewidth=2, markersize=12, label="Empirical Rate")
plt.xlabel('T')
plt.ylabel('Number of Error; Rate * 10')
plt.legend(loc="upper right")
plt.show()
