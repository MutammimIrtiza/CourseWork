#include<bits/stdc++.h>
using namespace std;

#define For(i, a, b) for(int i = (a); i <= (b); ++i)
#define ll long long
const ll inf = 1e15;

vector<vector<array<ll, 2>>> edges;
vector<vector<array<ll, 2>>> revEdges;

vector<ll> dijkstra(ll start, vector<vector<array<ll, 2>>> &gr, int n){
    vector<ll> d(n+1);
    For(i, 1, n) d[i] = inf;
    d[start] = 0;   

    bool vis[n+1] = {0};

    priority_queue<array<ll, 2>, vector<array<ll, 2>>, greater<array<ll, 2>>> pq; 
    pq.push({0LL, start});

    while(!pq.empty()){
        auto [dist, node] = pq.top();
        pq.pop();

        if(vis[node]) continue;
        vis[node] = 1;

        for(auto [nextNode, weight] : gr[node]){
            if(weight + dist < d[nextNode]){
                d[nextNode] = weight + dist;
                pq.push({d[nextNode], nextNode});
            }
        }
    }
    return d;
}

int main() {
    int n, m; cin >> n >> m;
    edges.resize(n+1); revEdges.resize(n+1);

    int u, v, w;
    For(i, 1, m) {
        cin >> u >> v >> w;
        edges[u].push_back({v, w});
        revEdges[v].push_back({u, w});
    }

    vector<ll> dis_from_1 = dijkstra(1, edges, n);
    vector<ll> dis_from_n = dijkstra(n, revEdges, n);

    ll ans = inf;

    For(node, 1, n) {
        for(auto [ch, w] : edges[node]) {
            ans = min(ans, dis_from_1[node] + w/2 + dis_from_n[ch]);
        }
    }

    cout << ans << endl;

}
