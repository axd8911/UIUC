package edu.ncsu.csc.itrust.unit.T822.UC93.unit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PriorPregBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetRecDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.util.Date;
import java.util.List;

public class ObstetRecDAOTest extends TestCase {

    private DAOFactory factory;
    private ObstetRecDAO OBDAO;
    private ObstetPriorPregBean ppBean;
    private ObstetRecBean orBean;
    private ObstetBeanLoader obloader;
    private PriorPregBeanLoader pploader;
    TestDataGenerator gen = new TestDataGenerator();

    private long hcpId = 9000000012L;

    @Override
    protected void setUp() throws Exception {

        gen.clearAllTables();
        gen.uc93();

        obloader = new ObstetBeanLoader();
        pploader = new PriorPregBeanLoader();

        ppBean = new ObstetPriorPregBean();

        ppBean.setPriorPregRecID(3);
        ppBean.setMid(401);
        ppBean.setConceptionYear(2007);
        ppBean.setPregWeeks(40);
        ppBean.setPregDays(3);
        ppBean.setLaborHours(4.5f);
        ppBean.setWeightGain(15.5f);
        ppBean.setDeliveryType("vaginal_delivery");
        ppBean.setMultiplet(1);


        orBean = new ObstetRecBean();

        orBean.setMID(401);
        orBean.setObstetRecID(3);
        orBean.setLmpDate(new Date(2018-1900,8,8));
        orBean.setInitDate(new Date(2018-1900, 10,10));

        orBean = obloader.calculateDates(orBean);


        this.factory = TestDAOFactory.getTestInstance();
        this.OBDAO = factory.getObstetRecDAO();
    }

    public void testGetObstetRecsSuccess() throws DBException {
        List<ObstetRecBean> res = OBDAO.getObstetRecs(401);

        assertEquals(2, res.size());

    }

    public void testGetObstetRecsZeroResults() {
        try {
            List<ObstetRecBean> res = OBDAO.getObstetRecs(-7);
            assertEquals(0, res.size());
        } catch(DBException e){
            ;
        }
    }

    public void testGetObstetRecSuccess() throws DBException {
        OBDAO.addObstetRec(orBean);
        ObstetRecBean res = OBDAO.getObstetRec(3);

        assertEquals(orBean, res);

    }

    public void testGetObstetRecFailure() throws DBException {
        OBDAO.addObstetRec(orBean);
        ObstetRecBean res = OBDAO.getObstetRec(-1);

        assertEquals(null, res);
    }

    public void testGetPriorPregSuccess() throws DBException {
        OBDAO.addPriorPregc(ppBean);
        ObstetPriorPregBean res = OBDAO.getPriorPreg(3);

        assertEquals(ppBean, res);
    }

    public void testGetPriorPregFailure() throws DBException {
        OBDAO.addPriorPregc(ppBean);
        ObstetPriorPregBean res = OBDAO.getPriorPreg(-1);

        assertEquals(null, res);
    }

    public void testGetPriorPregsSuccess() throws DBException {
        List<ObstetPriorPregBean> res = OBDAO.getPriorPregs(401);

        assertEquals(2, res.size());

    }

    public void testGetPriorPregsZeroResults() throws DBException {
        List<ObstetPriorPregBean> res = OBDAO.getPriorPregs(402);

        assertEquals(0, res.size());

    }

    public void testEditObstetRec() throws DBException{
        orBean.setObstetRecID(2);
        OBDAO.editObstetRec(orBean, hcpId);

        ObstetRecBean res = OBDAO.getObstetRec(2);
        res.setInitDate(new Date(2018-1900, 10,10));
        res = obloader.calculateDates(res);

        assertEquals(orBean, res);
    }

    public void testEditObstetRecFailure() throws DBException{
        orBean.setObstetRecID(5);
        OBDAO.editObstetRec(orBean, hcpId);

        ObstetRecBean res = OBDAO.getObstetRec(5);
        try {
            res.setInitDate(new Date(2018 - 1900, 10, 10));
            fail();
        } catch (NullPointerException e){
            ;
        }
    }


    public void testEditPriorPreg() throws DBException{
        ppBean.setPriorPregRecID(2);
        OBDAO.editPriorPreg(ppBean, hcpId);

        ObstetPriorPregBean res = OBDAO.getPriorPreg(2);

        assertEquals(ppBean, res);
    }

    public void testEditPriorPregFailure() throws DBException{
        ppBean.setPriorPregRecID(5);
        OBDAO.editPriorPreg(ppBean, hcpId);

        ObstetPriorPregBean res = OBDAO.getPriorPreg(5);

        assertEquals(null, res);
    }



}
