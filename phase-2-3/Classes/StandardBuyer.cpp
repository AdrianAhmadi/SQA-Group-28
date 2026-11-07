#include "StandardBuyer.h"
#include "User.h"
#include "Game.h"
#include "StandardSeller.h"

#include <iostream>
using namespace std;

void StandardBuyer::buy(Game game) {
    if (this->getBalance() >= game.getPrice()) {
        float newUserBalance = this->getBalance() - game.getPrice();
        this->setBalance(newUserBalance);
        gameCatalog.push_back(game);
    } else {
        cout << "insuficiant funds" << endl;
    }
}

void StandardBuyer::requestRefund(Game game, StandardSeller seller) {
    cout << "refund for " << game.getName() << " has been sent to creator: " << game.getCreator() << endl;
}

StandardBuyer::StandardBuyer(string userName): User(userName){}