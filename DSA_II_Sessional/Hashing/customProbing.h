#include <vector>
#include "HashTable.h"
#include "HashFunctions.h"
using namespace std;

template <typename Key>
struct CustomAuxHash
{
    int operator()(const Key &key) const
    {
        string s = toString(key);
        int h = 0;
        for (char c : s)
            h += c;
        return h ? h : 1;
    }
};

template <typename Key, typename Value, typename Hash>
class CustomProbingHashTable : public HashTable<Key, Value>
{
private:
    struct Entry
    {
        Key key;
        Value value;
        bool occupied;
        bool deleted;
    };
    vector<Entry> table;
    int C1, C2;

public:
    CustomProbingHashTable(int c1 = 1, int c2 = 1) : C1(c1), C2(c2)
    {
        table.resize(this->init_table_size);
    }

    void insert(const Key &key, const Value &value) override
    {
        int N = this->table_size;
        int h1 = Hash{}(key) % N;
        int h2 = CustomAuxHash<Key>{}(key) % N;
        int first_deleted = -1;
        for (int i = 0; i < N; ++i)
        {
            int idx = (h1 + C1 * i * h2 + C2 * i * i) % N;
            if (table[idx].occupied)
            {
                if (!table[idx].deleted && table[idx].key == key)
                {
                    table[idx].value = value;
                    return;
                }
                this->coll_cnt++;
            }
            else
            {
                if (table[idx].deleted && first_deleted == -1)
                {
                    first_deleted = idx;
                }
                else if (!table[idx].deleted)
                {
                    int insert_idx = (first_deleted != -1) ? first_deleted : idx;
                    table[insert_idx] = Entry{key, value, true, false};
                    this->elem_cnt++;
                    this->ins_since_prev_exp++;
                    this->rehash();
                    return;
                }
            }
        }
    }

    bool search(const Key &key, Value &value) override
    {
        int N = this->table_size;
        int h1 = Hash{}(key) % N;
        int h2 = CustomAuxHash<Key>{}(key) % N;
        if (h2 == 0)
            h2 = 1;
        for (int i = 0; i < N; ++i)
        {
            int idx = (h1 + C1 * i * h2 + C2 * i * i) % N;
            this->hit_cnt++;
            if (!table[idx].occupied && !table[idx].deleted)
                return false;
            if (table[idx].occupied && table[idx].key == key)
            {
                value = table[idx].value;
                return true;
            }
        }
        return false;
    }

    bool erase(const Key &key) override
    {
        int N = this->table_size;
        int h1 = Hash{}(key) % N;
        int h2 = CustomAuxHash<Key>{}(key) % N;
        if (h2 == 0)
            h2 = 1;
        for (int i = 0; i < N; ++i)
        {
            int idx = (h1 + C1 * i * h2 + C2 * i * i) % N;
            if (!table[idx].occupied && !table[idx].deleted)
                return false;
            if (table[idx].occupied && table[idx].key == key)
            {
                table[idx].deleted = true;
                table[idx].occupied = false;
                this->elem_cnt--;
                this->del_since_prev_compc++;
                this->rehash();
                return true;
            }
        }
        return false;
    }

protected:
    void rehashAux(int newSize) override
    {
        auto oldTable = table;
        table.clear();
        table.resize(newSize);
        this->table_size = newSize;
        this->elem_cnt = 0;
        for (const auto &entry : oldTable)
        {
            if (entry.occupied && !entry.deleted)
            {
                insert(entry.key, entry.value);
            }
        }
    }
};
