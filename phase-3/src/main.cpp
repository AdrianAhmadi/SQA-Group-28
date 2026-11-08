#include <iostream>
#include <fstream>
#include <sstream>
#include <unordered_map>
#include <string>
#include <vector>
#include <map>
#include <cctype>

#include "Classes/User.h"
#include "Classes/Admin.h"
#include "Classes/StandardBuyer.h"
#include "Classes/StandardSeller.h"
#include "Classes/StandardFull.h"
#include "Classes/Game.h"

using namespace std;

enum Inputs {login, buy, sell, refund, create, deleteuser, addcredit, accountlist, gamelist, usersearch, logout, quit};

const map<string, Inputs> inputMap = {{"login", login}, {"buy", buy}, {"sell", sell}, {"refund", refund}, {"create", create},{"delete", deleteuser}, {"addcredit", addcredit},
{"gamelist", gamelist}, {"accountlist", accountlist}, {"search", usersearch}, {"logout", logout}, {"quit", quit}};

User* userLogin(string username, string accounts){
    // Open the AccountList.csv to check if the username exists in the database, and what permissions it has
    fstream AccountList;
    AccountList.open(accounts);

    if (!AccountList.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return nullptr;
    }

    //placeholder variables that are used within the while loop
    bool found = false;
    User* currentUser = nullptr;
    vector<string> row; //
    string line, word, temp;
    string perms = "";
    if (username.length() > 15){ 
        cout << "Username too long" << endl;
        return nullptr;
    }
        string formatted = username;
        formatted.append(15 - formatted.length(), ' ');

    while (getline(AccountList, line)){
        row.clear();

        stringstream s(line);

        while (getline(s, word, '_')){
            row.push_back(word);
        }
        
        if (row.size() >= 3 && row[0] == formatted){

            //convert the vector field for user perms into an integer for the switch case (this isnt strictly necessary but its simpler to do)
            perms = row[1];
            found = true;
            //switch case decides which constructor is called based on the permissions flag in the accountlist
            if (perms == "AA"){
                currentUser = new Admin(username);
                break;
            } else if (perms == "BS"){
                currentUser = new StandardBuyer(username);
                break;
            } else if (perms == "SS"){
                currentUser = new StandardSeller(username);
                break;
            } else if (perms == "FS"){
                currentUser = new StandardFull(username);
                break;
            } else {
                //cout << "Error: Permissions flag is not a valid integer input for userLogin function." << endl;
                break;
            }
        }
    }
    if (found == false) cout << "User not found" << endl;
    return currentUser;
}

int main(int argc, char* argv[]){
    if (argc != 5){
        cerr << "Usage: distribution-system currentaccounts.txt availablegames.txt gamescollection.txt transout.atf" << endl;
        return 1;
    }

    bool session = false;
    User* currentUser = nullptr;
    string choice = "";
    string accounts = argv[1];
    string games = argv[2];
    string gamesCollection = argv[3];
    string transactionOutput = argv[4];
    do
    {
        /*cout << "\nMain Menu"<< endl;
        cout << "Options:" << endl;
        cout << "login"<< endl;
        if (session == true){
            cout << "buy" << endl;
            cout << "sell"<< endl;
            cout << "refund"<< endl;
            cout << "create"<< endl;
            cout << "delete"<< endl;
            cout << "addcredit"<< endl;
            cout << "accountlist"<< endl;
            cout << "search" << endl;
            cout << "logout" << endl;
        }
        cout << "quit"<< endl;
        cout << "Select a function you would like to perform: ";*/
        if (!(cin >> choice)){
            break;
        }

        for (auto& x : choice){
            x = tolower(x);
        }

        auto command = inputMap.find(choice);
        if (command != inputMap.end()){
            switch (command->second){
                case login:
                    if (session == false){
                        cout << "Enter username:" << endl;
                        string username = "";
                        cin >> username;
                        currentUser = userLogin(username, accounts);

                        if (currentUser == nullptr){
                            //cout << "Login failed: no such user found." << endl;
                        } else {
                            //cout << "Login successful." << endl;
                            //ofstream log(transactionOutput, ios::trunc);
                            session = true;
                        }
                        break;
                    } else {
                        //cout << "Permission denied"<< endl;
                        break;
                    }
                case buy:
                    if (session == true && dynamic_cast<StandardBuyer*>(currentUser) != nullptr){
                        string gameTitle;
                        string sellerName;
                        cout << "Enter game name:"<< endl;
                        cin >> gameTitle;
                        cout << "Enter seller username:"<< endl;
                        cin >> sellerName;
                        StandardBuyer* buyer = dynamic_cast<StandardBuyer*>(currentUser);
                        buyer->buy(gameTitle, sellerName, games, gamesCollection, accounts);
                        /*if (games database contains gameTitle && currentUser balance >= game price)
                        currentUser.buy(game)*/ 
                        break;
                    } else {
                        cout << "Permission denied"<< endl;
                        break;
                    }
                case sell:
                    if (session == true && dynamic_cast<StandardSeller*>(currentUser) != nullptr){
                        string gameTitle;
                        double price;
                        cout << "Enter game name:" << endl;
                        cin >> gameTitle;
                        cout << "Enter price:" << endl;
                        cin >> price;
                        StandardSeller* sellerUser = dynamic_cast<StandardSeller*>(currentUser);
                        sellerUser->sell(gameTitle, price, sellerUser->getUserName(), games);
                        //new game* = Game(gameTitle, price, currentUser.username());
                        //write new game to the csv file
                        break;
                    } else {
                        cout << "Permission denied"<< endl;
                        break;
                    }
                case refund:
                    if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                        double price;
                        string sellerUsername;
                        string buyerUsername;
                        cout << "Enter buyer username:" << endl;
                        cin >> buyerUsername;
                        cout << "Enter seller username:" << endl;
                        cin >> sellerUsername;
                        cout << "Enter refund amount:" << endl;
                        cin >> price;
                        if (sellerUsername.length() <= 15 && buyerUsername.length() <= 15){
                            Admin* adminUser = dynamic_cast<Admin*>(currentUser);
                            adminUser->processRefund(buyerUsername, sellerUsername, price, accounts);
                            //TODO: output to the daily transaction file
                        }
                    } else {
                        cout << "Permission denied"<< endl;
                        break;
                    }
                case create:
                    if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                        string userType;
                        string newUsername;
                        //cout << "\nEnter a username for the account you wish to create (Must be 15 characters or less): ";
                        cout << "Enter new username:" << endl;
                        cin >> newUsername;
                        //cout << "\nWhat type of user is this?\nBS => standard buy\nSS => standard sell\nFS => standard full\nAA => admin" << endl;
                        cout << "Enter user type (AA/FS/BS/SS):"<< endl;
                        cin >> userType;
                        if (newUsername.length() <= 15){
                            Admin* adminUser = dynamic_cast<Admin*>(currentUser);
                            adminUser->create(newUsername, userType, accounts);
                            //TODO: output to the daily transaction file
                        } else {
                            //cout << "\nInvalid input: please try again.";
                        }
                        break;
                    } else {
                        cout << "Permission denied"<< endl;
                        break;
                    }
                case deleteuser:
                    if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                        string toDelete;
                        cout << "Enter username to delete:" << endl;
                        cin >> toDelete;
                        if (toDelete == currentUser->getUserName()){
                            //cout << "\nError: cannot delete the current user." << endl;
                            break;
                        } else {
                            Admin* adminUser = dynamic_cast<Admin*>(currentUser);
                            adminUser->deleteUser(toDelete, accounts);
                            //TODO: output to the daily transaction file
                        }
                        break;
                    } else {
                        cout << "Permission denied"<< endl;
                        break;
                    }
                case addcredit:
                    if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                        string toCredit;
                        double amount;
                        cout << "Enter username:" << endl;
                        cin >> toCredit;
                        cout << "Enter amount:" << endl;
                        cin >> amount;
                        Admin* adminUser = dynamic_cast<Admin*>(currentUser);
                        adminUser->addCredit(toCredit, amount, accounts);
                        //TODO: output to the daily transaction file
                        break;
                    } else {
                        double amount;
                        cout << "Enter amount:" << endl;
                        cin >> amount;
                        //do the other version of add credit
                        currentUser->addCredit(amount, accounts);
                        break;
                    }
                case accountlist:
                    if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                        //for (users in userlist){cout << userlist.username}
                        Admin* adminUser = dynamic_cast<Admin*>(currentUser);
                        adminUser->OutputAccountList(accounts);
                        break;
                    } else {
                        cout << "Permission denied"<< endl;
                        break;
                    }
                case gamelist:
                    if (session == true && currentUser != nullptr){
                        currentUser->OutputGameList(accounts);
                        break;
                    } else {
                        break;
                    }
                case usersearch: //TODO: implement this (we didnt)
                    if(session == true){
                        //print the thingy
                        break;
                    } else{
                        //print the other thingy
                        break;
                    }
                case logout:
                    if (session == true){
                        //cout << "\nLogging out..."<< endl;
                        currentUser = nullptr;
                        session = false;
                        break;
                    } else {
                        //cout << "\nError: not a valid input. No user is logged in."<< endl;
                        break;
                    }
                case quit:
                    //cout << "\nExiting program..." << endl;
                    break;
                default:
                    //cout << "\nError: not a valid input. Please enter a number from 1-9"<< endl;
                    break;
            }
        } 
    } while (choice != "quit");

    return 0;
}