#pragma once
#include <vector>
#include <string>
#include "User.h"
#include "Game.h"

class StandardSeller; // Forward declaration
using namespace std;

class StandardBuyer : public virtual User {
    public:
        vector<Game> gameCatalog = {};
        void buy(Game game);
        void requestRefund(Game game, StandardSeller seller);
        StandardBuyer(string userName);
};