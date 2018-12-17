package Components;

import Entities.MenuItem;
import Loaders.MenuFileLoader;

import java.util.List;

public class Menu {

    private List<MenuItem> menuItems;

    public Menu(String fileName) {
        MenuFileLoader menuLoader = new MenuFileLoader(fileName);
        menuItems = menuLoader.load();
    }

    public MenuItem[] getAllDishes(){
        return menuItems.toArray(new MenuItem[] {});
    }


    public MenuItem getItemByName(String name){
        MenuItem result = null;

        for(MenuItem item : getAllDishes()){
            if(item.getDishName().equals(name)){
                result = item;
            }
        }

        return result;
    }

}
