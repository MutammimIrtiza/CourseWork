#include<bits/stdc++.h>
using namespace std;

#define For(i,a,b) for(int i = a; i <= b; i++)

#ifdef LOCAL
#include "debug.h"
#else
#define deb(...) (void)0
#endif

struct DSU {
    vector<int> p, sz;
    
    DSU(int n) {
        p.resize(n+1); sz.resize(n+1);
        For(i, 0, n-1) p[i] = i, sz[i] = 1;
    }

    int find(int x) {
        if(p[x] == x) return x;
        return p[x] = find(p[x]);
    }

    void unite(int a, int b) {
        a = find(a), b = find(b);
        if(a == b) return;
        if(sz[a] < sz[b]) swap(a, b);
        p[b] = a;
        sz[a] += sz[b];
    }
};

struct edge {
    int w, u, v;
    edge() {}
    edge(int w, int u, int v) :  w(w), u(u), v(v) {}
    bool operator<(const edge& other) const { // needed for sorting in kruskals
        return w < other.w;  
    }
    bool operator>(const edge& other) const { // needed for min heap
        return w > other.w;  
    }
};

void check_span(int n, vector<edge> &mstEdges) {
    if ((int)mstEdges.size() != n - 1) {
        cout << "Doesn't span\n";
        return;
    }

    vector<vector<int>> tree(n+1);
    for (auto [w, u, v] : mstEdges) {
        tree[u].push_back(v);
        tree[v].push_back(u);
    }

    vector<bool> vis(n+1, false);
    queue<int> q;

    q.push(0);
    vis[0] = true;
    int cnt = 1;

    while(!q.empty()) {
        int node = q.front(); q.pop();
        for(int ch : tree[node]) {
            if(!vis[ch]) {
                vis[ch] = true;
                q.push(ch);
                cnt++;
            }
        }
    }

    if (cnt == n) cout << "Span verified\n";
    else cout << "Doesn't span\n";
}


void prims(vector<vector<pair<int, int>>> &gr, int root) {
    int n = gr.size();
    vector<bool> inMST(n, false);
    int total = 0;

    priority_queue<edge, vector<edge>, greater<edge>> pq;
    vector<edge> mstEdges;
    pq.push({0, -1, root});

    while(pq.size()) {
        auto [w, par, node] = pq.top(); 
        pq.pop();

        if(inMST[node]) continue;
        inMST[node] = true;
        total += w;
        if(par != -1) mstEdges.push_back({w, par, node});

        for(auto [ch, nextw] : gr[node]) {
            if(!inMST[ch]) {
                pq.push({nextw, node, ch});
            }
        }
    }

    cout << "Total weight " << total << endl;
    cout << "Root node " << root << endl;
    for(auto [_, par, node] : mstEdges) cout << par << ' ' << node << endl;

    check_span(n, mstEdges);
}

void kruskals(vector<vector<pair<int, int>>> &gr) {

    int n = gr.size();
    vector<edge> edges, mstEdges;

    For(node, 0, n-1) {
        for(auto [ch, w] : gr[node]) {
            edges.push_back({w, ch, node});
        }
    }

    sort(edges.begin(), edges.end());

    DSU dsu(n);
    int total = 0;

    for(auto [w, u, v] : edges) {
        if(dsu.find(u) != dsu.find(v)) {
            mstEdges.push_back({w, u, v});
            total += w;
            dsu.unite(u, v);
        }
    }

    cout << "Total weight " << total << endl;
    for(auto [w, u, v] : mstEdges) {
        cout << u << ' ' << v << endl;
    }
    check_span(n, mstEdges);
}

int main() {
    int n, m; cin >> n >> m;
    vector<vector<pair<int, int>>> gr(n);
    
    For(i, 1, m) {
        int u, v, w; cin >> u >> v >> w;
        gr[u].push_back({v, w});
        gr[v].push_back({u, w});
    }

    int root; cin >> root;

    prims(gr, root);
    // kruskals(gr);

}