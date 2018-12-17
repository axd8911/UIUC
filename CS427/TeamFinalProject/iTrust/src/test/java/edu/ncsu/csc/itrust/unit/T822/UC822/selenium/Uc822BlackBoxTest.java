package edu.ncsu.csc.itrust.unit.T822.UC822.selenium;

import java.util.concurrent.TimeUnit;

import edu.ncsu.csc.itrust.util.OSInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Uc822BlackBoxTest extends iTrustSeleniumTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private StringBuffer verificationErrors = new StringBuffer();
    
    @Override
    protected void setUp() throws Exception {

        String userDir = System.getProperty("user.dir");

        //Determine operating system so proper chromedriver (v2.43) executable will be selected:
        OSInfo.OS os = OSInfo.getOs();
        String chromeDriverLocation = "";
        
        System.out.println(userDir + "/chromedrivers/chromedriver.exe");

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
     * Test add new patient for postnatal care
     *  
     * @throws Exception
     */
    public void testAddPatient() throws Exception {
        WebDriver driver = new HtmlUnitDriver();
    	// 1.) Login
    	driver = login("9000000012", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
		driver.findElement(By.linkText("Postnatal Care Record")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("402");
		driver.findElement(By.xpath("//input[@value='402']")).submit();

        // check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-postnatal/initPostnatalCare.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 4.) Choose "Add Postnatal Care Record"
        driver.findElement(By.id("addPostnatalCareRecordButton")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-postnatal/addPostnatalCareRecord.jsp", driver.getCurrentUrl());
        
        // 5.) Enter "Year of Conception"
        driver.findElement(By.name("record_date")).sendKeys("2018-11-24");
        
        // 6.) Enter "Weeks of pregnancy"
        driver.findElement(By.name("childbirth_date")).sendKeys("2018-11-23");
        
        // 7.) Select "Delivery Type" dropdown list
        Select deliveryTypes = new Select(driver.findElement(By.name("delivery_type")));
        deliveryTypes.selectByVisibleText("Casesarean Section");
        
        // 8.) Choose Submit
        driver.findElement(By.xpath("//input[@value='Add']")).click();

        // Check postconditions
        try {
            // get the massage that the info has been updated
            assertEquals("The patient's postnatal care record has been successfully added!",
                    driver.findElement(By.cssSelector("span.iTrustMessage")).getText());
        } catch (Error e) {
            fail();
        }

        //checks to make sure the event was logged
        assertLogged(TransactionType.CREATE_POSTNATAL_CARE_RECORD, 9000000012L, 402, "");
    }
    
    /**
     * Edit patient: Run AFTER add patient!
     * @throws Exception
     */
    public void testEditPatient() throws Exception {
        WebDriver driver = new HtmlUnitDriver();
    	// 1.) Login
    	driver = login("9000000012", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // 2.) Choose the "Initialize Obstetrics Patient" link from sidebar
		driver.findElement(By.linkText("Postnatal Care Record")).click();
        assertEquals("iTrust - Please Select a Patient", driver.getTitle());

        // 3.) Select: Amelia Davidson's MID = 402
		driver.findElement(By.name("UID_PATIENTID")).sendKeys("400");
		driver.findElement(By.xpath("//input[@value='400']")).submit();

        // check if its on the correct page
        try {
            assertEquals("http://localhost:8080/iTrust/auth/hcp-postnatal/initPostnatalCare.jsp", driver.getCurrentUrl());
        } catch (Error e) {
            fail();
        }

        // 4.) Choose "Add Postnatal Care Record"
        driver.findElement(By.linkText("1")).click();
        assertEquals("http://localhost:8080/iTrust/auth/hcp-postnatal/editPostnatalCareRecord.jsp?recID=1", driver.getCurrentUrl());
  
        // 5.) Enter "Comment"
        driver.findElement(By.name("comment")).clear();
        driver.findElement(By.name("comment")).sendKeys("No comment");
        
     	// 6.) Enter "Release date"
        driver.findElement(By.name("release_date")).clear();
        driver.findElement(By.name("release_date")).sendKeys("2018-11-25"); 
        
        // 7.) Choose Submit
        driver.findElement(By.xpath("//input[@value='Update']")).click();

        // Check postconditions
        try {
            // get the massage that the info has been updated
        	System.out.println(driver.findElement(By.cssSelector("span.iTrustError")).getText());
            assertEquals("The patient's Postnatal Care Record has been successfully updated!",
                    driver.findElement(By.cssSelector("span.iTrustMessage")).getText());
        } catch (Error e) {
            fail();
        }

        //checks to make sure the event was logged
        assertLogged(TransactionType.EDIT_POSTNATAL_CARE_RECORD, 9000000012L, 400, "");
    }
}
