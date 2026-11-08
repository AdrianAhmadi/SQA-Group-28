#pragma once
#include <string>
using namespace std;

class User {
    private:
        string userName;
        float balance = 0.0;
        
    public:
        User(string userName);
        virtual ~User() = default;
        virtual void addCredit(float credit, string accounts);
        void setBalance(float amount);
        float getBalance();
        string getUserName();
};