#include "Admin.h"
#include "User.h"
#include "StandardSeller.h"
#include "StandardBuyer.h"
#include "StandardFull.h"

User* Admin::create(string userName, string userType) {
    if (userType == "standard seller") {
        return new StandardSeller(userName);
    } else if (userType == "standard buyer") {
        return new StandardBuyer(userName);
    } else if (userType == "standard full") {
        return static_cast<User*>(static_cast<StandardSeller*>(new StandardFull(userName)));
    } else if (userType == "admin") {
        return new Admin(userName);
    }
}

float Admin::processRefund(Game game, User user) {
    user.setBalance(game.getPrice());
    return game.getPrice();
}

void Admin::OutputAccountList() {}

void Admin::OutputGameList() {}

void Admin::deleteUser(User &user) {}//should probably take the username as the argument

Admin::Admin(string userName): User(userName) {}