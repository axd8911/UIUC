package Components;

import Entities.SingleTable;
import Loaders.TableFileLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatingSystem {

    private String tableConfigFilePath;
    private Map<SingleTable,Boolean> tables;

    public SeatingSystem(String tableConfigFilePath){
        this.tableConfigFilePath = tableConfigFilePath;
        populateSeats();
    }

    private void populateSeats(){
        TableFileLoader loader = new TableFileLoader(tableConfigFilePath);
        List<SingleTable> tableList = loader.load();
        tables = new HashMap<SingleTable,Boolean>();
        for (SingleTable i : tableList) {
            tables.put(i, false);
        }
    }

    public int[] getNumberOfSeatsForAllTable(){
        int[] seats = new int[tables.size()];
        int i = 0;
        for(SingleTable table : tables.keySet()){
            seats[i] = table.getNumberOfSeats();
            i++;
        }
        return seats;
    }

    public int[] getIndexForAllTable(){
        int[] indices = new int[tables.size()];
        int i = 0;
        for(SingleTable table : tables.keySet()){
            indices[i] = table.getIndex();
            i++;
        }
        return indices;
    }

    public SingleTable getAvailableTable(int numberOfPeople){
        SingleTable result = null;
        for( Map.Entry<SingleTable, Boolean> entry : tables.entrySet()){
            if(!entry.getValue()
            &&  entry.getKey().getNumberOfSeats() >= numberOfPeople
            && (result == null || entry.getKey().getNumberOfSeats() < result.getNumberOfSeats())){
                result = entry.getKey();
            }
        }
        return result;
    }

    public boolean occupy(SingleTable table){

        boolean result = false;

        if(!tables.get(table)){
            tables.put(table, true);
            result = true;
        }

        return result;
    }

    public boolean vacate(SingleTable table){

        boolean result = false;

        if(tables.get(table)){
            tables.put(table,false);
            result = true;
        }

        return result;
    }



}
