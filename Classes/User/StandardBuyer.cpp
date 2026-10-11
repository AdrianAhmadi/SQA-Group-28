#include "User.cpp";
#include "Game.cpp";
#include <vector>

using namespace std;

class StandardBuyer : public User{
    private:
        vector<Game> gameCatalog = {};
};