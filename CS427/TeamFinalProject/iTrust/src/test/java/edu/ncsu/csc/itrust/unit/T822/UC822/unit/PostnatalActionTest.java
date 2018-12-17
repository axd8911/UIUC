package edu.ncsu.csc.itrust.unit.T822.UC822.unit;

import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.action.PostnatalAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class PostnatalActionTest extends TestCase {

	private PostnatalAction action;
	private DAOFactory factory;
	private long hcpId = 9000000012L;
	private SimpleDateFormat sdf;
	private TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {

		gen.clearAllTables();
		gen.uc93();
		gen.uc822();

		this.factory = TestDAOFactory.getTestInstance();
		this.action = new PostnatalAction(this.factory, this.hcpId);
		sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	public void testIsOBGYN() throws Exception {
		assertTrue(action.isOBGYN(9000000012L));
		assertFalse(action.isOBGYN(9000000000L));
	}

	public void testGetCurrentDateinString() {
		Calendar c = Calendar.getInstance();
		assertEquals(PostnatalAction.getCurrentDateinString(), sdf.format(c.getTime()));
	}

	public void testGetName() throws ITrustException {

		assertEquals(action.getName(9000000012L), "Kathryn Evans");
		assertEquals(action.getName(400), "Daria Griffin");
	}

	public void testIsPostnatalEligible() throws ITrustException {
		assertTrue(action.isPostnatalCareEligible(400L));
		// assertFalse(action.isPostnatalCareEligible(403L));
		// assertFalse(action.isPostnatalCareEligible(9000000012L));
	}

	public void testIsValidDateInput() {
		String inputString = "2018-11-26";
		assertTrue(action.isValidDateInput(inputString));
		inputString = "2018-11-26a";
		assertFalse(action.isValidDateInput(inputString));
	}

	public void testGetPostnatalCareRecord() throws ITrustException {
		PostnatalCareRecordBean bean = action.getPostnatalCareRecord(5);
		Date recordDate = sdf.parse("2018-12-08", new ParsePosition(0));

		assertEquals(bean.getRecordDate(), recordDate);
	}

	public void testUpdatePostnatalCareRecord() throws ITrustException, InterruptedException {
		PostnatalCareRecordBean bean = action.getPostnatalCareRecord(2);
		Date releaseDate = sdf.parse("2018-11-22", new ParsePosition(0));
		bean.setReleaseDate(releaseDate);
		action.updatePostnatalCareRecord(bean);

		PostnatalCareRecordBean beanNew = action.getPostnatalCareRecord(2);
		assertEquals(beanNew.getReleaseDate(), releaseDate);
	}

	public void testAddPostnatalCareRecord()
			throws ITrustException, FormValidationException, SQLException, InterruptedException {
		PostnatalCareRecordBean bean = new PostnatalCareRecordBean();

		List<PostnatalCareRecordBean> resultList = action.getPostnatalCareRecords(400L);
		int numberOfEntry = resultList.size();
		Calendar c = Calendar.getInstance();
		c.set(2018, 11, 26);

		Date recordDate = sdf.parse("2018-11-20", new ParsePosition(0));
		Date childbirthDate = sdf.parse("2018-11-20", new ParsePosition(0));

		long patientMid = 400L;
		bean.setMid(patientMid);
		bean.setRecordDate(recordDate);
		bean.setChildbirthDate(childbirthDate);
		bean.setDeliveryType("vaginal_delivery");
		bean.setAdditionalComment("");

		action.addPostnatalCareRecord(bean);

		PostnatalCareRecordBean beanNew = action.getPostnatalCareRecord(numberOfEntry + 1);
		assertEquals(beanNew.getPostnatalCareRecordId(), numberOfEntry + 1);
	}

}
