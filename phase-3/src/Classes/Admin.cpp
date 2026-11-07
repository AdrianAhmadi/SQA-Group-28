#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <cstdio>
#include "Admin.h"
#include "User.h"
#include "StandardSeller.h"
#include "StandardBuyer.h"
#include "StandardFull.h"

void Admin::create(string username, string userType, string accounts) {
    ifstream instream;
    ofstream outstream;

    bool found = false;
    vector<string> row;
    string line, word, temp;
    vector<string> perms = {"AA", "BS", "SS", "FS"};

    for (auto& x : userType){
        x = toupper(x);
    }

    instream.open(accounts);

    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }

    while(getline(instream, line)){
        row.clear();
        stringstream s(line);

        while(getline(s, word, ',')){
            row.push_back(word);
        }
        if (row[0] == username){
            found = true;
            continue;
        }
    }

    instream.close();

    if (found == true){
        cout << "Error: User already exists." << endl;
        return;
    }

    if (found == false){
        for (int i = 0; i < perms.size(); i++){
            if (userType == perms[i]){
                outstream.open(accounts, ios::app);
                if (!outstream.is_open()){
                    cout << "Error: Could not open file." << endl;
                    return;
                }
                outstream << username << ',' << 0 << ',' << i << endl;
                outstream.close();
                cout << "User successfully created." << endl;
                return;
            }
        }
        cout << "Error: Invalid input for user type." << endl;
        return;
    }
}

float Admin::processRefund(Game game, User user) {
    user.setBalance(game.getPrice());
    return game.getPrice();
}

void Admin::OutputAccountList() {}

void Admin::OutputGameList() {}

void Admin::deleteUser(string username, string accounts) {
    string tempfile = ".\\data\\temp.txt";
    ifstream instream;
    ofstream outstream;

    bool found = false;
    vector<string> row;
    string line, word, temp;


    instream.open(accounts);
    outstream.open(tempfile);

    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }

    if (!outstream.is_open()){
        cerr << "Error: Could not create output file." << endl;
        return;
    }

    while(getline(instream, line)){
        row.clear();
        stringstream s(line);

        while(getline(s, word, ',')){
            row.push_back(word);
        }
        if (row[0] == username){
            cout << "User deleted." << endl;
            found = true;
            continue;
        }
        outstream << line << '\n';
    }
    instream.close();
    outstream.close();

    if (found == false){
        cout << "No such user found." << endl;
        remove(tempfile.c_str());
        return;
    }
    remove(accounts.c_str());
    rename(tempfile.c_str(), accounts.c_str());
}

Admin::Admin(string userName): User(userName) {}