package Entities;

public class MenuItem {

    private String dishName;
    private double calorie;
    private double price;
    private MenuItemType type;

    public MenuItem(String name, String type, double price, double calorie){
        dishName = name;
        this.type = MenuItemType.valueOf(type.toUpperCase());
        this.price = price;
        this.calorie = calorie;
    }
    public String getDishName() {
        return dishName;
    }

    public double getCalorie() {
        return calorie;
    }

    public double getPrice() {
        return price;
    }

    public MenuItemType getType() {
        return type;
    }
}
