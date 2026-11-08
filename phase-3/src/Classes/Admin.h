#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class Admin : public User {
    public:
        using User::addCredit;
        void create(string username, string userType, string accounts);
        void processRefund(string buyer, string seller, double amount, string accounts);
        void OutputAccountList(string accounts);
        void OutputGameList();
        void deleteUser(string username, string accounts);
        void addCredit(string username, double credit, string accounts);
    
        Admin(string userName);
};