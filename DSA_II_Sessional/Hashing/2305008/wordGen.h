#pragma once

#include <string>
#include <unordered_set>
#include <vector>
#include <time.h>
using namespace std;

vector<string> generateWords(int count, int length){
    srand(time(0));
    unordered_set<string> seen;
    vector<string> words;

    while(words.size() < count){
        string s;
        for(int i=0; i<length; i++)
            s.push_back('a' + rand()%26);

        if(!seen.count(s)) {
            words.push_back(s);
        }
            
    }
    return words;
}
