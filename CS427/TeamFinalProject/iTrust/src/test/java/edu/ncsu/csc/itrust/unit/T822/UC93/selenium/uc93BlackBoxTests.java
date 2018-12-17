package edu.ncsu.csc.itrust.unit.T822.UC93.selenium;

import java.util.List;

import edu.ncsu.csc.itrust.util.OSInfo;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;

import static java.util.concurrent.TimeUnit.SECONDS;

public class uc93BlackBoxTests extends iTrustSeleniumTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private StringBuffer verificationErrors = new StringBuffer();

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
        gen.hcp0();
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

    @Override
    protected void tearDown() {
        driver.quit();
    }


    /**
     * Test adding obstetrics record for an ineligible patient
     *
     * @throws Exception
     */
    public void testAddIneligiblePatient() throws Exception {
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Thane Ross's MID = 403
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("403");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='403']")));
        driver.findElement(By.xpath("//input[@value='403']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Check if appropriate error message is displayed.
        try {
            // get the error message
            assertEquals("The selected patient is not eligible for obstetric care. Please select a different patient.",
                    driver.findElement(By.cssSelector("span.iTrustMessage")).getText());
        } catch (Error e) {
            fail();
        }
    }


    /**
     * Test add obstetrics record for a patients who has two priors pregnancy
     * (two obstetircs records)
     * @throws Exception
     */
    public void testAddPatient2Priors() throws Exception {
        // 1.) Login
        

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Brenna Lowery's MID = 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Choose "Add Obstetrics Record" and check if it's on the correct page
        driver.findElement(By.id("addObstetRecButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addObstetRec.jsp",
                driver.getCurrentUrl());

        // 6.) Enter LMP: 2018-07-26
        driver.findElement(By.name("lmpDate")).sendKeys("07-26-2018");

        // 7.) Choose Submit
        driver.findElement(By.id("addPatient")).click();

        // Check postconditions
        try {
            // get the massage that the info has been updated
            assertEquals("The patient's obstetric record has been successfully added!",
                    driver.findElement(By.xpath("//span[@class='iTrustMessage']")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
            fail();
        }

        // checks to make sure init obstetrics patient was logged id = 9300
        // Login MID = 9000000012
        // Secondary MID = 401 which is the patient MID
        assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Thu May 02 00:00:00 CDT 2019");

    }

    public void testAddPatientChangeMind() throws Exception {

        // 1.) Login

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 406
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("406");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='406']")));
        driver.findElement(By.xpath("//input[@value='406']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Select different patient
        driver.findElement(By.linkText("Select a Different Patient")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

    }


    /**
     * Test add obstetrics record for a patients who has two priors pregnancy
     * (two obstetircs records)
     *
     * @throws Exception
     */
    public void testAddPatientEnterPrior() throws Exception {
        // 1.) Login
        

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("402");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 4.) Choose "Add Obstetrics Record"
        driver.findElement(By.id("addPriorPregButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addPriorPreg.jsp", driver.getCurrentUrl());

        // 5.) Enter "Year of Conception"
        driver.findElement(By.name("conceptionYear")).sendKeys("2013");

        // 6.) Enter "Weeks of pregnancy"
        driver.findElement(By.name("pregnant_weeks")).sendKeys("41");
        driver.findElement(By.name("pregnant_days")).sendKeys("6");

        // 7.) Enter "Hours in Labor"
        driver.findElement(By.name("laborHours")).sendKeys("35");

        // 8.) Enter "Gained weight"
        driver.findElement(By.name("weightGain")).sendKeys("13");

        // 9.) Select "Delivery Type" dropdown list
        Select deliveryTypes = new Select(driver.findElement(By.name("deliveryType")));
        deliveryTypes.selectByVisibleText("Casesarean Section");

        //13.) Enter "Pregnancy multiple"
        driver.findElement(By.name("multiplet")).sendKeys("1");

        // 14.) Choose Submit
        driver.findElement(By.xpath("//input[@value='Add' and @type='submit']")).click();

        // The test past before this point
        // Check postconditions
        try {
            // get the massage that the info has been updated
            assertEquals("The patient's prior pregnancy record has been successfully added!",
                    driver.findElement(By.cssSelector("span.iTrustMessage")).getText());
        } catch (Error e) {
            fail();
        }

        //checks to make sure init obstetrics patient was logged
        assertLogged(TransactionType.CREATE_PRIOR_PREGNANCY_RECORD, 9000000012L, 402, "caesarean_section");
    }

    //last to pass
    public void testAddPatientFutureLMP() throws Exception {

        // 1.) Login
        

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Rock Solid's MID = 405
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("405");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='405']")));
        driver.findElement(By.xpath("//input[@value='405']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Choose "Add Obstetrics Record"
        driver.findElement(By.id("addObstetRecButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addObstetRec.jsp",
                driver.getCurrentUrl());

        // 6.) Enter LMP: 2018-07-14
        driver.findElement(By.name("lmpDate")).sendKeys("05-14-2045");

        // 7.) Choose Submit
        driver.findElement(By.id("addPatient")).click();

        // The test past before this point
        // Check postconditions
        try {
            // get the massage that the info has been updated
            assertEquals("LMP date cannot be after today.",
                    driver.findElement(By.cssSelector("span.iTrustError")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
            fail();
        }

        //checks to make sure init obstetrics record was created event was not logged.
        assertNotLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 405, "Sun Feb 18 00:00:00 CDT 2046");
    }

    public void testAddPatientNoPriors() throws Exception {

        // 1.) Login
        

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 400
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("400");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='400']")));
        driver.findElement(By.xpath("//input[@value='400']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Choose "Add Obstetrics Record"
        driver.findElement(By.id("addObstetRecButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addObstetRec.jsp",
                driver.getCurrentUrl());

        // 6.) Enter LMP: 2018-07-14
        driver.findElement(By.name("lmpDate")).sendKeys("07-14-2018");

        // Do in unit test

        // 7.) Choose Submit
        driver.findElement(By.id("addPatient")).click();

        try {
            // get the massage that the info has been updated
            assertEquals("The patient's obstetric record has been successfully added!",
                    driver.findElement(By.cssSelector("span.iTrustMessage")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
            fail();
        }

        // checks to make sure init obstetrics patient was logged id = 9300
        // Login MID = 9000000012
        // Secondary MID = 400 which is the patient MID
        assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 400, "Sat Apr 20 00:00:00 CDT 2019");
    }

    public void testAddingNonPatientById () throws Exception {
        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Enter someone not in database
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("nobody");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='searchBox']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"searchTarget\"]/table/tbody/tr[2]/td[1]/input")));
        assertTrue(driver.getPageSource().contains("Found 0 Records"));

    }

    public void testAddingPatientByName () throws Exception {
    WebElement element;
        // 1.) Login
        

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());


        // 3.) Select: 404
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Mary Hadalamb");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='404']")));
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='404']")));
        driver.findElement(By.xpath("//input[@value='404']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Choose "Add Obstetrics Record"
        driver.findElement(By.id("addObstetRecButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addObstetRec.jsp",
                driver.getCurrentUrl());

        // 6.) Enter LMP: 2018-07-14
        driver.findElement(By.name("lmpDate")).sendKeys("07-14-2018");

        // 7.) Choose Submit
        driver.findElement(By.id("addPatient")).click();

        try {
            // get the massage that the info has been updated
            assertEquals("The patient's obstetric record has been successfully added!",
                    driver.findElement(By.cssSelector("span.iTrustMessage")).getText());
        } catch (Error e) {
            fail();
        }

        assertLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 404, "Sat Apr 20 00:00:00 CDT 2019");
    }

    public void testPatientViewForNonObstreticHCP()  throws Exception {

        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000000");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Brenna Lowery's MID = 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();


        // Kelly being non OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 0));

        webElementList = driver.findElements(By.id("addPriorPregButton"));
        assertEquals(true, (webElementList.size() == 0));

        //make sure nonobgyn can't navigate to add obstetric record.
        driver.get("http://localhost:8080/iTrust/auth/hcp-obstet/addObstetRec.jsp");
        assertTrue(driver.getPageSource().contains("Authorization Error!"));

        //log back in:
        driver.get("http://localhost:8080/iTrust/");
        driver.findElement(By.id("j_username")).sendKeys("9000000000");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());


        // 3.) Select: Brenna Lowery's MID = 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        //make sure nonobgyn can't navigate to add obstetric record prior preg.
        driver.get("http://localhost:8080/iTrust/auth/hcp-obstet/addPriorPreg.jsp");
        assertTrue(driver.getPageSource().contains("Authorization Error!"));
    }


    public void testViewEditPatientObstetricsHCP() throws Exception {

        // 1.) Login
        

        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }
        // is an OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 1));

        // 5.) Choose Initialization Record 2
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/table/tbody/tr[3]/td[1]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetRec.jsp?recID=2",
                driver.getCurrentUrl());

        assertLogged(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Thu Oct 07 00:00:00 CDT 2010");

        // 6.) Edit the LMP
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input")).click();
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input")).sendKeys(Keys.END);
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input")).sendKeys("0");

        // 7.) submit
        driver.findElement(By.xpath("//*[@id=\"editObstetRecButton\"]")).click();

        assertLogged(TransactionType.EDIT_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Wed Oct 06 00:00:00 CDT 2010");
        assertTrue(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/span")).getText().contains("The patient's obstetric record has been successfully changed!"));
    }

    public void testViewEditPriorPregHCP() throws Exception {

        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // is an OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 1));

        // 5.) Choose Prior preg Record 2
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[2]/table/tbody/tr[3]/td[1]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editPriorPreg.jsp?recID=2",
                driver.getCurrentUrl());

        assertLogged(TransactionType.VIEW_PRIOR_PREGNANCY_RECORD, 9000000012L, 401, "vaginal_delivery_vacuum_assist");

        // 6.) Edit delivery method
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[8]/td[2]/select")).click();
        driver.findElement(By.xpath("//option[@value='vaginal_delivery']")).click();

        // 7.) submit
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/input[2]")).click();

        assertLogged(TransactionType.EDIT_PRIOR_PREGNANCY_RECORD, 9000000012L, 401, "vaginal_delivery");
        assertTrue(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/span")).getText().contains("The patient's prior pregnancy record has been successfully updated!"));
    }


    public void testAddingRecWithTooOldDate () throws Exception {

        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 404
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("Mary Hadalamb");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='404']")));
        driver.findElement(By.xpath("//input[@value='404']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Choose "Add Obstetrics Record"
        driver.findElement(By.id("addObstetRecButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addObstetRec.jsp",
                driver.getCurrentUrl());

        // 6.) Enter LMP: 2018-07-14
        driver.findElement(By.name("lmpDate")).sendKeys("07-14-2016");

        // 7.) Choose Submit
        driver.findElement(By.id("addPatient")).click();

        try {
            // get the massage that the info has been updated
            assertEquals("LMP date is too old to be possible, enter a more realistic date.",
                    driver.findElement(By.cssSelector("span.iTrustError")).getText());
        } catch (Error e) {
            fail();
        }

        assertNotLogged(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, 9000000012L, 404, "Sat Apr 20 00:00:00 CDT 2019");
    }

    public void testAddPatientEnterPriorBadYearInput() throws Exception {
        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("402");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='402']")));
        driver.findElement(By.xpath("//input[@value='402']")).click();

        // check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 4.) Choose "Add Obstetrics Record"
        driver.findElement(By.id("addPriorPregButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/addPriorPreg.jsp", driver.getCurrentUrl());

        // 5.) Enter "Year of Conception"
        driver.findElement(By.name("conceptionYear")).sendKeys("Meow I am the best");

        // 6.) Enter "Weeks of pregnancy"
        driver.findElement(By.name("pregnant_weeks")).sendKeys("41");
        driver.findElement(By.name("pregnant_days")).sendKeys("6");

        // 7.) Enter "Hours in Labor"
        driver.findElement(By.name("laborHours")).sendKeys("35");

        // 8.) Enter "Gained weight"
        driver.findElement(By.name("weightGain")).sendKeys("13");

        // 9.) Select "Delivery Type" dropdown list
        Select deliveryTypes = new Select(driver.findElement(By.name("deliveryType")));
        deliveryTypes.selectByVisibleText("Casesarean Section");

        //13.) Enter "Pregnancy multiple"
        driver.findElement(By.name("multiplet")).sendKeys("1");

        // 14.) Choose Submit
        driver.findElement(By.xpath("//input[@value='Add' and @type='submit']")).click();

        // The test past before this point
        // Check postconditions
        try {
            // get the massage that the info has been updated
            assertEquals("This form has not been validated correctly. The following fields are not properly filled in: [Year of Conception: Should be integer of form YYYY]",
                    driver.findElement(By.cssSelector("span.iTrustError")).getText());
        } catch (Error e) {
            fail();
        }

        //checks to make sure init obstetrics patient was logged
        assertNotLogged(TransactionType.CREATE_PRIOR_PREGNANCY_RECORD, 9000000012L, 402, "caesarean_section");
    }

    public void testEditPatientFutureLMP() throws Exception {

        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }
        // is an OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 1));
        // 5.) Choose Initialization Record 2
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/table/tbody/tr[3]/td[1]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetRec.jsp?recID=2",
                driver.getCurrentUrl());

        assertLogged(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Thu Oct 07 00:00:00 CDT 2010");

        // 6.) Edit the LMP
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input")).click();
        driver.findElement(By.name("lmpDate")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        driver.findElement(By.name("lmpDate")).sendKeys(Keys.chord(Keys.DELETE));
        driver.findElement(By.name("lmpDate")).sendKeys("2045-05-14");

        // 7.) submit
        driver.findElement(By.xpath("//*[@id=\"editObstetRecButton\"]")).click();

        assertNotLogged(TransactionType.EDIT_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Wed Oct 06 00:00:00 CDT 2010");
        assertEquals(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/span")).getText(), "LMP date cannot be after today.");
    }

    public void testEditPatientNullLMP() throws Exception {

        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }
        // is an OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 1));

        // 5.) Choose Initialization Record 2
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div[1]/table/tbody/tr[3]/td[1]/a")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetRec.jsp?recID=2",
                driver.getCurrentUrl());

        assertLogged(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Thu Oct 07 00:00:00 CDT 2010");

        // 6.) Edit the LMP
        driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/table/tbody/tr[5]/td[2]/input")).click();
        driver.findElement(By.name("lmpDate")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        driver.findElement(By.name("lmpDate")).sendKeys(Keys.chord(Keys.DELETE));

        // 7.) submit
        driver.findElement(By.xpath("//*[@id=\"editObstetRecButton\"]")).click();

        assertNotLogged(TransactionType.EDIT_INITIAL_OBSTETRICS_RECORD, 9000000012L, 401, "Wed Oct 06 00:00:00 CDT 2010");
        assertEquals(driver.findElement(By.xpath("//*[@id=\"iTrustContent\"]/div/form/span")).getText(), "Please enter a valid date for Last Menstrual Period (LMP) in format of: YYYY-MM-DD");
    }

    public void testPatientEditForNonObstreticHCP()  throws Exception {

        // 1.) Login
        driver.findElement(By.id("j_username")).sendKeys("9000000000");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Brenna Lowery's MID = 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();


        // Kelly being non OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 0));

        webElementList = driver.findElements(By.id("addPriorPregButton"));
        assertEquals(true, (webElementList.size() == 0));

        //make sure nonobgyn can't navigate to add obstetric record.
        driver.get("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetRec.jsp?recID=2");
        assertTrue(driver.getPageSource().contains("Authorization Error!"));

        //log back in:
        driver.get("http://localhost:8080/iTrust/");
        driver.findElement(By.id("j_username")).sendKeys("9000000000");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());


        // 3.) Select: Brenna Lowery's MID = 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        //make sure nonobgyn can't navigate to add obstetric record prior preg.
        driver.get("http://localhost:8080/iTrust/auth/hcp-obstet/editPriorPreg.jsp?recID=2");
        assertTrue(driver.getPageSource().contains("Authorization Error!"));
    }

    public void testEditObstetRecNoRecID() throws Exception {

        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: 401
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("401");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='401']")));
        driver.findElement(By.xpath("//input[@value='401']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }
        // is an OB/GYN HCP
        List<WebElement> webElementList = driver.findElements(By.id("addObstetRecButton"));
        assertEquals(true, (webElementList.size() == 1));


        driver.get("http://localhost:8080/iTrust/auth/hcp-obstet/editObstetRec.jsp");
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        driver.get("http://localhost:8080/iTrust/auth/hcp-obstet/editPriorPreg.jsp");
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp",
                    driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        }

    public void testNoPriors() throws Exception {

        // 1.) Login


        driver.findElement(By.id("j_username")).sendKeys("9000000012");
        driver.findElement(By.id("j_password")).sendKeys("pw");

        driver.findElement(By.cssSelector("input[type='submit']")).click();
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
        driver.findElement(By.xpath("//div[@anim-target='#obstet-menu']")).click();
        driver.findElement(By.xpath("//a[@href='/iTrust/auth/hcp-obstet/initObstetPatient.jsp']")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Daria Griffin's MID = 400
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys("400");
        driver.findElement(By.xpath("//input[@id='searchBox']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='400']")));
        driver.findElement(By.xpath("//input[@value='400']")).click();

        // 4.) check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-obstet/initObstetPatient.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 5.) Check no records are displaying
        assertTrue(driver.getPageSource().contains("The patient has no obstetric record."));
        assertTrue(driver.getPageSource().contains("The patient has no prior pregnancy record."));

    }
}