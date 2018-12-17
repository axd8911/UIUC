package UnitTests.Loaders;

import Entities.MenuItem;
import Entities.SingleTable;
import Loaders.MenuFileLoader;
import org.junit.Test;

import java.util.List;

import static Util.FileUtil.writeLinesToFile;
import static org.junit.Assert.assertEquals;

public class MenuFileLoaderTest {

    @Test
    public void loadTest(){

        String[] fakeMenu = {
                "Steak, Beef, 15.0, 234",
                "Pork Rib, Pork, 12.0, 259",
        };
        writeLinesToFile(fakeMenu, "src/test/file/menu.txt");

        MenuFileLoader loader = new MenuFileLoader("src/test/file/menu.txt");
        List<MenuItem> list = loader.load();
        assertEquals(fakeMenu.length, list.size());
        for(int i = 0;i< 2;i++){
            String[] menuEntry = fakeMenu[i].split(",");
            assertEquals(menuEntry[0].trim(),list.get(i).getDishName());
            assertEquals(menuEntry[1].trim().toUpperCase(),list.get(i).getType().name());
            assertEquals(Double.parseDouble(menuEntry[2].trim()),list.get(i).getPrice(),0);
            assertEquals(Double.parseDouble(menuEntry[3].trim()),list.get(i).getCalorie(),0);
        }
    }

    @Test
    public void loadDefaultTest(){

        String[] fakeMenu = {
                "Nice Steak, Beef, 12.0, 210",
                "Sour Pork Rib, Pork, 10.0, 150"
        };

        MenuFileLoader loader = new MenuFileLoader("nonexistfile.txt");
        List<MenuItem> list = loader.load();
        assertEquals(fakeMenu.length, list.size());
        for(int i = 0;i< 2;i++){
            String[] menuEntry = fakeMenu[i].split(",");
            assertEquals(menuEntry[0].trim(),list.get(i).getDishName());
            assertEquals(menuEntry[1].trim().toUpperCase(),list.get(i).getType().name());
            assertEquals(Double.parseDouble(menuEntry[2].trim()),list.get(i).getPrice(),0);
            assertEquals(Double.parseDouble(menuEntry[3].trim()),list.get(i).getCalorie(),0);
        }
    }
}
