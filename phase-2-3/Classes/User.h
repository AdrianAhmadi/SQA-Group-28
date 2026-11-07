#pragma once
#include <string>
using namespace std;

class User {
    private:
        string userName;
        float balance = 0.0;

        void login();
        void logout();
        virtual void addCredit();

    public:
        User(string userName);
        virtual ~User() = default;

        void setBalance(float amount);
        float getBalance();
        string getUserName();
};