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
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetRecDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class ObstetAction {
	
	private long loggedInMID;
	
	/**patientDAO*/
	protected PatientDAO patientDAO;
	/**personnelDAO*/
	protected PersonnelDAO personnelDAO;
	/**obstetRecDAO**/
	protected ObstetRecDAO obstetRecDAO;
	
	/**
	 * ApptAction
	 * @param factory factory
	 * @param loggedInMID loggedMID
	 */
	public ObstetAction(DAOFactory factory, long loggedInMID) {
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.obstetRecDAO = factory.getObstetRecDAO();
		
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Get current date in format of "yyyy-MM-dd" in string.
	 * @return
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
	 * Determines if MID is eligible for obstetric care
	 *
	 * @param mid the MID of the user
	 * @return the user's name
	 * @throws ITrustException
	 */
	public boolean isObstetricEligible(long mid) throws ITrustException {
		if(mid < 7000000000L)
			return patientDAO.getPatient(mid).getObstetricEligible();
		else
			return false;
	}
	
	/**
	 * Check if HCP is an OB/GYN specialty
	 * @param mid
	 * @return
	 * @throws ITrustException
	 */
	public boolean isOBGYN(long mid) throws ITrustException {
		PersonnelBean bean = personnelDAO.getPersonnel(mid);
		String specialty = bean.getSpecialty();
		if (specialty != null && specialty.equalsIgnoreCase("OB/GYN")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * View obstetric records
	 * @param patientMID
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<ObstetRecBean> getObstetRecs(long patientMID) throws SQLException, DBException {
		return obstetRecDAO.getObstetRecs(patientMID);
	}

	/**
	 * Takes the information out of the ObstetRecBean param and updates the patient's information
	 *
	 * @param ob
	 *            the new patient information
	 * @throws ITrustException
	 * @throws FormValidationException
	 */
	public void updateObstetRec(ObstetRecBean ob) throws ITrustException {
		ObstetBeanLoader loader = new ObstetBeanLoader();

		ob = loader.calculateDates(ob, ob.getLmpDate(), ob.getInitDate());
		obstetRecDAO.editObstetRec(ob, loggedInMID);
		TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_INITIAL_OBSTETRICS_RECORD, loggedInMID,
				ob.getMID(), ob.getEddDate().toString());
	}

	public ObstetRecBean getObstetRec(int recID, long mid, boolean formIsFilled) throws ITrustException{
		ObstetRecBean result = obstetRecDAO.getObstetRec(recID);
		if (!formIsFilled) {
			TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, loggedInMID,
					mid, result.getEddDate().toString());
		}
		return result;
	}

	public void updatePriorPreg(ObstetPriorPregBean ob) throws ITrustException {
		obstetRecDAO.editPriorPreg(ob, loggedInMID);
		TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_PRIOR_PREGNANCY_RECORD, loggedInMID,
				ob.getMid(), ob.getDeliveryType());
	}

	public ObstetPriorPregBean getPriorPreg(int recID, long mid, boolean formIsFilled) throws ITrustException{
		ObstetPriorPregBean result = obstetRecDAO.getPriorPreg(recID);

		if (!formIsFilled) {
			TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_PRIOR_PREGNANCY_RECORD, loggedInMID,
					mid, result.getDeliveryType());
		}

		return obstetRecDAO.getPriorPreg(recID);
	}

	/**
	 * Add new obstetric record
	 * @param ob
	 * @param loggedInMID
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public void addObstetRec(ObstetRecBean ob, long loggedInMID) throws FormValidationException, ITrustException {
		ObstetBeanLoader loader = new ObstetBeanLoader();

		ob = loader.calculateDates(ob, ob.getLmpDate(), ob.getInitDate());
		TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, loggedInMID,
				ob.getMID(), ob.getEddDate().toString());
		obstetRecDAO.addObstetRec(ob);
	}
	
	public List<ObstetPriorPregBean> getPriorPregs(long patientMID) throws SQLException, DBException {
		return obstetRecDAO.getPriorPregs(patientMID);
	}
	
	public void addPriorPreg(ObstetPriorPregBean ob, long loggedInMID) throws FormValidationException, ITrustException {
		obstetRecDAO.addPriorPregc(ob);
		TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_PRIOR_PREGNANCY_RECORD, loggedInMID,
				ob.getMid(), ob.getDeliveryType());
	}
}
