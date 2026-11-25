import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//Path coverage for TextFilePrinter.createFiles (3 loops, 3 try/catch, final if/else).
// Loops tested with 0, 1 and 2+ iterations.

class CreateFilesPathCoverageTest extends BackendTestBase {

    // data builders
    private static HashMap<String, String[]> accts(int n) {
        HashMap<String, String[]> m = new HashMap<>();
        for (int i = 1; i <= n; i++) m.put("user" + i, new String[]{"FS", "000100.00"});
        return m;
    }
    private static HashMap<String, String[]> gms(int n) {
        HashMap<String, String[]> m = new HashMap<>();
        for (int i = 1; i <= n; i++) m.put(pad("Game" + i, 25), new String[]{"seller01", "010.00"});
        return m;
    }
    private static ArrayList<String> colls(int n) {
        ArrayList<String> l = new ArrayList<>();
        for (int i = 1; i <= n; i++) l.add(collLine("Game" + i, "user" + i));
        return l;
    }

    // expected file contents
    private static Set<String> expectedAccountLines(int n) {
        Set<String> s = new HashSet<>();
        for (int i = 1; i <= n; i++) s.add(acctLine("user" + i, "FS", "000100.00"));
        return s;
    }
    private static Set<String> expectedGameLines(int n) {
        Set<String> s = new HashSet<>();
        for (int i = 1; i <= n; i++) s.add(gameLine("Game" + i, "seller01", "010.00"));
        return s;
    }
    private static List<String> expectedCollectionLines(int n) {
        return colls(n);
    }

    private void checkAccounts(int n) throws Exception {
        List<String> got = readLines(newAccounts);
        assertEquals("END", got.get(got.size() - 1), "accounts file must end with END");
        assertEquals(expectedAccountLines(n), new HashSet<>(got.subList(0, got.size() - 1)));
        assertEquals(n, got.size() - 1, "number of account lines");
    }
    private void checkGames(int n) throws Exception {
        List<String> got = readLines(newGames);
        assertEquals("END", got.get(got.size() - 1), "games file must end with END");
        assertEquals(expectedGameLines(n), new HashSet<>(got.subList(0, got.size() - 1)));
        assertEquals(n, got.size() - 1, "number of game lines");
    }
    private void checkCollection(int n) throws Exception {
        List<String> got = readLines(newCollection);
        assertEquals("END", got.get(got.size() - 1), "collection file must end with END");
        assertEquals(expectedCollectionLines(n), got.subList(0, got.size() - 1));
    }

    private void allSucceed(int a, int g, int c) throws Exception {
        assertTrue(Backend.TextFilePrinter.createFiles(accts(a), gms(g), colls(c)));
        checkAccounts(a);
        checkGames(g);
        checkCollection(c);
        assertEquals("", out(), "no error message expected");
    }

    @Test @DisplayName("PATH-01 accounts loop x0, games loop x0, collection loop x0, all writers succeed -> true")
    void path01_accounts0_games0_collection0() throws Exception {
        allSucceed(0, 0, 0);
    }

    @Test @DisplayName("PATH-02 accounts loop x0, games loop x0, collection loop x1, all writers succeed -> true")
    void path02_accounts0_games0_collection1() throws Exception {
        allSucceed(0, 0, 1);
    }

    @Test @DisplayName("PATH-03 accounts loop x0, games loop x0, collection loop x2+, all writers succeed -> true")
    void path03_accounts0_games0_collection2() throws Exception {
        allSucceed(0, 0, 2);
    }

    @Test @DisplayName("PATH-04 accounts loop x0, games loop x1, collection loop x0, all writers succeed -> true")
    void path04_accounts0_games1_collection0() throws Exception {
        allSucceed(0, 1, 0);
    }

    @Test @DisplayName("PATH-05 accounts loop x0, games loop x1, collection loop x1, all writers succeed -> true")
    void path05_accounts0_games1_collection1() throws Exception {
        allSucceed(0, 1, 1);
    }

    @Test @DisplayName("PATH-06 accounts loop x0, games loop x1, collection loop x2+, all writers succeed -> true")
    void path06_accounts0_games1_collection2() throws Exception {
        allSucceed(0, 1, 2);
    }

    @Test @DisplayName("PATH-07 accounts loop x0, games loop x2+, collection loop x0, all writers succeed -> true")
    void path07_accounts0_games2_collection0() throws Exception {
        allSucceed(0, 2, 0);
    }

    @Test @DisplayName("PATH-08 accounts loop x0, games loop x2+, collection loop x1, all writers succeed -> true")
    void path08_accounts0_games2_collection1() throws Exception {
        allSucceed(0, 2, 1);
    }

    @Test @DisplayName("PATH-09 accounts loop x0, games loop x2+, collection loop x2+, all writers succeed -> true")
    void path09_accounts0_games2_collection2() throws Exception {
        allSucceed(0, 2, 2);
    }

    @Test @DisplayName("PATH-10 accounts loop x1, games loop x0, collection loop x0, all writers succeed -> true")
    void path10_accounts1_games0_collection0() throws Exception {
        allSucceed(1, 0, 0);
    }

    @Test @DisplayName("PATH-11 accounts loop x1, games loop x0, collection loop x1, all writers succeed -> true")
    void path11_accounts1_games0_collection1() throws Exception {
        allSucceed(1, 0, 1);
    }

    @Test @DisplayName("PATH-12 accounts loop x1, games loop x0, collection loop x2+, all writers succeed -> true")
    void path12_accounts1_games0_collection2() throws Exception {
        allSucceed(1, 0, 2);
    }

    @Test @DisplayName("PATH-13 accounts loop x1, games loop x1, collection loop x0, all writers succeed -> true")
    void path13_accounts1_games1_collection0() throws Exception {
        allSucceed(1, 1, 0);
    }

    @Test @DisplayName("PATH-14 accounts loop x1, games loop x1, collection loop x1, all writers succeed -> true")
    void path14_accounts1_games1_collection1() throws Exception {
        allSucceed(1, 1, 1);
    }

    @Test @DisplayName("PATH-15 accounts loop x1, games loop x1, collection loop x2+, all writers succeed -> true")
    void path15_accounts1_games1_collection2() throws Exception {
        allSucceed(1, 1, 2);
    }

    @Test @DisplayName("PATH-16 accounts loop x1, games loop x2+, collection loop x0, all writers succeed -> true")
    void path16_accounts1_games2_collection0() throws Exception {
        allSucceed(1, 2, 0);
    }

    @Test @DisplayName("PATH-17 accounts loop x1, games loop x2+, collection loop x1, all writers succeed -> true")
    void path17_accounts1_games2_collection1() throws Exception {
        allSucceed(1, 2, 1);
    }

    @Test @DisplayName("PATH-18 accounts loop x1, games loop x2+, collection loop x2+, all writers succeed -> true")
    void path18_accounts1_games2_collection2() throws Exception {
        allSucceed(1, 2, 2);
    }

    @Test @DisplayName("PATH-19 accounts loop x2+, games loop x0, collection loop x0, all writers succeed -> true")
    void path19_accounts2_games0_collection0() throws Exception {
        allSucceed(2, 0, 0);
    }

    @Test @DisplayName("PATH-20 accounts loop x2+, games loop x0, collection loop x1, all writers succeed -> true")
    void path20_accounts2_games0_collection1() throws Exception {
        allSucceed(2, 0, 1);
    }

    @Test @DisplayName("PATH-21 accounts loop x2+, games loop x0, collection loop x2+, all writers succeed -> true")
    void path21_accounts2_games0_collection2() throws Exception {
        allSucceed(2, 0, 2);
    }

    @Test @DisplayName("PATH-22 accounts loop x2+, games loop x1, collection loop x0, all writers succeed -> true")
    void path22_accounts2_games1_collection0() throws Exception {
        allSucceed(2, 1, 0);
    }

    @Test @DisplayName("PATH-23 accounts loop x2+, games loop x1, collection loop x1, all writers succeed -> true")
    void path23_accounts2_games1_collection1() throws Exception {
        allSucceed(2, 1, 1);
    }

    @Test @DisplayName("PATH-24 accounts loop x2+, games loop x1, collection loop x2+, all writers succeed -> true")
    void path24_accounts2_games1_collection2() throws Exception {
        allSucceed(2, 1, 2);
    }

    @Test @DisplayName("PATH-25 accounts loop x2+, games loop x2+, collection loop x0, all writers succeed -> true")
    void path25_accounts2_games2_collection0() throws Exception {
        allSucceed(2, 2, 0);
    }

    @Test @DisplayName("PATH-26 accounts loop x2+, games loop x2+, collection loop x1, all writers succeed -> true")
    void path26_accounts2_games2_collection1() throws Exception {
        allSucceed(2, 2, 1);
    }

    @Test @DisplayName("PATH-27 accounts loop x2+, games loop x2+, collection loop x2+, all writers succeed -> true")
    void path27_accounts2_games2_collection2() throws Exception {
        allSucceed(2, 2, 2);
    }

    @Test @DisplayName("PATH-28 accounts writer fails (catch runs), games and collection succeed")
    void path28_accountsWriterFails() throws Exception {
        assertTrue(newAccounts.mkdir());
        Backend.TextFilePrinter.createFiles(accts(1), gms(1), colls(1));
        assertTrue(out().contains("Error creating new accounts file"), "console was: " + out());
        checkGames(1);
        checkCollection(1);
    }

    @Test @DisplayName("PATH-29 games writer fails (catch runs), accounts and collection succeed")
    void path29_gamesWriterFails() throws Exception {
        assertTrue(newGames.mkdir());
        Backend.TextFilePrinter.createFiles(accts(1), gms(1), colls(1));
        assertTrue(out().contains("Error creating new available games file"), "console was: " + out());
        checkAccounts(1);
        checkCollection(1);
    }

    @Test @DisplayName("PATH-30 collection writer fails (catch runs), accounts and games succeed")
    void path30_collectionWriterFails() throws Exception {
        assertTrue(newCollection.mkdir());
        Backend.TextFilePrinter.createFiles(accts(1), gms(1), colls(1));
        assertTrue(out().toLowerCase().contains("collection"),
                "the message for the collection block should mention the collection; console was: " + out());
        checkAccounts(1);
        checkGames(1);
    }

    // all three fail, final if is false

    @Test @DisplayName("PATH-31 all three writers fail, no temp file exists -> final if false -> returns false")
    void path31_allWritersFail() {
        File missing = new File(dir, "no-such-folder");
        Backend.TextFilePrinter.newAccountFile = new File(missing, "a.txt");
        Backend.TextFilePrinter.newGamesListFile = new File(missing, "g.txt");
        Backend.TextFilePrinter.newGamesCollectionFile = new File(missing, "c.txt");
        assertFalse(Backend.TextFilePrinter.createFiles(accts(1), gms(1), colls(1)));
    }
}
