import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


// White box tests (statement coverage) for Backend.TextInterpreter
// open file, loop over lines, catch IOException.

class TextInterpreterTest extends BackendTestBase {

    // ReadAccountsFile

    @Test @DisplayName("TI-ACC-01 valid accounts file is read into username -> {type, balance}")
    void readAccounts_validFile() throws Exception {
        write(accounts, acctLine("admin01", "AA", "000500.00"),
                        acctLine("fulluser", "FS", "000100.00"),
                        "END");
        HashMap<String, String[]> map = Backend.TextInterpreter.ReadAccountsFile();
        assertEquals(2, map.size());
        assertArrayEquals(new String[]{"AA", "000500.00"}, map.get("admin01"));
        assertArrayEquals(new String[]{"FS", "000100.00"}, map.get("fulluser"));
    }

    @Test @DisplayName("TI-ACC-02 missing accounts file -> error message and empty map")
    void readAccounts_missingFile() {
        HashMap<String, String[]> map = Backend.TextInterpreter.ReadAccountsFile();
        assertTrue(map.isEmpty());
        assertTrue(out().contains("Error reading file"));
    }

    // ReadAvailableGames

    @Test @DisplayName("TI-GAME-01 valid games file is read into name -> {seller, price}")
    void readGames_validFile() throws Exception {
        write(games, gameLine("Hades", "seller01", "024.99"),
                     gameLine("Celeste", "admin01", "019.99"),
                     "END");
        HashMap<String, String[]> map = Backend.TextInterpreter.ReadAvailableGames();
        assertEquals(2, map.size());
        assertArrayEquals(new String[]{"seller01", "024.99"}, map.get(pad("Hades", 25)));
        assertArrayEquals(new String[]{"admin01", "019.99"}, map.get(pad("Celeste", 25)));
    }

    @Test @DisplayName("TI-GAME-02 missing games file -> error message and empty map")
    void readGames_missingFile() {
        HashMap<String, String[]> map = Backend.TextInterpreter.ReadAvailableGames();
        assertTrue(map.isEmpty());
        assertTrue(out().contains("Error reading file"));
    }

    // ReadGameCollection

    @Test @DisplayName("TI-COLL-01 valid collection file is read line by line (END not stored)")
    void readCollection_validFile() throws Exception {
        write(collection, collLine("Celeste", "fulluser"), collLine("Hades", "buyer01"), "END");
        ChildJvm.Result r = ChildJvm.run("collection", txn, accounts, games, collection,
                                         newAccounts, newGames, newCollection);
        assertTrue(r.returnedNormally(), "ReadGameCollection did not return: " + r);
        assertTrue(r.output.contains("SIZE=2"), r.toString());
        assertTrue(r.output.contains("ITEM=[" + collLine("Celeste", "fulluser") + "]"), r.toString());
    }

    @Test @DisplayName("TI-COLL-02 missing collection file -> error message and empty list")
    void readCollection_missingFile() {
        ArrayList<String> list = Backend.TextInterpreter.ReadGameCollection();
        assertTrue(list.isEmpty());
        assertTrue(out().contains("Error reading file"));
    }

    // ReadTransactionList

    @Test @DisplayName("TI-TXN-01 valid transaction file is read line by line (END not stored)")
    void readTransactions_validFile() throws Exception {
        write(txn, userRec("01", "newplayer", "FS", "000000.00"),
                   userRec("00", "admin01", "AA", "000500.00"));
        ChildJvm.Result r = ChildJvm.run("transactions", txn, accounts, games, collection,
                                         newAccounts, newGames, newCollection);
        assertTrue(r.returnedNormally(), "ReadTransactionList did not return: " + r);
        assertTrue(r.output.contains("SIZE=2"), r.toString());
    }

    @Test @DisplayName("TI-TXN-02 missing transaction file -> error message and empty list")
    void readTransactions_missingFile() {
        ArrayList<String> list = Backend.TextInterpreter.ReadTransactionList();
        assertTrue(list.isEmpty());
        assertTrue(out().contains("Error reading file"));
    }
}
