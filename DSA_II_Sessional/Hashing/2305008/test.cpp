#include "chainingHashTable.h"
#include "doubleHashing.h"
#include "customProbing.h"
#include "hashFunctions.h"
#include "wordGen.h"
#include <iostream>
#include <string>

int main()
{
    ChainingHashTable<string, int, PolyHash<string>> ch_ht_poly;
    ChainingHashTable<string, int, DJB2Hash<string>> ch_ht_dj;
    DoubleHashingHashTable<string, int, PolyHash<string>> db_ht_poly;
    DoubleHashingHashTable<string, int, DJB2Hash<string>> db_ht_dj;
    CustomProbingHashTable<string, int, PolyHash<string>> cp_ht_poly(1, 1);
    CustomProbingHashTable<string, int, DJB2Hash<string>> cp_ht_dj(1, 1);

    vector<string> words = generateWords(10000, 10);

    for (int i = 0; i < 10000; i++)
    {
        ch_ht_poly.insert(words[i], i);
        ch_ht_dj.insert(words[i], i);
        db_ht_poly.insert(words[i], i);
        db_ht_dj.insert(words[i], i);
        cp_ht_poly.insert(words[i], i);
        cp_ht_dj.insert(words[i], i);
    }

    for (int i = 0; i < 1000; i++)
    {
        int val;
        int idx = rand() % 10000;
        ch_ht_poly.search(words[idx], val);
        ch_ht_dj.search(words[idx], val);
        db_ht_poly.search(words[idx], val);
        db_ht_dj.search(words[idx], val);
        cp_ht_poly.search(words[idx], val);
        cp_ht_dj.search(words[idx], val);
    }

    cout << "\n";
    cout << "                        Hash1 (PolyHash)                     Hash2 (DJB2Hash)" << endl;
    cout << "                No. of Collisions   Avg Hits          No. of Collisions  Avg Hits" << endl;
    cout << "-----------------------------------------------------------------------------------" << endl;
    cout << "Chaining Method      ";
    cout.width(15);
    cout << left << ch_ht_poly.get_coll_count();
    cout.width(22);
    cout << left << (ch_ht_poly.get_hit_cnt() / 1000.0);
    cout.width(15);
    cout << left << ch_ht_dj.get_coll_count();
    cout << fixed << (ch_ht_dj.get_hit_cnt() / 1000.0) << endl;

    cout << "Double Hashing       ";
    cout.width(15);
    cout << left << db_ht_poly.get_coll_count();
    cout.width(22);
    cout << left << (db_ht_poly.get_hit_cnt() / 1000.0);
    cout.width(15);
    cout << left << db_ht_dj.get_coll_count();
    cout << fixed << (db_ht_dj.get_hit_cnt() / 1000.0) << endl;

    cout << "Custom Probing       ";
    cout.width(15);
    cout << left << cp_ht_poly.get_coll_count();
    cout.width(22);
    cout << left << (cp_ht_poly.get_hit_cnt() / 1000.0);
    cout.width(15);
    cout << left << cp_ht_dj.get_coll_count();
    cout << fixed << (cp_ht_dj.get_hit_cnt() / 1000.0) << endl;

    return 0;
}
