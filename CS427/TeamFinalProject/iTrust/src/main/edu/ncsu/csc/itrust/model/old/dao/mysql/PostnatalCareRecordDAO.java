package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PostnatalCareBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class PostnatalCareRecordDAO {

	private transient final DAOFactory factory;
	private transient final PostnatalCareBeanLoader postnatalCareBeanLoader;

	/**
	 * Constructor for the DAO class
	 * 
	 * @param factory
	 */
	public PostnatalCareRecordDAO(final DAOFactory factory) {
		this.factory = factory;
		this.postnatalCareBeanLoader = new PostnatalCareBeanLoader();
	}

	/**
	 * Read all records of a patient form DB
	 * 
	 * @param mid
	 * @return a list of the bean results
	 * @throws DBException
	 */
	public List<PostnatalCareRecordBean> getPostnatalCareRecords(long mid) throws DBException {
		try {
			Connection conn = factory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM postnatal_care_record WHERE patient_mid=?;");
			stmt.setLong(1, mid);

			final ResultSet results = stmt.executeQuery();
			final List<PostnatalCareRecordBean> postnatalCareRecordList = this.postnatalCareBeanLoader
					.loadList(results);
			results.close();
			conn.close();
			return postnatalCareRecordList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Read a signle entry from DB
	 * 
	 * @param recID
	 * @return a single result with a specific record id
	 * @throws DBException
	 */
	public PostnatalCareRecordBean getPostnatalCareRecord(int recordID) throws DBException {
		try {
			Connection conn = factory.getConnection();
			PreparedStatement stmt = conn
					.prepareStatement("SELECT * FROM postnatal_care_record WHERE postnatal_care_record_id=?");
			stmt.setLong(1, recordID);

			final ResultSet results = stmt.executeQuery();
			PostnatalCareRecordBean resultBean = results.next() ? postnatalCareBeanLoader.loadSingle(results) : null;
			results.close();
			conn.close();
			return resultBean;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Add a new postnatal care record
	 * 
	 * @param postnatalCareRecordBean
	 * @throws DBException
	 */
	public void addPostnatalCareRecord(final PostnatalCareRecordBean postnatalCareRecordBean) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.postnatalCareBeanLoader.loadParameters(conn.prepareStatement(
						"INSERT INTO postnatal_care_record (patient_mid, record_date, childbirth_date, delivery_type, comment, release_date) "
								+ "VALUES (?, ?, ?, ?, ?, ?)"),
						postnatalCareRecordBean)) {
			stmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's information for the given MID
	 *
	 * @param ob The PostnatalCareRecord bean representing the new information
	 * for the patient.
	 * @throws DBException
	 */
	public void editPostnatalCareRecord(PostnatalCareRecordBean bean, long hcpid) throws DBException {

		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"UPDATE postnatal_care_record SET patient_mid=?, record_date=?, childbirth_date=?, delivery_type=?, "
								+ "comment=?, release_date=? WHERE postnatal_care_record_id=?")) {

			ps.setLong(1, bean.getMid());
			ps.setDate(2, new java.sql.Date(bean.getRecordDate().getTime()));
			ps.setDate(3, new java.sql.Date(bean.getChildbirthDate().getTime()));
			ps.setString(4, bean.getDeliveryType());
			ps.setString(5, bean.getAdditionalComment());

			if (bean.getReleaseDate() == null) {
				ps.setDate(6, null);
			} else {
				ps.setDate(6, new java.sql.Date(bean.getReleaseDate().getTime()));
			}
			ps.setLong(7, bean.getPostnatalCareRecordId());
			// System.out.println(ps);
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}
	}

}
