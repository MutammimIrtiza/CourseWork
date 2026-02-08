#pragma once

bool isPrime(int n){
    if(n < 2) return false;
    for(int i=2; i*i<=n; i++)
        if(n%i==0) return false;
    return true;
}

int nextPrime(int n){
    while(!isPrime(n)) n++;
    return n;
}

int prevPrime(int n){
    while(!isPrime(n)) n--;
    return n;
}
