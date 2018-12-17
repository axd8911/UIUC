package edu.ncsu.csc.itrust.unit.T822.UC95.selenium;

import edu.ncsu.csc.itrust.selenium.iTrustSeleniumTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckLaborAndDeliveryReports extends iTrustSeleniumTest {

    @Override
    protected void setUp() throws Exception {
        //TODO: Pending implementation.
    }

    @Override
    protected void tearDown() {
        //TODO: pending implementation.
    }

    public void testLaborAndDeliveryReports() throws Exception {

        WebDriver driver = login("9000000000", "pw");
        //assertLogged(TransactionType.HOME_VIEW, 9000000001L, 0L, "");

        assertEquals("iTrust - HCP Home", driver.getTitle());
        driver.findElement(By.linkText("Labor and Delivery Report")).click();



    }


}
