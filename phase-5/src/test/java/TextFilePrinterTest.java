import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// White box tests (statement coverage) for Backend.TextFilePrinter
class TextFilePrinterTest extends BackendTestBase {

    //  createFiles 

    @Test @DisplayName("TFP-CREATE-01 populated data -> three temp files written in file format, returns true")
    void createFiles_populated() throws Exception {
        HashMap<String, String[]> acc = new HashMap<>();
        acc.put("admin01", new String[]{"AA", "000500.00"});
        HashMap<String, String[]> gm = new HashMap<>();
        gm.put(pad("Hades", 25), new String[]{"seller01", "024.99"});
        ArrayList<String> coll = new ArrayList<>(List.of(collLine("Celeste", "fulluser")));

        assertTrue(Backend.TextFilePrinter.createFiles(acc, gm, coll));

        assertEquals(List.of(acctLine("admin01", "AA", "000500.00"), "END"), readLines(newAccounts));
        assertEquals(List.of(gameLine("Hades", "seller01", "024.99"), "END"), readLines(newGames));
        assertEquals(List.of(collLine("Celeste", "fulluser"), "END"), readLines(newCollection));
        assertEquals("", out(), "no error expected");
    }

    @Test @DisplayName("TFP-CREATE-02 empty data -> files contain only END, returns true")
    void createFiles_empty() throws Exception {
        assertTrue(Backend.TextFilePrinter.createFiles(new HashMap<>(), new HashMap<>(), new ArrayList<>()));
        assertEquals(List.of("END"), readLines(newAccounts));
        assertEquals(List.of("END"), readLines(newGames));
        assertEquals(List.of("END"), readLines(newCollection));
    }

    @Test @DisplayName("TFP-CREATE-03 cannot create temp files -> three error messages, returns false")
    void createFiles_cannotWrite() {
        File missing = new File(dir, "no-such-folder");
        Backend.TextFilePrinter.newAccountFile = new File(missing, "a.txt");
        Backend.TextFilePrinter.newGamesListFile = new File(missing, "g.txt");
        Backend.TextFilePrinter.newGamesCollectionFile = new File(missing, "c.txt");

        assertFalse(Backend.TextFilePrinter.createFiles(new HashMap<>(), new HashMap<>(), new ArrayList<>()));
        assertTrue(out().contains("Error creating new accounts file"), out());
        assertTrue(out().contains("Error creating new available games file"), out());
        assertTrue(out().toLowerCase().contains("collection"),
                "collection writer's error message should mention the collection: " + out());
    }

    //  replaceFiles 

    @Test @DisplayName("TFP-REPLACE-01 temp files replace the live files, no error reported")
    void replaceFiles_success() throws Exception {
        write(accounts, "OLD-ACCOUNTS");
        write(games, "OLD-GAMES");
        write(collection, "OLD-COLLECTION");
        write(newAccounts, "NEW-ACCOUNTS");
        write(newGames, "NEW-GAMES");
        write(newCollection, "NEW-COLLECTION");

        Backend.TextFilePrinter.replaceFiles();

        assertEquals(List.of("NEW-ACCOUNTS"), readLines(accounts));
        assertEquals(List.of("NEW-GAMES"), readLines(games));
        assertEquals(List.of("NEW-COLLECTION"), readLines(collection));
        assertFalse(newAccounts.exists());
        assertFalse(newGames.exists());
        assertFalse(newCollection.exists());
        assertEquals("", err());
    }

    @Test @DisplayName("TFP-REPLACE-02 temp files missing -> rename failure is reported on stderr")
    void replaceFiles_renameFails() throws Exception {
        write(accounts, "OLD-ACCOUNTS");
        write(games, "OLD-GAMES");
        write(collection, "OLD-COLLECTION");
        // no temp files exist, so every renameTo() returns false

        Backend.TextFilePrinter.replaceFiles();

        assertTrue(err().contains("ERROR: File renaming unsuccessful"), "stderr was: " + err());
    }
}
