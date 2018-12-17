package UnitTests.Components;

import Components.SeatingSystem;
import Entities.SingleTable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Util.FileUtil.writeLinesToFile;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SeatingSystemTest {

    private SeatingSystem ss;


    @BeforeClass
    public static void setUpClass(){
        String[] fakeTables = {
                "0,2",
                "1,2",
                "2,4",
                "3,4",
                "4,6"
        };
        writeLinesToFile(fakeTables, "src/test/file/tables.txt");
    }

    @Before
    public void setUp(){
        ss = new SeatingSystem("src/test/file/tables.txt");
    }

    @Test
    public void populateSeatsTest(){
        int[] seats = ss.getNumberOfSeatsForAllTable();
        assertEquals(5, seats.length );
        List<Integer> correct = new ArrayList<Integer>(5);
        correct.add(2);
        correct.add(2);
        correct.add(4);
        correct.add(4);
        correct.add(6);
        for(int i = 0; i < seats.length;i++){
            correct.remove((Integer) seats[i]);
        }
        assertEquals(0,correct.size());


        int[] indices =  ss.getIndexForAllTable();
        assertEquals(5, indices.length);
        List<Integer> correct2 = new ArrayList<Integer>(5);
        correct.add(0);
        correct.add(1);
        correct.add(2);
        correct.add(3);
        correct.add(4);
        for(int i = 0; i < seats.length;i++){
            correct2.remove((Integer) seats[i]);
        }
        assertEquals(0,correct2.size());
    }

    @Test
    public void getAvailableTableTest(){
        SingleTable table = ss.getAvailableTable(1);
        assertNotEquals(null, table);
        assertEquals(2,table.getNumberOfSeats());

        SingleTable table2 = ss.getAvailableTable(2);
        assertNotEquals(null, table2);
        assertEquals(2,table2.getNumberOfSeats());

        SingleTable table3 = ss.getAvailableTable(5);
        assertNotEquals(null, table3);
        assertEquals(6,table3.getNumberOfSeats());

        SingleTable table4 = ss.getAvailableTable(7);
        assertEquals(null, table4);
    }

    @Test
    public void occupyTest(){
        SingleTable table = ss.getAvailableTable(1);
        boolean result = ss.occupy(table);
        assertEquals(true, result);

        boolean result2 = ss.occupy(table);
        assertEquals(false ,result2);

        SingleTable table2 = ss.getAvailableTable(3);
        boolean result3 = ss.occupy(table2);
        assertEquals(true, result3);

        SingleTable table3 = ss.getAvailableTable(3);
        assertNotEquals(table3.getIndex(), table2.getIndex());
        boolean result4 = ss.occupy(table3);
        assertEquals(true, result4);

        boolean result5 = ss.occupy(table3);
        assertEquals(false, result5);
    }

    @Test
    public void cavateTest(){
        SingleTable table = ss.getAvailableTable(1);
        ss.occupy(table);
        boolean result = ss.vacate(table);
        assertEquals(true, result);

        boolean result2 = ss.vacate(table);
        assertEquals(false, result2);

        //---

        SingleTable table2 = ss.getAvailableTable(2);
        ss.occupy(table2);
        SingleTable table3 = ss.getAvailableTable(2);
        ss.occupy(table3);
        SingleTable table4 = ss.getAvailableTable(2);
        assertEquals(4, table4.getNumberOfSeats());

        boolean result3 = ss.vacate(table2);
        assertEquals(true, result3);
        SingleTable table5 = ss.getAvailableTable(2);
        assertEquals(2, table5.getNumberOfSeats());
    }
}
