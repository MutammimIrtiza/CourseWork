#include <bits/stdc++.h>
using namespace std;

#define L(i, a, b) for(int i = (a); i <= (b); ++i)

template<typename K, typename V>
class AVLTree {
private:
    struct Node {
        K key;
        V value;
        int height;
        Node *left, *right;
        Node(K k, V v) : key(k), value(v), height(1), left(nullptr), right(nullptr) {}
    };
    Node* root = nullptr;

    int get_height(Node* n) {
        if(n) return n->height;
        return 0;
    }

    void update_h(Node* x) {
        if(x) x->height = 1 + get_height(x->left) + get_height(x->right);
    }

    int diff(Node* n) {
        if(n) return get_height(n->left) - get_height(n->right);
        return 0;
    }

    Node* rightRotate(Node* a) {
        Node* b = a->left;
        Node* e = b->right;
        b->right = a;
        a->left = e;
        update_h(a);
        update_h(b);
        return b;
    }

    Node* leftRotate(Node* a) {
        Node* c = a->right;
        Node* d = c->left;
        c->left = a;
        a->right = d;
        update_h(a);
        update_h(c);
        return c;
    }

    Node* minValNode(Node* node) {
        Node* cur = node;
        while(cur->left) cur = cur->left;
        return cur;
    }

    Node* insert(Node* node, K key, V value, bool &success) {
        if(!node) {
            success = true;
            return new Node(key, value);
        }
        if(key < node->key) node->left = insert(node->left, key, value, success);
        else if(key > node->key) node->right = insert(node->right, key, value, success);
        else {
            success = false;
            return node;
        }

        update_h(node);
        int d = diff(node);

        if(d > 1) {
            if (key < node->left->key) return rightRotate(node); // left-left
            else { // left-right
                node->left = leftRotate(node->left);
                return rightRotate(node);
            }
        }   
        else if(d < -1) {
            if (key > node->right->key) return leftRotate(node); // right-right
            else { // right-left
                node->right = rightRotate(node->right);
                return leftRotate(node);
            }
        }
        return node;
    }

    Node* deleteNode(Node* node, K key, bool &success) {
        if(!node){
            success = false;
            return nullptr;
        }

        if(key < node->key) node->left = deleteNode(node->left, key, success);
        else if(key > node->key) node->right = deleteNode(node->right, key, success);
        else{
            success = true;

            if(!node->left || !node->right) {
                Node* temp = node->left ? node->left : node->right;
                delete node;
                return temp;
            }

            Node* minNode = minValNode(node->right);
            node->key = minNode->key;
            node->value = minNode->value;
            node->right = deleteNode(node->right, minNode->key, success);
        }

        update_h(node);
        int d = diff(node);

        if(d > 1) {
            if(diff(node->left) >= 0) return rightRotate(node);
            else {
                node->left = leftRotate(node->left);
                return rightRotate(node);
            }
        }

        if(d < -1) {
            if(diff(node->right) <= 0) return leftRotate(node);
            else {
                node->right = rightRotate(node->right);
                return leftRotate(node);
            }
        }
        return node;
    }

    void preorder(Node* n, vector<pair<K,V>>& v) {
        if(!n) return;
        v.push_back({n->key, n->value});
        preorder(n->left, v);
        preorder(n->right, v);
    }

    void inorder(Node* n, vector<pair<K,V>>& v) {
        if(!n) return;
        inorder(n->left, v);
        v.push_back({n->key, n->value});
        inorder(n->right, v);
    }

    void postorder(Node* n, vector<pair<K,V>>& v) {
        if(!n) return;
        postorder(n->left, v);
        postorder(n->right, v);
        v.push_back({n->key, n->value});
    }

    void clear(Node* node) {
        if(node) {
            clear(node->left);
            clear(node->right);
            delete node;
        }
    }

public:

    bool insert(K key, V value) {
        bool success = false;
        root = insert(root, key, value, success);
        return success;
    }

    bool remove(K key) {
        bool success = false;
        root = deleteNode(root, key, success);
        return success;
    }

    vector<pair<K,V>> preorder() {
        vector<pair<K,V>> v;
        preorder(root, v);
        return v;
    }

    vector<pair<K,V>> inorder() {
        vector<pair<K,V>> v;
        inorder(root, v);
        return v;
    }

    vector<pair<K,V>> postorder() {
        vector<pair<K,V>> v;
        postorder(root, v);
        return v;
    }

    vector<pair<K,V>> levelorder() {
        vector<pair<K,V>> v;
        if(!root) return v;
        queue<Node*> q;
        q.push(root);
        while(!q.empty()) {
            Node* cur = q.front(); q.pop();
            v.push_back({cur->key, cur->value});
            if (cur->left) q.push(cur->left);
            if (cur->right) q.push(cur->right);
        }
        return v;
    }

    ~AVLTree() {
        clear(root);
    }

};

int main() {

    freopen("avl_in1", "r", stdin);
    freopen("avl_out1", "w", stdout);

    int n; cin >> n;
    cout << n << "\n";

    AVLTree<int, int> tree;

    while (n--) {
        int e, x, y;
        cin >> e >> x;

        if(e == 0) { 
            bool ok = tree.remove(x);
            cout << "0 " << x << " " << (ok ? 1 : 0) << "\n";
        }
        else if(e == 1) { 
            bool ok = tree.insert(x, x);
            cout << "1 " << x << " " << (ok ? 1 : 0) << "\n";
        }
        else { 
            vector<pair<int,int>> res;
            if      (x == 1) res = tree.preorder();
            else if (x == 2) res = tree.inorder();
            else if (x == 3) res = tree.postorder();
            else             res = tree.levelorder();
            for (auto &p : res) cout << p.first << " ";
            cout << "\n";
        }
    }
}