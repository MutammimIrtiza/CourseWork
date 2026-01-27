#include<bits/stdc++.h>
using namespace std;

#define For(i, a, b) for(int i = (a); i <= (b); ++i)
#define ll long long
const ll inf = 1e15;

int main() {
    int n, m; cin >> n >> m;
    vector<vector<array<ll, 2>>> edges(n+1);

    int u, v; ll w;
    For(i, 1, m) {
        cin >> u >> v >> w;
        edges[u].push_back({v, w});
    }

    vector<ll> dis(n+1, inf);
    vector<int> par(n+1);

    For(node, 0, n) edges[0].push_back({node, 0});
    dis[0] = 0;

    For(iter, 1, n) { // n+1 nodes, so n times
        For(node, 0, n) {
            for(auto [ch, w] : edges[node]) {
                if(dis[node] + w < dis[ch]) {
                    dis[ch] = dis[node] + w;
                    par[ch] = node;
                }
            }
        }
    }

    u = -1;
    For(node, 0, n) {
        if(u != -1) break;
        for(auto [ch, w] : edges[node]) {
            if(dis[node] + w < dis[ch]) {
                dis[ch] = dis[node] + w;
                par[ch] = node;
                u = ch;
                break;
            }
        }
    }

    if(u == -1) {
        cout << -1 << endl;
    } else {
        bool vis[n+1] = {0};
        while(!vis[u]) {
            vis[u] = true;
            u = par[u];
        }

        For(node, 0, n) vis[node] = false;
        vector<int> cycle;
        while(!vis[u]) {
            vis[u] = true;
            cycle.push_back(u);
            u = par[u];
        }

        for(int x : cycle) cout << x << " ";
        cout << endl;
    }

}
