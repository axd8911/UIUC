package edu.ncsu.csc.itrust.unit.T822.UC93.unit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;

import edu.ncsu.csc.itrust.action.ObstetAction;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ObstetActionTest extends TestCase{

	private ObstetAction action;
	private DAOFactory factory;
	private long hcpId = 9000000012L;
	TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {

		gen.clearAllTables();
		gen.hcp0();
		gen.uc93();

		this.factory = TestDAOFactory.getTestInstance();
		this.action = new ObstetAction(this.factory, this.hcpId);
	}

	/**
	 * Test for obgyn check
	 * @throws Exception
	 */
	public void testIsOBGYN() throws Exception {

		assertTrue(action.isOBGYN(9000000012L));
		assertFalse(action.isOBGYN(9000000000L));
	}

	public void testGetCurrentDateinString(){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();

		assertEquals(ObstetAction.getCurrentDateinString(), sdf.format(c.getTime()));

	}

	public void testGetName() throws ITrustException{


		assertEquals(action.getName(9000000012L), "Kathryn Evans");
		assertEquals(action.getName(400L), "Daria Griffin");
	}

	public void testIsObstetricEligible() throws ITrustException {
		assertTrue(action.isObstetricEligible(400L));
		assertFalse(action.isObstetricEligible(403L));
		assertFalse(action.isObstetricEligible(9000000012L));
	}

	public void testUpdateObstetRec() throws ITrustException{
		ObstetRecBean ob = action.getObstetRec(2, 401, false);
		Date newDate = new Date(2018, 9, 1);
		ob.setLmpDate(new Date(2018, 9, 1));
		action.updateObstetRec(ob);
		ob = action.getObstetRec(2, 401, false);
		assertEquals(ob.getLmpDate(), newDate);

	}

	public void testGetObstetRecFormNotFilled() throws ITrustException, InterruptedException{

		ObstetRecBean ob = action.getObstetRec(2, 401, false);
		assertEquals(ob.getEddDate().toString(), "Thu Oct 07 00:00:00 CDT 2010");
	}

	public void testUpdatePriorPreg() throws ITrustException{
		ObstetPriorPregBean ob = action.getPriorPreg(2, 401, false);
		int newYear = 2012;

		ob.setConceptionYear(newYear);

		action.updatePriorPreg(ob);
		ob = action.getPriorPreg(2, 401, false);

		assertEquals(ob.getConceptionYear(), newYear);

	}

	public void testAddObstetRec() throws ITrustException, FormValidationException {
        ObstetRecBean ob = new ObstetRecBean();

        ob.setMID(402);
        ob.setLmpDate(new Date(2018,8,8));
        ob.setInitDate(new Date(2018, 10,10));

        action.addObstetRec(ob, hcpId);

        ob = action.getObstetRec(3, 402, true);

        assertEquals(ob.getObstetRecID(), 3);
    }

    public void testAddPriorPreg() throws ITrustException, FormValidationException {
        ObstetPriorPregBean ob = new ObstetPriorPregBean();

        ob.setMid(402);
        ob.setConceptionYear(2015);
        ob.setPregWeeks(40);
        ob.setPregDays(5);
        ob.setLaborHours(5.5f);
        ob.setWeightGain(15.5f);
        ob.setDeliveryType("vaginal_delivery");

        action.addPriorPreg(ob, hcpId);

        ob = action.getPriorPreg(3, 402, true);

        assertEquals(ob.getPriorPregRecID(), 3);
    }

}

