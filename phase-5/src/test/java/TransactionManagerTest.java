import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


// white box tests (statement coverage) for Backend.TransactionManager.
// test ids (TM-xxx-nn) match the test-case table in the report.

class TransactionManagerTest extends BackendTestBase {

    private static void assertAccount(Backend.TransactionManager m, String user, String type, String bal) {
        String[] d = m.AccountList.get(user);
        assertNotNull(d, "account '" + user + "' should exist");
        assertEquals(type, d[0], "account type of " + user);
        assertEquals(bal, d[1], "balance of " + user);
    }

    // TransactionCode

    @Test @DisplayName("TM-CODE-01 two-digit code is parsed to an int")
    void transactionCode_parsesCode() {
        Backend.TransactionManager m = manager(userRec("01", "newplayer", "FS", "000000.00"),
                                               sellRec("Portal", "admin01", "025.00"),
                                               userRec("00", "admin01", "AA", "000500.00"));
        assertEquals(1, m.TransactionCode(0));
        assertEquals(3, m.TransactionCode(1));
        assertEquals(0, m.TransactionCode(2));
    }

    @Test @DisplayName("TM-CODE-02 non-numeric code throws NumberFormatException")
    void transactionCode_nonNumeric() {
        Backend.TransactionManager m = manager("XX garbage");
        assertThrows(NumberFormatException.class, () -> m.TransactionCode(0));
    }

    @Test @DisplayName("TM-CODE-03 index outside the list throws IndexOutOfBoundsException")
    void transactionCode_badIndex() {
        Backend.TransactionManager m = manager();
        assertThrows(IndexOutOfBoundsException.class, () -> m.TransactionCode(0));
    }

    // create

    @Test @DisplayName("TM-CREATE-01 new username is added with its type and credit")
    void create_newUser() {
        Backend.TransactionManager m = manager(userRec("01", "newplayer", "FS", "000000.00"));
        m.create(0);
        assertAccount(m, "newplayer", "FS", "000000.00");
        assertEquals("", out(), "no error expected");
    }

    @Test @DisplayName("TM-CREATE-02 new admin with credit keeps the full credit field")
    void create_newUserWithCredit() {
        Backend.TransactionManager m = manager(userRec("01", "newadmin", "AA", "000750.50"));
        m.create(0);
        assertAccount(m, "newadmin", "AA", "000750.50");
    }

    @Test @DisplayName("TM-CREATE-03 existing username -> error message, account untouched")
    void create_existingUser() {
        Backend.TransactionManager m = manager(userRec("01", "fulluser", "AA", "000999.00"));
        account(m, "fulluser", "FS", "000100.00");
        m.create(0);
        assertTrue(out().contains("ERROR: Username already exists"), "console was: " + out());
        assertAccount(m, "fulluser", "FS", "000100.00");
    }

    // delete

    @Test @DisplayName("TM-DELETE-01 existing user is removed")
    void delete_existingUser() {
        Backend.TransactionManager m = manager(userRec("02", "deleteme", "FS", "000020.00"));
        account(m, "deleteme", "FS", "000020.00");
        account(m, "fulluser", "FS", "000100.00");
        m.delete(0);
        assertFalse(m.AccountList.containsKey("deleteme"));
        assertTrue(m.AccountList.containsKey("fulluser"), "other users must stay");
        assertEquals("", out());
    }

    @Test @DisplayName("TM-DELETE-02 unknown user -> error message, nothing removed")
    void delete_unknownUser() {
        Backend.TransactionManager m = manager(userRec("02", "ghost", "FS", "000000.00"));
        account(m, "fulluser", "FS", "000100.00");
        m.delete(0);
        assertTrue(out().contains("ERROR: User does not exist"), "console was: " + out());
        assertEquals(1, m.AccountList.size());
    }

    // sell

    @Test @DisplayName("TM-SELL-01 game is added with seller and price")
    void sell_addsGame() {
        Backend.TransactionManager m = manager(sellRec("Portal", "admin01", "025.00"));
        m.sell(0);
        assertEquals(1, m.GameList.size());
        String key = m.GameList.keySet().iterator().next();
        assertEquals("Portal", key.trim());
        String[] d = m.GameList.get(key);
        assertEquals("admin01", d[0], "seller");
        assertEquals("025.00", d[1], "price");
    }

    @Test @DisplayName("TM-SELL-02 game name containing spaces is kept whole")
    void sell_nameWithSpaces() {
        Backend.TransactionManager m = manager(sellRec("Grand Theft Auto 5", "seller01", "059.99"));
        m.sell(0);
        String key = m.GameList.keySet().iterator().next();
        assertEquals("Grand Theft Auto 5", key.trim());
        assertEquals("059.99", m.GameList.get(key)[1]);
    }

    // buy

    @Test @DisplayName("TM-BUY-01 enough credit: balances move, game added to buyer's collection")
    void buy_success() {
        Backend.TransactionManager m = manager(buyRec("Spiderman", "seller01", "fulluser", "015.00"));
        account(m, "fulluser", "FS", "000100.00");
        account(m, "seller01", "SS", "000050.00");
        m.buy(0);
        assertEquals("", out(), "no error expected, console was: " + out());
        assertEquals("000085.00", m.AccountList.get("fulluser")[1], "buyer balance");
        assertEquals("000065.00", m.AccountList.get("seller01")[1], "seller balance");
        assertEquals(1, m.GameCollection.size());
        assertEquals(collLine("Spiderman", "fulluser"), m.GameCollection.get(0));
    }

    @Test @DisplayName("TM-BUY-02 not enough credit: error message, nothing changes")
    void buy_insufficientFunds() {
        Backend.TransactionManager m = manager(buyRec("Spiderman", "seller01", "pooruser", "075.00"));
        account(m, "pooruser", "BS", "000010.00");
        account(m, "seller01", "SS", "000050.00");
        m.buy(0);
        assertTrue(out().contains("ERROR: User balance cannot be negative"), "console was: " + out());
        assertEquals("000010.00", m.AccountList.get("pooruser")[1]);
        assertEquals("000050.00", m.AccountList.get("seller01")[1]);
        assertTrue(m.GameCollection.isEmpty());
    }

    @Test @DisplayName("TM-BUY-03 price exactly equal to balance is allowed (balance becomes 0, not negative)")
    void buy_priceEqualsBalance() {
        Backend.TransactionManager m = manager(buyRec("Hades", "seller01", "pooruser", "010.00"));
        account(m, "pooruser", "BS", "000010.00");
        account(m, "seller01", "SS", "000050.00");
        m.buy(0);
        assertEquals("", out(), "no error expected, console was: " + out());
        assertEquals("000000.00", m.AccountList.get("pooruser")[1]);
        assertEquals("000060.00", m.AccountList.get("seller01")[1]);
        assertEquals(1, m.GameCollection.size());
    }

    // refund

    @Test @DisplayName("TM-REFUND-01 refund moves the amount from seller back to buyer")
    void refund_movesCredit() {
        Backend.TransactionManager m = manager(refundRec("buyer01", "seller01", "000010.00"));
        account(m, "buyer01", "BS", "000100.00");
        account(m, "seller01", "SS", "000050.00");
        m.refund(0);
        assertEquals("000110.00", m.AccountList.get("buyer01")[1], "buyer should be credited");
        assertEquals("000040.00", m.AccountList.get("seller01")[1], "seller should be debited");
    }

    // addCredit

    @Test @DisplayName("TM-ADDCR-01 credit field is ADDED to the existing balance")
    void addCredit_addsToBalance() {
        Backend.TransactionManager m = manager(userRec("06", "fulluser", "FS", "000500.00"));
        account(m, "fulluser", "FS", "000100.00");
        m.addCredit(0);
        assertAccount(m, "fulluser", "FS", "000600.00");
    }

    @Test @DisplayName("TM-ADDCR-02 decimal amount is added correctly")
    void addCredit_decimalAmount() {
        Backend.TransactionManager m = manager(userRec("06", "buyer01", "BS", "000025.50"));
        account(m, "buyer01", "BS", "000100.00");
        m.addCredit(0);
        assertAccount(m, "buyer01", "BS", "000125.50");
    }

    @Test @DisplayName("TM-ADDCR-03 unknown user -> error that says the user does not exist")
    void addCredit_unknownUser() {
        Backend.TransactionManager m = manager(userRec("06", "ghost", "FS", "000500.00"));
        m.addCredit(0);
        assertTrue(out().contains("ERROR"), "console was: " + out());
        assertFalse(out().contains("already exists"), "message is wrong for a missing user: " + out());
        assertFalse(m.AccountList.containsKey("ghost"), "must not create the account");
    }

    // getters / setters

    @Test @DisplayName("TM-ACC-01 account list setter/getter")
    void accessors_accountList() {
        Backend.TransactionManager m = new Backend.TransactionManager();
        java.util.HashMap<String, String[]> x = new java.util.HashMap<>();
        m.setAccountList(x);
        assertSame(x, m.getAccountList());
    }

    @Test @DisplayName("TM-ACC-02 game list setter/getter")
    void accessors_gameList() {
        Backend.TransactionManager m = new Backend.TransactionManager();
        java.util.HashMap<String, String[]> x = new java.util.HashMap<>();
        m.setGameList(x);
        assertSame(x, m.getGameList());
    }

    @Test @DisplayName("TM-ACC-03 game collection setter/getter")
    void accessors_gameCollection() {
        Backend.TransactionManager m = new Backend.TransactionManager();
        java.util.ArrayList<String> x = new java.util.ArrayList<>();
        m.setGameCollection(x);
        assertSame(x, m.getGameCollection());
    }

    @Test @DisplayName("TM-ACC-04 transaction list setter/getter")
    void accessors_transactionList() {
        Backend.TransactionManager m = new Backend.TransactionManager();
        java.util.ArrayList<String> x = new java.util.ArrayList<>();
        m.setTransactionList(x);
        assertSame(x, m.getTransactionList());
    }
}
