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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

import static Util.FileUtil.writeLinesToFile;
import static Util.StringUtil.getLastLine;

public class SystemOutTest {

    private ServiceDeskTerminal sdt;
    private KitchenTerminal kt;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
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
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown(){
        sdt.closeBusinesss();
        sdt.end();
        sdt = null;
        System.setOut(System.out);
        System.setErr(System.err);
    }


    @Test
    public void twoPeopleOneDishPrint(){
        CustomerTerminal ct = sdt.checkIn(2);
        assertEquals("Terminals.ServiceDeskTerminal: Information - New table 0 checked in, number of people: 2", outContent.toString().trim());
        SingleTable table = ct.getTable();
        Restaurant re = Restaurant.getInstance();
        SeatingSystem ss = re.getSeatingSystem();

        ct.orderDish("Pork Rib");
        Order order = kt.getOrder();
        MenuItem item = order.getOrderedItem();
        kt.serveOrder(new Serving(table, new Dish(item)));
        sdt.serveDish();

        assertEquals("Terminals.ServiceDeskTerminal: Information - Pork Rib served to table 0", getLastLine(outContent.toString()).trim());

        ct.checkout();
        sdt.checkOut(table);
        assertEquals("Terminals.ServiceDeskTerminal: Information - Table 0 checked out.",getLastLine(outContent.toString()).trim());

        sdt.checkOut(table);
        assertEquals("Terminals.ServiceDeskTerminal: Error - Vacating table 0 failed.",getLastLine(outContent.toString()).trim());
    }


}
