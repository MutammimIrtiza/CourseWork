#pragma once
#include "PrimeUtilities.h"

template <typename Key, typename Value>
class HashTable {

protected:
    static constexpr int init_table_size = 13;
    static constexpr double mx_load_factor = 0.5;
    static constexpr double mn_load_factor = 0.25;

    int elem_cnt;
    int table_size;
    int ins_since_prev_exp;
    int del_since_prev_compc;
    int coll_cnt, hit_cnt;

    HashTable() {
        this->coll_cnt = 0;
        this->hit_cnt = 0;
        this->elem_cnt = 0;
        this->table_size = this->init_table_size;
        this->ins_since_prev_exp = 0;
        this->del_since_prev_compc = 0;
    }

    virtual void rehashAux(int newSize) = 0;

    void rehash() {
        if(loadFactor() > mx_load_factor && ins_since_prev_exp > elem_cnt/2) {
            rehashAux(nextPrime(2*table_size));
            ins_since_prev_exp = 0;
        }
        if(table_size > init_table_size && 
             loadFactor() < mn_load_factor && del_since_prev_compc > elem_cnt/2) {
            rehashAux(prevPrime(table_size/2));
            del_since_prev_compc = 0;
        }
    }

public:

    virtual void insert(const Key& key, const Value& value) = 0;
    virtual bool search(const Key& key, Value& value) = 0;
    virtual bool erase(const Key& key) = 0;

    virtual ~HashTable() {}

    double loadFactor() {
        return (double) elem_cnt/table_size;
    }

    int get_coll_count() {
        return coll_cnt;
    }

    int get_hit_cnt() {
        return hit_cnt;
    }

};