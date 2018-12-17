package edu.ncsu.csc.itrust.model.old.beans;

import java.io.Serializable;
import java.util.Date;

public class PostnatalCareRecordBean implements Serializable {

	private static final long serialVersionUID = 3391922877099577123L;

	private int postnatalCareRecordId;
	private long mid;
	private String firstName;
	private String lastName;
	private Date recordDate;
	private Date childbirthDate;
	private String deliveryType;
	private String additionalComment;
	private Date releaseDate;

	public int getPostnatalCareRecordId() {
		return postnatalCareRecordId;
	}

	public void setPostnatalCareRecordId(int postnatalCareRecordId) {
		this.postnatalCareRecordId = postnatalCareRecordId;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Date getChildbirthDate() {
		return childbirthDate;
	}

	public void setChildbirthDate(Date childbirthDate) {
		this.childbirthDate = childbirthDate;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getAdditionalComment() {
		return additionalComment;
	}

	public void setAdditionalComment(String additionalComment) {
		this.additionalComment = additionalComment;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public int hashCode() {
		return postnatalCareRecordId;
	}

}
