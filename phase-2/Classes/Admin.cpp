#include "User.cpp";
#include "StandardSeller.cpp";
#include "StandardBuyer.cpp";
#include "StandardFull.cpp";

class Admin : public User {
    private:
        User* create(string userName, string userType) {
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

        float processRefund(Game game, User user) {
            user.setBalance(game.getPrice());
            return game.getPrice();
        }

        void OutputAccountList() {}
        void OutputGameList() {}

        void deleteUser(User &user) {}//should probably take the username as the argument

    public:
        Admin(string userName): User(userName) {}
};