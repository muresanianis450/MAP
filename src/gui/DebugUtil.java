package gui;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class DebugUtil {
    private static final String DEBUG_FILE = "GUI_debug.txt";

    public static void log(String msg) {
        String line = "[" + LocalDateTime.now() + "] " + msg;
        // Try console
        System.err.println(line);

        // Also write to file (append)
        try (FileWriter fw = new FileWriter(DEBUG_FILE, true)) {
            fw.write(line + System.lineSeparator());
        } catch (IOException ignored) {
        }
    }
}
