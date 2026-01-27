#include<bits/stdc++.h>
using namespace std;

int main() {
    int n; int tcase = 0;
    while(cin >> n)
    {
        tcase++;
        map<string, int> mp;
        for(int i = 1; i <= n; i++) {
            string s; cin >> s;
            mp[s] = i;
        }

        long double best[n+1][n+1];
        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                if(i == j) best[i][j] = 1;
                else best[i][j] = 0;
            }
        }

        int m; cin >> m;
        for(int i = 1; i <= m; i++) {
            string s1, s2; long double r; cin >> s1 >> r >> s2;
            int u = mp[s1]; int v = mp[s2];
            best[u][v] = max(best[u][v], r);
        }

        for(int k = 1; k <= n; k++) {
            for(int i = 1; i <= n; i++) {
                for(int j = 1; j <= n; j++) {
                    if(best[i][k] != 0 and best[k][j] != 0)
                    best[i][j] = max(best[i][j], best[i][k] * best[k][j]);
                }
            }
        }

        bool found = false;
        for(int i = 1; i <= n; i++) 
            if(best[i][i] > 1) found = true;

        cout << "Case " << tcase << ": " << (found ? "Yes" : "No") << endl;
    }
    

}