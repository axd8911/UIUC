package edu.ncsu.csc.itrust.unit.T822.UC822.unit;

import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

import edu.ncsu.csc.itrust.action.PostnatalAction;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PostnatalCareBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PostnatalCareRecordDAO;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class PostnatalCareRecordDAOTest extends TestCase {

	private DAOFactory factory;
	private PostnatalCareRecordDAO dao;
	private PostnatalCareRecordBean bean;
	private PostnatalCareBeanLoader loader;
	private SimpleDateFormat sdf;
	private TestDataGenerator gen = new TestDataGenerator();

	private long hcpId = 9000000012L;

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.uc93();
		gen.uc822();

		sdf = new SimpleDateFormat("yyyy-MM-dd");
		loader = new PostnatalCareBeanLoader();
		bean = new PostnatalCareRecordBean();

		bean.setMid(400L);
		bean.setRecordDate(sdf.parse("2018-11-17", new ParsePosition(0)));
		bean.setChildbirthDate(sdf.parse("2018-11-15", new ParsePosition(0)));
		bean.setDeliveryType("vaginal_delivery");
		bean.setAdditionalComment("");

		this.factory = TestDAOFactory.getTestInstance();
		this.dao = factory.getPostnatalCareRecordDAO();
	}

	public void testGetPostnatalCareRecordsSuccess() throws DBException, SQLException {
		PostnatalAction action = new PostnatalAction(this.factory, this.hcpId);
		;
		List<PostnatalCareRecordBean> expected = action.getPostnatalCareRecords(400L);
		List<PostnatalCareRecordBean> res = dao.getPostnatalCareRecords(400L);
		assertEquals(expected.size(), res.size());
	}

	public void testAddPostnatalCareRecord() throws DBException {
		dao.addPostnatalCareRecord(bean);
		PostnatalCareRecordBean resBean = dao.getPostnatalCareRecord(1);

		assertEquals(bean.getMid(), resBean.getMid());
		assertEquals(bean.getChildbirthDate(), resBean.getChildbirthDate());
		assertEquals(bean.getRecordDate(), resBean.getRecordDate());
	}

	public void testEditObstetRec1() throws DBException {

		bean.setPostnatalCareRecordId(3);
		bean.setAdditionalComment("extra");

		dao.editPostnatalCareRecord(bean, hcpId);
		PostnatalCareRecordBean resBean = dao.getPostnatalCareRecord(3);

		assertEquals(bean.getMid(), resBean.getMid());
		assertEquals(bean.getChildbirthDate(), resBean.getChildbirthDate());
		assertEquals(bean.getRecordDate(), resBean.getRecordDate());
		assertEquals(bean.getAdditionalComment(), resBean.getAdditionalComment());
	}

	public void testEditObstetRec2() throws DBException {

		bean.setPostnatalCareRecordId(4);
		bean.setReleaseDate(sdf.parse("2018-11-22", new ParsePosition(0)));

		dao.editPostnatalCareRecord(bean, hcpId);
		PostnatalCareRecordBean resBean = dao.getPostnatalCareRecord(4);

		assertEquals(bean.getMid(), resBean.getMid());
		assertEquals(bean.getChildbirthDate(), resBean.getChildbirthDate());
		assertEquals(bean.getRecordDate(), resBean.getRecordDate());
		assertEquals(bean.getAdditionalComment(), resBean.getAdditionalComment());
		assertEquals(bean.getReleaseDate(), resBean.getReleaseDate());
	}

}
