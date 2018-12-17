package UnitTests.Loaders;

import Entities.MenuItem;
import Entities.SingleTable;
import Loaders.MenuFileLoader;
import Loaders.TableFileLoader;
import org.junit.Test;

import java.util.List;

import static Util.FileUtil.writeLinesToFile;
import static org.junit.Assert.assertEquals;

public class TableFileLoaderTest {
    @Test
    public void loadTest(){
        String[] fakeTableConfig = {
                "0, 2",
        };
        writeLinesToFile(fakeTableConfig, "src/test/file/tables.txt");

        TableFileLoader loader = new TableFileLoader("src/test/file/tables.txt");
        List<SingleTable> list = loader.load();
        assertEquals(fakeTableConfig.length, list.size());
        for(int i = 0;i< 1;i++){
            String[] menuEntry = fakeTableConfig[i].split(",");
            assertEquals(Integer.parseInt(menuEntry[0].trim()),list.get(i).getIndex());
            assertEquals(Integer.parseInt(menuEntry[1].trim()),list.get(i).getNumberOfSeats());
        }
    }

    @Test
    public void loaddefaultTest(){
        String[] fakeTableConfig = {
                "0, 2",
                "1, 2",
                "2, 2",
                "3, 2",
                "4, 2",
                "5, 2",
                "6, 4",
                "7, 4",
                "8, 4",
                "9, 4",
                "10, 8",
                "11, 8"
        };

        TableFileLoader loader = new TableFileLoader("nonexistfile.txt");
        List<SingleTable> list = loader.load();
        assertEquals(fakeTableConfig.length, list.size());
        for(int i = 0;i< 2;i++){
            String[] menuEntry = fakeTableConfig[i].split(",");
            assertEquals(Integer.parseInt(menuEntry[0].trim()),list.get(i).getIndex());
            assertEquals(Integer.parseInt(menuEntry[1].trim()),list.get(i).getNumberOfSeats());
        }
    }
}
