package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.RandomPassword;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.CHVBabyBean;
import edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.CHVDeliveryDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.CHVDeliveryBeanValidator;

/**
 * A class provides childbirth hospital delivery related operations
 * 
 * @author brycelin
 *
 */
public class CHVDeliveryAction {

	/**delvDAO*/
	protected CHVDeliveryDAO delvDAO;
	/**patientDAO*/
	protected PatientDAO patientDAO;
	/**personnelDAO*/
	protected PersonnelDAO personnelDAO;
	
	private AuthDAO authDAO;
	
	private CHVDeliveryBeanValidator validator = new CHVDeliveryBeanValidator();
	private long loggedInMID;
	
	public CHVDeliveryAction(DAOFactory factory, long loggedInMID) {
		this.delvDAO = factory.getCHVDeliveryDAO();
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.authDAO = factory.getAuthDAO();
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Get patient's or personnel's name based on their medical identifier
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
	 * Add a new childbirth hospital delivery record, and add corresponding baby and patient records
	 * @param delv
	 * @return
	 * @throws FormValidationException
	 * @throws SQLException
	 * @throws DBException
	 */
	public String addDelv(CHVDeliveryBean delv) throws FormValidationException, SQLException, DBException {
		validator.validate(delv);
		
		try {
			//delvDAO.addDelivery(delv);
			long newDelvID = delvDAO.addEmptyDelivery();
			delv.setDeliveryID(newDelvID);
			delvDAO.editDelivery(delv);
			
			addCHVBaby(delv);
			
			TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_BABY_BORN, loggedInMID, delv.getPatient(), "");
			if(delv.getDosEpidural() + delv.getDosMagnesium() + delv.getDosNitrous() + delv.getDosPethidine() + delv.getDosPitocin() + delv.getDosRHimmune() >0 ) {
				TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_DRUGS_ADD, loggedInMID, delv.getPatient(), "");
			}
			return "Success: childbirth delivery for " + delv.getDate() + " added";
		}
		catch (DBException e) {
			
			return e.getMessage();
		} 
	}	
	
	/**
	 * Gets a user's delivery records
	 * 
	 * @param mid the MID of the user
	 * @return a list of the user's appointments
	 * @throws SQLException
	 * @throws DBException 
	 */
	public List<CHVDeliveryBean> getDelvs(long MID) throws SQLException, DBException {
		TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_READ, loggedInMID, MID, "");
		return delvDAO.getAllDelvsFor(MID);
	}
	
	/**
	 * Retrieves an delivery record from the database, given its ID.
	 * Returns null if there is no match, or get the latest one if there are multiple matches.
	 * 
	 * @param apptID apptID
	 * @return ApptBean with matching ID
	 * @throws DBException 
	 * @throws SQLException 
	 */
	public CHVDeliveryBean getDelv(int delvID) throws DBException, SQLException {
		try {
			List<CHVDeliveryBean> delvBeans = delvDAO.getDelivery(delvID);
			if (delvBeans.size() == 1){
				TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_READ, loggedInMID, delvBeans.get(0).getPatient(), "");
				return delvBeans.get(0);
			}
			return null;
		} catch (DBException e) {
			return null;
		}
	}
	
	/**
	 * Updates an existing childbirth hospital delivery record
	 * 
	 * @param delv Delivery Bean containing the updated information
	 * @param ignoreConflicts ignoreConflicts
	 * @return Message to be displayed
	 * @throws FormValidationException
	 * @throws SQLException 
	 * @throws DBException 
	 */
	public String editDelv(CHVDeliveryBean delv) throws FormValidationException, SQLException, DBException {
		validator.validate(delv);
		
		try {
			delvDAO.editDelivery(delv);
			TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_EDIT, loggedInMID, delv.getPatient(), ""+delv.getDeliveryID());
			return "Success: Delivery changed";
		} catch (DBException e) {
			
			return e.getMessage();
		} 
	}
	
	
	
	/**
	 * Removes an existing childbirth hospital delivery record
	 * 
	 * @param delv Delivery Bean containing the ID of the appointment to be removed.
	 * @return Message to be displayed
	 * @throws DBException 
	 */
	public String removeDelv(CHVDeliveryBean delv) throws DBException, SQLException {
		try {
			delvDAO.removeDelivery(delv);
			TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_VISIT_REMOVE, loggedInMID, delv.getPatient(), ""+delv.getDeliveryID());
			return "Success: Delivery removed";
		} catch (SQLException e) {
			
			return e.getMessage();
		}
	}
	
	
	private long addPatient(PatientBean p) throws DBException {
		
		long newMID = patientDAO.addEmptyPatient();
		p.setMID(newMID);
		String pwd = authDAO.addUser(newMID, Role.PATIENT, RandomPassword.getRandomPassword());
		p.setPassword(pwd);
		patientDAO.editPatient(p, loggedInMID);
		TransactionLogger.getInstance().logTransaction(TransactionType.PATIENT_CREATE, loggedInMID, p.getMID(), "");
		return newMID;
	}
	
	public String addCHVBaby(CHVDeliveryBean delv) throws DBException {
		try {
			List<CHVBabyBean> babies = delv.getBabies();
			for (CHVBabyBean baby : babies) {
				
				PatientBean p = new PatientBean();
				p.setFirstName(baby.getFirstName());
				p.setLastName(baby.getLastName());
				p.setGenderStr(baby.getGender());
				long newMID = addPatient(p);
				
				baby.setDeliveryID(delv.getDeliveryID());
				baby.setMid(newMID);
				
				delvDAO.addBaby(baby);
				
				TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_CHILDBRITH_BABY_CREATE, loggedInMID, p.getMID(), "");
			}
			return "Success: Babies added";
		} catch (SQLException e) {
			return e.getMessage();
		} 
	}
	
	/**
	 * Get all babies records associated with a specified childbirth delivery
	 * @param delivery_id
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<CHVBabyBean> getBabies(long delivery_id) throws SQLException, DBException {
		return delvDAO.getBabiesFor(delivery_id);
	}
	
	/**
	 * Get a baby record based on baby id in the database
	 * @param baby_id
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public CHVBabyBean getBaby(long baby_id) throws SQLException, DBException {
		return delvDAO.getBaby(baby_id);
	}

}
