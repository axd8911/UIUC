package edu.ncsu.csc.itrust.unit.T822.UC822.unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.itrust.action.PostnatalAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PostnatalCareBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PostnatalCareRecordDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class PostnatalCareBeanLoaderTest extends TestCase {

	private DAOFactory factory;
	private PostnatalCareRecordDAO dao;
	private PostnatalCareBeanLoader loader;
	private SimpleDateFormat sdf;
	private TestDataGenerator gen = new TestDataGenerator();
	private long hcpId = 9000000012L;

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.uc93();
		gen.uc822();

		this.factory = TestDAOFactory.getTestInstance();
		this.dao = factory.getPostnatalCareRecordDAO();
		this.loader = new PostnatalCareBeanLoader();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	public void testLoadSingle() throws DBException {

		PostnatalCareRecordBean bean = new PostnatalCareRecordBean();

		bean.setRecordDate(sdf.parse("2018-11-17", new ParsePosition(0)));
		bean.setChildbirthDate(sdf.parse("2018-11-15", new ParsePosition(0)));
		bean.setMid(400L);
		bean.setDeliveryType("vaginal_delivery");
		bean.setAdditionalComment("");

		dao.addPostnatalCareRecord(bean);

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM postnatal_care_record WHERE patient_mid=?")) {
			stmt.setLong(1, 400L);
			ResultSet results = stmt.executeQuery();
			results.next();

			PostnatalCareRecordBean resBean = loader.loadSingle(results);
			results.close();
			conn.close();

			assertEquals(bean.getMid(), resBean.getMid());
			assertEquals(bean.getChildbirthDate(), resBean.getChildbirthDate());
			assertEquals(bean.getRecordDate(), resBean.getRecordDate());

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}
	}

	public void testLoadList() throws DBException, SQLException {
		PostnatalAction action = new PostnatalAction(this.factory, this.hcpId);
		int expectedListLength = action.getPostnatalCareRecords(400L).size();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM postnatal_care_record")) {
			ResultSet results = stmt.executeQuery();
			List<PostnatalCareRecordBean> resBeans = loader.loadList(results);
			results.close();
			conn.close();

			assertEquals(expectedListLength, resBeans.size());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}

	}

	public void testLoadParameters() throws SQLException, DBException {
		PostnatalCareRecordBean bean = new PostnatalCareRecordBean();

		bean.setRecordDate(sdf.parse("2018-11-20", new ParsePosition(0)));
		bean.setChildbirthDate(sdf.parse("2018-11-18", new ParsePosition(0)));
		bean.setMid(400L);
		bean.setDeliveryType("vaginal_delivery");
		bean.setAdditionalComment("");

		dao.addPostnatalCareRecord(bean);

		Connection conn = factory.getConnection();
		PreparedStatement stmt = this.loader.loadParameters(conn.prepareStatement(
				"INSERT INTO postnatal_care_record (patient_mid, record_date, childbirth_date, delivery_type, comment, release_date) "
						+ "VALUES (?, ?, ?, ?, ?, ?)"),
				bean);

		String expectedStmt = "INSERT INTO postnatal_care_record (patient_mid, record_date, childbirth_date, delivery_type, comment, release_date) "
				+ "VALUES (400, '2018-11-20', '2018-11-18', 'vaginal_delivery', '', null)";
		String actualStmt = stmt.toString().split(":")[1].trim();
		assertEquals(actualStmt, expectedStmt);
	}

	public void testBeanBuilderSuccess() throws FormValidationException {
		Map<String, String[]> map = new HashMap<>();

		map.put("record_date", new String[] { "2018-11-29" });
		map.put("childbirth_date", new String[] { "2018-11-27" });
		map.put("delivery_type", new String[] { "vaginal_delivery" });
		map.put("comment", new String[] { "" });
		map.put("release_date", new String[] { "2018-11-30" });

		PostnatalCareRecordBean bean = PostnatalCareBeanLoader.beanBuilder(map);

		assertEquals(bean.getRecordDate(), sdf.parse("2018-11-29", new ParsePosition(0)));
		assertEquals(bean.getChildbirthDate(), sdf.parse("2018-11-27", new ParsePosition(0)));
		assertEquals(bean.getDeliveryType(), "vaginal_delivery");
		assertEquals(bean.getAdditionalComment(), "");
		assertEquals(bean.getReleaseDate(), sdf.parse("2018-11-30", new ParsePosition(0)));
	}
}
