#pragma once
#include <string>
using namespace std;

class Game {
    private:
        string name;
        float price;
        string creator;

    public:
        Game(float price, string name, string creator);

        float getPrice();
        string getName();
        string getCreator();

        void setPrice(float p);
        void setName(string n);
        void setCreator(string c);
};