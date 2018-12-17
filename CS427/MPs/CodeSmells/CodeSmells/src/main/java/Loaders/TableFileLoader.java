package Loaders;

import Entities.SingleTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableFileLoader {

    private final static String[] HEADERS = {"Table Number", "Number of Seats"};
    private String fileName;
    public TableFileLoader(String fileName){
        this.fileName = fileName;
    }

    public File getDefaultFile() {
        return new File("./tables.txt");
    }

    public List<SingleTable> load(){
        BufferedReader breader = null;
        File file;
        List<List<String>> result = new ArrayList<List<String>>();

        try {
            file = new File(fileName);
            if (!file.exists()) {
                file = getDefaultFile();
            }

            breader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            readLines(breader, result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBReader(breader);
        }
        return fillTables(result);
    }

    private List<SingleTable> fillTables(List<List<String>> result) {
        List<SingleTable> tables = new ArrayList<SingleTable>();
        for(List<String> line : result ){
            int tableIndex = Integer.parseInt(line.get(0).trim());
            int tableSeats = Integer.parseInt(line.get(1).trim());
            SingleTable table = new SingleTable(tableIndex,tableSeats);
            tables.add(table);
        }
        return tables;
    }

    private void closeBReader(BufferedReader breader){
        try {
            if (breader != null) {
                breader.close();
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void readLines(BufferedReader breader, List<List<String>> result) throws IOException{
        int numberOfColumns = HEADERS.length;
        String line = breader.readLine();
        while(line != null){
            String[] entries = line.split(",");
            List<String> lineEntry = new ArrayList<String>();

            if(entries.length != numberOfColumns){
                throw new IllegalArgumentException("The specified Columns are incorrect.");
            }
            for(String entry : entries){
                lineEntry.add(entry);
            }
            result.add(lineEntry);
            line = breader.readLine();
        }
    }
}
