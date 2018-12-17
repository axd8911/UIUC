package edu.ncsu.csc.itrust.unit.T822.UC96.unit;

import edu.ncsu.csc.itrust.action.CHVApptAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.CHVApptBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.CHVApptDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CHVApptActionTest extends TestCase {

    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private CHVApptDAO chvapptDAO = factory.getCHVApptDAO();
    private CHVApptBean bean;
    private TestDataGenerator gen = new TestDataGenerator();
    private DBBuilder dbBuilder = new DBBuilder();
    private long hcpid = 9000000012L;
    private long pid = 402L;
    private CHVApptAction action = new CHVApptAction(factory, hcpid);

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

    public void testAddCHVApptSucc() throws Exception{
        bean = action.getAppt(1);
        bean.setApptID(2);
        String actual = action.addCHVAppt(bean, true);

        assertEquals("Success: Childbirth for 2019-10-14 10:00:00.0 added", actual);

    }

    public void testAddCHVApptFail() throws Exception{
        bean = action.getAppt(1);
        dbBuilder.dropTables();
        try{
            String actual = action.addCHVAppt(bean, true);
            fail();
        } catch (DBException | SQLException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }

    }

    public void testAddCHVApptPassedDate() throws Exception{
        bean = chvapptDAO.getAppt(1).get(0);
        bean.setApptID(2);
        bean.setDate(new Timestamp(1544226214000L));
        String actual = action.addCHVAppt(bean, true);

        assertEquals("The scheduled date of this Appointment (2018-12-07 17:43:34.0) has already passed.", actual);

    }

    public void testGetAppointments() throws Exception{
        List<CHVApptBean> result = action.getAppointments(hcpid);
        assertEquals(1, result.size());
    }

    public void testGetAppointment() throws Exception{
        bean = action.getAppt(1);
        assertEquals(1, bean.getApptID());
    }

    public void testGetAppointmentNull() throws Exception{
        bean = action.getAppt(4);
        assertEquals(null, bean);
    }

    public void testEditAppointment() throws Exception{
        bean = action.getAppt(1);
        bean.setComment("I AM THE COMMENT NOW");
        String actual = action.editAppt(bean, true);
        bean = action.getAppt(1);
        assertEquals("I AM THE COMMENT NOW", bean.getComment());
        assertEquals("Success: Appointment changed", actual);
    }

    public void testEditAppointmentBadDate() throws Exception{
        bean = action.getAppt(1);
        bean.setDate(new Timestamp(1544226214000L));
        String actual = action.editAppt(bean, true);
        bean = action.getAppt(1);
        assertEquals("The scheduled date of this appointment (2018-12-07 17:43:34.0) has already passed.", actual);
    }

    public void testRemoveAppointment() throws Exception{
        bean = action.getAppt(1);
        bean.setDate(new Timestamp(1544226214000L));
        String actual = action.removeAppt(bean);
        assertEquals("Success: Appointment removed", actual);
    }


}
