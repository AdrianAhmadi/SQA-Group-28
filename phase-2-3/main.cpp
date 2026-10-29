#include <iostream>
#include <fstream>
#include <sstream>
#include <unordered_map>
#include <string>

#include "Classes\\User.cpp"
#include "Classes\\Admin.cpp"
#include "Classes\\StandardBuyer.cpp"
#include "Classes\\StandardSeller.cpp"
#include "Classes\\StandardFull.cpp"
#include "Classes\\Game.cpp"

using namespace std;


User* userLogin(string username){
    //TODO: check the csv file for user permissions, then make switch block to call the appropriate constructor
    User* currentUser = new User(username);
    return currentUser;
}



int main(){
    bool session = false;
    User* currentUser = nullptr;
    int choice = -1;
    string accounts = "AccountList.csv";
    string games = "GameList.csv";
    /*if(ifstream inputFileStream{accounts}; inputFileStream) {
        string name;
        double balance;
        int userType;

        while (getline(inputFileStream >> ws, name, ',') >> balance >> "," >> userType){

        }
    } else {
        cerr << "Error, could not open accounts file.";
    }
    */

    do
    {
        cout << "Main Menu"<< endl;
        cout << "Enter the number of the option you would like to select:"<< endl;
        cout << "1. Login"<< endl;
        if (!session){
            cout << "2. Buy" << endl;
            cout << "3. Sell"<< endl;
            cout << "4. Refund"<< endl;
            cout << "5. Create"<< endl;
            cout << "6. Delete"<< endl;
            cout << "7. Add Credit"<< endl;
            cout << "8. Account List"<< endl;
            cout << "9. Logout"<< endl;
            cout << "0. Exit"<< endl;
        }
    cin >> choice;

    if (cin.fail()){
        cout << "Error: please enter a number from 1-9"<< endl;
        cin.clear();
        continue;
    }

    switch (choice){
        case 1:
            if (session){
                cout << "Enter your username:";
                string username = "";
                cin >> username;
                currentUser = userLogin(username);

                if (currentUser != nullptr){
                    session = 1;
                }
                break;
            } else {
                cout << "\nError: not a valid input. A user is already logged in"<< endl;
                break;
            }
        case 2:
            if (!session && dynamic_cast<StandardBuyer*>(currentUser) != nullptr){
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
        case 3:
            if (!session && dynamic_cast<StandardSeller*>(currentUser) != nullptr){
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
        case 4:
            if (!session){
                //man this one is gonna suck ass I dont wanna actually do it
                break;
            } else {
                cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                break;
            }
        case 5:
            if (!session && dynamic_cast<Admin*>(currentUser) != nullptr){
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
        case 6:
            if (!session && dynamic_cast<Admin*>(currentUser) != nullptr){
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
        case 7:
            if (!session && dynamic_cast<Admin*>(currentUser) != nullptr){
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
        case 8:
            if (!session && dynamic_cast<Admin*>(currentUser) != nullptr){
                //for (users in userlist){cout << userlist.username}
                break;
            } else {
                cout << "\nError: not a valid input. You do not have sufficient permissions."<< endl;
                break;
            }
        case 9:
            if (!session){
                cout << "\nLogging out..."<< endl;
                currentUser = nullptr;
                session = false;
                break;
            } else {
                cout << "\nError: not a valid input. No user is logged in."<< endl;
                break;
            }
        case 0:
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
    } while (choice != 0);
    
    
}