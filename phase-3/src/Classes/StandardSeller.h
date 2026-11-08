#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class StandardSeller : public virtual User {
    public:
        void sell(string gameTitle, float price, string sellerName, string gamesFilePath);
    
        StandardSeller(string userName);
};