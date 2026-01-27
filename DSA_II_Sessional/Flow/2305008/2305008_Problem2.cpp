#include<bits/stdc++.h>
using namespace std;

#define For(i, a, b) for(int i = (a); i <= (b); i++)

#ifdef LOCAL
#include "debug.h"
#else
#define deb(...) (void)0
#endif

struct Edge {
    int to, revIdx, capacity, flow;
};


vector<vector<Edge>> adj;
vector<pair<int, int>> pre;

bool bfs(int s, int t) {
    For(i, 0, (int)adj.size()-1) pre[i].first = -1;
    queue<int> q;
    q.push(s);
    pre[s].first = s;

    while(q.size()) {
        int u = q.front(); q.pop();

        For(i, 0, (int)adj[u].size()-1) {
            Edge e = adj[u][i];

            if(pre[e.to].first != -1) continue;
            if(e.flow == e.capacity) continue;

            pre[e.to] = {u, i};
            if(e.to == t) return true;
            q.push(e.to);
        }
    }
    return false;
}

int Edmonds_Karp(int s, int t) {
    int maxFlow = 0;

    while(bfs(s, t)) {
        int cur = t;
        int bottleneck = INT_MAX;

        while (cur != s) {
            Edge e = adj[pre[cur].first][pre[cur].second];
            bottleneck = min(bottleneck, e.capacity - e.flow);
            cur = pre[cur].first;
        }

        cur = t;
        while (cur != s) {
            Edge &e = adj[pre[cur].first][pre[cur].second];
            e.flow += bottleneck;
            adj[cur][e.revIdx].flow -= bottleneck;
            cur = pre[cur].first;
        }
        maxFlow += bottleneck;
    }
    return maxFlow;
}

vector<pair<int, int>> edges;
vector<int> idx;
void addEdge(int u, int v, int w) {
    Edge fwd = {v, adj[v].size(), w, 0};
    Edge bw = {u, adj[u].size(), 0, 0};

    edges.push_back({u, v});
    idx.push_back(adj[u].size());

    adj[u].push_back(fwd);
    adj[v].push_back(bw);
}

int main() {
    int n, m, k;
    cin >> n >> k >> m;
    adj.resize(n+2);
    pre.resize(n+2);

    For(i, 0, m-1) {
        int u, v; cin >> u >> v;
        addEdge(u, v, 1);
    }

    int s = n, t = n+1;

    For(pres, 0, k-1) {
        addEdge(s, pres, 1);
    }

    For(poll, k, n-1) {
        addEdge(poll, t, 1);
    }

    int maxFlow = Edmonds_Karp(s, t);
    cout << maxFlow << endl;

    For(i, 0, m-1) {
        Edge e = adj[edges[i].first][idx[i]];
        if(e.flow) {
            cout << edges[i].first << ' ' << edges[i].second << endl;
        }
    }

}