package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.CHVApptBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Database access object for childbirth hospital visit appointment related db operations
 * @author brycelin
 *
 */
public class CHVApptDAO {

	private transient final DAOFactory factory;
	private transient final CHVApptBeanLoader abloader;

	private static final int MIN_MID = 999999999;

	/**
	 * Constructor for initializing default class variables
	 * @param factory
	 */
	public CHVApptDAO(final DAOFactory factory) {
		this.factory = factory;
		this.abloader = new CHVApptBeanLoader();
	}

	/**
	 * Get a childbirth hospital visit appointment from appointment ID
	 * @param apptID
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<CHVApptBean> getAppt(final int apptID) throws SQLException, DBException {
		ResultSet results = null;
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chvappointment WHERE appt_id=?")) {
			stmt.setInt(1, apptID);
			results = stmt.executeQuery();
			final List<CHVApptBean> abList = this.abloader.loadList(results);
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Get a user's childbirth hospital visit appointment records from databaes based on their medical ID
	 * @param mid
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<CHVApptBean> getApptsFor(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = (mid >= MIN_MID)
						? conn.prepareStatement(
								"SELECT * FROM chvappointment WHERE doctor_id=? AND sched_date > NOW() ORDER BY sched_date desc;")
						: conn.prepareStatement(
								"SELECT * FROM chvappointment WHERE patient_id=? AND sched_date > NOW() ORDER BY sched_date desc;")) {
			stmt.setLong(1, mid);

			ResultSet results = stmt.executeQuery();
			List<CHVApptBean> abList = this.abloader.loadList(results);
			results.close();
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Add a new childbirth hospital visit appointment record to database
	 * @param appt
	 * @throws SQLException
	 * @throws DBException
	 */
	public void scheduleAppt(final CHVApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.abloader.loadParameters(conn.prepareStatement(
						"INSERT INTO chvappointment (appt_type, patient_id, doctor_id, sched_date, comment, prefer_dmethod, when_scheduled) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?)"),
						appt)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}

	/**
	 * Update an existing childbirth hospital visit appointment record in database
	 * @param appt
	 * @throws SQLException
	 * @throws DBException
	 */
	public void editAppt(final CHVApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE chvappointment SET appt_type=?, sched_date=?, comment=?, prefer_dmethod=?, when_scheduled=? WHERE appt_id=?")) {
			stmt.setString(1, appt.getApptType());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setString(3, appt.getComment());
			stmt.setString(4, appt.getPreferDMethod());
			stmt.setString(5, appt.getWhenScheduled());
			stmt.setInt(6, appt.getApptID());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Remove an existing childbirth hospital visit appointment record from database
	 * @param appt
	 * @throws SQLException
	 * @throws DBException
	 */
	public void removeAppt(final CHVApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM chvappointment WHERE appt_id=?")) {
			stmt.setInt(1, appt.getApptID());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
