package Entities;

public class Order {

    private SingleTable fromTable;
    private MenuItem orderedItem;

    public Order(SingleTable fromTable, MenuItem orderedItem) {
        this.fromTable = fromTable;
        this.orderedItem = orderedItem;
    }

    public MenuItem getOrderedItem() {
        return orderedItem;
    }

    public SingleTable getFromTable() {
        return fromTable;
    }
}
