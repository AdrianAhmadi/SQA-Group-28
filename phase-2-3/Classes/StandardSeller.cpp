#include <string>
#include "User.cpp"
#include "Game.cpp"

class StandardSeller : public virtual User {
    private:
        void sell(Game game) {}
    
    public:
        StandardSeller(string userName): User(userName){}
};