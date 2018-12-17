package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.CHVBabyBean;
import edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.CHVDeliveryBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * A class provides childbirth hospital delivery related database operations
 * @author brycelin
 *
 */
public class CHVDeliveryDAO {

	private transient final DAOFactory factory;
	private transient final CHVDeliveryBeanLoader delvLoader;

	private static final int MIN_MID = 999999999;

	/**
	 * Constructor for initializing default class variables
	 * @param factory
	 */
	public CHVDeliveryDAO(final DAOFactory factory) {
		this.factory = factory;
		this.delvLoader = new CHVDeliveryBeanLoader();
	}

	/**
	 * Retrieve a childbirth hospital delivery record from database based on delivery ID
	 * @param delvID
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<CHVDeliveryBean> getDelivery(final int delvID) throws SQLException, DBException {
		ResultSet results = null;
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chvdelivery WHERE delivery_id=?")) {
			stmt.setInt(1, delvID);
			results = stmt.executeQuery();
			final List<CHVDeliveryBean> abList = this.delvLoader.loadList(results);
			conn.close();
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Retrieve all childbirth hospital delivery records from database based on user's medical ID
	 * @param mid
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<CHVDeliveryBean> getAllDelvsFor(long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = (mid >= MIN_MID)
						? conn.prepareStatement("SELECT * FROM chvdelivery WHERE doctor_id=? ORDER BY delivery_date desc;")
						: conn.prepareStatement("SELECT * FROM chvdelivery WHERE patient_id=? ORDER BY delivery_date desc;")) {
			stmt.setLong(1, mid);

			final ResultSet results = stmt.executeQuery();
			final List<CHVDeliveryBean> delvList = this.delvLoader.loadList(results);
			results.close();
			conn.close();
			return delvList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}

	}

	/**
	 * Add a new childbirth hospital delivery record to database
	 * @param appt
	 * @throws SQLException
	 * @throws DBException
	 */
	public void addDelivery(final CHVDeliveryBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.delvLoader.loadParameters(conn.prepareStatement(
						"INSERT INTO chvdelivery (patient_id, doctor_id, delivery_date, delivery_method, dosPitocin, dosNitrous, dosPethidine, dosEpidural, dosMagnesium, dosRHimmune) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
						appt)) {
			stmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}

	/**
	 * Update an existing childbirth hospital delivery record
	 * @param delv
	 * @throws SQLException
	 * @throws DBException
	 */
	public void editDelivery(final CHVDeliveryBean delv) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE chvdelivery SET doctor_id=?, patient_id=?, delivery_date=?, delivery_method=?, dosPitocin=?, dosNitrous=?, dosPethidine=?, dosEpidural=?, dosMagnesium=?, dosRHimmune=? WHERE delivery_id=?")) {
			stmt.setLong(1,  delv.getHcp());
			stmt.setLong(2, delv.getPatient());
			stmt.setTimestamp(3, delv.getDate());
			stmt.setString(4, delv.getDeliveryMethod());
			stmt.setInt(5,  delv.getDosPitocin());
			stmt.setInt(6,  delv.getDosNitrous());
			stmt.setInt(7,  delv.getDosPethidine());
			stmt.setInt(8,  delv.getDosEpidural());
			stmt.setInt(9,  delv.getDosMagnesium());
			stmt.setInt(10,  delv.getDosRHimmune());
			stmt.setLong(11, delv.getDeliveryID());

			stmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Delete an existing childbirth hospital delivery record
	 * @param delv
	 * @throws SQLException
	 * @throws DBException
	 */
	public void removeDelivery(final CHVDeliveryBean delv) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM chvdelivery WHERE delivery_id=?")) {
			stmt.setLong(1, delv.getDeliveryID());

			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Add a new childbirth hospital delivery record and get delivery ID
	 * @return
	 * @throws DBException
	 */
	public long addEmptyDelivery() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO chvdelivery(delivery_id) VALUES(NULL)")) {
			ps.executeUpdate();
			long result = DBUtil.getLastInsert(conn);
			conn.close();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Add a new baby record to database
	 * @param baby
	 * @throws SQLException
	 * @throws DBException
	 */
	public void addBaby(CHVBabyBean baby) throws SQLException, DBException {
		try {
			Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO chvbaby (delivery_id, mid, first_name, last_name, gender) "
								+ "VALUES (?, ?, ?, ?, ?)");
			ps.setLong(1, baby.getDeliveryID());
			ps.setLong(2, baby.getMid());
			ps.setString(3, baby.getFirstName());
			ps.setString(4, baby.getLastName());
			ps.setString(5,  baby.getGender());
			
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}
	
	/**
	 * Retrieve all baby records associated with a delivery from database
	 * @param delivery_id
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<CHVBabyBean> getBabiesFor(long delivery_id) throws SQLException, DBException {
		try {
			Connection conn = factory.getConnection();
		
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chvbaby WHERE delivery_id=? order by baby_id desc;");
			stmt.setLong(1, delivery_id);

			ResultSet rs = stmt.executeQuery();
			List<CHVBabyBean> list = new ArrayList<CHVBabyBean>();
			while (rs.next()) {
				CHVBabyBean bean = new CHVBabyBean();
				bean.setBabyID(rs.getLong("baby_id"));
				bean.setDeliveryID(rs.getLong("delivery_id"));
				bean.setMid(rs.getLong("mid"));
				bean.setFirstName(rs.getString("first_name"));
				bean.setLastName(rs.getString("last_name"));
				bean.setGender(rs.getString("gender"));
				
				list.add(bean);
			}
			conn.close();
			rs.close();
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}

	}
	
	/**
	 * Retrive a baby record based on baby ID
	 * @param baby_id
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public CHVBabyBean getBaby(long baby_id) throws SQLException, DBException {
		try {
			Connection conn = factory.getConnection();
		
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chvbaby WHERE baby_id=?;");
			stmt.setLong(1, baby_id);

			ResultSet rs = stmt.executeQuery();
			CHVBabyBean bean = new CHVBabyBean();
			if (rs.next()) {
				bean.setBabyID(rs.getLong("baby_id"));
				bean.setDeliveryID(rs.getLong("delivery_id"));
				bean.setMid(rs.getLong("mid"));
				bean.setFirstName(rs.getString("first_name"));
				bean.setLastName(rs.getString("last_name"));
				bean.setGender(rs.getString("gender"));
			}

			conn.close();
			rs.close();
			return bean;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}

	}
}
