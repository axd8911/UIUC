package edu.ncsu.csc.itrust.unit.T822.UC93.unit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PriorPregBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetRecDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PriorPregBeanLoaderTest extends TestCase {

    private DAOFactory factory;
    private ObstetRecDAO OBDAO;
    private PriorPregBeanLoader pploader;
    private ObstetPriorPregBean ppBean;
    TestDataGenerator gen = new TestDataGenerator();

    private long hcpId = 9000000012L;

    @Override
    protected void setUp() throws Exception {

        gen.clearAllTables();
        gen.uc93();

        ppBean = new ObstetPriorPregBean();

        ppBean.setPriorPregRecID(3);
        ppBean.setMid(402);
        ppBean.setConceptionYear(2007);
        ppBean.setPregWeeks(40);
        ppBean.setPregDays(3);
        ppBean.setLaborHours(4.5f);
        ppBean.setWeightGain(15.5f);
        ppBean.setDeliveryType("vaginal_delivery");
        ppBean.setMultiplet(1);

        this.factory = TestDAOFactory.getTestInstance();
        this.OBDAO = factory.getObstetRecDAO();
        this.pploader = new PriorPregBeanLoader();
    }

    public void testLoadList() throws  DBException {
        int expectedListLength = 2;
        try (
                Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetpriorpreg"))
        {
            ResultSet results = stmt.executeQuery();
            List<ObstetPriorPregBean> resBeans = pploader.loadList(results);
            results.close();
            assertEquals(expectedListLength, resBeans.size());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException(e);
        }
    }


    public void testLoadParameters() throws SQLException, DBException{

        OBDAO.addPriorPregc(ppBean);


        Connection conn = factory.getConnection();
        PreparedStatement stmt = this.pploader.loadParameters(conn.prepareStatement(
                "INSERT INTO obstetpriorpreg (patientMID, conception_year, pregnant_weeks, pregnant_days, labor_hours, weight_gain, delivery_type, multiplet) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"),
                ppBean);
        String expectedStmt = "INSERT INTO obstetpriorpreg (patientMID, conception_year, pregnant_weeks, pregnant_days, labor_hours, weight_gain, delivery_type, multiplet) VALUES (402, 2007, 40, 3, 4.5, 15.5, 'vaginal_delivery', 1)";
        String actualStmt = stmt.toString();
        assertTrue(actualStmt.contains(expectedStmt));

    }

    public void testLoadSingle() throws DBException {
        OBDAO.addPriorPregc(ppBean);


        try (
                Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetpriorpreg WHERE obstetpriorpreg_id=?"))
        {
            stmt.setLong(1, ppBean.getPriorPregRecID());
            ResultSet results = stmt.executeQuery();
            results.next();
            ObstetPriorPregBean resBean = pploader.loadSingle(results);
            results.close();
            assertEquals(ppBean, resBean);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DBException(e);
        }
    }

    public void testBeanBuilderSuccess() throws FormValidationException {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2007"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);

        assertEquals(ob.getDeliveryType(), ppBean.getDeliveryType());
    }

    public void testBeanBuilderBadYearFormat() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"Poop"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Year of Conception: Should be integer of form YYYY"));
        }
    }

    public void testBeanBuilderYearTooOld() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"1850"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Year of Conception: Past year not possible."));
        }
    }


    public void testBeanBuilderYearFuture() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2850"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Year of Conception: Can't be a future year"));
        }
    }


    public void testBeanBuilderPregWeeksBadFormat() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"Butt"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Pregnant Weeks: Should be an integer between 0 and 52"));
        }
    }

    public void testBeanBuilderPregWeeksNegative() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"-4"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Pregnant Weeks: Should be an integer between 0 and 52"));
        }
    }

    public void testBeanBuilderPregWeeksToBig() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"69"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Pregnant Weeks: Should be an integer between 0 and 52"));
        }
    }

    public void testBeanBuilderPregDaysBadFormat() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"The cake is a lie"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Days Pregnant : Should be an integer between 0 and 6"));
        }
    }

    public void testBeanBuilderPregDaysNegative() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"-7"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Days Pregnant : Should be an integer between 0 and 6"));
        }
    }

    public void testBeanBuilderPregDaysToBig() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"69"});
        map.put("laborHours", new String[] {"4.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Days Pregnant : Should be an integer between 0 and 6"));
        }
    }

    public void testBeanBuilderLaborBadFormat() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"Tralala"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Hours in Labor: Should be a nonzero decimal"));
        }
    }

    public void testBeanBuilderLaborTooBig() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"15000"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Hours in Labor: Not physically possible, use smaller decimal"));
        }
    }

    public void testBeanBuilderLaborNegative() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"-5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Hours in Labor: Should be a nonzero decimal"));
        }
    }

    public void testBeanBuilderWeightBadFormat() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"The game"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Weight Gained: Should be a decimal greater than 0"));
        }
    }

    public void testBeanBuilderWeightTooBig() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"23456"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Weight Gained: Not physically possible, use smaller decimal"));
        }
    }

    public void testBeanBuilderWeightNegative() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"-69"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Weight Gained: Should be a decimal greater than 0"));
        }
    }

    public void testBeanBuilderMultipletBadFormat() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"Eat Babies"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Multiplet: Should be an integer greater than 0"));
        }
    }

    public void testBeanBuilderMultipletNegative() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"-5"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Multiplet: Should be an integer greater than 0"));
        }
    }

    public void testBeanBuilderMultipletZero() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"0"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Multiplet: Should be an integer greater than 0"));
        }
    }

    public void testBeanBuilderMultipletTooBig() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"vaginal_delivery"});
        map.put("multiplet", new String[] {"69"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Multiplet: Not physically possible, use smaller integer"));
        }
    }

    public void testBeanBuilderDeliveryInvalidChoice() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"ice cream"});
        map.put("multiplet", new String[] {"1"});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Delivery Type: Invalid selection."));
        }
    }

    public void testBeanBuilderDeliveryNull() {
        Map<String, String[]> map = new HashMap<>();

        map.put("conceptionYear", new String[] {"2018"});
        map.put("pregnant_weeks", new String[] {"40"});
        map.put("pregnant_days", new String[] {"3"});
        map.put("laborHours", new String[] {"5.5"});
        map.put("weightGain", new String[] {"15.5"});
        map.put("deliveryType", new String[] {"ice cream"});
        map.put("multiplet", new String[] {null});

        try {
            ObstetPriorPregBean ob = PriorPregBeanLoader.beanBuilder(map);
            fail();
        } catch( FormValidationException e){
            assertTrue(e.getMessage().contains("Delivery Type: Invalid selection."));
        }
    }



}
