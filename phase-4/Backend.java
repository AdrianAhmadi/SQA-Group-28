import java.io.*;
import java.util.*;

public class Backend {

    public static void main(String[] args) {

        if (args.length == 4){
            String TransactionFile = args[0];
            String AccountList = args[1];
            String GameList = args[2];
            String GameCollection = args[3];

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

        } else {
            System.out.println("TODO: Add real error message here");
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
                System.out.println("ERROR: User does not exist.");
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
}

