package edu.ncsu.csc.itrust.unit.T822.UC93.unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetRecDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ObstetBeanLoaderTest extends TestCase {

	private DAOFactory factory;
	private ObstetRecDAO OBDAO;
	private ObstetBeanLoader obloader;
	TestDataGenerator gen = new TestDataGenerator();

	private long hcpId = 9000000012L;

	@Override
	protected void setUp() throws Exception {

		gen.clearAllTables();
		gen.uc93();

		this.factory = TestDAOFactory.getTestInstance();
		this.OBDAO = factory.getObstetRecDAO();
		this.obloader = new ObstetBeanLoader();
	}

	/**
	 * Test the function to calculate edd date and other date infor
	 * @throws SQLException
	 */
	public void testCalculateDates() throws SQLException {
		
		ObstetRecBean bean = new ObstetRecBean();
		
		Calendar lmpDate = Calendar.getInstance();
		lmpDate.set(2018, 7, 14);
		Calendar initDate = Calendar.getInstance();
		initDate.set(2018, 11, 6);

		bean = obloader.calculateDates(bean, lmpDate.getTime(), initDate.getTime());
		
		Calendar expectedEddDate = Calendar.getInstance();
		expectedEddDate.set(2019, 4, 21);
		
		Calendar returnedEddDate = Calendar.getInstance();
		returnedEddDate.setTime(bean.getEddDate());
		
		assertEquals(expectedEddDate.get(Calendar.YEAR), returnedEddDate.get(Calendar.YEAR));	
		assertEquals(expectedEddDate.get(Calendar.MONTH), returnedEddDate.get(Calendar.MONTH));	
		assertEquals(expectedEddDate.get(Calendar.DAY_OF_MONTH), returnedEddDate.get(Calendar.DAY_OF_MONTH));	
		assertEquals(16, bean.getPregWeeks());
		assertEquals(2, bean.getPregDays());		
	}

	public void testLoadSingle() throws DBException {

		ObstetRecBean ob = new ObstetRecBean();
		int recID = 3;

		ob.setInitDate(new Date(2018-1900, 7,8));
		ob.setLmpDate(new Date(2018-1900, 5,6));
		ob.setMID(402);
		ob.setObstetRecID(recID);
		ob.setEddDate(new Date(2019-1900,2,13));
		ob.setPregDays(0);
		ob.setPregWeeks(9);

		OBDAO.addObstetRec(ob);


		try (
				Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrec WHERE obstetrec_id=?"))
		{
			stmt.setLong(1, recID);
			ResultSet results = stmt.executeQuery();
			results.next();
			ObstetRecBean resBean = obloader.loadSingle(results);
			results.close();
			assertEquals(ob, resBean);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}
	}

	public void testLoadList() throws DBException {
		int expectedListLength = 2;
		try (
				Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrec"))
		{
			ResultSet results = stmt.executeQuery();
			List<ObstetRecBean> resBeans = obloader.loadList(results);
			results.close();
			assertEquals(expectedListLength, resBeans.size());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}
	}

	public void testLoadParameters() throws SQLException, DBException{
		ObstetRecBean ob = new ObstetRecBean();
		int recID = 3;

		ob.setInitDate(new Date(2018-1900, 7,8));
		ob.setLmpDate(new Date(2018-1900, 5,6));
		ob.setMID(402);
		ob.setObstetRecID(recID);
		ob.setEddDate(new Date(2019-1900,2,13));
		ob.setPregDays(0);
		ob.setPregWeeks(9);

		OBDAO.addObstetRec(ob);


		Connection conn = factory.getConnection();
		PreparedStatement stmt = this.obloader.loadParameters(conn.prepareStatement(
				"INSERT INTO obstetrec (patientMID, lmp_date, init_date) "
						+ "VALUES (?, ?, ?)"), ob);



		String expectedStmt = "INSERT INTO obstetrec (patientMID, lmp_date, init_date) VALUES (402, '2018-06-06', '2018-08-08')";
		String actualStmt = stmt.toString();
		assertTrue(actualStmt.contains(expectedStmt));
		conn.close();
	}

	public void testBeanBuilderSuccess() throws FormValidationException {
		Date expectedDate = new Date(2018-1900, 5, 6);

		Map<String, String[]> map = new HashMap<>();
		String[] ss = {"2018-06-06"};

		map.put("lmpDate", ss);

		ObstetRecBean ob = ObstetBeanLoader.beanBuilder(map);

		assertEquals(ob.getLmpDate(), expectedDate);
	}

	public void testBeanBuilderBadDateFormat() {

		Map<String, String[]> map = new HashMap<>();
		String[] ss = {"06-06-2018"};

		try {
			map.put("lmpDate", ss);
			ObstetRecBean ob = ObstetBeanLoader.beanBuilder(map);
			fail();
		} catch (FormValidationException e){
			;
		}
	}
}
