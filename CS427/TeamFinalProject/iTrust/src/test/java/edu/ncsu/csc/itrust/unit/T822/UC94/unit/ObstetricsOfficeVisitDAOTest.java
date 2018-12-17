package edu.ncsu.csc.itrust.unit.T822.UC94.unit;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.Assume;

import java.util.List;

public class ObstetricsOfficeVisitDAOTest extends TestCase {
    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private ObstetricOfficeVisitDAO officeVisitDAO = factory.getObstetricOfficeVisitDAO();
    TestDataGenerator gen = new TestDataGenerator();

    @Override
    protected void setUp() throws Exception {
    	gen.clearAllTables();
        gen.uc93();
        gen.uc94();
    }

    /* test get all office visits */
    public void testPatientAllObstetricOfficeVisits() throws Exception {
        List<ObstetricOVBean> officeVisitList = officeVisitDAO.
                getPatientOfficeVisitsByPatientId(402);
        assertEquals(1,officeVisitList.size());
    }

    /* test get all HCP and Patients office visits */
    public void testHCPPatientAllObstetricOfficeVisits() throws Exception {
        List<ObstetricOVBean> officeVisitList = officeVisitDAO.
                getPatientOfficeVisitsByPatientHCPId(402,9000000012L);
        assertEquals(1,officeVisitList.size());
    }

    /* test to validate the OB patient is valid or not */
    public void testOBPatientValidation () throws Exception {
        ObstetricOVBean obstetricOVBean = new ObstetricOVBean();
        obstetricOVBean.setPatientMID(402);
        assertTrue(officeVisitDAO.validateOBPatient(obstetricOVBean));
        obstetricOVBean.setPatientMID(99999);
        assertFalse(officeVisitDAO.validateOBPatient(obstetricOVBean));
    }

    /* test edit patient records by Non-OB/GYN HCP*/
    public void testAddAndRemoveNewOBOfficeVisit () throws Exception {
        ObstetricOVBean obstetricOVBean = new ObstetricOVBean();
        obstetricOVBean.setPatientMID(402);
        obstetricOVBean.setoBhcpMID(9000000012L);
        byte val = 2;
        obstetricOVBean.setMultiplet(val);
        long visitId = officeVisitDAO.addObstetricRecord(obstetricOVBean);
        Assume.assumeTrue(visitId>0);
        obstetricOVBean.setObVisitID(visitId);
        assertTrue(officeVisitDAO.deleteObstetricRecord(obstetricOVBean));
    }

}
