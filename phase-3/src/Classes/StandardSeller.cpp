#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <iomanip>
#include <vector>
#include "StandardSeller.h"


StandardSeller::StandardSeller(string userName): User(userName){}

void StandardSeller::sell(string gameTitle, float price, string sellerName, string gamesFilePath) {
    ifstream instream;
    ofstream outstream;

    bool found = false;
    vector<string> row;
    string line, word;

    // Check if the game already exists in availablegames.txt
    instream.open(gamesFilePath);

    if (!instream.is_open()){
        cerr << "Error: Could not open games file." << endl;
        return;
    }

    while(getline(instream, line)){
        row.clear();
        stringstream s(line);

        while(getline(s, word, ',')){
            row.push_back(word);
        }
        if (row.size() >= 2 && row[1] == gameTitle){
            found = true;
            break;
        }
    }

    instream.close();

    if (found == true){
        //cout << "Error: Game already exists." << endl;
        return;
    }

    // Validate game name length and price range constraints
    if (gameTitle.length() == 0 || gameTitle.length() > 25 || price > 999.99 || price < 0.00){
        //cout << "Error: Invalid game name or price." << endl;
        return;
    }

    if (found == false) {
        outstream.open(gamesFilePath, ios::app);
                if (!outstream.is_open()){
                    cout << "Error: Could not open file." << endl;
                    return;
                }
                outstream << '\n'  << price << gameTitle << sellerName;
                outstream.close();
                //cout << "Game is now up for sale" << endl;
                return;
    }
}