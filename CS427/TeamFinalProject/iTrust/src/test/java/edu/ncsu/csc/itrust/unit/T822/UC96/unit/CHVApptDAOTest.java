package edu.ncsu.csc.itrust.unit.T822.UC96.unit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.CHVApptBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.CHVApptDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.SQLException;
import java.util.List;

public class CHVApptDAOTest extends TestCase {

    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private CHVApptDAO chvapptDAO = factory.getCHVApptDAO();
    private CHVApptBean bean;
    private TestDataGenerator gen = new TestDataGenerator();
    private CHVApptBeanLoader loader = new CHVApptBeanLoader();
    private DBBuilder dbBuilder = new DBBuilder();
    private long hcpid = 9000000012L;

    @Override
    protected void setUp() throws Exception{

        bean = null;
        gen.clearAllTables();
        gen.uc93();
        gen.uc96();
    }

    public void testGetBeanSuccess() throws Exception {
        bean = chvapptDAO.getAppt(1).get(0);

        assertEquals(1, bean.getApptID());
    }

    public void testGetBeanFailure() throws Exception {
        dbBuilder.dropTables();
        try{
            bean = chvapptDAO.getAppt(2).get(0);
            fail();
        } catch (DBException e){
            ;
        }
        dbBuilder.createTables();
    }

    public void testGetApptsForSuccess(){
        try{
            List<CHVApptBean> beans = chvapptDAO.getApptsFor(hcpid);
            assertEquals(1, beans.size());
        }catch (DBException | SQLException e){
            fail();
        }
    }

    public void testGetApptsForFailure() throws Exception{
        dbBuilder.dropTables();
        try{
            List<CHVApptBean> beans = chvapptDAO.getApptsFor(hcpid);
            assertEquals(1, beans.size());
        }catch (DBException | SQLException e){
        }
        dbBuilder.createTables();
    }

    public void testSceduleApptSuccess() throws Exception{
        bean = chvapptDAO.getAppt(1).get(0);
        bean.setApptID(2);
        chvapptDAO.scheduleAppt(bean);

        bean = chvapptDAO.getAppt(2).get(0);
        assertEquals(2, bean.getApptID());
    }

    public void testSceduleApptFailure() throws Exception{
        dbBuilder.dropTables();
        try{
            bean = chvapptDAO.getAppt(1).get(0);
            bean.setApptID(2);
            chvapptDAO.scheduleAppt(bean);
            fail();
        } catch (DBException e){
            ;
        }
        dbBuilder.createTables();
    }

    public void testEditApptSuccess() throws Exception{
        bean = chvapptDAO.getAppt(1).get(0);
        bean.setComment("I AM NOW THE COMMENT");
        chvapptDAO.editAppt(bean);

        bean = chvapptDAO.getAppt(1).get(0);
        assertEquals("I AM NOW THE COMMENT", bean.getComment());
    }

    public void testEditApptFailure() throws Exception{
        dbBuilder.dropTables();
        try{
            bean = chvapptDAO.getAppt(1).get(0);
            bean.setComment("I AM NOW THE COMMENT");
            chvapptDAO.editAppt(bean);
            fail();
        } catch (DBException e){
            ;
        }
        dbBuilder.createTables();
    }

    public void testRemoveApptSuccess() throws Exception{
        try{
            bean = chvapptDAO.getAppt(1).get(0);
            chvapptDAO.removeAppt(bean);
            bean = chvapptDAO.getAppt(1).get(0);
            fail();
        } catch (IndexOutOfBoundsException e){
            ;
        }
    }

    public void testRemoveApptFailure() throws Exception{
        dbBuilder.dropTables();
        try{
            bean = chvapptDAO.getAppt(1).get(0);
            chvapptDAO.removeAppt(bean);
            fail();
        } catch (DBException e){
            ;
        }
        dbBuilder.createTables();
    }

}
