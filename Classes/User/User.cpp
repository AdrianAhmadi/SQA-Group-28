#include <string>
#include <iostream>
#pragma once
using namespace std;

class User {
    private:
        string userName;
        float balance = 0.0;

        void login() {

        }

        void logout() {

        }

        virtual void addCredit() {
            cout << "credit method used" << endl;
        }

    public:
        User(string userName) {this->userName = userName;}
        virtual ~User() {}

        void setBalance(float amount) {
            this->balance = amount;
        }

        float getBalance() {
            return this->balance;
        }

        string getUserName() {
            return this->userName;
        }
};