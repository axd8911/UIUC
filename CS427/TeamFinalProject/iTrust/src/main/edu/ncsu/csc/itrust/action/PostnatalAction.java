package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetRecDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PostnatalCareRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class PostnatalAction {
	private long loggedInMID;
	
	/**patientDAO*/
	protected PatientDAO patientDAO;
	/**personnelDAO*/
	protected PersonnelDAO personnelDAO;
	/**Post natal care cDAO**/
	protected PostnatalCareRecordDAO postnatalCareRecordDAO;
	
	/**
	 * 
	 * @param factory
	 * @param loggedInMID
	 */
	public PostnatalAction(DAOFactory factory, long loggedInMID) {
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.postnatalCareRecordDAO = factory.getPostnatalCareRecordDAO();
		
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Get current date in format of "yyyy-MM-dd" in string.
	 * @return date in string format
	 */
	public static String getCurrentDateinString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		return sdf.format(c.getTime());
	}
	
	/**
	 * Gets a users's name from their MID
	 * 
	 * @param mid the MID of the user
	 * @return the user's name
	 * @throws ITrustException
	 */
	public String getName(long mid) throws ITrustException {
		if(mid < 7000000000L)
			return patientDAO.getName(mid);
		else
			return personnelDAO.getName(mid);
	}
	
	/**
	 * Determines if MID is eligible for postnatal care
	 * TODO: Modify this once the flag of postnatal care is implemented, or remove if not needed
	 * 
	 * @param mid the MID of the user
	 * @return the user's name
	 * @throws ITrustException
	 */
	public boolean isPostnatalCareEligible(long mid) throws ITrustException {
		return true;
	}
	
	/**
	 * Check the input string contains char other than numbers and hyphen
	 * @param inputString
	 * @return the boolean value of whether the input string contains any character other than numbers and hyphen
	 */
	public boolean isValidDateInput (String inputString) {
		
		for (int i = 0; i < inputString.length(); ++i) {
			char currChar = inputString.charAt(i);
			if (currChar < 45 || (currChar > 45 && currChar < 48) || currChar > 57) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Check if HCP is an OB/GYN specialty
	 * @param mid
	 * @return whether the input is an OBGYN
	 * @throws ITrustException
	 */
	public boolean isOBGYN(long mid) throws ITrustException {
		PersonnelBean bean = personnelDAO.getPersonnel(mid);
		System.out.println(bean==null);
		if (bean==null) return false;
		String specialty = bean.getSpecialty();
		if (specialty != null && specialty.equalsIgnoreCase("OB/GYN")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Query for all postntal care records for a patient
	 * @param patientMID
	 * @return A whole list of the records of a patient
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<PostnatalCareRecordBean> getPostnatalCareRecords (long patientMID) throws SQLException, DBException {
		TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_POSTNATAL_CARE_RECORD, loggedInMID, patientMID, "");
		return postnatalCareRecordDAO.getPostnatalCareRecords(patientMID);
	}

	/**
	 * Query for a specific postnatal care entry
	 * @param recID
	 * @return a single record of the specified ID
	 * @throws ITrustException
	 */
	public PostnatalCareRecordBean getPostnatalCareRecord (int recID) throws ITrustException{
		return postnatalCareRecordDAO.getPostnatalCareRecord(recID);
	}

	/**
	 * Takes the information out of the Postnatal param and updates the patient's information
	 *
	 * @param bean: the new patient information
	 * @throws ITrustException
	 * @throws FormValidationException
	 */
	public void updatePostnatalCareRecord (PostnatalCareRecordBean bean) throws ITrustException {
		TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_POSTNATAL_CARE_RECORD, loggedInMID, bean.getMid(), "");
		postnatalCareRecordDAO.editPostnatalCareRecord(bean, loggedInMID);	
	}

	/**
	 * Add new postnatal care record
	 * @param ob
	 * @param loggedInMID
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public void addPostnatalCareRecord (PostnatalCareRecordBean bean) throws FormValidationException, ITrustException {
		TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_POSTNATAL_CARE_RECORD, loggedInMID, bean.getMid(), "");
		postnatalCareRecordDAO.addPostnatalCareRecord(bean);
	}
}
