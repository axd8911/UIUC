package IntegrationTests;

import Components.Restaurant;
import Components.SeatingSystem;
import Entities.*;
import Terminals.CustomerTerminal;
import Terminals.KitchenTerminal;
import Terminals.ServiceDeskTerminal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import static Util.FileUtil.writeLinesToFile;

public class ScenarioTest {

    private ServiceDeskTerminal sdt;
    private KitchenTerminal kt;

    @Before
    public void setUp(){
        sdt = new ServiceDeskTerminal();
        sdt.start();
        String[] fakeTableConfig = {
          "0, 2",
        };
        String[] fakeMenu = {
                "Steak, Beef, 15.0, 234",
                "Pork Rib, Pork, 12.0, 259",
        };
        writeLinesToFile(fakeTableConfig, "src/test/file/tables.txt");
        writeLinesToFile(fakeMenu, "src/test/file/menu.txt");
        kt = sdt.grandOpening("MyRest","src/test/file/tables.txt","src/test/file/menu.txt");

    }

    @After
    public void tearDown(){
        sdt.closeBusinesss();
        sdt.end();
        sdt = null;
    }

    @Test
    public void twoPeopleOneDish(){
        CustomerTerminal ct = sdt.checkIn(2);
        SingleTable table = ct.getTable();
        Restaurant re = Restaurant.getInstance();
        SeatingSystem ss = re.getSeatingSystem();

        assertEquals(false, ss.occupy(table));

        ct.orderDish("Pork Rib");
        Order order = kt.getOrder();
        MenuItem item = order.getOrderedItem();
        kt.serveOrder(new Serving(table, new Dish(item)));
        sdt.serveDish();
        assertEquals(1,ct.getTable().getDishOnTable().length);
        assertEquals(order.getOrderedItem(),ct.getTable().getDishOnTable()[0].getMenuItem());

        String dishes = ct.getAllDishAsString();
        assertEquals("Dish 0: Pork Rib",dishes);

        ct.checkout();
        sdt.checkOut(table);
        assertEquals(true, ss.occupy(table));

    }


    @Test
    public void twoPeopleTwoDish(){
        CustomerTerminal ct = sdt.checkIn(2);
        SingleTable table = ct.getTable();
        Restaurant re = Restaurant.getInstance();
        SeatingSystem ss = re.getSeatingSystem();

        assertEquals(false, ss.occupy(table));

        ct.orderDish("Pork Rib");
        Order order = kt.getOrder();
        MenuItem item = order.getOrderedItem();
        kt.serveOrder(new Serving(table, new Dish(item)));
        sdt.serveDish();
        assertEquals(1,ct.getTable().getDishOnTable().length);
        assertEquals(order.getOrderedItem(),ct.getTable().getDishOnTable()[0].getMenuItem());

        ct.orderDish("Steak");
        Order order2 = kt.getOrder();
        MenuItem item2 = order2.getOrderedItem();
        kt.serveOrder(new Serving(table, new Dish(item2)));
        sdt.serveDish();
        assertEquals(2,ct.getTable().getDishOnTable().length);
        assertEquals(order2.getOrderedItem(),ct.getTable().getDishOnTable()[1].getMenuItem());

        String dishes = ct.getAllDishAsString();
        assertEquals("Dish 0: Pork Rib, Dish 1: Steak",dishes);

        ct.checkout();
        sdt.checkOut(table);

        assertEquals(true, ss.occupy(table));
    }

}
