import random as rd

class alphaBetaAlvis(AlgorithmBase):


	def set_leave_nodes(self,leaves):
		for le in leaves:
			self.set_node_attribute(le,'leaf_value',rd.randint(1,100))

	def set_pruned_nodes(self,i,node_list,pruned):
		for j in range(i,len(node_list)):
			node = node_list[j]
			pruned.append(node)
			children = []
			for i in self.neighbors(node):
				if i > node:
					children.append(i)
					self.set_parent(i,node)
			self.set_pruned_nodes(0,children,pruned)

	def alphaBeta(self,node,alpha,beta,depth,opened,closed,pruned,child):
		children = []
		for i in self.neighbors(node):
			if i > node:
				children.append(i)
		if not children:
			self.alg_iteration_start()
			opened.pop()
			closed.append(node)
			self.alg_iteration_end()
			return self.get_node_attribute(node,'leaf_value')

		if depth%2 == 0:
			for i in range(len(children)):
				c = children[i]
				self.alg_iteration_start()
				self.set_parent(c,node)
				opened.append(c)
				self.alg_iteration_end()
				maxi = self.alphaBeta(c,alpha,beta,depth+1,opened,closed,pruned,child)
				if maxi > alpha:
					alpha = maxi
					child[node] = c
				if alpha >= beta:
					self.alg_iteration_start()
					self.set_pruned_nodes(i+1,children,pruned)
					opened.pop()
					closed.append(node)
					self.alg_iteration_end()
					self.set_node_attribute(node,'alpha_value',beta)
					return beta
			self.alg_iteration_start()
			opened.pop()
			closed.append(node)
			self.alg_iteration_end()
			self.set_node_attribute(node,'alpha_value',alpha)
			return alpha

		if depth%2 != 0:
			for i in range(len(children)):
				c = children[i]
				self.alg_iteration_start()
				self.set_parent(c,node)
				opened.append(c)
				self.alg_iteration_end()
				mini = self.alphaBeta(c,alpha,beta,depth+1,opened,closed,pruned,child)
				if mini < beta:
					beta = mini
					child[node] = c
				if alpha >= beta:
					self.alg_iteration_start()
					self.set_pruned_nodes(i+1,children,pruned)
					opened.pop()
					closed.append(node)
					self.alg_iteration_end()
					self.set_node_attribute(node,'beta_value',alpha)
					return alpha
			self.alg_iteration_start()
			opened.pop()
			closed.append(node)
			self.alg_iteration_end()
			self.set_node_attribute(node,'beta_value',beta)
			return beta

		return -1



	
	def execute(self):

		nodes = self.get_nodes()
		leaves = []; l = len(nodes)
		for n in nodes:
			if len(self.neighbors(n)) == 1:
				leaves.append(n)
		self.set_leave_nodes(leaves)

		alpha = float('-inf')
		beta  = float('inf')
		opened = self.get_list('open')
		closed = self.get_list('closed')
		pruned = self.get_list('prune')
		child = [-1]*l

		opened.append(0)
		alpha = self.alphaBeta(0,alpha,beta,0,opened,closed,pruned,child)
		
		path = [0]
		while child[path[-1]] != -1:
			path.append(child[path[-1]])
		self.show_path(path)


   