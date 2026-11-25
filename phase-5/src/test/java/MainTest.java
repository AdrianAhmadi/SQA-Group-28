import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// White box tests (statement coverage) for Backend.main.

class MainTest extends BackendTestBase {

    private void seedData() throws Exception {
        write(accounts, acctLine("admin01", "AA", "000500.00"),
                        acctLine("fulluser", "FS", "000100.00"),
                        acctLine("seller01", "SS", "000050.00"),
                        acctLine("buyer01", "BS", "000100.00"),
                        "END");
        write(games, gameLine("Spiderman", "seller01", "015.00"), "END");
        write(collection, collLine("Celeste", "fulluser"), "END");
    }

    private void runMain() throws Exception {
        ChildJvm.Result r = ChildJvm.run("main", txn, accounts, games, collection,
                                         newAccounts, newGames, newCollection);
        assertTrue(r.returnedNormally(), "Backend.main did not finish: " + r);
    }

    private List<String> lines(java.io.File f) throws Exception {
        return readLines(f).stream().filter(s -> !s.equals("END")).toList();
    }

    @Test @DisplayName("MAIN-01 empty transaction file -> data files are rewritten unchanged")
    void main_noTransactions() throws Exception {
        seedData();
        write(txn);
        runMain();
        assertEquals(4, lines(accounts).size());
        assertTrue(lines(accounts).contains(acctLine("admin01", "AA", "000500.00")));
    }

    @Test @DisplayName("MAIN-02 code 01 dispatches to create")
    void main_create() throws Exception {
        seedData();
        write(txn, userRec("01", "newplayer", "FS", "000000.00"));
        runMain();
        assertTrue(lines(accounts).contains(acctLine("newplayer", "FS", "000000.00")), lines(accounts).toString());
    }

    @Test @DisplayName("MAIN-03 code 02 dispatches to delete")
    void main_delete() throws Exception {
        seedData();
        write(txn, userRec("02", "fulluser", "FS", "000100.00"));
        runMain();
        assertEquals(3, lines(accounts).size());
        assertFalse(lines(accounts).contains(acctLine("fulluser", "FS", "000100.00")));
    }

    @Test @DisplayName("MAIN-04 code 03 dispatches to sell")
    void main_sell() throws Exception {
        seedData();
        write(txn, sellRec("Portal", "admin01", "025.00"));
        runMain();
        List<String> g = lines(games);
        assertEquals(2, g.size(), g.toString());
        assertTrue(g.stream().anyMatch(s -> s.startsWith("Portal") && s.contains("admin01") && s.endsWith("025.00")),
                g.toString());
    }

    @Test @DisplayName("MAIN-05 code 04 dispatches to buy")
    void main_buy() throws Exception {
        seedData();
        write(txn, buyRec("Spiderman", "seller01", "fulluser", "015.00"));
        runMain();
        assertTrue(lines(accounts).contains(acctLine("fulluser", "FS", "000085.00")), lines(accounts).toString());
        assertTrue(lines(accounts).contains(acctLine("seller01", "SS", "000065.00")), lines(accounts).toString());
        assertTrue(lines(collection).contains(collLine("Spiderman", "fulluser")), lines(collection).toString());
    }

    @Test @DisplayName("MAIN-06 code 05 dispatches to refund")
    void main_refund() throws Exception {
        seedData();
        write(txn, refundRec("buyer01", "seller01", "000010.00"));
        runMain();
        assertTrue(lines(accounts).contains(acctLine("buyer01", "BS", "000110.00")), lines(accounts).toString());
        assertTrue(lines(accounts).contains(acctLine("seller01", "SS", "000040.00")), lines(accounts).toString());
    }

    @Test @DisplayName("MAIN-07 code 06 dispatches to addCredit")
    void main_addCredit() throws Exception {
        seedData();
        write(txn, userRec("06", "fulluser", "FS", "000500.00"));
        runMain();
        assertTrue(lines(accounts).contains(acctLine("fulluser", "FS", "000600.00")), lines(accounts).toString());
    }

    @Test @DisplayName("MAIN-08 code 00 (end of session) matches no branch and changes nothing")
    void main_endOfSessionIgnored() throws Exception {
        seedData();
        write(txn, userRec("00", "admin01", "AA", "000500.00"));
        runMain();
        assertEquals(4, lines(accounts).size());
        assertTrue(lines(accounts).contains(acctLine("admin01", "AA", "000500.00")));
    }
}
