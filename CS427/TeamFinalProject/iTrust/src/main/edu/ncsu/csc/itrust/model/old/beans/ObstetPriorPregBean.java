package edu.ncsu.csc.itrust.model.old.beans;

import java.io.Serializable;

public class ObstetPriorPregBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7075244938516225271L;

	private int priorPregRecID;
	private long mid;
//	private String firstName;
//	private String lastName;
	private int conceptionYear;
	private int pregWeeks;
	private int pregDays;
	private float laborHours;
	private float weightGain;
	private String deliveryType;
	private int multiplet;
	
	public int getPriorPregRecID() {
		return priorPregRecID;
	}
	public void setPriorPregRecID(int priorPregRecID) {
		this.priorPregRecID = priorPregRecID;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
//	public String getFirstName() {
//		return firstName;
//	}
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
	public int getConceptionYear() {
		return conceptionYear;
	}
	public void setConceptionYear(int conceptionYear) {
		this.conceptionYear = conceptionYear;
	}
	public int getPregWeeks() {
		return pregWeeks;
	}
	public void setPregWeeks(int pregWeeks) {
		this.pregWeeks = pregWeeks;
	}
	public int getPregDays() {
		return pregDays;
	}
	public void setPregDays(int pregDays) {
		this.pregDays = pregDays;
	}
	public float getLaborHours() {
		return laborHours;
	}
	public void setLaborHours(float laborHours) {
		this.laborHours = laborHours;
	}
	public float getWeightGain() {
		return weightGain;
	}
	public void setWeightGain(float weightGain) {
		this.weightGain = weightGain;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public int getMultiplet() {
		return multiplet;
	}
	public void setMultiplet(int multiplet) {
		this.multiplet = multiplet;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public int hashCode() {
		return priorPregRecID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObstetPriorPregBean){
			ObstetPriorPregBean other = (ObstetPriorPregBean) obj;
			return (priorPregRecID == other.getPriorPregRecID()   &&
					mid == other.getMid()                         &&
					conceptionYear == other.getConceptionYear()   &&
					pregWeeks == other.getPregWeeks()             &&
					pregDays == other.getPregDays()               &&
					laborHours == other.getLaborHours()           &&
					weightGain == other.getWeightGain()           &&
					deliveryType.equals(other.getDeliveryType())  &&
					multiplet == other.getMultiplet()              	);
		} else {
			return false;
		}
	}

}
