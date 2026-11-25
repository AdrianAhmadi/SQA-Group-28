import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

// This backend program, once run, will automatically take transout.atf and update the files in data accordingly.

public class Backend {
    static File TransactionFile = new File("transout.atf");
    static File AccountList =new File("data\\currentaccounts.txt");
    static File GameList = new File("data\\availablegames.txt");
    static File GameCollection = new File("data\\gamescollection.txt");

    public static void main(String[] args) {
        // creates the transaction manager object so that the temporary versions of currentaccounts, availablegames and gamescollection can be stored while being operated on.
        TransactionManager manager = new TransactionManager();

        // reads the files into the manager using TextInterpreter class
        // could probably do this in the constructor but whatever
        manager.setAccountList(TextInterpreter.ReadAccountsFile());
        manager.setGameList(TextInterpreter.ReadAvailableGames());
        manager.setGameCollection(TextInterpreter.ReadGameCollection());
        manager.setTransactionList(TextInterpreter.ReadTransactionList());

        // the transaction list is transformed into an ArrayList of strings, each of which is an entire line from transout.atf
        // int i refers to the index of TransactionList, which every function needs so that they can find the transaction they need to perform
        // this for loop then iterates upon the TransactionList, in order, so that each operation is performed in the order it was added to the list
        for (int i = 0; i < manager.getTransactionList().size(); i++){
            int code = manager.TransactionCode(i);
            if (code == 1){
                manager.create(i);
            } else if (code == 2){
                manager.delete(i);
            } else if (code == 3){
                manager.sell(i);
            } else if (code == 4){
                manager.buy(i);
            } else if (code == 5){
                manager.refund(i);
            } else if (code == 6){
                manager.addCredit(i);
            }
        }
        
        // if TextFilePrinter successfully creates the new temp versions of the \data files, it then deletes the old versions and renames the temp ones to match the old names
        if (TextFilePrinter.createFiles(manager.getAccountList(), manager.getGameList(), manager.getGameCollection()) == true) {
            TextFilePrinter.replaceFiles();
        }
    }

    public static class TextInterpreter{

        // Need separate functions for reading each file since they are formatted differently.
        // Turning currentaccounts and availablegames into HashMaps allows us to search by key later on, which gamescollection and transout dont need
        public static HashMap<String,String[]> ReadAccountsFile(){
            HashMap<String,String[]> map = new HashMap<String,String[]>();
            try (BufferedReader br = new BufferedReader(new FileReader(AccountList))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    // usernames cannot have spaces so slice string from index 0 to first space
                    int nameEnd = line.indexOf(" ");
                    String username = line.substring(0, nameEnd);

                    //slice out usertype
                    String userType = line.substring(16, 18);

                    // The balance should always start at 19 Im pretty sure because 0-14 is username, 15 is a space, 16 and 17 are the usertype and 18 is a space
                    String balanceStr = line.substring(19, line.length()-1);

                    // put details in an array so that the hashmap can effectively have two items for the same key
                    // same method used for availablegames for seller and price
                    String[] details = {userType, balanceStr};
                    map.put(username, details);
                }
                br.close();
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return map;
        }

        public static HashMap<String,String[]> ReadAvailableGames(){
            HashMap<String,String[]> map = new HashMap<String,String[]>();
            try (BufferedReader br = new BufferedReader(new FileReader(GameList))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    // slice out the whole gamename section since you cant account for games with spaces in the title 
                    // (i.e. "Grand Theft Auto" with the username style of .indexOf(" ") slicing would give key "Grand", so "Grand Theft Auto 5" and "Grand Theft Auto 6" would have the same keys)
                    String gameName = line.substring(0, 25);
                    int sellerNameEnd = line.indexOf(" ", 26);
                    String sellerName = line.substring(26, sellerNameEnd);
                    String price = line.substring(42, line.length()-1);
                    String[] details = {sellerName, price};
                    map.put(gameName, details);
                }
                br.close();
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return map;
        }

        // Its not indicated whether or not refund actually needs to remove any games from the buyer's collection
        // and since the transaction log doesnt specify which game is being removed we kind of cant remove a game from their collection anyway? 
        // so gamescollection is functionally append-only which sucks but we can fix it later maybe
        public static ArrayList<String> ReadGameCollection(){
            ArrayList<String> collection = new ArrayList<String>();
            try (BufferedReader br = new BufferedReader(new FileReader(GameCollection))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    // just adds the entire line to the ArrayList since at no point do these items need to be edited
                    collection.add(line);
                }
                br.close();
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return collection;
        }

        public static ArrayList<String> ReadTransactionList(){
            ArrayList<String> transactions = new ArrayList<String>();
            try (BufferedReader br = new BufferedReader(new FileReader(TransactionFile))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    transactions.add(line);
                }
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return transactions;
        }
    }

    public static class TransactionManager{
        public HashMap<String,String[]> AccountList;
        public HashMap<String,String[]> GameList;
        public ArrayList<String> GameCollection;
        public ArrayList<String> TransactionList;

        public void create(int index){
            // get the string from the TransactionList arraylist
            String transaction = this.TransactionList.get(index);

            // slice the string starting at the first space after the username to get the username 
            // (technically unnecessary as shown with availablegames just using the full index including trailing spaces)
            int usernameEnd = transaction.indexOf(" ", 3);
            String username = transaction.substring(3, usernameEnd);

            // if the username does not already exist, create a new user in AccountList with that username, usertype and credit
            if (this.AccountList.containsKey(username) == false){
                String userType = transaction.substring(19, 21);
                String credit = transaction.substring(21, transaction.length()-1);
                String[] details = {userType, credit};
                this.AccountList.put(username, details);
            } else {
                // One of only two errors the backend should check for: cannot create a user with an existing username
                System.out.println("ERROR: Username already exists");
            }
        }

        //removes a key from the AccountList hashmap, effectively deleting the user from currentaccounts
        public void delete(int index){
            String transaction = this.TransactionList.get(index);
            int usernameEnd = transaction.indexOf(" ", 3);
            String username = transaction.substring(3, usernameEnd);
            if (this.AccountList.containsKey(username) == true){
                this.AccountList.remove(username);
            } else {
                System.out.println("ERROR: User does not exist");
            }
        }


        public void sell(int index){
            String transaction = this.TransactionList.get(index);

            // slice the indexes of the string which contain the game name field
            // we cannot slice at the first space since game titles often have spaces in them, so we must use the entire string to avoid having multiple items with the same key
            String gameName = transaction.substring(3, 29);
            int sellerNameEnd = transaction.indexOf(" ", 30);
            String sellerName = transaction.substring(30, sellerNameEnd);
            String price = transaction.substring(45, transaction.length()-1);
            String[] details = {sellerName, price};

            // add the game to availablegames. this technically also means you cant create a game with a name that already exists thanks to how hashmaps work
            // TODO: fix the implementation so it doesnt do that maybe
            this.GameList.put(gameName, details);
        }

        public void buy(int index){
            // slice the transaction string to get all the information (game name, seller name, buyer name and price)
            String transaction = this.TransactionList.get(index);
            String gamename = transaction.substring(3, 29);
            int sellerNameEnd = transaction.indexOf(" ", 30);
            String sellerName = transaction.substring(30, sellerNameEnd);
            int buyerNameEnd = transaction.indexOf(" ", 45);
            String buyerName = transaction.substring(45, buyerNameEnd);
            String priceStr = transaction.substring(61, transaction.length()-1);

            // convert the price and balances of the buyer and seller into doubles
            Double price = Double.parseDouble(priceStr);
            Double buyerBalance = Double.parseDouble(this.AccountList.get(buyerName)[1]);
            Double sellerBalance = Double.parseDouble(this.AccountList.get(sellerName)[1]);
            if (price < buyerBalance){
                // sets a decimal format so that, when turned into a string, the balances have the correct number of leading and trailing zeros
                // this was not necessary for sell() because the input should already be in the correct format, 
                // whereas we undo that formatting by converting from a string to a double
                DecimalFormat df = new DecimalFormat("000000.00");

                // honestly I dont know if this changes the original item Im pretty sure it does but we will find out in testing I guess
                buyerBalance -= price;
                this.AccountList.get(buyerName)[1] = df.format(buyerBalance);

                sellerBalance += price;
                this.AccountList.get(sellerName)[1] = df.format(sellerBalance);

                // correctly format the username with trailing spaces for adding a new item to GameCollection
                int count = 15 - buyerName.length();
                String collectionItem = gamename + ' ' + buyerName + String.valueOf(' ').repeat(count);
                this.GameCollection.add(collectionItem);
            } else {
                // The other error this program checks for: cannot buy a game if you have insufficient funds
                System.out.println("ERROR: User balance cannot be negative");
            }
        }

        public void refund(int index){

        }

        public void addCredit(int index){
            // This function currently is implemented as if the transaction file's credit field is the total amount of credit for that account
            // This should be true if the document is correct but I am unsure
            String transaction = this.TransactionList.get(index);
            int usernameEnd = transaction.indexOf(" ", 3);
            String username = transaction.substring(3, usernameEnd);
            if (this.AccountList.containsKey(username) == true){
                String userType = transaction.substring(19, 21);
                String credit = transaction.substring(21, transaction.length()-1);
                String[] details = {userType, credit};
                this.AccountList.put(username, details);
            } else {
                System.out.println("ERROR: Username already exists");
            }
        }
        
        // blank constructor
        TransactionManager(){};

        // slices out the transaction code in transactionlist so that you can call the correct operation in the loop
        public int TransactionCode(int index){
            String codeStr = this.TransactionList.get(index).substring(0, 2);
            int code = Integer.parseInt(codeStr);
            return code;
        }

        // getters and setters
        public void setAccountList(HashMap<String,String[]> AccountList){
            this.AccountList = AccountList;
        }

        public HashMap<String,String[]> getAccountList(){
            return this.AccountList;
        }

        public void setGameList(HashMap<String,String[]> GameList){
            this.GameList = GameList;
        }

        public HashMap<String,String[]> getGameList(){
            return this.GameList;
        }

        public void setGameCollection(ArrayList<String> GameCollection){
            this.GameCollection = GameCollection;
        }

        public ArrayList<String> getGameCollection(){
            return this.GameCollection;
        }

        public void setTransactionList(ArrayList<String> TransactionList){
            this.TransactionList = TransactionList;
        }

        public ArrayList<String> getTransactionList(){
            return this.TransactionList;
        }
    }

    // class to print and create text files for our database
    public class  TextFilePrinter {
        static File newAccountFile = new File("data\\newAccountList.txt");
        static File newGamesListFile = new File("data\\newGamesList.txt");
        static File newGamesCollectionFile = new File("data\\newGamesCollection.txt");

        // create and write into a temp txt file
        public static boolean createFiles(HashMap<String, String[]> accountList, HashMap<String, String[]> gameList, ArrayList<String> gameCollection) {
            

            // accounts list writer
            try {
                BufferedWriter accountWriter = new BufferedWriter(new FileWriter(newAccountFile));

                for (String key : accountList.keySet()) {
                    String line = key;
                    // adds spaces to make the total length of the username + trailing spaces 16 characters (15 from the username and one to seperate the user type code)
                    line += String.valueOf(" ").repeat(16-key.length());
                    // adds usertype, a space, and the balance, then writes it to the file
                    line += accountList.get(key)[0] + " " + accountList.get(key)[1];
                    accountWriter.write(line);
                    accountWriter.newLine();
                }
                // once all users have been added to the file, writes "END" and closes the file
                accountWriter.write("END");
                accountWriter.close();
                
            } catch (IOException e){
                System.out.println("Error creating new accounts file");
            }

            // game list writer
            try {
                BufferedWriter gamewriter = new BufferedWriter(new FileWriter(newGamesListFile));

                for (String key : gameList.keySet()) {
                    String line = key;
                    // gets the game name, then adds a space, the sellers name, the appropriate amount of spaces so that the sellers name + spaces = 16 characters, and the price
                    line += " " + gameList.get(key)[0] + String.valueOf(" ").repeat(16-gameList.get(key)[0].length()) + gameList.get(key)[1];
                    gamewriter.write(line);
                    gamewriter.newLine();
                }
                // once all games have been added from the hashmap, writes "END" and closes the file
                gamewriter.write("END");
                gamewriter.close();
                
            } catch (IOException e){
                System.out.println("Error creating new available games file");
            }

            // games collection writer
            try {
                BufferedWriter collectionWriter = new BufferedWriter(new FileWriter(newGamesCollectionFile));

                for (String game : gameCollection) {
                    collectionWriter.write(game);
                    collectionWriter.newLine();
                }

                collectionWriter.write("END");
                collectionWriter.close();
                
            } catch (IOException e){
                System.out.println("Error creating new available games file");
            }

            if (newAccountFile.exists() && newGamesListFile.exists() && newGamesCollectionFile.exists()) {
                return true; // returns true if all the temp files were successfully created, otherwise returns false
            } else {
                return false;
            }
        }

        // deletes the old files and renames the temp ones to the correct names
        public static void replaceFiles(){
            AccountList.delete();
            boolean accountflag = newAccountFile.renameTo(AccountList);
            GameList.delete();
            boolean gameflag = newGamesListFile.renameTo(GameList);
            GameCollection.delete();
            boolean collectionflag = newGamesCollectionFile.renameTo(GameCollection);

            if (accountflag == false || gameflag == false || collectionflag == false){
                System.err.println("ERROR: File renaming unsuccessful");
            }
        }
    }
}

