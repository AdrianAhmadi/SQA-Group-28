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

enum Inputs {login, buy, sell, refund, create, deleteuser, addcredit, accountlist, search, logout, quit};

const map<string, Inputs> inputMap = {{"login", login}, {"buy", buy}, {"sell", sell}, {"refund", refund}, {"create", create},{"delete", deleteuser}, {"addcredit", addcredit},
{"accountlist", accountlist}, {"search", search}, {"logout", logout}, {"quit", quit}};

User* userLogin(string username, string accounts){
    //TODO: check the csv file for user permissions, then make switch block to call the appropriate constructor
    // Open the AccountList.csv to check if the username exists in the database, and what permissions it has
    fstream AccountList;
    AccountList.open(accounts);

    if (!AccountList.is_open()){
        cerr << "Error: Could not open account file." << endl;
        return nullptr;
    }

    //placeholder variables that are used within the while loop
    User* currentUser = nullptr;
    vector<string> row; //
    string line, word, temp;
    int perms = -1;

    while (getline(AccountList, line)){
        row.clear();

        stringstream s(line);

        while (getline(s, word, ',')){
            row.push_back(word);
        }

        if (row.size() >= 3 && row[0] == username){
            //convert the vector field for user perms into an integer for the switch case (this isnt strictly necessary but its simpler to do)
            perms = stoi(row[2]);

            //switch case decides which constructor is called based on the permissions flag in the accountlist
            switch (perms){
                case 0:
                    currentUser = new Admin(username);
                    break;
                case 1:
                    currentUser = new StandardBuyer(username);
                    break;
                case 2:
                    currentUser = new StandardSeller(username);
                    break;
                case 3:
                    currentUser = new StandardFull(username);
                    break;
                default:
                    cout << "Error: Permissions flag is not a valid integer input for userLogin function." << endl;
                    break;
            }
            break;
        }
    }
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
    // string accounts = argv[1];
    // string games = argv[2];
    // string gamesCollection = argv[3];
    // string transactionOutput = argv[4];
    string accounts, games, gamesCollection, transactionOutput = "../data/";
    accounts.append(argv[1]);
    games.append(argv[2]);
    gamesCollection.append(argv[3]);
    transactionOutput.append(argv[4]);
    do
    {
        cout << "Main Menu"<< endl;
        cout << "Select a function you would like to perform:"<< endl;
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
    cin >> choice;
    for (auto& x : choice){
        x = tolower(x);
    }
    if (cin.fail()){
        cout << "Error: please enter a number from 0-10"<< endl;
        cin.clear();
        continue;
    }
    auto command = inputMap.find(choice);
    if (command != inputMap.end()){
        switch (command->second){
            case login:
                if (session == false){
                    cout << "Enter your username:";
                    string username = "";
                    cin >> username;
                    currentUser = userLogin(username, accounts);

                    if (currentUser != nullptr){
                        cout << "Login successful" << endl;
                        session = true;
                    }
                    break;
                } else {
                    cout << "\nError: not a valid input. A user is already logged in"<< endl;
                    break;
                }
            case buy:
                if (session == true && dynamic_cast<StandardBuyer*>(currentUser) != nullptr){
                    string gameTitle;
                    cout << "\nEnter the name of the game you wish to buy: "<< endl;
                    cin >> gameTitle;
                    /*if (games database contains gameTitle && currentUser balance >= game price)
                    currentUser.buy(game)*/ 
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case sell:
                if (session == true && dynamic_cast<StandardSeller*>(currentUser) != nullptr){
                    string gameTitle;
                    double price;
                    cout << "\nEnter the title of the game you wish to sell: ";
                    cin >> gameTitle;
                    cout << "\nEnter the price of", cout << gameTitle;
                    cin >> price;
                    //new game* = Game(gameTitle, price, currentUser.username());
                    //write new game to the csv file
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case refund:
                if (session == true){
                    //man this one is gonna suck ass I dont wanna actually do it
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case create:
                if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                    string userType;
                    string newUsername;
                    cout << "\nEnter a username for the account you wish to create.";
                    cin >> newUsername;
                    cout << "\nWhat type of user is this? ('standard buy', 'standard sell', 'standard full', 'admin')";
                    cin >> userType;
                    //if username does not exist and username is shorter than 15 characters or whatever
                    //User newUser = currentUser.create(newUsername, userType);
                    //write newUser to AccountList.csv
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case deleteuser:
                if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                    string toDelete;
                    cout << "\nEnter the username of the user you wish to delete:";
                    cin >> toDelete;
                    //if (user exists){delete(toDelete)}
                    //remove toDelete row from AccountList.csv
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case addcredit:
                if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                    string toCredit;
                    double amount;
                    cout << "\nEnter the username of the account you want to add credit to: ";
                    cin >> toCredit;
                    cout << "\nHow much credit would you like to add: ";
                    cin >> amount;
                    //if user toCredit exists, change the credit value in the csv file
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case accountlist:
                if (session == true && dynamic_cast<Admin*>(currentUser) != nullptr){
                    //for (users in userlist){cout << userlist.username}
                    break;
                } else {
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case search:
                if(session == true){
                    //print the thingy
                    break;
                } else{
                    cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                    break;
                }
            case logout:
                if (session == true){
                    cout << "\nLogging out..."<< endl;
                    currentUser = nullptr;
                    session = false;
                    break;
                } else {
                    cout << "\nError: not a valid input. No user is logged in."<< endl;
                    break;
                }
            case quit:
                if (session){
                    cout << "\nExiting program...";
                    break;
                } else {
                    cout << "\nError: please log out before closing the program.";
                    break;
                }
            default:
                cout << "\nError: not a valid input. Please enter a number from 1-9"<< endl;
                break;
            }
        } 
    }    while (choice != "quit");
        
}