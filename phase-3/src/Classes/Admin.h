#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class Admin : public User {
    public:
        using User::addCredit;
        void create(string username, string userType, string accounts);
        float processRefund(Game game, User user);
        void OutputAccountList(string accounts);
        void deleteUser(string username, string accounts);
        void addCredit(string username, float credit, string accounts);
    
        Admin(string userName);
};