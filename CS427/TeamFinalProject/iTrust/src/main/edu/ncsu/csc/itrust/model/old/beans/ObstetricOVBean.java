package edu.ncsu.csc.itrust.model.old.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class ObstetricOVBean implements Serializable {

    private long obVisitID;
    private long oBhcpMID;
    private long patientMID;
    private Timestamp visitDate;
    private short numberDaysPregnant;
    private String bloodPressure;
    private short fhr;
    private byte multiplet;
    private byte llp;
    private byte ultraSound;
    private byte rhShotTaken;
    private short weight;
    private boolean addToGoogleCalendar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObstetricOVBean)) return false;
        ObstetricOVBean that = (ObstetricOVBean) o;
        return getObVisitID() == that.getObVisitID() &&
                getoBhcpMID() == that.getoBhcpMID() &&
                getPatientMID() == that.getPatientMID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObVisitID(), getoBhcpMID(), getPatientMID());
    }

    public long getObVisitID() {
        return obVisitID;
    }

    public void setObVisitID(long obVisitID) {
        this.obVisitID = obVisitID;
    }

    public long getoBhcpMID() {
        return oBhcpMID;
    }

    public void setoBhcpMID(long oBhcpMID) {
        this.oBhcpMID = oBhcpMID;
    }

    public long getPatientMID() {
        return patientMID;
    }

    public void setPatientMID(long patientMID) {
        this.patientMID = patientMID;
    }

    public Timestamp getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Timestamp visitDate) {
        this.visitDate = visitDate;
    }

    public short getNumberDaysPregnant() {
        return numberDaysPregnant;
    }

    public void setNumberDaysPregnant(short numberDaysPregnant) {
        this.numberDaysPregnant = numberDaysPregnant;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public short getFhr() {
        return fhr;
    }

    public void setFhr(short fhr) {
        this.fhr = fhr;
    }

    public byte getMultiplet() {
        return multiplet;
    }

    public void setMultiplet(byte multiplet) {
        this.multiplet = multiplet;
    }

    public byte getLlp() {
        return llp;
    }

    public void setLlp(byte llp) {
        this.llp = llp;
    }

    public byte getUltraSound() {
        return ultraSound;
    }

    public void setUltraSound(byte ultraSound) {
        this.ultraSound = ultraSound;
    }

    public byte getRhShotTaken() {
        return rhShotTaken;
    }

    public void setRhShotTaken(byte rhShotTaken) {
        this.rhShotTaken = rhShotTaken;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public boolean isAddToGoogleCalendar() {
        return addToGoogleCalendar;
    }

    public void setAddToGoogleCalendar(boolean addToGoogleCalendar) {
        this.addToGoogleCalendar = addToGoogleCalendar;
    }
}
