package edu.ncsu.csc.itrust.model.old.beans;

import java.io.Serializable;
import java.sql.Timestamp;


public class CHVApptBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -19657072636021183L;
	private String apptType;
	private int apptID;
	private long patient;
	private long hcp;
	private Timestamp date;
	private String comment;
	private String preferDMethod;
	private String whenScheduled;

	/**
	 * @return the apptType
	 */
	public String getApptType() {
		return apptType;
	}
	
	/**
	 * @param apptID the apptID to set
	 */
	public void setApptID(int apptID) {
		this.apptID = apptID;
	}
	
	public int getApptID() {
		return apptID;
	}
	
	/**
	 * @param apptType the apptType to set
	 */
	public void setApptType(String apptType) {
		this.apptType = apptType;
	}
	/**
	 * @return the patient
	 */
	public long getPatient() {
		return patient;
	}
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(long patient) {
		this.patient = patient;
	}
	/**
	 * @return the hcp
	 */
	public long getHcp() {
		return hcp;
	}
	/**
	 * @param hcp the hcp to set
	 */
	public void setHcp(long hcp) {
		this.hcp = hcp;
	}
	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return (Timestamp) date.clone();
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = (Timestamp) date.clone();
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public int hashCode() {
		return apptID; // any arbitrary constant will do
	}

	public String getPreferDMethod() {
		return preferDMethod;
	}

	public void setPreferDMethod(String preferDMethod) {
		this.preferDMethod = preferDMethod;
	}

	public String getWhenScheduled() {
		return whenScheduled;
	}

	public void setWhenScheduled(String whenScheduled) {
		this.whenScheduled = whenScheduled;
	}
}
