#include "Game.h"
#include <string>
using namespace std;

Game::Game(float price, string name, string creator): price(price), name(name), creator(creator) {}

float Game::getPrice() {
    return this->price;
}

string Game::getName() {
    return this->name;
}

string Game::getCreator() {
    return this->creator;
}

void Game::setPrice(float p) {
    this->price = p;
}

void Game::setName(string n) {
    this->name = n;
}

void Game::setCreator(string c) {
    this->creator = c;
}