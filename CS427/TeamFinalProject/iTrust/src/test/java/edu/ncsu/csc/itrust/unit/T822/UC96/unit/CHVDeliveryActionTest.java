package edu.ncsu.csc.itrust.unit.T822.UC96.unit;

import edu.ncsu.csc.itrust.action.CHVApptAction;
import edu.ncsu.csc.itrust.action.CHVDeliveryAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.CHVBabyBean;
import edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.CHVDeliveryDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;

import java.sql.Timestamp;
import java.util.List;

public class CHVDeliveryActionTest extends TestCase {
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private CHVDeliveryDAO chvDeliveryDAO = factory.getCHVDeliveryDAO();
    private CHVDeliveryBean bean;
    private CHVBabyBean baby;
    private TestDataGenerator gen = new TestDataGenerator();
    private DBBuilder dbBuilder = new DBBuilder();
    private long hcpid = 9000000012L;
    private long pid = 402L;
    private CHVDeliveryAction action = new CHVDeliveryAction(factory, hcpid);

    @Override
    protected void setUp() throws Exception{

        bean = null;
        gen.clearAllTables();
        gen.uc93();
        gen.uc96();
    }

    public void testGetNamePatient() throws ITrustException {
        String actual = action.getName(pid);
        assertEquals("Amelia Davidson", actual);
    }

    public void testGetNameHCP() throws ITrustException {
        String actual = action.getName(hcpid);
        assertEquals("Kathryn Evans", actual);
    }

    public void testAddDelv() throws Exception {
        bean = action.getDelv(1);
        bean.setDeliveryID(2);
        String actual = action.addDelv(bean);
        assertEquals("Success: childbirth delivery for 2018-10-14 10:00:00.0 added", actual);
    }

    public void testGetDelvs() throws Exception {
        List<CHVDeliveryBean> result = action.getDelvs(hcpid);
        assertEquals(1, result.size());
    }

    public void testGetDelivery() throws Exception{
        bean = action.getDelv(1);
        assertEquals(1, bean.getDeliveryID());
    }

    public void testGetDeliveryNull() throws Exception{
        bean = action.getDelv(4);
        assertEquals(null, bean);
    }

    public void testEditDelv() throws Exception{
        bean = action.getDelv(1);
        bean.setDate(new Timestamp(1544226214000L));
        String actual = action.editDelv(bean);

        assertEquals("Success: Delivery changed", actual);
    }

    public void testRemoveDelv() throws Exception{
        bean = action.getDelv(1);
        String actual = action.removeDelv(bean);

        assertEquals("Success: Delivery removed", actual);
    }

    public void testGetBaby () throws Exception {
        baby = action.getBaby(1);
        assertEquals(1, baby.getBabyID());

    }

    public void testGetBabies () throws Exception {
        List<CHVBabyBean>  result = action.getBabies(1);

        assertEquals(1, result.size());

    }

    public void testAddBaby () throws Exception {

        CHVBabyBean baby1 = new CHVBabyBean();
        CHVBabyBean baby2 = new CHVBabyBean();
        
        baby1.setBabyID(2);
        baby1.setFirstName("Frank");
        baby1.setLastName("Ferg");
        baby1.setDeliveryID(2);
        baby1.setMid(hcpid);
        baby1.setGender("Male");
        
        baby2.setBabyID(3);
        baby2.setFirstName("Frank");
        baby2.setLastName("Ferg");
        baby2.setDeliveryID(2);
        baby2.setMid(hcpid);
        baby2.setGender("Male");

        
        bean = action.getDelv(1);
        bean.setDeliveryID(2);
        bean.getBabies().clear();
        bean.getBabies().add(baby1);
        bean.getBabies().add(baby2);

        String actual = action.addCHVBaby(bean);

        assertEquals("Success: Babies added", actual);
    }
}
