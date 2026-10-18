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

int main(){
    int session = NULL;
    User current = NULL;
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
        cout << "Main Menu";
        cout << "Enter the number of the option you would like to select:";
        cout << "1. Login";
        if (session != NULL){
            cout << "2. Buy";
            cout << "3. Sell";
            cout << "4. Refund";
            cout << "5. Create";
            cout << "6. Delete";
            cout << "7. Add Credit";
            cout << "8. Account List";
            cout << "9. Logout";
            cout << "0. Exit";
        }
    cin >> choice;

    if (cin.fail()){
        cout << "Error: please enter a number from 1-9";
        cin.clear();
        continue;
    }

    switch (choice){
        case 1:
            if (session == NULL){
                cout << "Enter your username:";
                string username = "";
                cin >> username;
                User* currentUser = login(username);
                session = 1;
                break;
            } else {
                cout << "\nError: not a valid input. A user is already logged in";
                break;
            }
        case 2:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 3:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 4:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 5:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 6:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 7:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 8:
            if (session != NULL){
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 9:
            if (session != NULL){
                session = NULL;
                break;
            } else {
                cout << "\nError: not a valid input. Please log in before proceeding.";
                break;
            }
        case 0:
            if (session == NULL){
                cout << "\nExiting program...";
                break;
            } else {
                cout << "\nError: please log out before closing the program.";
                break;
            }
        default:
            cout << "\nError: not a valid input. Please enter a number from 1-9";
            break;
        }
    } while (choice != 0);
    
    
}

User* login(string username){
    User* currentUser = new User(username);
    return currentUser;
}