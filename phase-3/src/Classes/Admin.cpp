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

void Admin::processRefund(string buyer, string seller, double amount, string accounts) {
    string tempfile = ".\\data\\temp.txt";
<<<<<<< HEAD
    ifstream instream;
    ofstream outstream;

    bool first = true;
    bool foundBuyer = false;
    bool foundSeller = false;
    vector<string> row;
    string line, word, temp;
    string formattedBuyer = buyer.append((15 - buyer.length()), ' ');
    string formattedSeller = seller.append((15 - seller.length()), ' ');

    instream.open(accounts);

    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }

    outstream.open(tempfile);

    if (!outstream.is_open()){
        cerr << "Error: Could not open account file." << endl;
    }

    while(getline(instream, line)){
        row.clear();
        stringstream s(line);

        while(getline(s, word, '_')){
            row.push_back(word);
        }
        if (row[0] == formattedBuyer){
            if (stod(row[2]) <= (999999.99 - amount)){
                foundBuyer = true;
            } else {
                return;
            }
        }
        if (row[0] == formattedSeller){
            if (stod(row[2]) > amount){
                foundSeller = true;
            } else {
                //cout << "Error: Seller has insufficient funds to process this refund." << endl;
                return;
            }
            continue;
        }
    }
    instream.close();
    instream.open(accounts);
    
    if (foundBuyer == true && foundSeller == true){
        while(getline(instream, line)){
            row.clear();
            stringstream s2(line);

            while(getline(s2, word, '_')){
                row.push_back(word);
            }
            if (row.size() >= 3 && row[0] == formattedBuyer){
                double total = stod(row[2]) + amount;
                if (total > 999999.99){
                    //cout << "Error: account would exceed total credit limit." << endl;
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
                }
            } else if (row.size() >= 3 && row[0] == formattedSeller){
                double total = stod(row[2]) - amount;
                if (total < 0){
                    //cout << "Error: account would exceed total credit limit." << endl;
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
                }
            }
            if (first == false){
                outstream << '\n';
            }
            outstream << row[0] << '_' << row[1] << '_' << row[2];
            first = false;
        }
    }
    instream.close();
    outstream.close();
    if (foundBuyer == true && foundSeller == true){
        remove(accounts.c_str());
        rename(tempfile.c_str(), accounts.c_str());
    } else {
        remove(tempfile.c_str());
    }
}

void Admin::OutputAccountList() {
=======
>>>>>>> origin/phase-3-Denis
    ifstream instream;
    ofstream outstream;

    bool first = true;
    bool foundBuyer = false;
    bool foundSeller = false;
    vector<string> row;
    string line, word, temp;
    string formattedBuyer = buyer.append((15 - buyer.length()), ' ');
    string formattedSeller = seller.append((15 - seller.length()), ' ');

    instream.open(accounts);

    if (!instream.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return;
    }

    outstream.open(tempfile);

    if (!outstream.is_open()){
        cerr << "Error: Could not open account file." << endl;
    }

    while(getline(instream, line)){
        row.clear();
        stringstream s(line);

        while(getline(s, word, '_')){
            row.push_back(word);
        }
        if (row[0] == formattedBuyer){
            if (stod(row[2]) <= (999999.99 - amount)){
                foundBuyer = true;
            } else {
                return;
            }
        }
        if (row[0] == formattedSeller){
            if (stod(row[2]) > amount){
                foundSeller = true;
            } else {
                //cout << "Error: Seller has insufficient funds to process this refund." << endl;
                return;
            }
            continue;
        }
    }
    instream.close();
    instream.open(accounts);
    
    if (foundBuyer == true && foundSeller == true){
        while(getline(instream, line)){
            row.clear();
            stringstream s2(line);

            while(getline(s2, word, '_')){
                row.push_back(word);
            }
            if (row.size() >= 3 && row[0] == formattedBuyer){
                double total = stod(row[2]) + amount;
                if (total > 999999.99){
                    //cout << "Error: account would exceed total credit limit." << endl;
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
                }
            } else if (row.size() >= 3 && row[0] == formattedSeller){
                double total = stod(row[2]) - amount;
                if (total < 0){
                    //cout << "Error: account would exceed total credit limit." << endl;
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
                }
            }
            if (first == false){
                outstream << '\n';
            }
            outstream << row[0] << '_' << row[1] << '_' << row[2];
            first = false;
        }
    }
    instream.close();
    outstream.close();
    if (foundBuyer == true && foundSeller == true){
        remove(accounts.c_str());
        rename(tempfile.c_str(), accounts.c_str());
    } else {
        remove(tempfile.c_str());
    }
}

void Admin::OutputAccountList(string accounts) {
    ifstream instream;
    instream.open(accounts);
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
    string tempfile = "./data/temp.txt";
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

<<<<<<< HEAD
void Admin::addCredit(string username, double credit, string accounts){
    string tempfile = ".\\data\\temp.txt";
=======
void Admin::addCredit(string username, float credit, string accounts){
    string tempfile = "./data/temp.txt";
>>>>>>> origin/phase-3-Denis
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

        while(getline(s, word, '_')){
            row.push_back(word);
        }
        if (row.size() < 3){
            continue;
        }
        
        if (row.size() >= 3 && row[0] == formatted){
            total = stod(row[2]) + credit;
            if (total > 999999.99 || total < 0){
                //cout << "Error: account would exceed total credit limit." << endl;
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