package Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil {

    public static void writeLinesToFile(String[] content, String path){
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(path));

            for (String s : content) {
                pw.println(s);
            }

            pw.close();
        }catch(IOException e){
            System.err.println("Failed to write to file: " + path + ": " + e.getLocalizedMessage());
        }
    }
}
