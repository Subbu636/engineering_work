#include<bits/stdc++.h>
#include "MST.h"
#include "./Matching/Matching.h"
#include "./Matching/Graph.h"

/*
Christofides algorithm
returns a pair containing a vector and a double
the vector contains the indices of the edges in the solution
the double is the solution cost
*/

void euler_cycle(list<int> &cycle, vector< vector<int> > A, const Graph & G,vector<int> traversed){
	cycle.push_back(0);
	list<int>::iterator itp = cycle.begin();
	while(itp != cycle.end())
	{
		//Let u be the current vertex in the cycle, starting at the first
		int u = *itp;
		list<int>::iterator jtp = itp;
		jtp++;

		//if there are non-traversed edges incident to u, find a subcycle starting at u
		//replace u in the cycle by the subcycle
		while(not A[u].empty())
		{
			while(not A[u].empty() and traversed[ G.GetEdgeIndex(u, A[u].back()) ] == 0)
				A[u].pop_back();

			if(not A[u].empty())
			{
				int v = A[u].back();
				A[u].pop_back();
				cycle.insert(jtp, v);
				traversed[G.GetEdgeIndex(u, v)]--;
				u = v;
			}
		}

		//go to the next vertex in the cycle and do the same
		itp++;
	}
}

double shortcutting_simple(vector<int> &solution, list<int> &cycle, const Graph & G, const vector<double> & cost){
	vector<bool> visited(G.GetNumVertices(), false);
	double obj = 0;

	int u = 0;
	visited[u] = true;
	list<int>::iterator it = ++(cycle.begin());
	for(; it != cycle.end(); it++)
	{
		int v = *it;
		if(visited[v]) 
			continue;

		visited[v] = true;
		obj += cost[ G.GetEdgeIndex(u, v) ];
		solution.push_back( G.GetEdgeIndex(u, v) );
		u = v;
	}
	obj += cost[ G.GetEdgeIndex(u, 0) ];
	solution.push_back( G.GetEdgeIndex(u, 0) );
	return obj;
}

double shortcutting_triangle_opt(vector<int> &solution, list<int> &cycle, const Graph & G, const vector<double> & cost){
	vector <int> cycle_vec{cycle.begin(), cycle.end()};
	cycle_vec.pop_back();
	int n = G.GetNumVertices();
	vector<vector<int>> pos;
	for(int i = 0;i < n;++i){
		int min_ind = -1;
		double min_cost = DBL_MAX;
		int s = cycle_vec.size();
		int c = 0;
		for(int j = 0;j < s;++j){
			if(cycle_vec[j] == i){
				c++;
				// printf("%d,%d,%d\n",cycle_vec[(j+s-1)%s],i,cycle_vec[(j+1)%s]);
				double val = cost[G.GetEdgeIndex(cycle_vec[(j+s-1)%s],i)]+cost[G.GetEdgeIndex(cycle_vec[(j+1)%s],i)];
				if(cycle_vec[(j+s-1)%s] != cycle_vec[(j+1)%s]){
					val -= cost[G.GetEdgeIndex(cycle_vec[(j+s-1)%s],cycle_vec[(j+1)%s])];
				}
				if (val < min_cost){
					min_ind = c;
					min_cost = val;
				}
			}
		}
		c = 0;
		for(int j = 0;j < (int)cycle_vec.size();++j){
			if(cycle_vec[j] == i){
				c++;
				if (c != min_ind){
					cycle_vec.erase(cycle_vec.begin()+j);
				}
			}
		}
		for(int j = 1;j < (int)cycle_vec.size();++j){
			if(cycle_vec[j] == cycle_vec[j-1]){
				cycle_vec.erase(cycle_vec.begin()+j);
			}
		}
	}
	assert((int)cycle_vec.size() == n);
	int pos_zero = -1;
	double obj = 0;
	for(int i = 0;i < n;++i){
		if (cycle_vec[i] == 0){
			pos_zero = i;
			break;
		}
	}
	for(int i = 0;i < n;++i){
		solution.push_back(G.GetEdgeIndex(cycle_vec[(pos_zero+i)%n], cycle_vec[(pos_zero+i+1)%n]));
		obj += cost[G.GetEdgeIndex(cycle_vec[(pos_zero+i)%n], cycle_vec[(pos_zero+i+1)%n])];
	}
	return obj;
}

bool is_connected(vector< vector<int> > &A, vector<int> &traversed,const Graph & G, int x, int a,int b,int n){
	if (a == b) return false;
	for(int i = 0;i < (int)A[x].size();++i){
		if(A[x][i] != a && A[x][i] != b){
			traversed[G.GetEdgeIndex(x,A[x][i])]--;
		}
	}
	vector<bool> visited(n, false);
	visited[a] = true;
	visited[b] = true;
	int c = 0;
	deque <int> que;
	que.push_back(a);
	que.push_back(b);
	while(!que.empty()){
		int y = que.front();
		que.pop_front();
		c++;
		for(int i = 0;i < (int)A[y].size();++i){
			if((!visited[A[y][i]]) && (traversed[G.GetEdgeIndex(y,A[y][i])] > 0)){
				visited[A[y][i]] = true;
				que.push_back(A[y][i]);
			}
		}
	}
	for(int i = 0;i < (int)A[x].size();++i){
		if(A[x][i] != a && A[x][i] != b){
			traversed[G.GetEdgeIndex(x,A[x][i])]++;
		}
	}
	return (c == n);
}

double shortcutting_triangle_comp(vector<int> &solution,vector< vector<int> > &A , vector<int> traversed, const Graph & G, const vector<double> & cost){
	int n = G.GetNumVertices();
	// for(int i = 0;i < n;++i){
	// 	printf("%d-%d:\t",i,(int)A[i].size());
	// 	for(int j = 0;j < (int)A[i].size();++j){
	// 		printf("%d,",A[i][j]);
	// 	}
	// 	printf("\n");
	// }
	for(int i = 0;i < n;++i){
		if((int)A[i].size() <= 2) continue;
		int mini1 = -1,mini2 = -1;
		double mini = DBL_MAX;
		vector <int> temp;
		for(int j = 0;j < (int)A[i].size();++j){
			for(int k = j+1;k < (int)A[i].size();++k){
				// printf("%d,%d,%d,%d,%d,%d\n",i,A[i][j],A[i][k],traversed[G.GetEdgeIndex(i,A[i][j])],traversed[G.GetEdgeIndex(i,A[i][k])],(int)is_connected(A,traversed,G,i,A[i][j],A[i][k],n));
				if(traversed[G.GetEdgeIndex(i,A[i][j])] > 0 && traversed[G.GetEdgeIndex(i,A[i][k])] > 0 && is_connected(A,traversed,G,i,A[i][j],A[i][k],n)){
					double val =  cost[G.GetEdgeIndex(A[i][j],i)]+cost[G.GetEdgeIndex(A[i][k],i)];
					vector<int> var;
					for(int l = 0;l < (int)A[i].size();l++){
						if(l != j && l != k){
							var.push_back(A[i][l]);
						}
					}
					for(int l = 0; l+1 < (int)var.size(); l+=2){
						if (var[l] == var[l+1]) continue;
						val += cost[G.GetEdgeIndex(var[l],var[l+1])];
					}
					if(mini > val){
						mini1 = j;
						mini2 = k;
						mini = val;
						temp = var;
					}
				}
			}
		}
		// remove all except mini1 and mini2
		// printf("%d-%d / %d-%d\n",mini1,mini2,A[i][mini1],A[i][mini2]);
		assert(mini1 != -1 && mini2 != -1);
		for(int j = 0;j < (int)temp.size();j++){
			traversed[G.GetEdgeIndex(i,temp[j])]--;
			A[i].erase(find(A[i].begin(),A[i].end(),temp[j]));
			A[temp[j]].erase(find(A[temp[j]].begin(),A[temp[j]].end(),i));
		}
		for(int j = 0;j+1 < (int)temp.size();j+=2){
			if(temp[j] != temp[j+1]){
				traversed[G.GetEdgeIndex(temp[j],temp[j+1])]++;
				A[temp[j]].push_back(temp[j+1]);
				A[temp[j+1]].push_back(temp[j]);
			}
		}
		// for(int i = 0;i < n;++i){
		// 	printf("%d-%d:\t",i,(int)A[i].size());
		// 	for(int j = 0;j < (int)A[i].size();++j){
		// 		printf("%d,",A[i][j]);
		// 	}
		// 	printf("\n");
		// }
	}
	// verify it
	vector<bool> visited(n, false);
	visited[0] = true;
	deque <int> que;
	que.push_back(0);
	int y = -1;
	double obj = 0.0;
	while(!que.empty()){
		y = que.front();
		que.pop_front();
		assert(2 == (int)A[y].size());
		for(int i = 0;i < (int)A[y].size();++i){
			if((!visited[A[y][i]]) && (traversed[G.GetEdgeIndex(y,A[y][i])] > 0)){
				visited[A[y][i]] = true;
				que.push_back(A[y][i]);
				solution.push_back(G.GetEdgeIndex(y,A[y][i]));
				obj += cost[G.GetEdgeIndex(y,A[y][i])];
				break;
			}
		}
	}
	solution.push_back( G.GetEdgeIndex(y,0));
	obj += cost[G.GetEdgeIndex(y,0)];
	assert((int)solution.size() == n);
	return obj;
}


pair< vector<int>, double > Christofides(const Graph & G, const vector<double> & cost)
{
	//Solve minimum spanning tree problem
	pair< list<int>, double > p = Prim(G, cost);
	list<int> mst = p.first;

	//Build adjacency lists using edges in the tree
	vector< vector<int> > A(G.GetNumVertices(), vector<int>());
	for(list<int>::iterator it = mst.begin(); it != mst.end(); it++)
	{
		pair<int, int> p = G.GetEdge(*it);
		int u = p.first, v = p.second;

		A[u].push_back(v);
		A[v].push_back(u);
	}

	//Find vertices with odd degree
	vector<int> odd;
	for(int u = 0; u < G.GetNumVertices(); u++)
		if(A[u].size() % 2)
			odd.push_back(u);

	//Create a graph with the odd degree vertices
	Graph O(odd.size());
	vector<double> costO;
	for(int i = 0; i < (int)odd.size(); i++)
	{
		for(int j = i+1; j < (int)odd.size(); j++)
		{
			if(G.AdjMat()[odd[i]][odd[j]])
			{
				O.AddEdge(i, j);
				costO.push_back( cost[G.GetEdgeIndex(odd[i], odd[j])] );
			}
		}
	}

	//Find the minimum cost perfect matching of the graph of the odd degree vertices
	Matching M(O);
	p = M.SolveMinimumCostPerfectMatching(costO);
	list<int> matching = p.first;

	//Add the edges in the matching the the adjacency list
	for(list<int>::iterator it = matching.begin(); it != matching.end(); it++)
	{
		pair<int, int> p = O.GetEdge(*it);
		int u = odd[p.first], v = odd[p.second];

		A[u].push_back(v);
		A[v].push_back(u);
	}

	//This is to keep track of how many times we can traverse an edge
	vector<int> traversed(G.GetNumEdges(), 0);
	for(int u = 0; u < G.GetNumVertices(); u++)
	{
		for(vector<int>::iterator it = A[u].begin(); it != A[u].end(); it++)
		{
			int v = *it;
			
			//we do this so that the edge is not counted twice
			if(v < u) continue;

			traversed[G.GetEdgeIndex(u, v)]++;
		}
	}

	//Find an Eulerian cycle in the graph implied by A
	list<int> cycle;
	euler_cycle(cycle,A,G,traversed);

	vector <int> solution1;
	double obj1 = shortcutting_simple(solution1, cycle, G, cost);

	vector<int> solution2;
	double obj2 = shortcutting_triangle_opt(solution2, cycle, G, cost);

	vector <int> solution3;
	double obj3 = shortcutting_triangle_comp(solution3,A ,traversed,G, cost);

	printf("simple:%d\ntri opt:%d\ntri comp:%d\n",(int)obj1,(int)obj2,(int)obj3);

	return pair< vector<int>, double >(solution3, obj3);
}
