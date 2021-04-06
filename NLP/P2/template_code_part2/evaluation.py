from util import *
import math
# Add your import statements here




class Evaluation():

	def queryPrecision(self, query_doc_IDs_ordered, query_id, true_doc_IDs, k):
		return -1
		"""
		Computation of precision of the Information Retrieval System
		at a given value of k for a single query

		Parameters
		----------
		arg1 : list
			A list of integers denoting the IDs of documents in
			their predicted order of relevance to a query
		arg2 : int
			The ID of the query in question
		arg3 : list
			The list of IDs of documents relevant to the query (ground truth)
		arg4 : int
			The k value

		Returns
		-------
		float
			The precision value as a number between 0 and 1
		"""

		precision = 0.0
		num = 0.0
		for i in range(k):
			if query_doc_IDs_ordered[i] in true_doc_IDs:
				num+=1


		#Fill in code here
		precision = num/k
		return precision


	def meanPrecision(self, doc_IDs_ordered, query_ids, qrels, k):
		return -1
		"""
		Computation of precision of the Information Retrieval System
		at a given value of k, averaged over all the queries

		Parameters
		----------
		arg1 : list
			A list of lists of integers where the ith sub-list is a list of IDs
			of documents in their predicted order of relevance to the ith query
		arg2 : list
			A list of IDs of the queries for which the documents are ordered
		arg3 : list
			A list of dictionaries containing document-relevance
			judgements - Refer cran_qrels.json for the structure of each
			dictionary
		arg4 : int
			The k value

		Returns
		-------
		float
			The mean precision value as a number between 0 and 1
		"""

		meanPrecision = 0.0
		d = {}
		for q in query_ids:
			d[q] = set() 
		# print(e["query_num"])
		# print(e["id"])
		for e in qrels:
			if d.get(e["query_num"],None):
				d[e["query_num"]].add(e["id"])

		#Fill in code here
		for i in range(len(query_ids)):
			meanPrecision += self.queryPrecision(doc_IDs_ordered[i],query_ids[i],d[query_ids[i]],k)
		meanPrecision /= len(query_ids)
		return meanPrecision

	
	def queryRecall(self, query_doc_IDs_ordered, query_id, true_doc_IDs, k):
		return -1
		"""
		Computation of recall of the Information Retrieval System
		at a given value of k for a single query

		Parameters
		----------
		arg1 : list
			A list of integers denoting the IDs of documents in
			their predicted order of relevance to a query
		arg2 : int
			The ID of the query in question
		arg3 : list
			The list of IDs of documents relevant to the query (ground truth)
		arg4 : int
			The k value

		Returns
		-------
		float
			The recall value as a number between 0 and 1
		"""

		recall = 0.0
		num = 0.0
		for i in range(k):
			if query_doc_IDs_ordered[i] in true_doc_IDs:
				num+=1
		#Fill in code here
		num_relev_docs = len(true_doc_IDs)
		recall = num/num_relev_docs
		return recall


	def meanRecall(self, doc_IDs_ordered, query_ids, qrels, k):
		return -1
		"""
		Computation of recall of the Information Retrieval System
		at a given value of k, averaged over all the queries

		Parameters
		----------
		arg1 : list
			A list of lists of integers where the ith sub-list is a list of IDs
			of documents in their predicted order of relevance to the ith query
		arg2 : list
			A list of IDs of the queries for which the documents are ordered
		arg3 : list
			A list of dictionaries containing document-relevance
			judgements - Refer cran_qrels.json for the structure of each
			dictionary
		arg4 : int
			The k value

		Returns
		-------
		float
			The mean recall value as a number between 0 and 1
		"""

		meanRecall = 0.0
		d = {}
		for q in query_ids:
			d[q] = set() 
		for e in qrels:
			if d.get(e["query_num"],None):
				d[e["query_num"]].add(e["id"])

		#Fill in code here
		for i in range(len(query_ids)):
			meanRecall += self.queryRecall(doc_IDs_ordered[i],query_ids[i],d[query_ids[i]],k)
		meanRecall /= len(query_ids)

		return meanRecall


	def queryFscore(self, query_doc_IDs_ordered, query_id, true_doc_IDs, k):
		return -1
		"""
		Computation of fscore of the Information Retrieval System
		at a given value of k for a single query

		Parameters
		----------
		arg1 : list
			A list of integers denoting the IDs of documents in
			their predicted order of relevance to a query
		arg2 : int
			The ID of the query in question
		arg3 : list
			The list of IDs of documents relevant to the query (ground truth)
		arg4 : int
			The k value

		Returns
		-------
		float
			The fscore value as a number between 0 and 1
		"""

		fscore = 0.0
		precision = 0.0
		recall = 0.0
		num = 0.0
		for i in range(k):
			if query_doc_IDs_ordered[i] in true_doc_IDs:
				num+=1
		#Fill in code here
		num_relev_docs = len(true_doc_IDs)
		recall = num/num_relev_docs
		precision = num/k
		#Fill in code here
		fscore = (2*precision*recall) / (precision + recall)
		return fscore


	def meanFscore(self, doc_IDs_ordered, query_ids, qrels, k):
		return -1
		"""
		Computation of fscore of the Information Retrieval System
		at a given value of k, averaged over all the queries

		Parameters
		----------
		arg1 : list
			A list of lists of integers where the ith sub-list is a list of IDs
			of documents in their predicted order of relevance to the ith query
		arg2 : list
			A list of IDs of the queries for which the documents are ordered
		arg3 : list
			A list of dictionaries containing document-relevance
			judgements - Refer cran_qrels.json for the structure of each
			dictionary
		arg4 : int
			The k value
		
		Returns
		-------
		float
			The mean fscore value as a number between 0 and 1
		"""

		meanFscore = 0.0
		d = {}
		for q in query_ids:
			d[q] = set() 
		for e in qrels:
			if d.get(e["query_num"],None):
				d[e["query_num"]].add(e["id"])

		#Fill in code here
		for i in range(len(query_ids)):
			meanFscore += self.queryFscore(doc_IDs_ordered[i],query_ids[i],d[query_ids[i]],k)
		meanFscore /= len(query_ids)
		#Fill in code here

		return meanFscore
	

	def queryNDCG(self, query_doc_IDs_ordered, query_id, true_doc_IDs, k):
		return -1
		"""
		Computation of nDCG of the Information Retrieval System
		at given value of k for a single query

		Parameters
		----------
		arg1 : list
			A list of integers denoting the IDs of documents in
			their predicted order of relevance to a query
		arg2 : int
			The ID of the query in question
		arg3 : list (Dict changed)
			The list of IDs of documents relevant to the query (ground truth)
		arg4 : int
			The k value

		Returns
		-------
		float
			The nDCG value as a number between 0 and 1
		"""

		nDCG = 0.0
		dcg = 0.0
		num = 0.0
		rel = []
		for i in range(k):
			if query_doc_IDs_ordered[i] in true_doc_IDs.keys():
				r =  true_doc_IDs[query_doc_IDs_ordered[i]]
				dcg += (5 - r) / math.log2(i+1)
				rel.append(r)
		rel.sort(reverse=True) 
		idcg = 0.0
		for i in range(k):
			idcg = (5-rel[i])/math.log2(i+1)
		nDCG = dcg / idcg

		return nDCG


	def meanNDCG(self, doc_IDs_ordered, query_ids, qrels, k):
		return -1
		"""
		Computation of nDCG of the Information Retrieval System
		at a given value of k, averaged over all the queries

		Parameters
		----------
		arg1 : list
			A list of lists of integers where the ith sub-list is a list of IDs
			of documents in their predicted order of relevance to the ith query
		arg2 : list
			A list of IDs of the queries for which the documents are ordered
		arg3 : list
			A list of dictionaries containing document-relevance
			judgements - Refer cran_qrels.json for the structure of each
			dictionary
		arg4 : int
			The k value

		Returns
		-------
		float
			The mean nDCG value as a number between 0 and 1
		"""

		meanNDCG = 0.0
		d = {}
		for q in query_ids:
			d[q] = {}
		for e in qrels:
			if d.get(e["query_num"],None):
				d[e["query_num"]][e["id"]] = e["position"]

		#Fill in code here
		for i in range(len(query_ids)):
			meanNDCG += self.queryNDCG(doc_IDs_ordered[i],query_ids[i],d[query_ids[i]],k)
		meanNDCG /= len(query_ids)

		return meanNDCG


	def queryAveragePrecision(self, query_doc_IDs_ordered, query_id, true_doc_IDs, k):
		return -1
		"""
		Computation of average precision of the Information Retrieval System
		at a given value of k for a single query (the average of precision@i
		values for i such that the ith document is truly relevant)

		Parameters
		----------
		arg1 : list
			A list of integers denoting the IDs of documents in
			their predicted order of relevance to a query
		arg2 : int
			The ID of the query in question
		arg3 : list
			The list of documents relevant to the query (ground truth)
		arg4 : int
			The k value

		Returns
		-------
		float
			The average precision value as a number between 0 and 1
		"""

		avgPrecision = 0.0
		num = 0.0
		for i in range(k):
			if query_doc_IDs_ordered[i] in true_doc_IDs:
				num+=1
				precision = num/k
				avgPrecision += precision
		#Fill in code here
		avgPrecision /= num
		return avgPrecision


	def meanAveragePrecision(self, doc_IDs_ordered, query_ids, q_rels, k):
		return -1
		"""
		Computation of MAP of the Information Retrieval System
		at given value of k, averaged over all the queries

		Parameters
		----------
		arg1 : list
			A list of lists of integers where the ith sub-list is a list of IDs
			of documents in their predicted order of relevance to the ith query
		arg2 : list
			A list of IDs of the queries
		arg3 : list
			A list of dictionaries containing document-relevance
			judgements - Refer cran_qrels.json for the structure of each
			dictionary
		arg4 : int
			The k value

		Returns
		-------
		float
			The MAP value as a number between 0 and 1
		"""

		meanAveragePrecision = 0.0
		d = {}
		for q in query_ids:
			d[q] = set() 
		for e in q_rels:
			if d.get(e["query_num"],None):
				d[e["query_num"]].add(e["id"])

		#Fill in code here
		for i in range(len(query_ids)):
			meanAveragePrecision += self.queryAveragePrecision(doc_IDs_ordered[i],query_ids[i],d[query_ids[i]],k)
		meanAveragePrecision /= len(query_ids)
		#Fill in code here

		return meanAveragePrecision

