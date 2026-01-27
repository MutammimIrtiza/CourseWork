#include<bits/stdc++.h>
using namespace std;

#define deb(...) debug(#__VA_ARGS__, __LINE__, __VA_ARGS__)

template<class T> 
auto pr(T x) -> decltype(cout<<x, void()) {cout<<x;}
void pr(string s) {cout << '"' << s << '"';}

template<class T, size_t N>
void pr(array<T, N> a) {cout << "{"; for(size_t i=0; i<N; i++) 
  {pr(a[i]);  if(i+1 < N) cout << ", ";  }   cout << "}"; }

template<class T, class U> 
void pr(pair<T,U> x) {cout << "{";   pr(x.first); 
    cout << ",";   pr(x.second);   cout << "}"; }

template<class T> 
auto pr(T v) -> decltype(v.begin(), v.end(), void()){
 cout << "["  ; for(auto it = v.begin(); it != v.end(); )
 { pr(*it);  if(++it != v.end()) cout << ", "  ; } cout << "]";}

#define eb emplace_back
template<class... A>
void debug(const char* s, int l, A... a){
    cout << l << "| ";   vector<string> names;   int i = 0;
for(stringstream ss(s);   getline(ss,  names.eb(),  ','  );  ){};
    ( (cout << "  " << names[i] << ": ",  pr(a),  i++),  ...);
    cout << "\n"; }
