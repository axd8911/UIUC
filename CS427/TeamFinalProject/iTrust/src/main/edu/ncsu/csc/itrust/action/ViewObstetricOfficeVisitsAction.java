package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.AddObstetricOfficeVisitValidator;
import edu.ncsu.csc.itrust.util.GoogleCalendarUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author kamenon2
 * @since 2018
 *  action supports Obstetric office visit action.
 */
public class ViewObstetricOfficeVisitsAction {

    public DAOFactory daoFactory;
    public String patientId;
    public long patientIdLngVal;
    public Long hcpId;
    public Map<Long, ObstetricOVBean> obstetricVisitMap;
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    
    private static final String DAO_INCORRECT_MSG = "DAO Factory not initialized correctly.";
    private static final String PATIENT_INCORRECT_MSG = "patient id cannot be null or empty";

    /**
     * Retrieve details for only that specific patient id.
     * @param daoFactory
     * @param patientId
     */
    //constructors ...
    public ViewObstetricOfficeVisitsAction(DAOFactory daoFactory, String patientId) {
        this.daoFactory = daoFactory;
        this.patientId = patientId;
        if (NumberUtils.isNumber(patientId)) {
            this.patientIdLngVal = Long.parseLong(patientId);
        }
    }

    /**
     * Retrieve details for the patient and the hcp id combined.
     * Only combination if visits exists, will be shown on the dashboard.
     * @param daoFactory
     * @param patientId
     * @param hcpId
     */
    public ViewObstetricOfficeVisitsAction(DAOFactory daoFactory, String patientId, Long hcpId) {
        this.daoFactory = daoFactory;
        this.patientId = patientId;
        this.hcpId = hcpId;
        if (NumberUtils.isNumber(patientId)) {
            this.patientIdLngVal = Long.parseLong(patientId);
        }
    }

    /**
     * @return obstetric office visits for the given patient id or the patient id and hcp id
     * combinations.
     * @throws FormValidationException
     * @throws ITrustException
     */
    //operations
    public List<ObstetricOVBean> getOBOVRecords()  throws FormValidationException, ITrustException {
        List<ObstetricOVBean> obstetricVisits = null;
        if (daoFactory!=null) {

            ObstetricOfficeVisitDAO obovDAO = daoFactory.getObstetricOfficeVisitDAO();
            if (StringUtils.isNotBlank(patientId)
                    && NumberUtils.isNumber(patientId)) {

                if (hcpId != null && hcpId > 0) {
                    obstetricVisits = obovDAO.getPatientOfficeVisitsByPatientHCPId(Long.parseLong(this.patientId), hcpId);
                } else {
                    obstetricVisits = obovDAO.getPatientOfficeVisitsByPatientId(Long.parseLong(this.patientId));
                }

                PatientDAO patientDAO = daoFactory.getPatientDAO();
                if (patientDAO != null) {
                    PatientBean patientBean = patientDAO.getPatient(Long.parseLong(this.patientId));
                    if (patientBean != null && obstetricVisits != null && !obstetricVisits.isEmpty()) {
                        for (ObstetricOVBean ovBean : obstetricVisits) {
                            ovBean.setRhShotTaken(patientBean.getRhShotIndicator());
                        }
                    }
                }

                //converted to map and set in session for avoiding roundtrip to db.
                if (obstetricVisits != null && !obstetricVisits.isEmpty()) {
                    setObstetricVisitMap(obstetricVisits.stream()
                            .collect(Collectors.toMap(ObstetricOVBean::getObVisitID, Function.identity())));
                }
            } else {
                throw new FormValidationException(PATIENT_INCORRECT_MSG);
            }
        } else {
            throw new ITrustException(DAO_INCORRECT_MSG);
        }

        return obstetricVisits;
    }

    /**
     * @param ovRecord
     * @return validate the Obstetric office visit patient records and return true if validated
     * successfully.
     * @throws FormValidationException
     * @throws ITrustException
     */
    public boolean validateOBOVPatientRecord(ObstetricOVBean ovRecord) throws FormValidationException, ITrustException {
        ObstetricOfficeVisitDAO obovDAO = daoFactory.getObstetricOfficeVisitDAO();
        logObstetricOVTransaction("oov-validate","Office Visit ID : "+ovRecord.getObVisitID());
        return obovDAO.validateOBPatient(ovRecord);
    }

    /**
     * @param ovRecord
     * @return return the updated office visit record once updated successfully.
     * @throws FormValidationException
     * @throws ITrustException
     */
    public ObstetricOVBean editOBOVPatientRecord(ObstetricOVBean ovRecord) throws FormValidationException, ITrustException {
        ObstetricOfficeVisitDAO obovDAO = daoFactory.getObstetricOfficeVisitDAO();
        logObstetricOVTransaction("edit-success","Office Visit ID : "+ovRecord.getObVisitID());
        return obovDAO.updateObstetricRecord(ovRecord);
    }

    /**
     * @param ovRecord
     * @return remove the Office visit records.
     * @throws FormValidationException
     * @throws ITrustException
     */
    public boolean removeOBOVPatientRecord(ObstetricOVBean ovRecord) throws FormValidationException, ITrustException {
        ObstetricOfficeVisitDAO obovDAO = daoFactory.getObstetricOfficeVisitDAO();
        logObstetricOVTransaction("edit-remove","Office Visit ID : "+ovRecord.getObVisitID());
        return obovDAO.deleteObstetricRecord(ovRecord);
    }

    /**
     * @param newOVRecord
     * @return add new Office visit record. Patient already visited are recorded through this
     * method.
     * @throws FormValidationException
     * @throws ITrustException
     */
    public ObstetricOVBean addOBOVPatientRecord(ObstetricOVBean newOVRecord) throws FormValidationException, ITrustException {

        new AddObstetricOfficeVisitValidator().validate(newOVRecord);

        List<ObstetricOVBean> patientOVRecords = getOBOVRecords();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if (newOVRecord.getVisitDate().compareTo(currentTime) > 0) {
            logObstetricOVTransaction("error","Selected date is greater than the current time for" +
                    " patient : " + patientId);
            throw new FormValidationException("Selected date is greater than the current time" +
                    ". Please select the date again.");
        }

        if (patientOVRecords!=null && !patientOVRecords.isEmpty()) {
            for (ObstetricOVBean ovBean : patientOVRecords) {
                if (ovBean.getVisitDate().compareTo(newOVRecord.getVisitDate()) == 0) {
                    logObstetricOVTransaction("error","Obstetric office visit has already been " +
                            "added. Already Visited on : " + newOVRecord.getVisitDate());
                    throw new FormValidationException("Obstetric office visit has already been added.");
                }
            }
        }

        PatientDAO patientDAO = daoFactory.getPatientDAO();
        TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_RHSHOT_CHECK, hcpId, Long.parseLong(patientId) , "Office Visit ID : "+newOVRecord.getObVisitID());
        if (newOVRecord.getRhShotTaken()>0) {
            patientDAO.updateRhShotIndicator(newOVRecord.getPatientMID(), newOVRecord.getRhShotTaken());
        } else {
            checkIfRhShotRequired(patientId,newOVRecord.getRhShotTaken()>0? String.valueOf(newOVRecord.getRhShotTaken()):null);
        }

        ObstetricOfficeVisitDAO obovDAO = daoFactory.getObstetricOfficeVisitDAO();
        newOVRecord.setObVisitID(obovDAO.addObstetricRecord(newOVRecord));

        logObstetricOVTransaction("add-record","Office Visit ID : "+newOVRecord.getObVisitID());

        addAppointmentToGoogleCalendar(newOVRecord,patientDAO.getPatient(newOVRecord.getPatientMID()));

        //FIXME: Capture the next Visit Id's from Google Calendar schedule if possibile here.
        String nextScheduledVisitId = ", the next visit's id";
        logObstetricOVTransaction("nextOVScheduled","Office Visit ID : "+newOVRecord.getObVisitID() + nextScheduledVisitId);

        return newOVRecord;
    }

    /**
     * @param newOVRecord
     * @param patientBean
     * @return add new appointment to google calendar.
     * @throws FormValidationException
     * @throws ITrustException
     */
    public boolean addAppointmentToGoogleCalendar(ObstetricOVBean newOVRecord, PatientBean patientBean)
            throws FormValidationException, ITrustException {
        if (newOVRecord!=null && patientBean!=null) {
            try {
                GoogleCalendarUtil util = new GoogleCalendarUtil();

                Map<String, String> inputMap = new HashMap<>();
                Timestamp currApptDt = newOVRecord.getVisitDate();
                String patientEmail = patientBean.getEmail();
                String patientFullName = patientBean.getFullName();

                inputMap.put("summary",patientFullName + "'s Appointment");
                inputMap.put("description",patientFullName + " has her obstetric office scheduled for today.");
                inputMap.put("patientAptStartTime", currApptDt.toString());

                Calendar apptDtPlusHour = Calendar.getInstance();
                apptDtPlusHour.setTimeInMillis(currApptDt.getTime());
                apptDtPlusHour.add(Calendar.HOUR,1);
                Timestamp timeAfter1Hour = new Timestamp(apptDtPlusHour.getTimeInMillis());

                inputMap.put("patientAptStartTime", currApptDt.getTime()+"");
                inputMap.put("patientAptEndTime", timeAfter1Hour.getTime()+"");
                inputMap.put("patient_email", patientEmail);

                String recurrenceRule = "RRULE:FREQ=MONTHLY;COUNT=9";
                String newReurrenceRule = calculateRecurrenceRule(patientBean.getMID()+"",currApptDt);
                if (newReurrenceRule!=null) {
                    recurrenceRule = newReurrenceRule;
                }

                inputMap.put("recurrence_rule", recurrenceRule);
                inputMap.put("patient_id",patientId);
                inputMap.put("visit_id",newOVRecord.getObVisitID()+"");

                util.createNewEvent(inputMap);

                return true;

            } catch (IOException ex) {
                throw new ITrustException("IOException: " + ex.getMessage());
            } catch (GeneralSecurityException ex){
                throw new ITrustException("GeneralSecurityException: " + ex.getMessage());
            } catch (SQLException ex){
                throw new ITrustException("SQLException: " + ex.getMessage());
            }
        }
        return false;
    }

    /**
     * @param patientId
     * @param currApptDt
     * @return calculate recurrence rules for the Google calendar.
     * @throws FormValidationException
     * @throws ITrustException
     * @throws SQLException
     */
    public String calculateRecurrenceRule (String patientId, Timestamp currApptDt) throws FormValidationException, ITrustException, SQLException {

        if (NumberUtils.isNumber(patientId)) {

            ObstetAction obstetAction = new ObstetAction(daoFactory, hcpId.longValue());
            List<ObstetRecBean> obstetricRecords = obstetAction.getObstetRecs(Long.parseLong(patientId));
            if (obstetricRecords != null && !obstetricRecords.isEmpty()) {
                obstetricRecords = obstetricRecords.stream()
                        .sorted((e1, e2) -> e1.getLmpDate().compareTo(e2.getLmpDate())).collect(Collectors.toList());
                ObstetRecBean obstetRecBean = obstetricRecords.get(0);

                if (obstetRecBean != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");//isoDateTimeUTCTZFormat
                    String dt = simpleDateFormat.format(obstetRecBean.getEddDate());

                    int weeksPreg = obstetRecBean.getPregWeeks();

                    if (weeksPreg >= 0 && weeksPreg <= 13) {
                        return "RRULE:FREQ=MONTHLY;UNTIL=" + dt;
                    } else if (weeksPreg >= 14 && weeksPreg <= 28) {
                        return "RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=" + dt;
                    } else if (weeksPreg >= 29 && weeksPreg <= 40) {
                        return "RRULE:FREQ=WEEKLY;UNTIL=" + dt;
                    } else if (weeksPreg >= 40 && weeksPreg <= 42) {
                        String byDay = "MO"; //default every monday
                        if (currApptDt != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(currApptDt.getTime());
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            if (Calendar.MONDAY == dayOfWeek) {
                                byDay = "MO";
                            } else if (Calendar.TUESDAY == dayOfWeek) {
                                byDay = "TU";
                            } else if (Calendar.WEDNESDAY == dayOfWeek) {
                                byDay = "WE";
                            } else if (Calendar.THURSDAY == dayOfWeek) {
                                byDay = "TH";
                            } else if (Calendar.FRIDAY == dayOfWeek) {
                                byDay = "FR";
                            } else if (Calendar.SATURDAY == dayOfWeek) {
                                byDay = "SA";
                            } else if (Calendar.SUNDAY == dayOfWeek) {
                                byDay = "SU";
                            }
                        }
                        return "RRULE:FREQ=WEEKLY;BYDAY=" + byDay + ";INTERVAL=2;UNTIL=" + dt;
                    }
                }
            }
        }

        return "RRULE:FREQ=MONTHLY;COUNT=1";
    }

    /**
     * Before submitting the office visit record, validate if patient requires RH shots.
     * @param pidString
     * @param rhShotSelected
     * @throws FormValidationException
     * @throws ITrustException
     */
    public void checkIfRhShotRequired(String pidString,String rhShotSelected) throws FormValidationException, ITrustException {

        logObstetricOVTransaction("rhshot-check",pidString);
        //TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_RHSHOT_CHECK,hcpId, 0L , pidString);
        PatientDAO patientDAO = daoFactory.getPatientDAO();
        if (patientDAO!=null && rhShotSelected == null) {
            if (NumberUtils.isNumber(pidString)) {
                PatientBean patientBean = patientDAO.getPatient(Long.parseLong(pidString));
                if (patientBean != null) {
                    BloodType bloodType = patientBean.getBloodType();
                    if (bloodType != null && bloodType.getName().contains("-") && patientBean.getRhShotIndicator() <= 0) {
                        logObstetricOVTransaction("error",pidString+ " : Patient needs RH " +
                                "Shot before the form can be submitted.");
                        throw new FormValidationException("Patient needs RH Shot before the form can be submitted.");
                    }
                }
            } else {
                logObstetricOVTransaction("error",pidString+ " : Patient id needs to be numeric");
                throw new FormValidationException("Patient id needs to be numeric");
            }
        }
    }

    /*
    public boolean eligibleForOfficeVisit() throws FormValidationException, ITrustException, SQLException {
        ObstetAction obstetAction = new ObstetAction(daoFactory, hcpId.longValue());
        if (obstetAction !=null) {
            List<ObstetRecBean> obstetricRecords = obstetAction.getObstetRecs(Long.parseLong(patientId));
            if (obstetricRecords == null ){
                return false;
            }

            if (obstetricRecords!=null && !obstetricRecords.isEmpty()) {
                obstetricRecords = obstetricRecords.stream()
                        .sorted((e1,e2) -> e2.getInitDate().compareTo(e1.getInitDate())).collect(Collectors.toList());

                if (obstetricRecords != null) {
                    Calendar date343DaysBefore = Calendar.getInstance();
                    date343DaysBefore.add(Calendar.DAY_OF_MONTH,-343);

                    ObstetRecBean obRecBean = obstetricRecords.get(0);
                    if (obRecBean.getLmpDate().getTime() < date343DaysBefore.getTimeInMillis()) {
                        return false;
                    }
                }
            }
        }

        ViewPersonnelAction personnelAction = new ViewPersonnelAction(daoFactory,hcpId);
        PersonnelBean personnel = personnelAction.getPersonnel(hcpId.toString());
        if(!"OB/GYN".equalsIgnoreCase(personnel.getSpecialty())) {
            return false;
        }
        return true;
    }
    */
    /*
    public void checkForFormValidity(Date date, String numDaysPreg, String bloodPress, String fhr,
                                       String multiple) throws FormValidationException, ITrustException {
        StringBuilder errMsgBuilder = new StringBuilder();
        if (date == null || StringUtils.isEmpty(numDaysPreg) || StringUtils.isEmpty(bloodPress)
            || StringUtils.isEmpty(fhr) || StringUtils.isEmpty(multiple))  {
            errMsgBuilder.append("Fields : ");
        }

        try {
            if (date == null) {
                errMsgBuilder.append("Date empty");
            } else {
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                format.format(date);
            }
        }catch (Exception ex) {
            errMsgBuilder.append("Invalid date. Use format (MM/dd/YYYY hh:mm a)");
            throw new FormValidationException(errMsgBuilder.toString());
        }

        if (StringUtils.isEmpty(numDaysPreg)) {
            String customErrMsg = "num days cannot be empty";
            createFinalErrMsg(errMsgBuilder,customErrMsg);
        }

        if (StringUtils.isEmpty(bloodPress)) {
            String customErrMsg = "blood pressure cannot be empty";
            createFinalErrMsg(errMsgBuilder,customErrMsg);
        }

        if (StringUtils.isEmpty(fhr)) {
            String customErrMsg = "fhr cannot be empty";
            createFinalErrMsg(errMsgBuilder,customErrMsg);
        }

        if (StringUtils.isEmpty(multiple)) {
            String customErrMsg = "multiple cannot be empty";
            createFinalErrMsg(errMsgBuilder,customErrMsg);
        }

        if (errMsgBuilder!=null && !errMsgBuilder.toString().isEmpty()) {
            throw new FormValidationException(errMsgBuilder.toString());
        }
    }
*/

    /**
     * @param args
     * @return true if transaction logging is done successfully.
     * @throws FormValidationException
     */
    public boolean logObstetricOVTransaction (String ... args) throws FormValidationException{
        if (args!=null && args.length>0) {
            try {
                if ("add-record".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICEVISIT_CREATE,
                            hcpId, patientIdLngVal, "Office Visit ID : " + args[1]);
                    return true;
                } else if ("edit-view".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICEVISIT_VIEW,
                            hcpId, patientIdLngVal, "Office Visit ID : " + args[1]);
                    return true;
                } else if ("edit-success".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICEVISIT_EDIT,
                            hcpId, patientIdLngVal, "Office Visit ID : " + args[1]);
                    return true;
                } else if ("ultrasound".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.ULTRASOUND_CREATE_ADD,
                            hcpId, patientIdLngVal, "Office Visit ID : " + args[1]);
                    return true;
                } else if ("nextOVScheduled".equalsIgnoreCase(args[0])) {
                    if (args.length>2) {
                        TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT,
                                hcpId, patientIdLngVal, "Current office visit ID : " + args[1] + ", next office visit ID : " + args[2]);
                    } else {
                        TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_NEXT,
                                hcpId, patientIdLngVal, "Current office visit ID : " + args[1]);
                    }
                    return true;
                } else if ("chidBirthOVScheduled".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICE_SCHEDULE_CHILDBIRTH,
                            hcpId, patientIdLngVal,
                            "Current office visit ID : " + args[1] + ", Child birth office visit ID : " + args[2]);
                    return true;
                } else if ("oov-validate".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICEVISIT_VALIDATE,
                            hcpId, patientIdLngVal, args[1]);
                    return true;
                } else if ("edit-remove".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICEVISIT_REMOVE,
                            hcpId, patientIdLngVal, args[1]);
                    return true;
                } else if ("rhshot-check".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_RHSHOT_CHECK,
                            hcpId, patientIdLngVal, args[1]);
                    return true;
                } else if ("error".equalsIgnoreCase(args[0])) {
                    TransactionLogger.getInstance().logTransaction(TransactionType.OBSTETRIC_OFFICEVISIT_ERROR,
                            hcpId, patientIdLngVal, args[1]);
                    return true;
                }
            } catch (Exception ex) {
                throw new FormValidationException("Error executing transactiong logging : " + args[0] + ". Message : " + ex.getMessage());
            }
        }
        return false;
    }

    /*
    private void createFinalErrMsg(StringBuilder sb, String errMsg) {
        if (!sb.toString().isEmpty()) {
            sb.append(", ").append(errMsg);
        } else {
            sb.append(errMsg);
        }
    }
    */

    //getter and setters
    public Map<Long, ObstetricOVBean> getObstetricVisitMap() {
        return obstetricVisitMap;
    }

    public void setObstetricVisitMap(Map<Long, ObstetricOVBean> obstetricVisitMap) {
        this.obstetricVisitMap = obstetricVisitMap;
    }
}







