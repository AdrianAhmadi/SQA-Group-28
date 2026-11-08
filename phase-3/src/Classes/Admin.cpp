#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <cstdio>
#include <iomanip>
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
    string formatted = username.append((15 - username.length()), ' ');

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

        while(getline(s, word, '_')){
            row.push_back(word);
        }
        if (row[0] == formatted){
            found = true;
            continue;
        }
    }

    instream.close();

    if (found == true){
        //cout << "Error: User already exists." << endl;
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
                outstream << '\n' << formatted << '_' << perms[i] << "_000000.00";
                outstream.close();
                //cout << "User successfully created." << endl;
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

void Admin::OutputAccountList() {
    ifstream instream;
    instream.open(".\\data\\currentaccounts.txt");
    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }
    
    string line;

    while(getline(instream, line)){
        cout << line << endl;
    }
}

void Admin::OutputGameList() {
    ifstream instream;
    instream.open(".\\data\\availablegames.txt");
    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }
    
    string line;

    while(getline(instream, line)){
        cout << line << endl;
    }
}

void Admin::deleteUser(string username, string accounts) {
    string tempfile = ".\\data\\temp.txt";
    ifstream instream;
    ofstream outstream;

    bool first = true;
    bool found = false;
    vector<string> row;
    string line, word, temp;
    string formatted = username.append((15 - username.length()), ' ');

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

        while(getline(s, word, '_')){
            row.push_back(word);
        }
        if (row[0] == formatted){
            //cout << "User deleted." << endl;
            found = true;
            continue;
        }
        if (first == false){
            outstream << '\n';
        }
        outstream << line;
        first = false;
    }
    instream.close();
    outstream.close();

    if (found == false){
        //cout << "No such user found." << endl;
        remove(tempfile.c_str());
        return;
    }
    remove(accounts.c_str());
    rename(tempfile.c_str(), accounts.c_str());
}

void Admin::addCredit(string username, float credit, string accounts){
    string tempfile = ".\\data\\temp.txt";
    ifstream instream;
    ofstream outstream;

    bool first = true;
    bool found = false;
    vector<string> row;
    string line, word, temp;
    double total;
    string formatted = username.append((15 - username.length()), ' ');

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
        if (row.size() < 3){
            continue;
        }
        
        if (row.size() >= 3 && row[0] == formatted){
            total = stod(row[2]) + credit;
            if (total > 999999.99){
                //cout << "Error: account would exceed total credit limit." << endl;
            } else {
                stringstream ss;
                ss << fixed << setprecision(2) << total;
                string newval = ss.str();
                string zeros = "";
                zeros.append('0', 9 - newval.length());
                zeros.append(newval);
                row[2] = zeros;
            }
            found = true;
        }
        if (first == false){
            outstream << '\n';
        }
        outstream << row[0] << '_' << row[1] << '_' << row[2];
        first = false;
    }
    instream.close();
    outstream.close();

    if (found == false){
        //cout << "No such user found." << endl;
        remove(tempfile.c_str());
        return;
    }
    remove(accounts.c_str());
    rename(tempfile.c_str(), accounts.c_str());
}

Admin::Admin(string userName): User(userName) {}