#include "User.cpp"
#include "Game.cpp"
#include "StandardSeller.cpp"
#include <vector>
#include <iostream>

using namespace std;

class StandardBuyer : public virtual User{
    private:
        vector<Game> gameCatalog = {};

        void buy(Game game) {
            if (this->getBalance() >= game.getPrice()) {
                float newUserBalance = this->getBalance() - game.getPrice();
                this->setBalance(newUserBalance);
                gameCatalog.push_back(game);
            } else {
                cout << "insuficiant funds" << endl;
            }
        }

        void requestRefund(Game game, StandardSeller seller) {
            cout << "refund for " << game.getName() << " has been sent to creator: " << game.getCreator() << endl;
        }

    public:
        StandardBuyer(string userName): User(userName){}
};