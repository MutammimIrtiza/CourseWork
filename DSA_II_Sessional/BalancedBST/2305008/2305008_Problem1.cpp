#include <bits/stdc++.h>
using namespace std;

#define L(i, a, b) for(int i = (a); i <= (b); ++i)

enum Color { RED, BLACK };
template <typename K, typename V>
class RBTree {
private:
    struct Node {
        K key;
        V value;
        Color color;
        Node *left, *right, *parent;
        int size;   

        Node(K k, V v) : key(k), value(v), color(RED), left(nullptr), right(nullptr), parent(nullptr), size(1) {}
    };

    Node* root;

    

    int getSize(Node* x) {
        return x ? x->size : 0;
    }

    void update_size(Node* x) {
        if(x) x->size = 1 + getSize(x->left) + getSize(x->right);
    }

    void leftRotate(Node* n) {
        if(!n) return;

        Node* nr = n->right;
        Node* np = n->parent;

        n->right = nr->left;
        if(nr->left) nr->left->parent = n;

        nr->parent = np;
        if(!np) root = nr;
        else if(n == np->left) np->left = nr;
        else np->right = nr;

        nr->left = n;
        n->parent = nr;

        update_size(n);
        update_size(nr);
    }

    void rightRotate(Node* n) {
        if(!n) return;

        Node* nl = n->left;
        Node* np = n->parent;

        n->left = nl->right;
        if(nl->right) nl->right->parent = n;

        nl->parent = np;
        if(!np) root = nl;
        else if(n == np->left) np->left = nl;
        else np->right = nl;

        nl->right = n;
        n->parent = nl;

        update_size(n);
        update_size(nl);
    }

    void transplant(Node* u, Node* v) {
        if(!u->parent) root = v;
        else if(u == u->parent->left) u->parent->left = v;
        else u->parent->right = v;

        if(v) v->parent = u->parent;
    }

    void fixInsert(Node* z) {
        while (z->parent && z->parent->color == RED && z->parent->parent) {
            Node *par = z->parent;
            if (par == par->parent->left) {
                Node* unc = par->parent->right;
                if(unc && unc->color == RED) {
                    par->color = BLACK;
                    unc->color = BLACK;
                    par->parent->color = RED;
                    z = par->parent;
                } else {
                    if (z == par->right) {
                        leftRotate(par);
                        z = par;
                        par = z->parent;
                    }
                    par->color = BLACK;
                    par->parent->color = RED;
                    rightRotate(par->parent);
                }
            } else {
                Node* unc = par->parent->left;
                if(unc && unc->color == RED) {
                    par->color = BLACK;
                    unc->color = BLACK;
                    par->parent->color = RED;
                    z = par->parent;
                } else {
                    if(z == par->left) {
                        rightRotate(par);
                        z = par;
                        par = z->parent;
                    }
                    par->color = BLACK;
                    par->parent->color = RED;
                    leftRotate(par->parent);
                }
            }
        }
        root->color = BLACK;
    }

    void fixDelete(Node* x, Node* xPar) {
        while(x != root && (!x || x->color == BLACK)) {
            if(x == xPar->left) {
                Node* w = xPar->right;
                
                if(w && w->color == RED) {
                    w->color = BLACK;
                    xPar->color = RED;
                    leftRotate(xPar);
                    w = xPar->right;
                }
                
                if((!w || !w->left || w->left->color == BLACK) &&
                    (!w || !w->right || w->right->color == BLACK)) {
                    if(w) w->color = RED;
                    x = xPar;
                    xPar = x->parent;

                } else {

                    if (!w || !w->right || w->right->color == BLACK) {
                        if(w && w->left) w->left->color = BLACK;
                        if(w) w->color = RED;
                        rightRotate(w);
                        w = xPar->right;
                    }
                    
                    if(w) w->color = xPar->color;
                    xPar->color = BLACK;
                    if(w && w->right) w->right->color = BLACK;
                    leftRotate(xPar);
                    x = root;
                    break;
                }
            } else {
                Node* w = xPar->left;
                
                if (w && w->color == RED) {
                    w->color = BLACK;
                    xPar->color = RED;
                    rightRotate(xPar);
                    w = xPar->left;
                }
                
                if ((!w || !w->right || w->right->color == BLACK) &&
                    (!w || !w->left || w->left->color == BLACK)) {
                    if(w) w->color = RED;
                    x = xPar;
                    xPar = x->parent;
                } else {

                    if (!w || !w->left || w->left->color == BLACK) {
                        if(w && w->right) w->right->color = BLACK;
                        if(w) w->color = RED;
                        leftRotate(w);
                        w = xPar->left;
                    }

                    if(w) w->color = xPar->color;
                    xPar->color = BLACK;
                    if(w && w->left) w->left->color = BLACK;
                    rightRotate(xPar);
                    x = root;
                    break;
                }
            }
        }
        if(x) x->color = BLACK;
    }

    Node* searchNode(K key) {
        Node* cur = root;
        while(cur) {
            if (key < cur->key) cur = cur->left;
            else if (key > cur->key) cur = cur->right;
            else return cur;
        }
        return nullptr;
    }

    void clear(Node* node) {
        if(!node) return;
        clear(node->left);
        clear(node->right);
        delete node;
    }

public:

    RBTree() : root(nullptr) {}

    

    bool insert(K key, V value) {

        Node* y = nullptr;
        Node* x = root;

        while(x) {
            y = x;
            if (key < x->key) x = x->left;
            else if (key > x->key) x = x->right;
            else return false;
        }

        Node* z = new Node(key, value);
        z->parent = y;

        if(!y) root = z;
        else if(key < y->key) y->left = z;
        else y->right = z;

        while(y) {
            update_size(y);
            y = y->parent;
        }

        fixInsert(z);
        return true;
    }

    bool _delete(K key) {

        Node* z = searchNode(key);
        if (!z) return false;

        if (!z->left) {
            
            Color orgCol = z->color;
            Node* x = z->right; 
            Node* zPar = z->parent; // save z's parent, coz x can be nullptr
            transplant(z, x);
            
            Node* p = zPar;
            while(p) {
                update_size(p);
                p = p->parent;
            }
            
            if(orgCol == BLACK) fixDelete(x, zPar);
            delete z;

        } else if (!z->right) {

            Color orgCol = z->color;
            Node* x = z->left;
            Node* zPar = z->parent;
            transplant(z, x);
            
            Node* p = zPar;
            while(p) {
                update_size(p);
                p = p->parent;
            }
            
            if (orgCol == BLACK) fixDelete(x, zPar);
            delete z;

        } else {

            Node *y = z->right;
            while (y->left) y = y->left;
            
            Color orgCol = y->color;
            Node *x = y->right;
            Node* xPar = y;
            
            if (y->parent != z) {
                xPar = y->parent;
                transplant(y, x);
                
                y->right = z->right;
                if(y->right) y->right->parent = y;
            }
            
            transplant(z, y);
            
            y->left = z->left;
            if(y->left) y->left->parent = y;
            y->color = z->color;
            
            Node* p = xPar;
            while(p) {
                update_size(p);
                p = p->parent;
            }
            
            if (orgCol == BLACK) fixDelete(x, xPar);
            delete z;

        }

        return true;
    }

    V* find(K key) {
        Node* n = searchNode(key);
        return n ? &(n->value) : nullptr;
    }

    bool search(K key) {
        return searchNode(key) != nullptr;
    }

    int countLessThan(K key) {
        Node* cur = root;
        int cnt = 0;
        while(cur) {
            if(key <= cur->key) cur = cur->left;
            else {
                cnt += getSize(cur->left) + 1;
                cur = cur->right;
            }
        }
        return cnt;
    }

    ~RBTree() {
        clear(root);
        root = nullptr;
    }

};

int main() {
    freopen("rbt_in1", "r", stdin);
    freopen("rbt_out1", "w", stdout);

    int n; cin >> n;
    RBTree<int, int> tree;

    cout << n << "\n";

    while(n--) {
        int e, x;
        cin >> e >> x;
        int r = 0;

        if(e == 0) r = tree._delete(x);
        else if(e == 1) r = tree.insert(x, x);
        else if(e == 2) r = tree.search(x);
        else if(e == 3) r = tree.countLessThan(x);

        cout << e << " " << x << " " << r << "\n";
    }
}
