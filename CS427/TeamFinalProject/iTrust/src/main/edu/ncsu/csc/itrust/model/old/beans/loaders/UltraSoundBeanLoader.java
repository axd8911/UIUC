package edu.ncsu.csc.itrust.model.old.beans.loaders;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.beans.UltraSoundBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UltraSoundBeanLoader implements BeanLoader<UltraSoundBean> {

    private static ErrorList errors = new ErrorList();

    @Override
    public List<UltraSoundBean> loadList(ResultSet rs) throws SQLException {
        ArrayList<UltraSoundBean> list = new ArrayList<UltraSoundBean>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    @Override
    public UltraSoundBean loadSingle(ResultSet rs) throws SQLException {
        if (rs != null) {
            UltraSoundBean ultraSoundBean = new UltraSoundBean();

            ultraSoundBean.setUltraSoundRecordId(rs.getLong("ultrasound_record_id"));
            ultraSoundBean.setPatientMid(rs.getLong("patient_mid"));
            Timestamp createDate = rs.getTimestamp("create_date");
            ultraSoundBean.setCreateDate(createDate);
            ultraSoundBean.setObstetricOfficeVisitId(rs.getLong("obstetric_office_visit_id"));
            ultraSoundBean.setImageLocation(rs.getString("image_location"));

            ultraSoundBean.setCrownRumpLength(rs.getDouble("crown_rump_length"));
            ultraSoundBean.setBiparietalDiameter(rs.getDouble("biparietal_diameter"));
            ultraSoundBean.setHeadCircumference(rs.getDouble("head_circumference"));
            ultraSoundBean.setFemurLength(rs.getDouble("femur_length"));
            ultraSoundBean.setOccipitofrontalDiameter(rs.getDouble("occipitofrontal_diameter"));
            ultraSoundBean.setAbdominalCircumference(rs.getDouble("abdominal_circumference"));
            ultraSoundBean.setHumerusLength(rs.getDouble("humerus_length"));
            ultraSoundBean.setEstimatedFetalWeight(rs.getDouble("estimated_fetal_weight"));
            return ultraSoundBean;
        }
        return null;
    }

    @Override
    public PreparedStatement loadParameters(PreparedStatement ps, UltraSoundBean bean) throws SQLException {
        throw new IllegalStateException("unimplemented exception");
    }

    public static UltraSoundBean beanBuilder(Map<String, String[]> map) throws FormValidationException {
        UltraSoundBean bean = new UltraSoundBean();
        errors.getMessageList().clear();


        String crownRumpLength = map.get("crownRumpLength")[0];
        String biparietalDiameter = map.get("biparietalDiameter")[0];
        String headCircumference = map.get("headCircumference")[0];
        String femurLength = map.get("femurLength")[0];
        String occipitofrontalDiameter = map.get("occipitofrontalDiameter")[0];
        String abdominalCircumference = map.get("abdominalCircumference")[0];
        String humerusLength = map.get("humerusLength")[0];
        String estimatedFetalWeight = map.get("estimatedFetalWeight")[0];


        addCrownRumpLength(bean, crownRumpLength);
        addBiparietalDiameter(bean, biparietalDiameter);
        addHeadCircumference(bean, headCircumference);
        addFemurLength(bean, femurLength);
        addDccipitofrontalDiameter(bean, occipitofrontalDiameter);
        addAbdominalCircumference(bean, abdominalCircumference);
        addHumerusLength(bean, humerusLength);
        addEstimatedFetalWeight(bean, estimatedFetalWeight);

        if (errors.hasErrors())
            throw new FormValidationException(errors);

        return bean;

    }

    private static void addEstimatedFetalWeight(UltraSoundBean bean, String estimatedFetalWeight) {
        try{

            bean.setEstimatedFetalWeight(Double.valueOf(estimatedFetalWeight));

            if (bean.getEstimatedFetalWeight() < 0){
                throw new Exception();
            }


        }catch (Exception e){
            errors.addIfNotNull("humerus length : Should be a decimal greater than 0");

        }
    }

    private static void addHumerusLength(UltraSoundBean bean, String humerusLength) {
        try{

            bean.setHumerusLength(Double.valueOf(humerusLength));

            if (bean.getHumerusLength() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("humerus length : Should be a decimal greater than 0");

        }
    }

    private static void addAbdominalCircumference(UltraSoundBean bean, String abdominalCircumference) {
        try{

            bean.setAbdominalCircumference(Double.valueOf(abdominalCircumference));

            if (bean.getAbdominalCircumference() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("Abdominal Circumference : Should be a decimal greater than 0");

        }
    }

    private static void addDccipitofrontalDiameter(UltraSoundBean bean, String occipitofrontalDiameter) {
        try{

            bean.setOccipitofrontalDiameter(Double.valueOf(occipitofrontalDiameter));

            if (bean.getOccipitofrontalDiameter() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("occipitofrontal diameter : Should be a decimal greater than 0");

        }
    }

    private static void addFemurLength(UltraSoundBean bean, String femurLength) {
        try{

            bean.setFemurLength(Double.valueOf(femurLength));
            if (bean.getFemurLength() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("femur length : Should be a decimal greater than 0");

        }
    }

    private static void addHeadCircumference(UltraSoundBean bean, String headCircumference) {
        try{

            bean.setHeadCircumference(Double.valueOf(headCircumference));

            if (bean.getHeadCircumference() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("Head Circumference : Should be a decimal greater than 0");

        }
    }


    private static void addBiparietalDiameter(UltraSoundBean bean, String biparietalDiameter) {
        try{

            bean.setBiparietalDiameter(Double.valueOf(biparietalDiameter));

            if (bean.getBiparietalDiameter() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("Biparietal Diameter: Should be a decimal greater than 0");

        }
    }

    private static void addCrownRumpLength(UltraSoundBean bean, String crownRumpLength) {
        try{

            bean.setCrownRumpLength(Double.valueOf(crownRumpLength));

            if (bean.getCrownRumpLength() < 0){
                throw new Exception();
            }

        }catch (Exception e){
            errors.addIfNotNull("Crown Rump Length : Should be a decimal greater than 0");

        }
    }


}
