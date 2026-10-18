#include <string>
#pragma once

using namespace std;

class Game {
    private:
        string name;
        float price;
        string creator;

    public:
        Game(float price, string name, string creator): price(price), name(name), creator(creator) {}

        float getPrice() {
            return this->price;
        }

        string getName() {
            return this->name;
        }

        string getCreator() {
            return this->creator;
        }

        void setPrice(float p) {
            this->price = p;
        }

        void setName(string n) {
            this->name = n;
        }

        void setCreator(string c) {
            this->creator = c;
        }
};