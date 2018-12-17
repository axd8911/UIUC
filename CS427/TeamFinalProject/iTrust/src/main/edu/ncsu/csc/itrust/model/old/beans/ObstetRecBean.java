package edu.ncsu.csc.itrust.model.old.beans;

import java.io.Serializable;
import java.util.Date;

public class ObstetRecBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3391922877099577696L;
	
	private int obstetRecID;
	private long mid;
	private Date lmpDate;
	private Date initDate;
	private Date eddDate;
	private int pregWeeks;
	private int pregDays;
	
	public long getMID() {
		return mid;
	}
	public void setMID(long mID) {
		mid = mID;
	}
	public Date getLmpDate() {
		return lmpDate;
	}
	public void setLmpDate(Date lmpDate) {
		this.lmpDate = lmpDate;
	}
	public int getObstetRecID() {
		return obstetRecID;
	}
	public void setObstetRecID(int obstetRecID) {
		this.obstetRecID = obstetRecID;
	}
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Date getEddDate() {
		return eddDate;
	}
	public void setEddDate(Date eddDate) {
		this.eddDate = eddDate;
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
	
	@Override
	public int hashCode() {
		return obstetRecID;
	}

	@Override
	public String toString() {
		return "" + obstetRecID + " " + lmpDate;
	}


	public String toBetterString() {
		return "" + obstetRecID + "\n" + mid + "\n" + lmpDate + "\n" +initDate + "\n" +eddDate + "\n" +pregWeeks + "\n" +pregDays;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObstetRecBean){
			ObstetRecBean other = (ObstetRecBean) obj;
			return (obstetRecID == other.getObstetRecID() &&
					mid == other.getMID()                 &&
					lmpDate.equals(other.getLmpDate())    &&
					initDate.equals(other.getInitDate())  &&
					eddDate.equals(other.getEddDate())    &&
					pregWeeks == other.getPregWeeks()     &&
					pregDays == other.getPregDays());
		} else {
			return false;
		}
	}
}
