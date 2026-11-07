#pragma once
#include <string>
#include "User.h"
#include "Game.h"
using namespace std;

class StandardSeller : public virtual User {
    private:
        void sell(Game game);
    
    public:
        StandardSeller(string userName);
};