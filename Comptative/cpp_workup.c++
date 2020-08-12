#include<bits/stdc++.h>
using namespace std;

int main(){
	ios_base::sync_with_stdio(false);
	cin.tie(NULL);

	// dequeue is 
	// dqueue <int> que; // both stack and queue
	// que.push_back(4);
	// que.push_front(5);
	// que.push_back(6);
	// int f = que.front();
	// int g = que.back();
	// que.pop_back(); // returns void use front to get val
	// que.pop_front();
	// bool s = que.empty(); // que.size() also available
	// sort(que.begin(),que.end());
	// same func for vector too

	// Hashmap
	map<string, int> dict; // or unordered_map <string, int> dict (more like hashmap O(1)) former har O(logn)
	dict["HOLA"] = 0;
	dict["xy"] = 1;
	cout<<dict["xy"]<<endl;
	if(dict["xyz"]==0){
		cout<<"its null"<<endl; // true for int its 0 and string its empty string 
		// be carefull
	}
	cout<<dict["xyz"]<<endl;
	dict.begin();
	dict.end();
	dict.size();
	dict.empty();
	dict.insert(pair<string,int>("xyz",100));
	dict.clear();
	return 0;
}