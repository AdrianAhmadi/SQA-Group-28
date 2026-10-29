#include "StandardSeller.cpp"
#include "StandardBuyer.cpp"
#include "User.cpp"

class StandardFull : public StandardBuyer, public StandardSeller {
    public:
        StandardFull(string userName): User(userName), StandardSeller(userName), StandardBuyer(userName){}
};