#pragma once
#include <vector>
#include <list>
#include "HashTable.h"
#include "HashFunctions.h"

template <typename Key, typename Value, typename Hash>
class ChainingHashTable : public HashTable<Key, Value>
{
private:
    vector<list<pair<Key, Value>>> table;

public:
    ChainingHashTable()
    {
        table.resize(this->init_table_size);
    }

    void insert(const Key &key, const Value &value) override
    {
        int idx = Hash{}(key) % this->table_size;
        if (!table[idx].empty())
        {
            this->coll_cnt++;
        }
        for (auto &kv : table[idx])
        {
            if (kv.first == key)
            {
                kv.second = value;
                return;
            }
        }
        table[idx].push_back({key, value});
        this->elem_cnt++;
        this->ins_since_prev_exp++;
        this->rehash();
    }

    bool search(const Key &key, Value &value) override
    {
        int idx = Hash{}(key) % this->table_size;
        for (const auto &kv : table[idx])
        {
            this->hit_cnt++;
            if (kv.first == key)
            {
                value = kv.second;
                return true;
            }
        }
        return false;
    }

    bool erase(const Key &key) override
    {
        int idx = Hash{}(key) % this->table_size;
        for (auto it = table[idx].begin(); it != table[idx].end(); ++it)
        {
            if (it->first == key)
            {
                table[idx].erase(it);
                this->elem_cnt--;
                this->del_since_prev_compc--;
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
        for (const auto &bucket : oldTable)
        {
            for (const auto &kv : bucket)
            {
                insert(kv.first, kv.second);
            }
        }
    }
};
