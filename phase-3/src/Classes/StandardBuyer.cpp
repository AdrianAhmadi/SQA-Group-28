#include "StandardBuyer.h"
#include "User.h"
#include "Game.h"
#include "StandardSeller.h"

#include <iostream>
#include <fstream>
#include <iostream>
#include <sstream>
#include <iomanip>
#include <vector>
#include <cstdio>
using namespace std;

// void StandardBuyer::buy(Game game) {
//     if (this->getBalance() >= game.getPrice()) {
//         float newUserBalance = this->getBalance() - game.getPrice();
//         this->setBalance(newUserBalance);
//         gameCatalog.push_back(game);
//     } else {
//         cout << "insuficiant funds" << endl;
//     }
// }

void StandardBuyer::buy(string gameTitle, string sellerName, string gamesFilePath, string gamesCollectionPath, string accountsFilePath) {
    ifstream instream;
    ofstream outstream;
    string line, word;
    vector<string> row;

    bool gameFound = false;
    float gamePrice = 0.0f;
    string tempGamesFile = "./data/temp_availablegames.txt";

    instream.open(gamesFilePath);
    outstream.open(tempGamesFile); // write into temp file

    if (!instream.is_open() || !outstream.is_open()) {
        cerr << "Error: Could not process available games file." << endl;
        return;
    }

    // find the game to buy
    while (getline(instream, line)) {
        row.clear();
        stringstream s(line);
        while (getline(s, word, ',')) {
            row.push_back(word);
        }

        if (row.size() >= 3 && row[1] == gameTitle && row[2] == sellerName) {
            gamePrice = stof(row[0]);
            gameFound = true;
            break;
        }
    }
    instream.close();

    if (!gameFound) {
        //cout << "Error: Game not found or seller mismatch." << endl;
        return;
    }

    // Check if buyer is trying to buy their own game
    if (getUserName() == sellerName) {
        //cout << "Error: Cannot buy your own game." << endl;
        return;
    }

    // check buyer and seller balances
    string tempAccountsFile = "./data/temp_accounts.txt";
    bool buyerFound = false;
    bool sellerFound = false;
    float buyerBalance = 0.0f;
    float sellerBalance = 0.0f;
    vector<vector<string>> accountsRows; // outer vector is the row for an individual account

    instream.open(accountsFilePath);
    if (!instream.is_open()) {
        cerr << "Error: Could not open accounts file." << endl;
        return;
    }

    // check to see if the seller and the buyer exsit
    while (getline(instream, line)) {
        row.clear();
        stringstream s(line);
        while (getline(s, word, ',')) {
            row.push_back(word);
        }
        if (row.size() >= 3) {
            if (row[0] == getUserName()) {
                buyerBalance = stof(row[1]);
                buyerFound = true;
            } else if (row[0] == sellerName) {
                sellerBalance = stof(row[1]);
                sellerFound = true;
            }
            accountsRows.push_back(row);
        }
    }
    instream.close();

    if (!buyerFound || !sellerFound) {
        return;
    }

    if (buyerBalance < gamePrice) {
        //cout << "Error: Insufficient funds." << endl;
        return;
    }

    if (sellerBalance + gamePrice > 999999.99) {
        //cout << "Error: Seller credit overflow." << endl;
        return;
    }

    outstream.open(tempAccountsFile);
    if (!outstream.is_open()) {
        cerr << "Error: Could not create temp accounts file." << endl;
        return;
    }

    // update the buyers and sellers balance
    for (size_t i = 0; i < accountsRows.size(); ++i) {
        if (accountsRows[i][0] == getUserName()) {
            buyerBalance -= gamePrice;
            stringstream ss;
            ss << fixed << setprecision(2) << buyerBalance;
            accountsRows[i][1] = ss.str();
        } else if (accountsRows[i][0] == sellerName) {
            sellerBalance += gamePrice;
            stringstream ss;
            ss << fixed << setprecision(2) << sellerBalance;
            accountsRows[i][1] = ss.str();
        }
        outstream << accountsRows[i][0] << ',' << accountsRows[i][1] << ',' << accountsRows[i][2];
        if (i + 1 < accountsRows.size()) {
            outstream << endl;
        }
    }
    outstream.close();

    // update the accounts file
    remove(accountsFilePath.c_str());
    rename(tempAccountsFile.c_str(), accountsFilePath.c_str());

    // add game to buyers collection
    outstream.open(gamesCollectionPath, ios::app);
    if (outstream.is_open()) {
        outstream << endl << gameTitle << ',' << getUserName();
        outstream.close();
    }
}

void StandardBuyer::requestRefund(Game game, StandardSeller seller) {
    cout << "refund for " << game.getName() << " has been sent to creator: " << game.getCreator() << endl;
}

StandardBuyer::StandardBuyer(string userName): User(userName){}