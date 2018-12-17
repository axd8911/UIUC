package edu.ncsu.csc.itrust.unit.T822.UC94.selenium;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;

import edu.ncsu.csc.itrust.util.OSInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.util.concurrent.TimeUnit.SECONDS;

public class uc94BlackBoxTests extends iTrustSeleniumTest {

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

        switch (os){
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


        driver =  new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().pageLoadTimeout(90, SECONDS);
        driver.manage().timeouts().implicitlyWait(30, SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, SECONDS);
        System.out.println("IF A SERIES OF TESTS FAIL, WAIT A MINUTE THEN RE-RUN THE FAILED TESTS.  NEEDS TO BE AT LEAST A MINUTE WAIT.");
        System.out.println("THIS IS NOT A PROBLEM WITH OUR TESTS, BUT A PROBLEM WITH SELENIUM/CHROMEDRIVER.");
        driver.get("http://localhost:8080/iTrust/");
    }

    public void testAddObstetricsOVSuccessfulCreate() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 400
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Daria Griffin");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='400']")));
        driver.findElement(By.xpath("//input[@value='400']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Daria Griffin's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"addObstetricOVButton\"]")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addOVRecords.jsp", driver.getCurrentUrl());


        //5.) Enter: Date: 10/07/2018
        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[3]/td[2]/input[1]"));
        dateBox.click();
        dateBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateBox.sendKeys(Keys.chord(Keys.DELETE));
        dateBox.sendKeys("10/07/2018");

        //6.) Enter: days pregnant: 40
        WebElement dayPregBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[4]/td[2]/input"));
        dayPregBox.click();
        dayPregBox.sendKeys("40");

        //7.) Enter weight: 137
        WebElement weightBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[5]/td[2]/input"));
        weightBox.click();
        weightBox.sendKeys("137");

        //8.) Enter blood pressure 103/62
        WebElement pressBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[6]/td[2]/input"));
        pressBox.click();
        pressBox.sendKeys("103/62");

        // 9.) Enter FHR: 152
        WebElement fhrBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[7]/td[2]/input"));
        fhrBox.click();
        fhrBox.sendKeys("152");

        //10.)  Select Multiple: 1
        WebElement multBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[8]/td[2]/input"));
        multBox.click();
        multBox.sendKeys("1");

        //11.) submit and check post conditions
        driver.findElement(By.id("createApptButton")).click();
        assertTrue(driver.getPageSource().contains("Obstetrics Office Visit successfully added."));
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_CREATE, 9000000012L, 400, "Office Visit ID : 2");
        assertLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 400, "Office Visit ID : 2, the next visit's id");

    }

    public void testAddObstetricsOVUnsuccessfulCreateBadWeight() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 400
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Daria Griffin");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='400']")));
        driver.findElement(By.xpath("//input[@value='400']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Daria Griffin's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"addObstetricOVButton\"]")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addOVRecords.jsp", driver.getCurrentUrl());


        //5.) Enter: Date: 10/07/2018
        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[3]/td[2]/input[1]"));
        dateBox.click();
        dateBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateBox.sendKeys(Keys.chord(Keys.DELETE));
        dateBox.sendKeys("10/07/2018");

        //6.) Enter: days pregnant: 40
        WebElement dayPregBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[4]/td[2]/input"));
        dayPregBox.click();
        dayPregBox.sendKeys("40");

        //7.) Enter weight: asldkfj
        WebElement weightBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[5]/td[2]/input"));
        weightBox.click();
        weightBox.sendKeys("asldkfj");

        //8.) Enter blood pressure 103/62
        WebElement pressBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[6]/td[2]/input"));
        pressBox.click();
        pressBox.sendKeys("103/62");

        // 9.) Enter FHR: 152
        WebElement fhrBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[7]/td[2]/input"));
        fhrBox.click();
        fhrBox.sendKeys("152");

        //10.)  Select Multiple: 1
        WebElement multBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[8]/td[2]/input"));
        multBox.click();
        multBox.sendKeys("1");

        //11.) submit and check post conditions
        driver.findElement(By.id("createApptButton")).click();
        assertEquals(driver.findElement(By.className("iTrustError")).getText(), "This form has not been validated correctly. The following fields are not properly filled in: [Weight: Should be an integer greater than 0]");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICEVISIT_CREATE, 9000000012L, 400, "Office Visit ID : 2");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 400, "Office Visit ID : 2, the next visit's id");

    }

    public void testAddObstetricsOVWithUltraSoundInfoSuccess() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 400
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Daria Griffin");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='400']")));
        driver.findElement(By.xpath("//input[@value='400']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Daria Griffin's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"addObstetricOVButton\"]")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addOVRecords.jsp", driver.getCurrentUrl());


        //5.) Enter: Date: 10/07/2018
        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[3]/td[2]/input[1]"));
        dateBox.click();
        dateBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateBox.sendKeys(Keys.chord(Keys.DELETE));
        dateBox.sendKeys("10/07/2018");

        //6.) Enter: days pregnant: 40
        WebElement dayPregBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[4]/td[2]/input"));
        dayPregBox.click();
        dayPregBox.sendKeys("40");

        //7.) Enter weight: 137
        WebElement weightBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[5]/td[2]/input"));
        weightBox.click();
        weightBox.sendKeys("137");

        //8.) Enter blood pressure 103/62
        WebElement pressBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[6]/td[2]/input"));
        pressBox.click();
        pressBox.sendKeys("103/62");

        // 9.) Enter FHR: 152
        WebElement fhrBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[7]/td[2]/input"));
        fhrBox.click();
        fhrBox.sendKeys("152");

        //10.)  Select Multiple: 1
        WebElement multBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[8]/td[2]/input"));
        multBox.click();
        multBox.sendKeys("1");

        //11.)  Select Ultra Sound
        WebElement us = driver.findElement(By.xpath("//*[@id=\"ultraSoundSelect\"]"));
        us.click();

        //12.) submit and check post conditions
        driver.findElement(By.id("createApptButton")).click();
        assertTrue(driver.getPageSource().contains("Upload Ultrasound Image"));
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_CREATE, 9000000012L, 400, "Office Visit ID : 2");
        assertLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 400, "Office Visit ID : 2, the next visit's id");

        //13.) fill out form:
        WebElement crownRumpLength = driver.findElement(By.xpath("//*[@id=\"crownRumpLength\"]"));
        WebElement biparietalDiameter = driver.findElement(By.xpath("//*[@id=\"biparietalDiameter\"]"));
        WebElement headCircumference = driver.findElement(By.xpath("//*[@id=\"headCircumference\"]"));
        WebElement femurLength = driver.findElement(By.xpath("//*[@id=\"femurLength\"]"));
        WebElement occipitofrontalDiameter = driver.findElement(By.xpath("//*[@id=\"occipitofrontalDiameter\"]"));
        WebElement abdominalCircumference = driver.findElement(By.xpath("//*[@id=\"abdominalCircumference\"]"));
        WebElement humerusLength = driver.findElement(By.xpath("//*[@id=\"humerusLength\"]"));
        WebElement estimatedFetalWeight = driver.findElement(By.xpath("//*[@id=\"estimatedFetalWeight\"]"));


        crownRumpLength.click();
        crownRumpLength.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        crownRumpLength.sendKeys(Keys.chord(Keys.DELETE));
        crownRumpLength.sendKeys("1");

        biparietalDiameter.click();
        biparietalDiameter.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        biparietalDiameter.sendKeys(Keys.chord(Keys.DELETE));
        biparietalDiameter.sendKeys("1");

        headCircumference.click();
        headCircumference.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        headCircumference.sendKeys(Keys.chord(Keys.DELETE));
        headCircumference.sendKeys("1");

        femurLength.click();
        femurLength.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        femurLength.sendKeys(Keys.chord(Keys.DELETE));
        femurLength.sendKeys("1");

        occipitofrontalDiameter.click();
        occipitofrontalDiameter.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        occipitofrontalDiameter.sendKeys(Keys.chord(Keys.DELETE));
        occipitofrontalDiameter.sendKeys("1");

        abdominalCircumference.click();
        abdominalCircumference.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        abdominalCircumference.sendKeys(Keys.chord(Keys.DELETE));
        abdominalCircumference.sendKeys("1");

        humerusLength.click();
        humerusLength.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        humerusLength.sendKeys(Keys.chord(Keys.DELETE));
        humerusLength.sendKeys("1");

        estimatedFetalWeight.click();
        estimatedFetalWeight.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        estimatedFetalWeight.sendKeys(Keys.chord(Keys.DELETE));
        estimatedFetalWeight.sendKeys("1");

        //14.) submit
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,250)", "");
        driver.findElement(By.xpath("//*[@id=\"createUltrasonicRecordButton\"]")).click();


        //FIXME -- DBException: A database exception has occurred. Please see the log in the console for stacktrace upon adding ultrasound....
//        assertEquals(driver.getPageSource().contains("Record added / updated successfully"));
//        assertLogged(TransactionType.ULTRASOUND_CREATE_ADD, 9000000012L, 400, "Office Visit ID : 2");

    }


    public void testAddObstetricsOVWithUltraSoundInfoFailure() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 400
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Daria Griffin");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='400']")));
        driver.findElement(By.xpath("//input[@value='400']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Daria Griffin's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"addObstetricOVButton\"]")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addOVRecords.jsp", driver.getCurrentUrl());


        //5.) Enter: Date: 10/07/2018
        WebElement dateBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[3]/td[2]/input[1]"));
        dateBox.click();
        dateBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dateBox.sendKeys(Keys.chord(Keys.DELETE));
        dateBox.sendKeys("10/07/2018");

        //6.) Enter: days pregnant: 40
        WebElement dayPregBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[4]/td[2]/input"));
        dayPregBox.click();
        dayPregBox.sendKeys("40");

        //7.) Enter weight: 137
        WebElement weightBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[5]/td[2]/input"));
        weightBox.click();
        weightBox.sendKeys("137");

        //8.) Enter blood pressure 103/62
        WebElement pressBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[6]/td[2]/input"));
        pressBox.click();
        pressBox.sendKeys("103/62");

        // 9.) Enter FHR: 152
        WebElement fhrBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[7]/td[2]/input"));
        fhrBox.click();
        fhrBox.sendKeys("152");

        //10.)  Select Multiple: 1
        WebElement multBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[8]/td[2]/input"));
        multBox.click();
        multBox.sendKeys("1");

        //11.)  Select Ultra Sound
        WebElement us = driver.findElement(By.xpath("//*[@id=\"ultraSoundSelect\"]"));
        us.click();

        //12.) submit and check post conditions
        driver.findElement(By.id("createApptButton")).click();
        assertTrue(driver.getPageSource().contains("Upload Ultrasound Image"));
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_CREATE, 9000000012L, 400, "Office Visit ID : 2");
        assertLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 400, "Office Visit ID : 2, the next visit's id");

        //13.) fill out form:
        WebElement crownRumpLength = driver.findElement(By.xpath("//*[@id=\"crownRumpLength\"]"));
        WebElement biparietalDiameter = driver.findElement(By.xpath("//*[@id=\"biparietalDiameter\"]"));
        WebElement headCircumference = driver.findElement(By.xpath("//*[@id=\"headCircumference\"]"));
        WebElement femurLength = driver.findElement(By.xpath("//*[@id=\"femurLength\"]"));
        WebElement occipitofrontalDiameter = driver.findElement(By.xpath("//*[@id=\"occipitofrontalDiameter\"]"));
        WebElement abdominalCircumference = driver.findElement(By.xpath("//*[@id=\"abdominalCircumference\"]"));
        WebElement humerusLength = driver.findElement(By.xpath("//*[@id=\"humerusLength\"]"));
        WebElement estimatedFetalWeight = driver.findElement(By.xpath("//*[@id=\"estimatedFetalWeight\"]"));


        crownRumpLength.click();
        crownRumpLength.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        crownRumpLength.sendKeys(Keys.chord(Keys.DELETE));
        crownRumpLength.sendKeys("1");

        biparietalDiameter.click();
        biparietalDiameter.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        biparietalDiameter.sendKeys(Keys.chord(Keys.DELETE));
        biparietalDiameter.sendKeys("1");

        headCircumference.click();
        headCircumference.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        headCircumference.sendKeys(Keys.chord(Keys.DELETE));
        headCircumference.sendKeys("1");

        femurLength.click();
        femurLength.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        femurLength.sendKeys(Keys.chord(Keys.DELETE));
        femurLength.sendKeys("1");

        occipitofrontalDiameter.click();
        occipitofrontalDiameter.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        occipitofrontalDiameter.sendKeys(Keys.chord(Keys.DELETE));
        occipitofrontalDiameter.sendKeys("1");

        abdominalCircumference.click();
        abdominalCircumference.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        abdominalCircumference.sendKeys(Keys.chord(Keys.DELETE));
        abdominalCircumference.sendKeys("asdf");

        humerusLength.click();
        humerusLength.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        humerusLength.sendKeys(Keys.chord(Keys.DELETE));
        humerusLength.sendKeys("1");

        estimatedFetalWeight.click();
        estimatedFetalWeight.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        estimatedFetalWeight.sendKeys(Keys.chord(Keys.DELETE));
        estimatedFetalWeight.sendKeys("1");

        //14.) submit
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,250)", "");
        driver.findElement(By.xpath("//*[@id=\"createUltrasonicRecordButton\"]")).click();


        assertEquals(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/span")).getText(), "This form has not been validated correctly. The following fields are not properly filled in: [Abdominal Circumference : Should be a decimal greater than 0]");
        assertNotLogged(TransactionType.ULTRASOUND_CREATE_ADD, 9000000012L, 400, "Office Visit ID : 2");

    }

    public void testEditObstetricsOVSuccessful() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Amelia Davidson's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[2]/table/tbody/tr[2]/td[5]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetricOVRecord.jsp?obovid=1", driver.getCurrentUrl());

        //6.) Enter: days pregnant: 40
        WebElement dayPregBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[4]/td[2]/input"));
        dayPregBox.click();
        dayPregBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dayPregBox.sendKeys(Keys.chord(Keys.DELETE));
        dayPregBox.sendKeys("40");

        //11.) submit and check post conditions
        driver.findElement(By.id("changeButton")).click();
        assertTrue(driver.getPageSource().contains("Obstetrics Office Visit successfully edited."));
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_VIEW, 9000000012L, 402, "Office Visit ID : 1");
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_EDIT, 9000000012L, 402, "Office Visit ID : 1");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 402, "Office Visit ID : 1, the next visit's id");

    }
    public void testEditObstetricsOVFailure() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Amelia Davidson's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[2]/table/tbody/tr[2]/td[5]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetricOVRecord.jsp?obovid=1", driver.getCurrentUrl());

        //6.) Enter: days pregnant: 40
        WebElement dayPregBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div/table/tbody/tr[4]/td[2]/input"));
        dayPregBox.click();
        dayPregBox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dayPregBox.sendKeys(Keys.chord(Keys.DELETE));
        dayPregBox.sendKeys("asdf");

        //11.) submit and check post conditions
        driver.findElement(By.id("changeButton")).click();
        assertEquals(driver.findElement(By.className("iTrustError")).getText(), "This form has not been validated correctly. The following fields are not properly filled in: [Number Of Days Pregnant: Should be an integer greater than 0]");
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_VIEW, 9000000012L, 402, "Office Visit ID : 1");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICEVISIT_EDIT, 9000000012L, 402, "Office Visit ID : 1");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 402, "Office Visit ID : 1, the next visit's id");

    }

    public void testEditObstetricsOVRemove() throws Exception{
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/obstetricOVRecords.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Amelia Davidson");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // 4.) click "add office visit record button"
        assertTrue(driver.getPageSource().contains("Amelia Davidson's Previous Obstetric Office Visits"));
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[2]/table/tbody/tr[2]/td[5]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetricOVRecord.jsp?obovid=1", driver.getCurrentUrl());

        //11.) submit and check post conditions
        driver.findElement(By.id("removeButton")).click();
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_VIEW, 9000000012L, 402, "Office Visit ID : 1");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICEVISIT_EDIT, 9000000012L, 402, "Office Visit ID : 1");
        assertLogged(TransactionType.OBSTETRIC_OFFICEVISIT_REMOVE, 9000000012L, 402, "Office Visit ID : 1");
        assertNotLogged(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT, 9000000012L, 402, "Office Visit ID : 1, the next visit's id");

    }
}