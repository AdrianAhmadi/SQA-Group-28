#include <string>
#pragma once
using namespace std;

class User {
    private:
        string userName;
        float balance = 0.0;

    public:
        virtual ~User() {}
};