from util import *

# Add your import statements here




class InformationRetrieval():

	def __init__(self):
		self.index = None

	def buildIndex(self, docs, docIDs):
		"""
		Builds the document index in terms of the document
		IDs and stores it in the 'index' class variable

		Parameters
		----------
		arg1 : list
			A list of lists of lists where each sub-list is
			a document and each sub-sub-list is a sentence of the document
		arg2 : list
			A list of integers denoting IDs of the documents
		Returns
		-------
		None
		"""
		self.terms_list = set()
		self.term_doc_freq = {}
		self.index = {}
		self.num_docs = len(docs)
		self.doc_len = {}
		self.doc_id = docIDs.copy()
		doc_terms = {}
		for i in range(self.num_docs):
			doc_terms[docIDs[i]] = []
			for sentence in docs[i]:
				for term in sentence:
					if term not in self.terms_list:
						self.terms_list.add(term)
					if self.index.get((term, docIDs[i]),0.0) == 0.0:
						doc_terms[docIDs[i]].append(term)
					self.index[(term, docIDs[i])] = self.index.get((term,docIDs[i]),0.0)+1.0
		for term in self.terms_list:
			for id in docIDs:
				if self.index.get((term,id),0.0) != 0.0:
					self.term_doc_freq[term] = 1.0+self.term_doc_freq.get(term,0.0)
		for k in self.index.keys():
			self.index[k] = self.index[k]*math.log10(self.num_docs/(self.term_doc_freq.get(k[0],0.0)+1.0))
		for id in docIDs:
			v = 0.0
			for term in doc_terms[id]:
				v += (math.pow(self.index.get((term,id),0.0),2.0))
			self.doc_len[id] = math.sqrt(v)
		# print(list(self.doc_len.values())[:4])
		# print(list(self.index.keys())[:4],list(self.index.values())[:4])
		return


	def rank(self, queries):
		"""
		Rank the documents according to relevance for each query

		Parameters
		----------
		arg1 : list
			A list of lists of lists where each sub-list is a query and
			each sub-sub-list is a sentence of the query
		

		Returns
		-------
		list
			A list of lists of integers where the ith sub-list is a list of IDs
			of documents in their predicted order of relevance to the ith query
		"""

		doc_IDs_ordered = []
		query_dic = {}
		query_len = {}
		query_terms = [[] for i in range(len(queries))]
		for i in range(len(queries)):
			for sentence in queries[i]:
				for term in sentence:
					if query_dic.get((term, i),0.0) == 0.0:
						query_terms[i].append(term)
					query_dic[(term, i)] = query_dic.get((term, i),0.0)+1.0
		for k in query_dic.keys():
			query_dic[k] = query_dic[k]*math.log10(self.num_docs/(self.term_doc_freq.get(k[0],0.0)+1.0))
		for id in range(len(queries)):
			v = 0.0
			for term in self.terms_list:
				v += (math.pow(query_dic.get((term,id),0.0),2.0))
			query_len[id] = math.sqrt(v)
		for i in range(len(queries)):
			buff = []
			for d in self.doc_id:
				if self.doc_len[d] == 0.0:
					buff.append((0.0,d))
					continue
				dot = 0.0
				for term in query_terms[i]:
					dot += (query_dic.get((term,i),0.0)*self.index.get((term,d),0.0))
				buff.append((dot/(query_len[i]*self.doc_len[d]),d))
			buff.sort(reverse=True)
			doc_IDs_ordered.append([i[1] for i in buff])
		return doc_IDs_ordered




