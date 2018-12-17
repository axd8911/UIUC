package UnitTests.Entities;

import Entities.Dish;
import Entities.MenuItem;
import Entities.SingleTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SingleTableTest {

    private SingleTable table;
    private final static int TABLE_INDEX = 1;
    private final static int SEATS = 5;

    @Before
    public void createTable(){
        table = new SingleTable(TABLE_INDEX,SEATS);

    }

    @Before
    public void addDishTest(){
        Dish dish1 = new Dish(new MenuItem("1","beef", 0.1,0.1));
        table.addDish(dish1);
        Dish[] dishes = table.getDishOnTable();
        assertEquals(dishes[0],dish1);
    }

    @Test
    public void getIndexTest() {
        assertEquals(TABLE_INDEX, table.getIndex());
    }

    @Test
    public void getNumberOfSeatsTest() {
        assertEquals(SEATS, table.getNumberOfSeats());
    }

    @Test
    public void checkingOutTest() {
        assertFalse(table.isCheckingOut());
        table.setCheckingOut(true);
        assertTrue(table.isCheckingOut());
    }
}
