#include "User.h"
#include <string>
#include <iostream>
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