#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class Admin : public User {
    public:
        void create(string username, string userType, string accounts);
        float processRefund(Game game, User user);
        void OutputAccountList();
        void OutputGameList();
        void deleteUser(string username, string accounts);

    
        Admin(string userName);
};