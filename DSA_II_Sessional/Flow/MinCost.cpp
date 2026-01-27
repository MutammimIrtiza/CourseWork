// gemini

#include<bits/stdc++.h>
using namespace std;

typedef long long ll;
const ll INF = 1e18;

struct Edge {
    int to, revIdx, capacity, flow, cost;
};

vector<vector<Edge>> adj;
vector<ll> dist;
vector<pair<int, int>> pre;

// Bellman-Ford finds the cheapest path in the residual graph
bool bellmanFord(int s, int t, int &flow, ll &cost, int n) {
    dist.assign(n + 1, INF);
    pre.assign(n + 1, {-1, -1});
    dist[s] = 0;

    // Standard Bellman-Ford: Relax all edges |V|-1 times
    for (int i = 1; i <= n; i++) {
        bool changed = false;
        for (int u = 1; u <= n; u++) {
            if (dist[u] == INF) continue;
            for (int j = 0; j < (int)adj[u].size(); j++) {
                Edge &e = adj[u][j];
                if (e.capacity > e.flow && dist[e.to] > dist[u] + e.cost) {
                    dist[e.to] = dist[u] + e.cost;
                    pre[e.to] = {u, j};
                    changed = true;
                }
            }
        }
        if (!changed) break; // Optimization: stop if no changes occur
    }

    if (dist[t] == INF) return false;

    // Determine the bottleneck (the maximum flow we can send along this path)
    int bottleneck = 2e9; 
    int cur = t;
    while (cur != s) {
        int u = pre[cur].first;
        int idx = pre[cur].second;
        bottleneck = min(bottleneck, adj[u][idx].capacity - adj[u][idx].flow);
        cur = u;
    }

    // Update global flow and cost
    flow += bottleneck;
    cost += (ll)bottleneck * dist[t];

    // Update residual capacities
    cur = t;
    while (cur != s) {
        int u = pre[cur].first;
        int idx = pre[cur].second;
        adj[u][idx].flow += bottleneck;
        int revIdx = adj[u][idx].revIdx;
        adj[cur][revIdx].flow -= bottleneck;
        cur = u;
    }

    return true;
}

void addEdge(int u, int v, int cap, int cost) {
    adj[u].push_back({v, (int)adj[v].size(), cap, 0, cost});
    adj[v].push_back({u, (int)adj[u].size() - 1, 0, 0, -cost});
}

int main() {
    int n, m;
    if (!(cin >> n >> m)) return 0;

    adj.assign(n + 1, vector<Edge>());
    
    for (int i = 0; i < m; i++) {
        int u, v, cap, cost;
        cin >> u >> v >> cap >> cost;
        addEdge(u, v, cap, cost);
    }

    int s, t;
    cin >> s >> t;

    int maxFlow = 0;
    ll minCost = 0;

    // Keep finding the cheapest path until no more flow can be sent
    while (bellmanFord(s, t, maxFlow, minCost, n));

    cout << "Max Flow: " << maxFlow << endl;
    cout << "Min Cost: " << minCost << endl;

    return 0;
}