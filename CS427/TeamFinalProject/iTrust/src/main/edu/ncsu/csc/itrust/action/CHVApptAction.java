package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.CHVApptDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.CHVApptBeanValidator;

/**
 * An action class for childbirth hospital visit appointment related operations
 * 
 * @author brycelin
 *
 */
public class CHVApptAction {

	/**apptDAO*/
	protected CHVApptDAO apptDAO;
	/**patientDAO*/
	protected PatientDAO patientDAO;
	/**personnelDAO*/
	protected PersonnelDAO personnelDAO;
	
	private CHVApptBeanValidator validator = new CHVApptBeanValidator();
	private long loggedInMID;
	
	/**
	 * Class constructor, set up default class variables
	 * @param factory
	 * @param loggedInMID
	 */
	public CHVApptAction(DAOFactory factory, long loggedInMID) {
		this.apptDAO = factory.getCHVApptDAO();
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Get patient or personnel name from their medical identifier
	 * @param mid
	 * @return
	 * @throws ITrustException
	 */
	public String getName(long mid) throws ITrustException {
		if(mid < 7000000000L)
			return patientDAO.getName(mid);
		else
			return personnelDAO.getName(mid);
	}
	
	/**
	 * Add a new childbirth hospital visit appointment 
	 * @param appt
	 * @param ignoreConflicts
	 * @return
	 * @throws FormValidationException
	 * @throws SQLException
	 * @throws DBException
	 */
	public String addCHVAppt(CHVApptBean appt, boolean ignoreConflicts) throws FormValidationException, SQLException, DBException {
		validator.validate(appt);
		if(appt.getDate().before(new Timestamp(System.currentTimeMillis()))) {
			return "The scheduled date of this Appointment ("+appt.getDate()+") has already passed.";
		}
		
		try {
			apptDAO.scheduleAppt(appt);
			TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_CREATE, loggedInMID, appt.getPatient(), "");
			
			return "Success: " + appt.getApptType() + " for " + appt.getDate() + " added";
		}
		catch (SQLException e) {
			
			return e.getMessage();
		} 
	}	
	
	/**
	 * Gets a user's appointments
	 * 
	 * @param mid the MID of the user
	 * @return a list of the user's appointments
	 * @throws SQLException
	 * @throws DBException 
	 */
	public List<CHVApptBean> getAppointments(long MID) throws SQLException, DBException {
		return apptDAO.getApptsFor(MID);
	}
	
	/**
	 * Retrieves an appointment from the database, given its ID.
	 * Returns null if there is no match, or get the latest one if there are multiple matches.
	 * 
	 * @param apptID apptID
	 * @return ApptBean with matching ID
	 * @throws DBException 
	 * @throws SQLException 
	 */
	public CHVApptBean getAppt(int apptID) throws DBException, SQLException {
		try {
			List<CHVApptBean> apptBeans = apptDAO.getAppt(apptID);
			if (apptBeans.size() == 1){
				return apptBeans.get(0);
			}
			return null;
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * Updates an existing childbirth hospital visit appointment
	 * 
	 * @param appt Appointment Bean containing the updated information
	 * @param ignoreConflicts ignoreConflicts
	 * @return Message to be displayed
	 * @throws FormValidationException
	 * @throws SQLException 
	 * @throws DBException 
	 */
	public String editAppt(CHVApptBean appt, boolean ignoreConflicts) throws FormValidationException, SQLException, DBException {
		validator.validate(appt);
		if(appt.getDate().before(new Timestamp(System.currentTimeMillis())))
			return "The scheduled date of this appointment ("+appt.getDate()+") has already passed.";
		
		try {
			apptDAO.editAppt(appt);
			TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_EDIT, loggedInMID, appt.getPatient(), ""+appt.getApptID());
			
			return "Success: Appointment changed";
		} catch (DBException e) {
			
			return e.getMessage();
		} 
	}
	
	
	
	/**
	 * Removes an existing childbirth hospital visit appointment
	 * 
	 * @param appt Appointment Bean containing the ID of the appointment to be removed.
	 * @return Message to be displayed
	 * @throws DBException 
	 */
	public String removeAppt(CHVApptBean appt) throws DBException, SQLException {
		try {
			apptDAO.removeAppt(appt);
			TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_REMOVE, loggedInMID, appt.getPatient(), ""+appt.getApptID());
			return "Success: Appointment removed";
		} catch (SQLException e) {
			
			return e.getMessage();
		}
	}

}
