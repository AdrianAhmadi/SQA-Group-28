#include "StandardSeller.cpp";
#include "StandardBuyer.cpp";
#include "User.cpp";
#pragma once

class StandardFull : public StandardBuyer, public StandardSeller {
    public:
        StandardFull(string userName): User(userName), StandardSeller(userName), StandardBuyer(userName){}
};