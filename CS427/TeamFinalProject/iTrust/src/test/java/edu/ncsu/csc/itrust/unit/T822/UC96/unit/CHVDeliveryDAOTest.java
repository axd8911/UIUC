package edu.ncsu.csc.itrust.unit.T822.UC96.unit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.CHVBabyBean;
import edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.CHVDeliveryBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.CHVDeliveryDAO;
import edu.ncsu.csc.itrust.unit.DBBuilder;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.SQLException;
import java.util.List;

public class CHVDeliveryDAOTest extends TestCase {
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private CHVDeliveryDAO chvDeliveryDAO = factory.getCHVDeliveryDAO();
    private CHVDeliveryBean bean;
    private CHVBabyBean baby;
    private TestDataGenerator gen = new TestDataGenerator();
    private CHVDeliveryBeanLoader loader = new CHVDeliveryBeanLoader();
    private DBBuilder dbBuilder = new DBBuilder();
    private long hcpid = 9000000012L;

    @Override
    protected void setUp() throws Exception{

        bean = null;
        gen.clearAllTables();
        gen.uc93();
        gen.uc96();
    }

    public void testGetDeliverySuccess() throws Exception {
        bean = chvDeliveryDAO.getDelivery(1).get(0);
        assertEquals(1, bean.getDeliveryID());
    }

    public void testGetDeliveryFailure() throws Exception {
        dbBuilder.dropTables();
        try{
            bean = chvDeliveryDAO.getDelivery(1).get(0);
            fail();
        } catch (DBException | SQLException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }
    }

    public void testGetDelvsForSuccess() throws Exception {
        List<CHVDeliveryBean> beans = chvDeliveryDAO.getAllDelvsFor(hcpid);
        assertEquals(1, beans.size());
    }

    public void testGetDelvsForFailure() throws Exception {
        dbBuilder.dropTables();
        try{
            List<CHVDeliveryBean> beans = chvDeliveryDAO.getAllDelvsFor(hcpid);
            fail();
        } catch (DBException | SQLException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }
    }

    public void testAddDeliverySuccess() throws Exception {
        bean = chvDeliveryDAO.getDelivery(1).get(0);
        bean.setDeliveryID(2);
        chvDeliveryDAO.addDelivery(bean);

        bean = chvDeliveryDAO.getDelivery(2).get(0);
        assertEquals(2, bean.getDeliveryID());
    }

    public void testAddDeliveryFailure() throws Exception {
        dbBuilder.dropTables();
        bean = new CHVDeliveryBean();

        try{
            chvDeliveryDAO.addDelivery(bean);
            fail();
        } catch (DBException | SQLException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }
    }

    public void testEditDeliverySuccess() throws Exception {
        bean = chvDeliveryDAO.getDelivery(1).get(0);
        bean.setDosEpidural(5);
        chvDeliveryDAO.editDelivery(bean);

        bean = chvDeliveryDAO.getDelivery(1).get(0);
        assertEquals(5, bean.getDosEpidural());
    }

    public void testEditDeliveryFailure() throws Exception {
        dbBuilder.dropTables();
        bean = new CHVDeliveryBean();

        try{
            chvDeliveryDAO.editDelivery(bean);
            fail();
        } catch (DBException | SQLException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }
    }

    public void testRemoveDeliverySuccess() throws Exception {
        bean = chvDeliveryDAO.getDelivery(1).get(0);
        chvDeliveryDAO.removeDelivery(bean);

        try{
            bean = chvDeliveryDAO.getDelivery(1).get(0);
            fail();
        } catch (IndexOutOfBoundsException e){
            ;
        }
    }

    public void testRemoveDeliveryFailure() throws Exception{
        dbBuilder.dropTables();
        bean = new CHVDeliveryBean();

        try{
            chvDeliveryDAO.removeDelivery(bean);
            fail();
        } catch (DBException | SQLException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }
    }

    public void testAddEmptyDeliverySuccess() throws Exception {
        chvDeliveryDAO.addEmptyDelivery();
        bean = chvDeliveryDAO.getDelivery(2).get(0);
        assertEquals(2, bean.getDeliveryID());
    }

    public void testAddEmptyDeliveryFailure() throws Exception{
        dbBuilder.dropTables();

        try{
            chvDeliveryDAO.addEmptyDelivery();
            fail();
        } catch (DBException e){
            try{
                dbBuilder.createTables();
            } catch (Exception ex){
                fail();
            }
        }
    }

    public void testAddBabySuccess() throws Exception {
        baby = chvDeliveryDAO.getBaby(1);
        baby.setBabyID(2);
        baby.setFirstName("FRANK");
        chvDeliveryDAO.addBaby(baby);
        baby = chvDeliveryDAO.getBaby(2);

        assertEquals("FRANK", baby.getFirstName());
    }

    public void testGetBabiesForSuccess() throws Exception {
        baby = chvDeliveryDAO.getBaby(1);
        baby.setBabyID(2);
        baby.setFirstName("FRANK");
        chvDeliveryDAO.addBaby(baby);
        List<CHVBabyBean> babies = chvDeliveryDAO.getBabiesFor(1);

        assertEquals(2, babies.size());
    }

}
