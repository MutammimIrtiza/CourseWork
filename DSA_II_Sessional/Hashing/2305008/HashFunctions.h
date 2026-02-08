#pragma once

#include<string>
#include "toString.h"
using namespace std;

/*
    A functor is simply a class that overloads operator() so that objects of the class can be called like a function.
*/

template<typename T>
struct PolyHash {
    long long mod = 1000000007; // avoid overflow
    long long P = 31;

    int operator()(const T& key) const {
        string s = toString(key);
        long long h = 0;
        long long power = 1;

        for (char c : s) {
            h = (h + (unsigned char)c * power) % mod; // s[0]*P^0 + s[1]*P^1 + ...
            power = (power * P) % mod;
        }
        return h;
    }
};

template<typename T>
struct DJB2Hash {
    int operator()(const T& key) const {
        string s = toString(key);
        int mod = 1000000007;
        unsigned long long h = 5381;
        for (char c : s) {
            h = ((h << 5) + h) + c;   // h = h * 33 + c
            h %= mod;
        }
        return h;
    }
};
