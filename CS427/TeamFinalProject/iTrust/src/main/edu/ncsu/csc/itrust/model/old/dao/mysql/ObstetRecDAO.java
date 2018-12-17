package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PriorPregBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class ObstetRecDAO {
	
	private transient final DAOFactory factory;
	private transient final ObstetBeanLoader obloader;
	private transient final PriorPregBeanLoader pbloader;

	public ObstetRecDAO(final DAOFactory factory) {
		this.factory = factory;
		this.obloader = new ObstetBeanLoader();
		this.pbloader = new PriorPregBeanLoader();
	}
	
	public List<ObstetRecBean> getObstetRecs(long mid) throws DBException {
		try {
			Connection conn = factory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrec WHERE patientMID=?;");
			stmt.setLong(1, mid);

			final ResultSet results = stmt.executeQuery();
			final List<ObstetRecBean> obList = this.obloader.loadList(results);
			results.close();
			conn.close();
			return obList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public ObstetRecBean getObstetRec(int recID) throws DBException {
		try (
			Connection conn = factory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetrec WHERE obstetrec_id=?"))
        {

		    stmt.setLong(1, recID);

			ResultSet results = stmt.executeQuery();
			ObstetRecBean resultBean = results.next() ? obloader.loadSingle(results) : null;
			results.close();
			conn.close();
			return resultBean;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public ObstetPriorPregBean getPriorPreg(int recID) throws DBException {
		try
            (Connection conn = factory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetpriorpreg WHERE obstetpriorpreg_id=?")){

			stmt.setLong(1, recID);

			final ResultSet results = stmt.executeQuery();
			final ObstetPriorPregBean resultBean =  results.next() ? this.pbloader.loadSingle(results) : null;
			results.close();
			conn.close();
			return resultBean;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void addObstetRec(final ObstetRecBean obstetRec) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.obloader.loadParameters(conn.prepareStatement(
						"INSERT INTO obstetrec (patientMID, lmp_date, init_date) "
								+ "VALUES (?, ?, ?)"),
						obstetRec)) {
			stmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	//
	public List<ObstetPriorPregBean> getPriorPregs(long mid) throws DBException {
		try {
			Connection conn = factory.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obstetpriorpreg WHERE patientMID=?;");
			stmt.setLong(1, mid);

			final ResultSet results = stmt.executeQuery();
			final List<ObstetPriorPregBean> pbList = this.pbloader.loadList(results);
			results.close();
			conn.close();
			return pbList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's information for the given MID
	 *
	 * @param ob
	 *            The obstetRec bean representing the new information for the
	 *            patient.
	 * @throws DBException
	 */
	public void editObstetRec(ObstetRecBean ob, long hcpid) throws DBException {
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("UPDATE obstetrec SET lmp_date=? WHERE obstetrec_id=?")) {
			ps.setDate(1, new java.sql.Date(ob.getLmpDate().getTime()));
			ps.setLong(2, ob.getObstetRecID());
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}
	}

	public void editPriorPreg(ObstetPriorPregBean ob, long hcpid) throws DBException {
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("UPDATE obstetpriorpreg SET conception_year=?, pregnant_weeks=?, pregnant_days=?, labor_hours=?, weight_gain=?, delivery_type=?, multiplet=? WHERE obstetpriorpreg_id=?")) {
			ps.setInt(1, ob.getConceptionYear());
			ps.setInt(2, ob.getPregWeeks());
			ps.setInt(3, ob.getPregDays());
			ps.setFloat(4, ob.getLaborHours());
			ps.setFloat(5, ob.getWeightGain());
			ps.setString(6, ob.getDeliveryType());
			ps.setInt(7, ob.getMultiplet());
			ps.setInt(8, ob.getPriorPregRecID());
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DBException(e);
		}
	}
	
	public void addPriorPregc(final ObstetPriorPregBean priorPreg) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.pbloader.loadParameters(conn.prepareStatement(
						"INSERT INTO obstetpriorpreg (patientMID, conception_year, pregnant_weeks, pregnant_days, labor_hours, weight_gain, delivery_type, multiplet) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"),
						priorPreg)) {
			stmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}
}
