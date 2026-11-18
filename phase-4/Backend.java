import java.io.*;
import java.util.*;


public class Backend {
    public static int nameSize = 15;
    public static int typeSize = 2;
    public static int creditSize = 9;

    public static void main(String[] args) {

        if (args.length == 4){
            String TransactionFile = args[0];
            String AccountList = args[1];
            String GameList = args[2];
            String GameCollection = args[3];


            HashMap<String,String[]> accounts;
        } else {
            System.out.println("TODO: Add real error message here");
        }
        
    }

    static class TextInterpreter{
        public HashMap<String,String[]> ReadAccountsFile(String filename){
            HashMap<String,String[]> map = new HashMap<String,String[]>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    int nameEnd = line.indexOf(" ");
                    String username = line.substring(0, nameEnd);
                    String userType = line.substring(16, 18);
                    String balance = line.substring(19, line.length()-1);
                    String[] details = {userType, balance};
                    map.put(username, details);
                }
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return map;
        }

        public HashMap<String,String[]> ReadAvailableGames(String filename){
            HashMap<String,String[]> map = new HashMap<String,String[]>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))){
                String line;
                while ((line = br.readLine()) != "END" || line != null){
                    int gameNameEnd = line.indexOf(" ");
                    String gameName = line.substring(0, gameNameEnd);
                    int sellerNameEnd = line.indexOf(" ", 27);
                    String sellerName = line.substring(27, sellerNameEnd);
                    String price = line.substring(43, line.length()-1);
                    String[] details = {sellerName, price};
                    map.put(gameName, details);
                }
            } catch (IOException e){
                System.out.println("Error reading file");
            }
            return map;
        }

        public ArrayList<String> ReadGameCollection(String filename){
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
    }

    static class ATFInterpreter{

    }
}

