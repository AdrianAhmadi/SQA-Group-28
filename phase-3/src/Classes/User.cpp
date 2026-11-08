#include "User.h"
#include <string>
#include <iostream>
#include <fstream>
using namespace std;

void User::addCredit() {
    cout << "credit method used" << endl;
}

User::User(string userName) {this->userName = userName;}

void User::setBalance(float amount) {
    this->balance = amount;
}

float User::getBalance() {
    return this->balance;
}

string User::getUserName() {
    return this->userName;
}

void User::OutputGameList(string games){
    ifstream instream;
    instream.open(games);
    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }
    
    string line;

    while(getline(instream, line)){
        cout << line << endl;
    }
}