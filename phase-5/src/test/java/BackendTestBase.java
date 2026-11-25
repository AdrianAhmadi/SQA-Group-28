import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Shared set-up for every Back End test.
abstract class BackendTestBase {

    protected File dir;
    protected File txn, accounts, games, collection;
    protected File newAccounts, newGames, newCollection;

    private File oTxn, oAcc, oGames, oColl, oNewAcc, oNewGames, oNewColl;
    private PrintStream oOut, oErr;
    private ByteArrayOutputStream outBuf, errBuf;

    @BeforeEach
    void baseSetUp() throws IOException {
        dir = Files.createTempDirectory("backend-test").toFile();

        oTxn = Backend.TransactionFile;
        oAcc = Backend.AccountList;
        oGames = Backend.GameList;
        oColl = Backend.GameCollection;
        oNewAcc = Backend.TextFilePrinter.newAccountFile;
        oNewGames = Backend.TextFilePrinter.newGamesListFile;
        oNewColl = Backend.TextFilePrinter.newGamesCollectionFile;

        txn = new File(dir, "transout.atf");
        accounts = new File(dir, "currentaccounts.txt");
        games = new File(dir, "availablegames.txt");
        collection = new File(dir, "gamescollection.txt");
        newAccounts = new File(dir, "newAccountList.txt");
        newGames = new File(dir, "newGamesList.txt");
        newCollection = new File(dir, "newGamesCollection.txt");

        Backend.TransactionFile = txn;
        Backend.AccountList = accounts;
        Backend.GameList = games;
        Backend.GameCollection = collection;
        Backend.TextFilePrinter.newAccountFile = newAccounts;
        Backend.TextFilePrinter.newGamesListFile = newGames;
        Backend.TextFilePrinter.newGamesCollectionFile = newCollection;

        oOut = System.out;
        oErr = System.err;
        outBuf = new ByteArrayOutputStream();
        errBuf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outBuf, true));
        System.setErr(new PrintStream(errBuf, true));
    }

    @AfterEach
    void baseTearDown() {
        System.setOut(oOut);
        System.setErr(oErr);
        Backend.TransactionFile = oTxn;
        Backend.AccountList = oAcc;
        Backend.GameList = oGames;
        Backend.GameCollection = oColl;
        Backend.TextFilePrinter.newAccountFile = oNewAcc;
        Backend.TextFilePrinter.newGamesListFile = oNewGames;
        Backend.TextFilePrinter.newGamesCollectionFile = oNewColl;
        deleteRecursively(dir);
    }

    protected String out() { return outBuf.toString(); }
    protected String err() { return errBuf.toString(); }

    protected static void write(File f, String... lines) throws IOException {
        Files.writeString(f.toPath(), String.join("\n", lines) + "\n");
    }

    protected static List<String> readLines(File f) throws IOException {
        return Files.readAllLines(f.toPath());
    }

    private static void deleteRecursively(File f) {
        if (f == null) return;
        File[] kids = f.listFiles();
        if (kids != null) for (File k : kids) deleteRecursively(k);
        f.delete();
    }

    protected static String pad(String s, int w) { return String.format("%-" + w + "s", s); }

    // currentaccounts.txt line: username(15) sp type(2) sp balance(9) 
    protected static String acctLine(String user, String type, String bal) {
        return pad(user, 15) + " " + type + " " + bal;
    }

    // availablegames.txt line: name(25) sp seller(15) sp price(6)
    protected static String gameLine(String name, String seller, String price) {
        return pad(name, 25) + " " + pad(seller, 15) + " " + price;
    }

    // gamescollection.txt line: name(26) sp owner(15) 
    protected static String collLine(String name, String owner) {
        return pad(name, 26) + " " + pad(owner, 15);
    }

    // ATF user records: 01 create, 02 delete, 06 addcredit, 00 end of session
    protected static String userRec(String code, String user, String type, String amount) {
        return code + " " + acctLine(user, type, amount);
    }
    protected static String sellRec(String name, String seller, String price) {
        return "03 " + pad(name, 26) + " " + pad(seller, 15) + " " + price;
    }
    protected static String buyRec(String name, String seller, String buyer, String price) {
        return "04 " + pad(name, 26) + " " + pad(seller, 15) + " " + pad(buyer, 15) + " " + price;
    }
    protected static String refundRec(String buyer, String seller, String amount) {
        return "05 " + pad(buyer, 15) + " " + pad(seller, 15) + " " + amount;
    }

    protected static Backend.TransactionManager manager(String... txnLines) {
        Backend.TransactionManager m = new Backend.TransactionManager();
        m.setAccountList(new HashMap<>());
        m.setGameList(new HashMap<>());
        m.setGameCollection(new ArrayList<>());
        m.setTransactionList(new ArrayList<>(List.of(txnLines)));
        return m;
    }

    protected static void account(Backend.TransactionManager m, String user, String type, String bal) {
        m.AccountList.put(user, new String[]{type, bal});
    }
}
