#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class StandardSeller : public virtual User {
    public:
        void sell(Game game);
    
        StandardSeller(string userName);
};