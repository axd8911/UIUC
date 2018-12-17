package edu.ncsu.csc.itrust.unit.T822.UC94.unit;

import edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitsAction;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.sql.Timestamp;

public class ObstetricOfficeVisitsBasicTest extends TestCase {
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private ObstetricOfficeVisitDAO officeVisitDAO = factory.getObstetricOfficeVisitDAO();
    private TestDataGenerator gen = new TestDataGenerator();
    private Long hcpid = 9000000012L;
    private String pid = "402";
    private ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(factory, pid, hcpid);
    private ObstetricOVBean newBean;
    private ObstetricOVBean oldBean;

    @Override
    protected void setUp() throws Exception {
        gen.clearAllTables();
        gen.uc93();
        gen.uc94();


        newBean = new ObstetricOVBean();

        newBean.setFhr((short)12);
        newBean.setMultiplet((byte)1);
        newBean.setNumberDaysPregnant((short)43);
        newBean.setWeight((short)123);
        newBean.setPatientMID(403);
        newBean.setObVisitID(4);
        newBean.setoBhcpMID(hcpid);
        newBean.setBloodPressure("123/123");
        newBean.setLlp((byte)0);
        newBean.setRhShotTaken((byte)0);
        newBean.setUltraSound((byte)0);
        newBean.setVisitDate(new Timestamp(1544119388L));

        oldBean = officeVisitDAO.getPatientOfficeVisitsByPatientId(402).get(0);
    }


    public void testValidateOBOVPatientRecordSuccess() throws Exception{
        assertTrue(action.validateOBOVPatientRecord(oldBean));
    }

    public void testValidateOBOVPatientRecordFailure() throws Exception{
        assertFalse(action.validateOBOVPatientRecord(newBean));
    }

    public void testEditOBOVPatientRecordSuccess() throws Exception{
        ObstetricOVBean res = action.editOBOVPatientRecord(oldBean);
        assertEquals(res, oldBean);
    }

    public void testRemoveOBOVPatientRecordSuccess() throws Exception {
        assertTrue(action.removeOBOVPatientRecord(oldBean));
    }

    public void testRemoveOBOVPatientRecordFailure() throws Exception {
        assertFalse(action.removeOBOVPatientRecord(newBean));
    }
}
