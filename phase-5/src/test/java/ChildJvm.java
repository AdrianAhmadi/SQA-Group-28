import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Runs Probe in a separate, memory-capped JVM and reports how it ended. 
final class ChildJvm {

    static final class Result {
        final int exit;          // 0 = returned normally, 2 = exception, 3 = OutOfMemoryError, -1 = timed out
        final String output;
        Result(int exit, String output) { this.exit = exit; this.output = output; }
        boolean returnedNormally() { return exit == 0; }
        @Override public String toString() { return "exit=" + exit + " output=" + output.trim(); }
    }

    static Result run(String mode, File txn, File acc, File games, File coll,
                      File newAcc, File newGames, File newColl) throws Exception {
        File log = File.createTempFile("probe", ".log");
        List<String> cmd = new ArrayList<>();
        cmd.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
        cmd.add("-Xmx96m");
        cmd.add("-cp");
        cmd.add(System.getProperty("java.class.path"));
        cmd.add("Probe");
        cmd.add(mode);
        for (File f : new File[]{txn, acc, games, coll, newAcc, newGames, newColl}) cmd.add(f.getAbsolutePath());

        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).redirectOutput(log).start();
        boolean done = p.waitFor(30, TimeUnit.SECONDS);
        int exit;
        if (!done) { p.destroyForcibly(); p.waitFor(); exit = -1; } else { exit = p.exitValue(); }
        StringBuilder sb = new StringBuilder();
        for (String line : Files.readAllLines(log.toPath()))
            if (!line.startsWith("Picked up ")) sb.append(line).append('\n');
        String text = sb.toString();
        log.delete();
        return new Result(exit, text);
    }
}
