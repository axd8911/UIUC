package UnitTests.Components;

import Components.Menu;
import Entities.MenuItem;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import static Util.FileUtil.writeLinesToFile;

public class MenuTest {

    @Test
    public void getAllDishesTest(){
        String[] fakeMenu = {
                "Steak, Beef, 15.0, 234",
                "Pork Rib, Pork, 12.0, 259",
        };
        writeLinesToFile(fakeMenu, "src/test/file/menu.txt");
        Menu menu = new Menu("src/test/file/menu.txt");
        MenuItem[] items = menu.getAllDishes();
        assertEquals(2,items.length);
        assertEquals("Steak",items[0].getDishName());
        assertEquals(15,items[0].getPrice(),0);
        assertEquals(234,items[0].getCalorie(),0);
        assertEquals("Pork Rib",items[1].getDishName());
        assertEquals(12,items[1].getPrice(),0);
        assertEquals(259,items[1].getCalorie(),0);
    }

    @Test
    public void getItemByNameTest(){
        String[] fakeMenu = {
                "Steak, Beef, 15.0, 234"
        };
        writeLinesToFile(fakeMenu, "src/test/file/menu.txt");
        Menu menu = new Menu("src/test/file/menu.txt");
        MenuItem[] items = menu.getAllDishes();
        assertEquals(items[0],menu.getItemByName("Steak"));
    }
}
