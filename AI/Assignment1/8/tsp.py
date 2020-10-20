import fileinput
import random
import sys
import heapq
import math
import copy
import time
start_time = 0
dis_type = "" 
num_cities = 0
coord = []
dist = []
def closest_neighbour(i,visited):
	m = sys.maxsize
	mind = -1
	for j in range(0,len(dist[i])):
		if j not in visited.keys() and m > dist[i][j]:
			m = dist[i][j]
			mind = j
	return mind
def nearest_neighbour_heuristic():
	visited = {}
	tour = []
	#random
	start = random.randint(0,num_cities-1)
	visited[start] = True
	tour_length = 0
	node = start
	while(1):
		tour.append(node)
		visited[node] = True
		cn = closest_neighbour(node,visited)
		if cn == -1:
			tour_length += dist[node][start]
			break
		else:
			tour_length += dist[node][cn]
			node = cn 
	return tour,tour_length
def nn(st):
	visited = {}
	tour = []
	#random
	start = st
	visited[start] = True
	tour_length = 0
	node = start
	while(1):
		tour.append(node)
		visited[node] = True
		cn = closest_neighbour(node,visited)
		if cn == -1:
			tour_length += dist[node][start]
			break
		else:
			tour_length += dist[node][cn]
			node = cn 
	return tour,tour_length
def dfs(edge_tour,a,na,vis):
	if a in vis.keys():
		return
	vis[a]= True
	na+=1
	l = len(edge_tour[a])
	if l>0:
		dfs(edge_tour,edge_tour[a][0],na,vis)
	if l>1:
		dfs(edge_tour,edge_tour[a][1],na,vis)
def check_valid(edge_tour,a,b):
	if len(edge_tour[a])==2 or len(edge_tour[b])==2:
		return False
	na = 0
	if len(edge_tour[a])==0 or len(edge_tour[b])==0:
		return True
	vis = {}
	dfs(edge_tour,a,na,vis)
	if not b in vis.keys():
		return True
	else:
		return False
def greedy_heuristic():
	edges = []
	visited = {}
	for i in range(num_cities):
		visited[i] = 0 
		for j in range(i+1,num_cities):
			edges.append((dist[i][j],i,j))
	edges.sort()
	tour_length = 0
	edge_tour = [[] for i in range(num_cities)]
	for i in range(len(edges)):
		if (check_valid(edge_tour,edges[i][1],edges[i][2])):
			# print(i,edge_tour[i],j,edge_tour[j])
			# print(edges[i][1],edges[i][2])
			# if(edges[i][1]==edges[i][2]):
				# print("hey")
			tour_length += edges[i][0]
			edge_tour[edges[i][1]].append(edges[i][2])
			edge_tour[edges[i][2]].append(edges[i][1])
	start = -1
	for i in range(len(visited)):
		if len(edge_tour[i])==1:
			start = i
		visited[i]=2
	# start = random.randint(0,num_cities-1)
	tour = []
	node = start
	while(1):
		tour.append(node)
		# print(node)
		visited[node]=0
		tnode = node
		# print(visited[edge_tour[node][0]],visited[edge_tour[node][1]])
		# print(edge_tour[node])
		for i in range(len(edge_tour[node])):
			if visited[edge_tour[node][i]]==2 :
				node = edge_tour[node][i] 
				break
		if node==tnode:
			tour_length += dist[start][node]
			break
	return tour,len(tour),tour_length
def check_valid_savings(edge_tour,a,b):
	# if len(edge_tour[a])==2 or len(edge_tour[b])==2:
	# 	return False
	# na = 0
	# if len(edge_tour[a])==0 or len(edge_tour[b])==0:
	# 	return True
	na = 0
	vis = {}
	dfs(edge_tour,a,na,vis)
	# print(a,b,vis)
	if not b in vis.keys():
		return True
	else:
		return False
def savings_heuristic():
	start = random.randint(0,num_cities-1)
	# start = 0
	visited = {}
	edges = []
	cost = 0
	for i in range(num_cities):
		visited[i] = 2
		if i!=start:
			cost+= 2*(dist[i][start])
		for j in range(i+1,num_cities):
			if i!=start and j!=start:
				edges.append((dist[i][j]-(dist[start][i]+dist[start][j]),i,j))
	heapq.heapify(edges)
	edge_tour = [[] for i in range(num_cities)]
	num_add = 0
	while(len(edges)!=0):
		p = heapq.heappop(edges)
		if visited[p[1]] >= 1 and visited[p[2]] >= 1 and check_valid_savings(edge_tour,p[1],p[2]):
			visited[p[1]] -=1
			visited[p[2]] -=1
			cost -= abs(p[0])
			edge_tour[p[1]].append(p[2])
			edge_tour[p[2]].append(p[1])
			num_add+=1
		if num_add == num_cities-2:
			break
	# for i in range(0,num_cities):
	# 	print(i,edge_tour[i])
	for i in range(0,num_cities):
		# print(i,edge_tour[i])
		visited[i]=0
		if i!=start and len(edge_tour[i])==1:
			edge_tour[i].append(start)
			edge_tour[start].append(i)
	# for i in range(0,num_cities):
	# 	print(i,len(edge_tour[i]))
	tour = []
	node = start
	tour_length = 0
	while(1):
		tour.append(node)
		# print(node)
		visited[node]=2
		tnode = node
		# print(edge_tour[node])
		for i in range(len(edge_tour[node])):
			if visited[edge_tour[node][i]]==0:
				node = edge_tour[node][i] 
				break
		if node==tnode:
			tour_length += dist[start][node]
			break
		else:
			tour_length += dist[tnode][node]
	return tour,len(tour),tour_length,cost
def swap(solution, x, y):
	return solution[:x] + solution[x:y + 1][::-1] + solution[y + 1:]
def swap_city(solution, x, y):
	temp = solution[y]
	solution[y] = solution[x]
	solution[x] = temp
	return solution

def compute_cost(tour):
	start = tour[0]
	cost = 0
	for i in range(num_cities-1):
		cost += dist[tour[i+1]][tour[i]]
	cost+= dist[tour[0]][tour[num_cities-1]]
	return cost
def two_edges(tour):
    solution = tour
    stable, best = False, compute_cost(solution)
    lengths, tours = [best], [solution]
    while not stable:
        stable = True
        for i in range(1, num_cities):
            for j in range(i + 1, num_cities):
                candidate = swap(solution, i, j)
                length_candidate = compute_cost(candidate)
                if best > length_candidate:
                    solution, best = candidate, length_candidate
                    tours.append(solution)
                    lengths.append(best)
                    # print(best)
                    stable = False
    return tours[-1],lengths[-1]
        # return [self.format_solution(step) for step in tours], lengths
def two_city(tour):
    solution = tour
    stable, best = False, compute_cost(solution)
    lengths, tours = [best], [solution]
    while not stable:
        stable = True
        for i in range(1, num_cities):
            for j in range(i + 1, num_cities):
                candidate = swap_city(solution, i, j)
                length_candidate = compute_cost(candidate)
                if best > length_candidate:
                    solution, best = candidate, length_candidate
                    tours.append(solution)
                    lengths.append(best)
                    # print(best)
                    stable = False
    return tours[-1],lengths[-1]
class GeneticAlgo():
	def __init__(self,num_cities,population_size,steps,k,cross_prob,mutate_prob):
		self.genes = []
		self.cross_prob = cross_prob 
		self.mutate_prob = mutate_prob
		self.steps = steps
		self.population_size = population_size
		self.k = k 
		self.num_cities = num_cities
	def generate_genes(self):
		prob = 0
		for i in range(self.population_size):
			gene =[]
			if random.random() < prob:
				st = random.randint(0,self.num_cities-1)
				gene = nn(st)[0]
			else:
				options = [k for k in range(self.num_cities)]
				while len(gene) < self.num_cities:
					# print(options)
					city = random.choice(options)
					loc = options.index(city)
					gene.append(city)
					del options[loc]
			self.genes.append(gene)
			print(i,gene,self.fitness(gene))
		return self.genes
	def fitness(self,tour):
		# print(tour)
		start = tour[0]
		cost = 0.0
		for i in range(num_cities-1):
			cost += dist[tour[i+1]][tour[i]]
		cost+= dist[tour[0]][tour[num_cities-1]]
		return cost
	def select(self):
		fitness_r = []
		su = 0.0
		for i in range(len(self.genes)):
			b = 1.0/self.fitness(self.genes[i])
			su += b
			fitness_r.append(b)
		new = []
		num = len(self.genes)
		for i in range(len(self.genes)):
			c = int(num * (fitness_r[i]/su) )
			for j in range(c):
				new.append(copy.deepcopy(self.genes[i]))
			while len(new) < num:
				k = random.randint(0,num)
				new.append(copy.deepcopy(self.genes[i]))
		return new
	def crossover_cut(self):
		first_cut = random.randint(0, self.num_cities - 1)
		return first_cut, random.randint(first_cut + 1, self.num_cities)
	def order_crossover(self, i1, i2):
		a, b = self.crossover_cut()
		ni1, ni2, i1, i2 = i1[a:b], i2[a:b], i1[b:] + i1[:b], i2[b:] + i2[:b]
		for x in i1:
			if x in ni2:
				continue
			ni2.append(x)
		for x in i2:
			if x in ni1:
				continue
			ni1.append(x)
		return ni1, ni2
	def partial_mapping(self, i1, i2, ni1, ni2, a, b):
		for x in i2[a:b]:
			if x in i1[a:b]:
				continue
			curr = x
			print("i1",i1)
			print("i2",i2)
			while True:
				index_curr = i2.index(curr)
				j = i1[index_curr]
				if not ni1[i2.index(j)]:
					ni1[i2.index(j)] = x
					break
				else:
					curr = ni2[i2.index(j)]
	def partially_mapped_crossover(self, i1, i2):
		a, b = self.crossover_cut()
		ni1, ni2 = [0] * self.num_cities, [0] * self.num_cities
		ni1[a:b], ni2[a:b] = i1[a:b], i2[a:b]
		self.partial_mapping(i1, i2, ni1, ni2, a, b)
		self.partial_mapping(i2, i1, ni2, ni1, a, b)
		ni1 = [x if x else i2[i] for i, x in enumerate(ni1)]
		ni2 = [x if x else i1[i] for i, x in enumerate(ni2)]
		return ni1, ni2
	def cross(self,a,b):
		return self.partially_mapped_crossover(a,b)
	def crossover(self,generation):
		next_gen = []
		i = 0
		j = 1
		while(i < len(generation) and j<len(generation)):
			if random.random() < self.cross_prob:
				next_gen.extend(self.cross(generation[i],generation[j]))
			else:
				next_gen.append(copy.deepcopy(generation[i]))
				next_gen.append(copy.deepcopy(generation[j]))
			i+=2
			j+=2
		if i < len(generation):
			next_gen.append(copy.deepcopy(generation[i]))
		if j < len(generation):
			next_gen.append(copy.deepcopy(generation[j]))
		generation.clear()
		return next_gen
	def swap_mutation(self, gen):
		i, j = random.randrange(self.num_cities), random.randrange(self.num_cities)
		gen[i], gen[j] = gen[j], gen[i]
		return gen
	def insertion_mutation(self, gen):
		random_city, random_position = random.randrange(self.num_cities), random.randrange(self.num_cities)
		city = gen.pop(random_city)
		gen.insert(random_position, city)
		return gen
	def mutation(self,gens):
		for i in range(len(gens)):
			if random.random() < self.mutate_prob:
				gens[i] = self.swap_mutation(gens[i])
		return gens
	def cycle(self,k):
		#Selection
		generation = self.select()
		next_gen = self.crossover(generation)
		final = self.mutation(next_gen)
		self.genes = sorted(self.genes,key = lambda x : self.fitness(x))
		final = sorted(final,key = lambda x : self.fitness(x))
		i = len(self.genes)-1
		j = 0
		c = 0
		while c < k:
			self.genes[i] = final[j]
			i-=1
			j+=1
			c+=1
		self.genes = sorted(self.genes,key = lambda x : self.fitness(x))
	def letsgo(self):
		self.genes = self.generate_genes()
		for i in range(self.steps):
			self.cycle(self.k)
			print(i,self.fitness(self.genes[0]))
if __name__ == "__main__":
	start_time = time.time()
	dis_type = str(input())
	num_cities = int(input())
	for i in range(num_cities):
		a = []
		b = input().split(' ')
		a.append(float(b[0]))
		a.append(float(b[1]))
		coord.append(a)
	for i in range(num_cities):
		a = []
		b = input().split(' ')
		for j in range(num_cities):
			a.append(float(b[j]))
		dist.append(a)
	# print(dist)
	# nntour,nncost= nearest_neighbour_heuristic()
	grtour,_,grcost = greedy_heuristic()
	# savtour,_,savcost,_ = savings_heuristic()
	# print("Nearest Neighbour Heuristic",nntour,compute_cost(nntour))
	# print("Nearest Neighbour Heuristic with 2-city-exchange",two_city(nntour))
	# print("Nearest Neighbour Heuristic with 2-edge-exchange",two_edges(nntour))
	# if time.time() - start_time >= 300:
	# 	exit(0)
	# print("Greedy Heuristic",grtour,compute_cost(grtour))
	# print("Greedy Heuristic with 2-city-exchange",two_city(grtour))
	L = two_edges(grtour)
	# print(len(L[0]))
	for i in range(len(L[0])):
		print(L[0][i],end = " ");
	print()
	# print(L[1])
	# print(time.time()-start_time);
	# if time.time() - start_time >= 300:
	# 	exit(0)
	# print("Savings Heuristic",savtour,compute_cost(savtour))
	# print("Savings Heuristic with 2-city-exchange",two_city(savtour))
	# print("Savings Heuristic with 2-edge-exchange",two_edges(savtour))
	# print(time.time()-start_time)
	# g = GeneticAlgo(num_cities = num_cities,population_size = int(0.8*num_cities),steps = 10000 ,k = int(0.4*num_cities),cross_prob = 0.6,mutate_prob = 0.3)
	# g.letsgo()








# def displacement_mutation(self, solution):
    #     a, b = self.crossover_cut()
    #     random_position = randint(0, self.size)
    #     substring = solution[a:b]
    #     solution = solution[:a] + solution[b:]
    #     return solution[:random_position] + substring + solution[random_position:]

# def maximal_preservative_crossover(self, i1, i2):
    #     c = len(i1) // 2
    #     r = randrange(self.size + 1)
    #     s1, s2 = (i1 * 2)[r:r + c], (i2 * 2)[r:r + c]
    #     for x in s1:
    #         i2.remove(x)
    #     for x in s2:
    #         i1.remove(x)
    #     ni1, ni2 = s2 + i1, s1 + i2
    #     return ni1, ni2






