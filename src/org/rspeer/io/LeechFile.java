package org.rspeer.io;

import org.rspeer.AstScouter;
import org.rspeer.gui.Settings;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.script.Script;
import org.rspeer.target.TargetHandler;
import org.rspeer.ui.Log;

import java.io.*;

public class LeechFile {

    private static File leechFile = new File(Script.getDataDirectory().toAbsolutePath().toString(), Settings.leechFileName + ".txt");
    private static File tempFile = new File(Script.getDataDirectory().toAbsolutePath().toString(), "TEMP_FILE.txt");

    public static boolean createFile() throws IOException {
        if (!leechFile.exists()) {
            Log.fine("Creating Leech File!");
            return leechFile.createNewFile();
        }

//        Log.severe("Leech file already exists!");
        return false;
    }

    public static void writeLeech(String string) throws IOException {
        FileWriter fw = new FileWriter(leechFile, true);

        fw.write(string + "\n");
        fw.flush();
        fw.close();
    }

    public static void addLeeches() throws IOException {
//        if (TargetHandler.leechSet.isEmpty()) {
            BufferedReader br = new BufferedReader(new FileReader(leechFile));

            String rsn;
            while ((rsn = br.readLine()) != null) {
                TargetHandler.leechSet.add(rsn);
                Time.sleep(25);
            }

            AstScouter.ready = true;
            br.close();
//            Log.fine("Finished adding leeches");
//            Log.fine(TargetHandler.leechSet);
//        }
    }

    public static void removeLeech(String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(leechFile));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile, true));

        String rsn;
        while ((rsn = br.readLine()) != null) {
            if (rsn.trim().equalsIgnoreCase(name)) {
                continue;
            } else {
                pw.println(rsn);
                pw.flush();
            }
            Time.sleep(25);
        }

        br.close();
        pw.flush();
        pw.close();
        leechFile.delete();

        if (tempFile.renameTo(leechFile)) {
            Log.fine("Updated file name");
        } else {
            Log.severe("Update failed");
        }
    }
}
