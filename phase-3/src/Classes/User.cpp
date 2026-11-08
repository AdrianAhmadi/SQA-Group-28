#include "User.h"
#include <iostream>
#include <fstream>
<<<<<<< HEAD
#include <sstream>
#include <string>
#include <cstdio>
#include <iomanip>
#include <vector>
=======
>>>>>>> origin/phase-3-Denis
using namespace std;

void User::addCredit(float credit, string accounts) {
   float sessionCreditAdded = 0.0;
   const float MAX_CREDIT_ADD = 1000.00;
   string username = this->getUserName();

   // check for valid credit amount
   if (credit <= 0.0 || (sessionCreditAdded + credit) > MAX_CREDIT_ADD) {
        return;
    }

    string tempfile = ".\\data\\temp.txt";
    ifstream instream;
    ofstream outstream;

    bool first = true;
    bool found = false;
    vector<string> row;
    string line, word;
    float total;

    string formatted = username.append((15 - username.length()), ' ');

    instream.open(accounts);
    outstream.open(tempfile);

    if (!instream.is_open()) {
        cerr << "Error: Could not open account file." << endl;
        return;
    }

    if (!outstream.is_open()) {
        cerr << "Error: Could not create output file." << endl;
        instream.close();
        return;
    }

    // find user to make sure that this user exsits in accounts file
    while (getline(instream, line)) {
        row.clear();
        stringstream s(line);

        while(getline(s, word, '_')) {
            row.push_back(word);
        }

        if (row.size() < 3) {
            continue;
        }

        if (row.size() >= 3 && row[0] == formatted) {
            total = stod(row[2]) + credit;
            if (total > 999999.99 || total < 0) {
                instream.close();
                outstream.close();
                remove(tempfile.c_str());
                return;
            } else {
                stringstream ss;
                ss << fixed << setprecision(2) << total;
                string newval = ss.str();
                string zeros = "";
                zeros.append(9 - newval.length(), '0');
                zeros.append(newval);
                row[2] = zeros;
                found = true;
            }
        }
        if (first == false) {
            outstream << '\n';
        }
        outstream << row[0] << '_' << row[1] << '_' << row[2];
        first = false;
    }

    instream.close();
    outstream.close();

    if (found == false) {
        remove(tempfile.c_str());
        return;
    }

    // Increment the session credit tracker
    sessionCreditAdded += credit;

    // update the accounts file
    remove(accounts.c_str());
    rename(tempfile.c_str(), accounts.c_str());
}

User::User(string userName) {this->userName = userName;}

void User::setBalance(float amount) {
    this->balance = amount;
}

float User::getBalance() {
    return this->balance;
}

string User::getUserName() {
    return this->userName;
}

void User::OutputGameList(string games){
    ifstream instream;
    instream.open(games);
    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }
    
    string line;

    while(getline(instream, line)){
        cout << line << endl;
    }
}