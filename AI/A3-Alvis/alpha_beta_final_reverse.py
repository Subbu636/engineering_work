# import
import random as rd

# code
class alphaBetaAlvis(AlgorithmBase):


	def set_leave_nodes(self,leaves): # sets the value of leaves to random heuristic values
		for le in leaves:
			self.set_node_attribute(le,'leaf_value',rd.randint(1,100))

	def set_pruned_nodes(self,i,node_list,pruned): # colours pruned nodes to blue
		for j in range(i,len(node_list)):
			node = node_list[j]
			pruned.append(node)
			children = []
			for i in self.neighbors(node):
				if i > node:
					children.append(i)
					self.set_parent(i,node)
			children.sort()
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
			self.show_info(str(node)+':leafValue:'+str(self.get_node_attribute(node,'leaf_value')))
			return self.get_node_attribute(node,'leaf_value')
		children.sort() 
		children = children[::-1] # right to left evaluation of nodes
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
				if alpha >= beta: # beta cut-off
					self.alg_iteration_start()
					self.set_pruned_nodes(i+1,children,pruned)
					opened.pop()
					closed.append(node)
					self.alg_iteration_end()
					self.set_node_attribute(node,'alpha_value',beta)
					self.show_info('betaCutoff')
					self.show_info(str(node)+':alphaValue:'+str(beta))
					return beta
			self.alg_iteration_start()
			opened.pop()
			closed.append(node)
			self.alg_iteration_end()
			self.show_info(str(node)+':alphaValue:'+str(alpha))
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
				if alpha >= beta: # alpha cut-off
					self.alg_iteration_start()
					self.set_pruned_nodes(i+1,children,pruned)
					opened.pop()
					closed.append(node)
					self.alg_iteration_end()
					self.set_node_attribute(node,'beta_value',alpha)
					self.show_info('alphaCutoff')
					self.show_info(str(node)+':betaValue:'+str(alpha))
					return alpha
			self.alg_iteration_start()
			opened.pop()
			closed.append(node)
			self.alg_iteration_end()
			self.set_node_attribute(node,'beta_value',beta)
			self.show_info(str(node)+':betaValue:'+str(beta))
			return beta
		return -1

	def minimax(self,depth,node): # MinMax algorithm for verfication 
		children = []
		for i in self.neighbors(node):
			if i > node:
				children.append(i)
		if not children:
			return self.get_node_attribute(node,'leaf_value')
		if depth%2==0:
			m = float('-inf')
			for i in range(len(children)):
				m = max(m,self.minimax(depth+1,children[i]))
			return m
		if depth%2 !=0:
			m = float('inf')
			for i in range(len(children)):
				m = min(m,self.minimax(depth+1,children[i]))
			return m
		return -1

	def verify(self,val):
		gameval = self.minimax(0,0)
		return gameval == val

	def execute(self):
		nodes = self.get_nodes()
		leaves = []; l = len(nodes)
		for n in nodes:
			if len(self.neighbors(n)) == 1:
				leaves.append(n)
		self.set_leave_nodes(leaves)

		alpha = float('-inf')
		beta  = float('inf')
		opened = self.get_list('open') # red
		closed = self.get_list('closed') # green
		pruned = self.get_list('prune') # light blue
		child = [-1]*l

		opened.append(0)
		alpha = self.alphaBeta(0,alpha,beta,0,opened,closed,pruned,child)

		path = [0] # contains path of final min-max value
		while child[path[-1]] != -1:
			path.append(child[path[-1]])
		self.show_path(path)

		if self.verify(alpha):
			self.show_info('Verified with MinMax')
		else:
			self.show_info('Game Value doesnt Match')


   