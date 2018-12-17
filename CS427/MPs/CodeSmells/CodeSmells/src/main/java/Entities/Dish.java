package Entities;

public class Dish {
    MenuItem menuItem;

    public Dish(MenuItem item){
        menuItem = item;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
