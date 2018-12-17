package Loaders;

import Entities.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuFileLoader {

    private final static String[] HEADERS = {"MenuItem Name", "Type" ,"Price", "Calorie"};
    private final static int DISH_CALORIE_LINE_INDEX = 3;
    private String fileName;
    public MenuFileLoader(String fileName){
        this.fileName = fileName;
    }

    public File getDefaultFile() {
        return new File("./menu.txt");
    }


    public List<MenuItem> load(){
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
        return fillMenuItems(result);
    }

    private List<MenuItem> fillMenuItems(List<List<String>> result) {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        for(List<String> line : result ){
            String dishName = line.get(0).trim();
            String dishType = line.get(1).toUpperCase().trim();
            double dishPrice = Double.parseDouble(line.get(2).trim());
            double dishCalorie = Double.parseDouble(line.get(DISH_CALORIE_LINE_INDEX).trim());
            MenuItem menuItem = new MenuItem(dishName,dishType,dishPrice,dishCalorie);
            menuItems.add(menuItem);
        }
        return menuItems;
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
