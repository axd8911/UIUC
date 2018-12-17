package UnitTests.Entities;

import Entities.MenuItem;
import Entities.Order;
import Entities.SingleTable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    @Test
    public void getOrderedItemTest(){
        SingleTable table = new SingleTable(0,2);
        MenuItem mi = new MenuItem("Cool Aid","beef",1.2,500.0);
        Order order = new Order(table,mi);
        assertEquals(mi,order.getOrderedItem());
    }

    @Test
    public void getFromTableTest(){
        SingleTable table = new SingleTable(0,2);
        MenuItem mi = new MenuItem("Cool Aid","beef",1.2,500.0);
        Order order = new Order(table,mi);
        assertEquals(table,order.getFromTable());
    }
}
