package edu.ncsu.csc.itrust.model.old.beans;

import java.io.File;
import java.sql.Timestamp;
import java.util.Objects;

public class UltraSoundBean {
    private long ultraSoundRecordId;
    private long patientMid;
    private Timestamp createDate;
    private long obstetricOfficeVisitId;
    private String imageLocation;
    private File image;
    private double crownRumpLength;
    private double biparietalDiameter;
    private double headCircumference;
    private double femurLength;
    private double occipitofrontalDiameter;
    private double abdominalCircumference;
    private double humerusLength;
    private double estimatedFetalWeight;

    public UltraSoundBean(long ultraSoundRecordId, long patientMid, Timestamp createDate, long obstetricOfficeVisitId,
			String imageLocation, File image, double crownRumpLength, double biparietalDiameter,
			double headCircumference, double femurLength, double occipitofrontalDiameter, double abdominalCircumference,
			double humerusLength, double estimatedFetalWeight) {
		super();
		this.ultraSoundRecordId = ultraSoundRecordId;
		this.patientMid = patientMid;
		this.createDate = createDate;
		this.obstetricOfficeVisitId = obstetricOfficeVisitId;
		this.imageLocation = imageLocation;
		this.image = image;
		this.crownRumpLength = crownRumpLength;
		this.biparietalDiameter = biparietalDiameter;
		this.headCircumference = headCircumference;
		this.femurLength = femurLength;
		this.occipitofrontalDiameter = occipitofrontalDiameter;
		this.abdominalCircumference = abdominalCircumference;
		this.humerusLength = humerusLength;
		this.estimatedFetalWeight = estimatedFetalWeight;
	}

    public UltraSoundBean(long ultraSoundRecordId, long patientMid, Timestamp createDate, long obstetricOfficeVisitId, double crownRumpLength, double biparietalDiameter,
                          double headCircumference, double femurLength, double occipitofrontalDiameter, double abdominalCircumference,
                          double humerusLength, double estimatedFetalWeight) {
        this(ultraSoundRecordId, patientMid, createDate, obstetricOfficeVisitId, "", null, crownRumpLength, biparietalDiameter, headCircumference, femurLength, occipitofrontalDiameter, abdominalCircumference, humerusLength, estimatedFetalWeight);
    }

	public UltraSoundBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UltraSoundBean)) return false;
        UltraSoundBean that = (UltraSoundBean) o;
        return getUltraSoundRecordId() == that.getUltraSoundRecordId() &&
                getPatientMid() == that.getPatientMid() &&
                getObstetricOfficeVisitId() == that.getObstetricOfficeVisitId() &&
                Double.compare(that.getCrownRumpLength(), getCrownRumpLength()) == 0 &&
                Double.compare(that.getBiparietalDiameter(), getBiparietalDiameter()) == 0 &&
                Double.compare(that.getHeadCircumference(), getHeadCircumference()) == 0 &&
                Double.compare(that.getFemurLength(), getFemurLength()) == 0 &&
                Double.compare(that.getOccipitofrontalDiameter(), getOccipitofrontalDiameter()) == 0 &&
                Double.compare(that.getAbdominalCircumference(), getAbdominalCircumference()) == 0 &&
                Double.compare(that.getHumerusLength(), getHumerusLength()) == 0 &&
                Double.compare(that.getEstimatedFetalWeight(), getEstimatedFetalWeight()) == 0 &&
                Objects.equals(getCreateDate(), that.getCreateDate()) &&
                Objects.equals(getImageLocation(), that.getImageLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUltraSoundRecordId(), getPatientMid(), getCreateDate(), getObstetricOfficeVisitId(), getImageLocation(), getCrownRumpLength(), getBiparietalDiameter(), getHeadCircumference(), getFemurLength(), getOccipitofrontalDiameter(), getAbdominalCircumference(), getHumerusLength(), getEstimatedFetalWeight());
    }

    public long getUltraSoundRecordId() {
        return ultraSoundRecordId;
    }

    public void setUltraSoundRecordId(long ultraSoundRecordId) {
        this.ultraSoundRecordId = ultraSoundRecordId;
    }

    public long getPatientMid() {
        return patientMid;
    }

    public void setPatientMid(long patientMid) {
        this.patientMid = patientMid;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public long getObstetricOfficeVisitId() {
        return obstetricOfficeVisitId;
    }

    public void setObstetricOfficeVisitId(long obstetricOfficeVisitId) {
        this.obstetricOfficeVisitId = obstetricOfficeVisitId;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File file) {
        this.image = file;
    }

    public double getCrownRumpLength() {
        return crownRumpLength;
    }

    public void setCrownRumpLength(double crownRumpLength) {
        this.crownRumpLength = crownRumpLength;
    }

    public double getBiparietalDiameter() {
        return biparietalDiameter;
    }

    public void setBiparietalDiameter(double biparietalDiameter) {
        this.biparietalDiameter = biparietalDiameter;
    }

    public double getHeadCircumference() {
        return headCircumference;
    }

    public void setHeadCircumference(double headCircumference) {
        this.headCircumference = headCircumference;
    }

    public double getFemurLength() {
        return femurLength;
    }

    public void setFemurLength(double femurLength) {
        this.femurLength = femurLength;
    }

    public double getOccipitofrontalDiameter() {
        return occipitofrontalDiameter;
    }

    public void setOccipitofrontalDiameter(double occipitofrontalDiameter) {
        this.occipitofrontalDiameter = occipitofrontalDiameter;
    }

    public double getAbdominalCircumference() {
        return abdominalCircumference;
    }

    public void setAbdominalCircumference(double abdominalCircumference) {
        this.abdominalCircumference = abdominalCircumference;
    }

    public double getHumerusLength() {
        return humerusLength;
    }

    public void setHumerusLength(double humerusLength) {
        this.humerusLength = humerusLength;
    }

    public double getEstimatedFetalWeight() {
        return estimatedFetalWeight;
    }

    public void setEstimatedFetalWeight(double estimatedFetalWeight) {
        this.estimatedFetalWeight = estimatedFetalWeight;
    }

}
