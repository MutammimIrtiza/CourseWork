#pragma once

#include<string>
using namespace std;

string toString(const string &key) {
    return key;
}

string toString(const int &key) {
    return to_string(key);
}

string toString(const double &key) {
    return to_string(key);
}

