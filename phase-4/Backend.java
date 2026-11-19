import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class Backend {
    static String TransactionFile = "transout.atf";
    static String AccountList = "data\\currentaccounts.txt";
    static String GameList = "data\\availablegames.txt";
    static String GameCollection = "data\\gamescollection.txt";

    public static void main(String[] args) {
        TransactionManager manager = new TransactionManager();


            manager.setAccountList(TextInterpreter.ReadAccountsFile(AccountList));
            manager.setGameList(TextInterpreter.ReadAvailableGames(GameList));
            manager.setGameCollection(TextInterpreter.ReadGameCollection(GameCollection));
            manager.setTransactionList(TextInterpreter.ReadTransactionList(TransactionFile));


            for (int i = 0; i < manager.getTransactionList().size(); i++){
                int code = manager.TransactionCode(i);
                if (code == 1){
                    manager.create(i);
                }
            }

            if (TextFilePrinter.createFiles(manager.getAccountList(), manager.getGameList(), manager.getGameCollection()) == true) {
                // replace old files with new data files
            }
    }

    public static class TextInterpreter{
        public static HashMap<String,String[]> ReadAccountsFile(String filename){
            HashMap<String,String[]> map = new HashMap<String,String[]>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    int nameEnd = line.indexOf(" ");
                    String username = line.substring(0, nameEnd);
                    String userType = line.substring(16, 18);
                    int balanceStart = line.indexOf("0", 16);
                    String balanceStr = line.substring(balanceStart, line.length()-1);
                    String[] details = {userType, balanceStr};
                    map.put(username, details);
                }
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return map;
        }

        public static HashMap<String,String[]> ReadAvailableGames(String filename){
            HashMap<String,String[]> map = new HashMap<String,String[]>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    String gameName = line.substring(0, 25);
                    int sellerNameEnd = line.indexOf(" ", 26);
                    String sellerName = line.substring(26, sellerNameEnd);
                    String price = line.substring(42, line.length()-1);
                    String[] details = {sellerName, price};
                    map.put(gameName, details);
                }
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return map;
        }
        public static ArrayList<String> ReadGameCollection(String filename){
            ArrayList<String> collection = new ArrayList<String>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    collection.add(line);
                }
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return collection;
        }

        public static ArrayList<String> ReadTransactionList(String filename){
            ArrayList<String> transactions = new ArrayList<String>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))){
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

        public static int nameSize = 15;
        public static int gameNameSize = 25;
        public static int typeSize = 2;
        public static int creditSize = 9;

        public void create(int index){
            String transaction = this.TransactionList.get(index);
            int usernameEnd = transaction.indexOf(" ", 3);
            String username = transaction.substring(3, usernameEnd);
            if (this.AccountList.containsKey(username) == false){
                String userType = transaction.substring(19, 21);
                String credit = transaction.substring(21, transaction.length()-1);
                String[] details = {userType, credit};
                this.AccountList.put(username, details);
            } else {
                System.out.println("ERROR: Username already exists");
            }
        }

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
            String gameName = transaction.substring(3, 29);
            int sellerNameEnd = transaction.indexOf(" ", 30);
            String sellerName = transaction.substring(30, sellerNameEnd);
            String price = transaction.substring(45, transaction.length()-1);
            String[] details = {sellerName, price};
            this.GameList.put(gameName, details);
        }

        public void buy(int index){
            String transaction = this.TransactionList.get(index);
            String gamename = transaction.substring(3, 29);
            int sellerNameEnd = transaction.indexOf(" ", 30);
            String sellerName = transaction.substring(30, sellerNameEnd);
            int buyerNameEnd = transaction.indexOf(" ", 45);
            String buyerName = transaction.substring(45, buyerNameEnd);
            String priceStr = transaction.substring(61, transaction.length()-1);
            Double price = Double.parseDouble(priceStr);
            Double buyerBalance = Double.parseDouble(this.AccountList.get(buyerName)[1]);
            Double sellerBalance = Double.parseDouble(this.AccountList.get(sellerName)[1]);
            if (price < buyerBalance){
                DecimalFormat df = new DecimalFormat("000.00");

                buyerBalance -= price;
                this.AccountList.get(buyerName)[1] = df.format(buyerBalance);

                sellerBalance += price;
                this.AccountList.get(sellerName)[1] = df.format(sellerBalance);

                int count = 15 - buyerName.length();
                String collectionItem = gamename + ' ' + buyerName + String.valueOf(' ').repeat(count);
                this.GameCollection.add(collectionItem);
            } else {
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

        TransactionManager(){};

        public int TransactionCode(int index){
            String codeStr = this.TransactionList.get(index).substring(0, 2);
            int code = Integer.parseInt(codeStr);
            return code;
        }

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
        // create and write into a temp txt file
        public static boolean createFiles(HashMap<String, String[]> accountList, HashMap<String, String[]> gameList, ArrayList<String> gameCollection) {
            File newAccountFile = new File("data\\newAccountList.txt");
            File newGamesListFile = new File("data\\newGamesList.txt");
            File newGamesCollectionFile = new File("data\\newGamesCollection.txt");

            // accounts list writer
            try {
                BufferedWriter accountWriter = new BufferedWriter(new FileWriter(newAccountFile));

                for (String key : accountList.keySet()) {
                    String line = key;
                    line += String.valueOf(" ").repeat(16-key.length());
                    line += accountList.get(key)[0] + " " + accountList.get(key)[1];
                    accountWriter.write(line);
                    accountWriter.newLine();
                }

                accountWriter.write("END");
                accountWriter.close();
                
            } catch (IOException e){
                System.out.println("Error reading file");
            }

            // game list writer
            try {
                BufferedWriter gamewriter = new BufferedWriter(new FileWriter(newGamesListFile));

                for (String key : gameList.keySet()) {
                    String line = key;
                    line += String.valueOf(" ").repeat(16-key.length());
                    line += gameList.get(key)[0] + String.valueOf(" ").repeat(16-gameList.get(key)[0].length()) + gameList.get(key)[1];
                    gamewriter.write(line);
                    gamewriter.newLine();
                }

                gamewriter.write("END");
                gamewriter.close();
                
            } catch (IOException e){
                System.out.println("Error reading file");
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
                System.out.println("Error reading file");
            }

            if (newAccountFile.exists() && newGamesListFile.exists() && newGamesCollectionFile.exists()) {
                return true;
            } else {
                return false;
            }
        }
    }
}

