package Entities;

import java.util.ArrayList;
import java.util.List;

public class SingleTable {

    private int index;
    private int numberOfSeats;
    private boolean checkingOut;
    private List<Dish> dishOnTable;

    public SingleTable(int tableIndex, int seats){
        index = tableIndex;
        numberOfSeats = seats;
        checkingOut = false;
        dishOnTable = new ArrayList<Dish>();
    }

    public void addDish(Dish dish){
        dishOnTable.add(dish);
    }

    public Dish[] getDishOnTable(){
        Dish[] dishes = new Dish[dishOnTable.size()];
        return dishOnTable.toArray(dishes);
    }

    public int getIndex() {
        return index;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public boolean isCheckingOut() {
        return checkingOut;
    }

    public void setCheckingOut(boolean checkingOut) {
        this.checkingOut = checkingOut;
    }
}
