package edu.ncsu.csc.itrust.model.old.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class CHVDeliveryBean {

	private long deliveryID;
	private long patient;
	private long hcp;
	private Timestamp date;
	private String deliveryMethod;
	private int dosPitocin;
	private int dosNitrous;
	private int dosPethidine;
	private int dosEpidural;
	private int dosMagnesium;
	private int dosRHimmune;
	private List<CHVBabyBean> babies = new ArrayList<CHVBabyBean>();
	
	public long getDeliveryID() {
		return deliveryID;
	}
	public void setDeliveryID(long deliveryID) {
		this.deliveryID = deliveryID;
	}
	public long getPatient() {
		return patient;
	}
	public void setPatient(long patient) {
		this.patient = patient;
	}
	public long getHcp() {
		return hcp;
	}
	public void setHcp(long hcp) {
		this.hcp = hcp;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	public int getDosPitocin() {
		return dosPitocin;
	}
	public void setDosPitocin(int dosPitocin) {
		this.dosPitocin = dosPitocin;
	}
	public int getDosNitrous() {
		return dosNitrous;
	}
	public void setDosNitrous(int dosNitrous) {
		this.dosNitrous = dosNitrous;
	}
	public int getDosEpidural() {
		return dosEpidural;
	}
	public void setDosEpidural(int dosEpidural) {
		this.dosEpidural = dosEpidural;
	}
	public int getDosPethidine() {
		return dosPethidine;
	}
	public void setDosPethidine(int dosPethidine) {
		this.dosPethidine = dosPethidine;
	}
	public int getDosMagnesium() {
		return dosMagnesium;
	}
	public void setDosMagnesium(int dosMagnesium) {
		this.dosMagnesium = dosMagnesium;
	}
	public int getDosRHimmune() {
		return dosRHimmune;
	}
	public void setDosRHimmune(int dosRHimmune) {
		this.dosRHimmune = dosRHimmune;
	}
	public List<CHVBabyBean> getBabies() {
		return babies;
	}
	public void setBabies(List<CHVBabyBean> babies) {
		this.babies = babies;
	}
	public void addBaby(CHVBabyBean baby) {
		this.babies.add(baby);
	}
	
}
