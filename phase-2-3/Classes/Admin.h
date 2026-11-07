#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class Admin : public User {
    private:
        User* create(string userName, string userType);
        float processRefund(Game game, User user);
        void OutputAccountList();
        void OutputGameList();
        void deleteUser(User &user);

    public:
        Admin(string userName);
};