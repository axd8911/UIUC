package edu.ncsu.csc.itrust.unit.T822.UC96.selenium;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;
import edu.ncsu.csc.itrust.util.OSInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;

public class UC96BlackBox extends iTrustSeleniumTest{

    protected WebDriver driver;
    protected WebDriverWait wait;

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
        gen.uc93();
        gen.uc94();
        gen.uc96();

        String userDir = System.getProperty("user.dir");

        //Determine operating system so proper chromedriver (v2.43) executable will be selected:
        OSInfo.OS os = OSInfo.getOs();
        String chromeDriverLocation = "";

        switch (os) {
            case WINDOWS:
                chromeDriverLocation = userDir + "/chromedrivers/chromedriver.exe";
                break;
            case UNIX:
                chromeDriverLocation = userDir + "/chromedrivers/chromedriverLinux";
                break;
            case POSIX_UNIX:
                chromeDriverLocation = userDir + "/chromedrivers/chromedriverLinux";
                break;
            case MAC:
                chromeDriverLocation = userDir + "/chromedrivers/chromedriverMac";
                break;
            case OTHER:
                throw new IllegalStateException("Unsupported operating system is being used.");
        }

        System.setProperty("webdriver.chrome.driver", chromeDriverLocation);

        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--window-size=1920,1080");


        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().pageLoadTimeout(90, SECONDS);
        driver.manage().timeouts().implicitlyWait(30, SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, SECONDS);
        System.out.println("IF A SERIES OF TESTS FAIL, WAIT A MINUTE THEN RE-RUN THE FAILED TESTS.  NEEDS TO BE AT LEAST A MINUTE WAIT.");
        System.out.println("THIS IS NOT A PROBLEM WITH OUR TESTS, BUT A PROBLEM WITH SELENIUM/CHROMEDRIVER.");
        driver.get("http://localhost:8080/iTrust/");
    }

    public void testViewObstetricInitializationREcords() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[1]/button")).click();



        Set<String> handle = driver.getWindowHandles();

        for (String n : handle){
            if (!n.equals(mainWindow)){
                driver.switchTo().window(n);
            }
        }


        assertTrue(driver.getPageSource().contains("Obstetric Initialization Records"));
        //close popup
        driver.findElement(By.xpath("/html/body/div/button")).click();
        driver.switchTo().window(mainWindow);
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));

        assertLogged(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_READ, 9000000012L, 402, "");


    }


    public void testScheduleVisitSuccess() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();


        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[1]/a")).click();
        assertEquals("iTrust - Schedule Childbirth Hospital Visit", driver.getTitle());

        //5.) Enter: Date: 10/07/2018
        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input[1]"));
        dateBox.click();
        dateBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateBox.sendKeys(Keys.chord(Keys.DELETE));
        dateBox.sendKeys("01/07/2019");

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).sendKeys("This is my silly comment");

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/input[2]")).click();
        assertTrue(driver.getPageSource().contains("The childbirth hospital visit appointment has been successfully scheduled!"));
        assertLogged(TransactionType.CREATE_CHILDBIRTH_VISIT, 9000000012L, 402, "");


    }

    public void testScheduleVisitFailure() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();


        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[1]/a")).click();
        assertEquals("iTrust - Schedule Childbirth Hospital Visit", driver.getTitle());

        //5.) Enter: Date: 10/07/2018
        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input[1]"));
        dateBox.click();
        dateBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateBox.sendKeys(Keys.chord(Keys.DELETE));
        dateBox.sendKeys("01/07/2019");

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).sendKeys("@#$%This is my silly comment");

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/input[2]")).click();
        assertEquals(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/span")).getText(), "This form has not been validated correctly. The following fields are not properly filled in: [Appointment Comment: Between 0 and 1000 alphanumerics with space, and other punctuation]");
        assertNotLogged(TransactionType.CREATE_CHILDBIRTH_VISIT, 9000000012L, 402, "");


    }

    public void testEditVisitSuccess() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();


        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[1]/table/tbody/tr[2]/td[1]/a")).click();
        assertEquals("iTrust - Modify Childbirth Hospital Visit", driver.getTitle());

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).sendKeys("This is my silly comment");

        driver.findElement(By.xpath("//*[@id=\"changeButton\"]")).click();
        assertTrue(driver.getPageSource().contains("The childbirth hospital visit appointment has been successfully changed!"));
        assertLogged(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_EDIT, 9000000012L, 402, "1");


    }


    public void testEditVisitFailure() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();


        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[1]/table/tbody/tr[2]/td[1]/a")).click();
        assertEquals("iTrust - Modify Childbirth Hospital Visit", driver.getTitle());

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/textarea")).sendKeys("@#$%This is my silly comment");

        driver.findElement(By.xpath("//*[@id=\"changeButton\"]")).click();
        assertEquals(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/span")).getText(), "This form has not been validated correctly. The following fields are not properly filled in: [Appointment Comment: Between 0 and 1000 alphanumerics with space, and other punctuation]");
        assertNotLogged(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_EDIT, 9000000012L, 402, "1");


    }

    public void testAddBabyDelivery() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();


        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[2]/a")).click();
        assertEquals("iTrust - Add Childbirth Delivery", driver.getTitle());

        //add baby
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[7]/td[2]/div/div/a")).click();

        // name baby
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[7]/td[2]/div/div[2]/input[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[7]/td[2]/div/div[2]/input[1]")).sendKeys("Frank");

        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[7]/td[2]/div/div[2]/input[2]")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[7]/td[2]/div/div[2]/input[2]")).sendKeys("Fergusson");



        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/input[2]")).click();
        assertTrue(driver.getPageSource().contains("The childbirth delivery record has been successfully added!"));
        assertLogged(TransactionType.OBSTETRIC_CHILDBRITH_BABY_BORN, 9000000012L, 402, "");


    }

    public void testAddBabyEdit() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//*[@id=\"obstet-menu\"]/ul/li[4]/a")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "Childbirth Hospital Visit button"
        assertTrue(driver.getPageSource().contains("Childbirth Hospital Visit"));
        String mainWindow = driver.getWindowHandle();


        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/div[2]/table/tbody/tr[2]/td[1]/a")).click();
        assertEquals("iTrust - Edit Childbirth Delivery", driver.getTitle());

        //change dose
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[6]/td[2]/table/tbody/tr[2]/td[2]/input")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[6]/td[2]/table/tbody/tr[2]/td[2]/input")).sendKeys(Keys.UP);

        driver.findElement(By.xpath("//*[@id=\"changeButton\"]")).click();
        assertTrue(driver.getPageSource().contains("The childbirth delivery has been successfully changed"));
        assertLogged(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_EDIT, 9000000012L, 402, "1");


    }


}
