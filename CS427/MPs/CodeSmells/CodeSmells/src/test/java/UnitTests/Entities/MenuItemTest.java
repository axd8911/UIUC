package UnitTests.Entities;

import Entities.MenuItem;
import Entities.MenuItemType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MenuItemTest {


    @Test
    public void gettersTest(){
        String dummyName = "dummyName";
        String dummyType = "beef";
        double dummyPrice = 1.0;
        double dummyCalorie = 1.0;
        MenuItem mi = new MenuItem(dummyName, dummyType,dummyPrice,dummyCalorie);
        assertEquals(dummyName,mi.getDishName());
        assertEquals(dummyType.toUpperCase(), MenuItemType.BEEF.name());
        assertEquals(dummyPrice,mi.getPrice(),0);
        assertEquals(dummyCalorie,mi.getCalorie(),0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest(){
        String dummyName = "dummyName";
        String dummyType = "INCORRECTBEEF";
        double dummyPrice = 1.0;
        double dummyCalorie = 1.0;
        MenuItem mi = new MenuItem(dummyName, dummyType,dummyPrice,dummyCalorie);

    }
}
