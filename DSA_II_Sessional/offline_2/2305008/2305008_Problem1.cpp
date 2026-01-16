#include<bits/stdc++.h>
using namespace std;

int main() {
    int n, m, q; cin >> n >> m >> q;
    
    long long dis[n+1][n+1];
    for(int i = 1; i <= n; i++) {
        for(int j = 1; j <= n; j++) {
            if(i == j) dis[i][j] = 0;
            else dis[i][j] = LLONG_MAX;
        }
    }

    for(int i = 1; i <= m; i++) {
        int u, v, w; cin >> u >> v >> w;
        dis[u][v] = min(dis[u][v], (long long)w);
        dis[v][u] = min(dis[v][u], (long long)w);
    }

    for(int k = 1; k <= n; k++) {
        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                if(dis[i][k] != LLONG_MAX && dis[k][j] != LLONG_MAX)
                dis[i][j] = min(dis[i][j], dis[i][k]+dis[k][j]);
            }
        }
    }

    while(q--) {
        int u, v; cin >> u >> v; 
        cout << (dis[u][v] == LLONG_MAX ? -1 : dis[u][v]) << endl;
    }

}