package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.beans.UltraSoundBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricOVBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.UltraSoundBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.*;
import java.util.List;

public class ObstetricOfficeVisitDAO {

    private transient final DAOFactory factory;
    private transient final ObstetricOVBeanLoader oovbLoader;
    private transient final UltraSoundBeanLoader ultraSoundBeanLoader;

    /*office visit queries*/
    private static final String OOV_BY_PATIENT_ID_ONLY = "SELECT * FROM obstetricofficevisit WHERE patientMID=?";
    private static final String OOV_BY_PATIENT_AND_HCP_ID = "SELECT * FROM obstetricofficevisit WHERE patientMID=? AND obhcpmid=?";
    private static final String UPDATE_OOV_RECORD = "UPDATE obstetricofficevisit set visitDate = ?, numberDaysPregnant=?, " +
            "blood_pressure=?, fhr=?, multiplet=?, llp=?, ultrasound=?, weight=? WHERE OBVisitID=?";
    private static final String DELETE_OOV_RECORD = "delete from obstetricofficevisit where obvisitid=?";

    private static final String INSERT_OOV_RECORD = "INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, " +
            "numberDaysPregnant, blood_pressure, fhr, multiplet, llp, ultrasound, weight) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /*ultrasound queries */
    private static final String INSERT_ULTRASOUND_RECORD = "insert into ultrasound_records (patient_mid, create_date," +
            "obstetric_office_visit_id, image_location, crown_rump_length, biparietal_diameter, head_circumference," +
            "femur_length, occipitofrontal_diameter, abdominal_circumference, humerus_length, estimated_fetal_weight) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ULTRASOUND_RECORD = "update ultrasound_records set create_date=?," +
            "crown_rump_length=?, biparietal_diameter=?, head_circumference=?," +
            "femur_length=?, occipitofrontal_diameter=?, abdominal_circumference=?, humerus_length=?, estimated_fetal_weight=? " +
            "where ultrasound_record_id = ? and patient_mid = ? and obstetric_office_visit_id = ?";

    private static final String INSERT_ULTRASOUND_IMAGE = "insert into ultrasound_records (image_location,patient_mid,obstetric_office_visit_id) " +
            "VALUES (?,?,?)";
    private static final String UPDATE_ULTRASOUND_IMAGE = "update ultrasound_records set image_location=? " +
            "where ultrasound_record_id = ? and patient_mid = ? and obstetric_office_visit_id = ?";

    private static final String ULTRASOUND_RECORDS = "select * from ultrasound_records where patient_mid = ? " +
            "and obstetric_office_visit_id = ?";

    public ObstetricOfficeVisitDAO(DAOFactory factory) {
        this.factory = factory;
        this.oovbLoader = new ObstetricOVBeanLoader();
        this.ultraSoundBeanLoader = new UltraSoundBeanLoader();
    }

    /**
     * @param patientMid
     * @return UC-94 User story 1: for retrieving Obstetric office visit patient mid only
     * @throws DBException
     */
    public List<ObstetricOVBean> getPatientOfficeVisitsByPatientId(long patientMid) throws DBException {

        List<ObstetricOVBean> ovList = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = factory.getConnection();
            stmt = conn.prepareStatement(OOV_BY_PATIENT_ID_ONLY);
            stmt.setLong(1, patientMid);

            final ResultSet results = stmt.executeQuery();
            ovList = this.oovbLoader.loadList(results);

            results.close();
            stmt.close();

        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
           handleConnectionHelper(conn,stmt);
        }

        return ovList;
    }

    /**
     * @param patientMid
     * @param hcpMid
     * @return UC-94 User story 1: for retrieving Obstetric office visit patient mid + hcp mid
     * @throws DBException
     */
    public List<ObstetricOVBean> getPatientOfficeVisitsByPatientHCPId(long patientMid, long hcpMid) throws DBException {

        List<ObstetricOVBean> ovList = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = factory.getConnection();
            stmt = conn.prepareStatement(OOV_BY_PATIENT_AND_HCP_ID);
            stmt.setLong(1, patientMid);
            stmt.setLong(2, hcpMid);

            final ResultSet results = stmt.executeQuery();
            ovList = this.oovbLoader.loadList(results);

            results.close();
            stmt.close();

        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }

        return ovList;
    }

    /**
     * @param obstetricOVBean
     * @return validate OB patient and return true if valid.
     * @throws DBException
     */
    public boolean validateOBPatient (ObstetricOVBean obstetricOVBean) throws DBException{
        List<ObstetricOVBean> obstetricOVBeans =
                getPatientOfficeVisitsByPatientId(obstetricOVBean.getPatientMID());
        return obstetricOVBeans!=null && !obstetricOVBeans.isEmpty()?Boolean.TRUE:Boolean.FALSE;
    }

    /**
     * @param obstetricOVBean
     * @return update obstetrics record
     * @throws DBException
     */
    public ObstetricOVBean updateObstetricRecord (ObstetricOVBean obstetricOVBean) throws DBException{

        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isUpdated = false;
        try {
            conn = factory.getConnection();
            stmt = conn.prepareStatement(UPDATE_OOV_RECORD);
            stmt.setTimestamp(1, obstetricOVBean.getVisitDate());
            stmt.setLong(2, obstetricOVBean.getNumberDaysPregnant());
            stmt.setString(3, obstetricOVBean.getBloodPressure());
            stmt.setShort(4, obstetricOVBean.getFhr());
            stmt.setByte(5, obstetricOVBean.getMultiplet());
            stmt.setByte(6, obstetricOVBean.getLlp());
            stmt.setByte(7, obstetricOVBean.getUltraSound());
            stmt.setShort(8, obstetricOVBean.getWeight());
            stmt.setLong(9, obstetricOVBean.getObVisitID());


            final int isExecuted = stmt.executeUpdate();
            if (isExecuted > 0) {
                isUpdated = true;
            }
            stmt.close();

        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }
        return obstetricOVBean;
    }


    /**
     * @param obstetricOVBean
     * @return delete obstetrics record and return true if done so successfully.
     * @throws DBException
     */
    public boolean deleteObstetricRecord (ObstetricOVBean obstetricOVBean) throws DBException{
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = factory.getConnection();
            stmt = conn.prepareStatement(DELETE_OOV_RECORD);
            stmt.setLong(1, obstetricOVBean.getObVisitID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }
    }

    /**
     * @param obstetricOVBean
     * @return add obstetric records and return the office visit id
     * @throws DBException
     */
    public Long addObstetricRecord (ObstetricOVBean obstetricOVBean) throws DBException{

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = factory.getConnection();
            stmt = conn.prepareStatement(INSERT_OOV_RECORD,Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, obstetricOVBean.getoBhcpMID());
            stmt.setLong(2, obstetricOVBean.getPatientMID());
            stmt.setTimestamp(3, obstetricOVBean.getVisitDate()!=null?
                    obstetricOVBean.getVisitDate(): new Timestamp(System.currentTimeMillis()));
            stmt.setLong(4, obstetricOVBean.getNumberDaysPregnant());
            stmt.setString(5, obstetricOVBean.getBloodPressure()!=null?
                    obstetricOVBean.getBloodPressure():"0/0");
            stmt.setShort(6, obstetricOVBean.getFhr());
            stmt.setByte(7, obstetricOVBean.getMultiplet());
            stmt.setByte(8, obstetricOVBean.getLlp());
            stmt.setByte(9, obstetricOVBean.getUltraSound());
            stmt.setShort(10, obstetricOVBean.getWeight());

            stmt.execute();

            Long officeVisitId = null;
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                officeVisitId=rs.getLong(1);
            }
            return officeVisitId;

        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }
    }

    /**
     * @param patientId
     * @param obstetricOfficeVisitId
     * @return list of patients ultrasound records.
     * @throws DBException
     */
    public List<UltraSoundBean> getPatientUltraSoundRecords (Long patientId, Long obstetricOfficeVisitId) throws DBException{
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (patientId !=null && obstetricOfficeVisitId != null) {
                conn = factory.getConnection();
                stmt = conn.prepareStatement(ULTRASOUND_RECORDS);
                stmt.setLong(1, patientId);
                stmt.setLong(2, obstetricOfficeVisitId);
                final ResultSet results = stmt.executeQuery();

                List<UltraSoundBean> ultraSoundBeanList = this.ultraSoundBeanLoader.loadList(results);
                return  ultraSoundBeanList;
            }
        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }
        return null;
    }

    /**
     * @param ultraSoundBean
     * @return ultrasound record id once add or update patients ultra sounds image is complete.
     * @throws DBException
     */
    public Long addOrUpdatePatientUltraSoundImage (UltraSoundBean ultraSoundBean) throws DBException{
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (ultraSoundBean!=null) {
                conn = factory.getConnection();

                List<UltraSoundBean> existingUltraSoundRecord = getPatientUltraSoundRecords(ultraSoundBean.getPatientMid()
                        ,ultraSoundBean.getObstetricOfficeVisitId());

                if (existingUltraSoundRecord!=null && !existingUltraSoundRecord.isEmpty()) {
                    //update
                    UltraSoundBean existingUltraSoundBean = existingUltraSoundRecord.get(0);
                    stmt = conn.prepareStatement(UPDATE_ULTRASOUND_IMAGE);
                    stmt.setString(1, ultraSoundBean.getImageLocation());
                    stmt.setLong(2, existingUltraSoundBean.getUltraSoundRecordId());
                    stmt.setLong(3, existingUltraSoundBean.getPatientMid());
                    stmt.setLong(4, existingUltraSoundBean.getObstetricOfficeVisitId());
                    stmt.execute();

                    return existingUltraSoundBean.getUltraSoundRecordId();
                } else {
                    //insert
                    stmt = conn.prepareStatement(INSERT_ULTRASOUND_IMAGE, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, ultraSoundBean.getImageLocation());
                    stmt.setLong(2, ultraSoundBean.getPatientMid());
                    stmt.setLong(3, ultraSoundBean.getObstetricOfficeVisitId());
                    stmt.execute();

                    Long ultraSoundRecordId = null;
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        ultraSoundRecordId = rs.getLong(1);
                    }
                    return ultraSoundRecordId;
                }
            }
        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }
        return null;
    }

    /**
     * @param ultraSoundBean
     * @return ultrasound record id once patient ultrasound reports details are submitted.
     * @throws DBException
     */
    public Long addOrUpdatePatientUltraSoundReport(UltraSoundBean ultraSoundBean) throws DBException{
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (ultraSoundBean!=null) {
                conn = factory.getConnection();
                List<UltraSoundBean> existingUltraSoundRecord = getPatientUltraSoundRecords(ultraSoundBean.getPatientMid()
                        ,ultraSoundBean.getObstetricOfficeVisitId());

                if (existingUltraSoundRecord!=null && !existingUltraSoundRecord.isEmpty()) {
                    stmt = conn.prepareStatement(UPDATE_ULTRASOUND_RECORD);
                    UltraSoundBean existingBean = existingUltraSoundRecord.get(0);

                    stmt.setTimestamp(1, ultraSoundBean.getCreateDate());
                    stmt.setDouble(2, ultraSoundBean.getCrownRumpLength());
                    stmt.setDouble(3, ultraSoundBean.getBiparietalDiameter());
                    stmt.setDouble(4, ultraSoundBean.getHeadCircumference());
                    stmt.setDouble(5, ultraSoundBean.getFemurLength());
                    stmt.setDouble(6, ultraSoundBean.getOccipitofrontalDiameter());
                    stmt.setDouble(7, ultraSoundBean.getAbdominalCircumference());
                    stmt.setDouble(8, ultraSoundBean.getHumerusLength());
                    stmt.setDouble(9, ultraSoundBean.getEstimatedFetalWeight());

                    stmt.setLong(10, existingBean.getUltraSoundRecordId());
                    stmt.setLong(11, existingBean.getPatientMid());
                    stmt.setLong(12, existingBean.getObstetricOfficeVisitId());
                    stmt.execute();

                    return existingBean.getUltraSoundRecordId();
                } else {
                    stmt = conn.prepareStatement(INSERT_ULTRASOUND_RECORD, Statement.RETURN_GENERATED_KEYS);
                    stmt.setLong(1, ultraSoundBean.getPatientMid());
                    stmt.setTimestamp(2, ultraSoundBean.getCreateDate());
                    stmt.setLong(3, ultraSoundBean.getObstetricOfficeVisitId());
                    stmt.setString(4, ultraSoundBean.getImageLocation());
                    stmt.setDouble(5, ultraSoundBean.getCrownRumpLength());
                    stmt.setDouble(6, ultraSoundBean.getBiparietalDiameter());
                    stmt.setDouble(7, ultraSoundBean.getHeadCircumference());
                    stmt.setDouble(8, ultraSoundBean.getFemurLength());
                    stmt.setDouble(9, ultraSoundBean.getOccipitofrontalDiameter());
                    stmt.setDouble(10, ultraSoundBean.getAbdominalCircumference());
                    stmt.setDouble(11, ultraSoundBean.getHumerusLength());
                    stmt.setDouble(12, ultraSoundBean.getEstimatedFetalWeight());
                    stmt.execute();

                    Long ultraSoundRecordId = null;
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        ultraSoundRecordId = rs.getLong(1);
                    }
                    return ultraSoundRecordId;
                }

            }
        } catch (SQLException sqe) {
            throw new DBException(sqe);
        } finally {
            handleConnectionHelper(conn,stmt);
        }
        return null;
    }


    /**
     * Helper method to handle connection handling.
     * @param connection
     * @param statement
     */
    private void handleConnectionHelper(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if(connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

