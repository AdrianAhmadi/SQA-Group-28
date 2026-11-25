import java.io.File;
import java.util.ArrayList;

// runs one Back End entry point in its own JVM

public class Probe {
    public static void main(String[] a) {
        Backend.TransactionFile = new File(a[1]);
        Backend.AccountList = new File(a[2]);
        Backend.GameList = new File(a[3]);
        Backend.GameCollection = new File(a[4]);
        Backend.TextFilePrinter.newAccountFile = new File(a[5]);
        Backend.TextFilePrinter.newGamesListFile = new File(a[6]);
        Backend.TextFilePrinter.newGamesCollectionFile = new File(a[7]);
        try {
            switch (a[0]) {
                case "collection": {
                    ArrayList<String> r = Backend.TextInterpreter.ReadGameCollection();
                    System.out.println("SIZE=" + r.size());
                    for (String s : r) System.out.println("ITEM=[" + s + "]");
                    break;
                }
                case "transactions": {
                    ArrayList<String> r = Backend.TextInterpreter.ReadTransactionList();
                    System.out.println("SIZE=" + r.size());
                    for (String s : r) System.out.println("ITEM=[" + s + "]");
                    break;
                }
                case "main":
                    Backend.main(new String[0]);
                    System.out.println("MAIN_RETURNED");
                    break;
                default:
                    System.out.println("UNKNOWN_MODE");
                    System.exit(4);
            }
        } catch (OutOfMemoryError e) {
            System.out.println("THROWN=OutOfMemoryError");
            System.exit(3);
        } catch (Throwable t) {
            System.out.println("THROWN=" + t.getClass().getName());
            System.exit(2);
        }
    }
}
