package edu.ncsu.csc.itrust.model.old.beans.loaders;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ObstetricOVBeanLoader implements BeanLoader<ObstetricOVBean> {
    private static ErrorList errors = new ErrorList();
    
    @Override
    public List<ObstetricOVBean> loadList(ResultSet rs) throws SQLException {
        ArrayList<ObstetricOVBean> list = new ArrayList<ObstetricOVBean>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    @Override
    public ObstetricOVBean loadSingle(ResultSet rs) throws SQLException {
        if (rs != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ObstetricOVBean oovBean = new ObstetricOVBean();
            oovBean.setObVisitID(rs.getLong("OBVisitID"));
            oovBean.setoBhcpMID(rs.getLong("OBhcpMID"));
            oovBean.setPatientMID(rs.getLong("patientMID"));

            Timestamp visitDate = rs.getTimestamp("visitDate");
            oovBean.setVisitDate(visitDate);

            oovBean.setNumberDaysPregnant(rs.getShort("numberDaysPregnant"));
            oovBean.setBloodPressure(rs.getString("blood_pressure"));
            oovBean.setFhr(rs.getShort("fhr"));
            oovBean.setMultiplet(rs.getByte("multiplet"));
            oovBean.setLlp(rs.getByte("llp"));
            oovBean.setUltraSound(rs.getByte("ultrasound"));
            oovBean.setWeight(rs.getShort("weight"));
            return oovBean;
        }
        return null;
    }

    @Override
    public PreparedStatement loadParameters(PreparedStatement ps, ObstetricOVBean bean) throws SQLException {
        throw new IllegalStateException("unimplemented exception");
    }

    //NATHAN: the below is needed to handle the parse errors that occur when one enters non integers values into the fields.
    //Could be refactored using the ValidationFormat.java, but I don't think it's worth the time.

    public static ObstetricOVBean beanBuilder(Map<String, String[]> map) throws FormValidationException{
        ObstetricOVBean bean = null;

        if (map!=null && !map.isEmpty()) {

            bean = new ObstetricOVBean();
            errors.getMessageList().clear();

            String numDaysPreg = map.get("numDaysPreg")[0];
            String fhr = map.get("fhr")[0];
            String multiple = map.get("multiple")[0];
            String weight = map.get("weight")[0];

            addNumDaysPreg(bean, numDaysPreg);
            addFhr(bean, fhr);
            addMultiple(bean, multiple);
            addWeight(bean, weight);

            if (errors.hasErrors())
                throw new FormValidationException(errors);
        }
        return bean;
    }

    private static void addWeight(ObstetricOVBean bean, String weight) {
        try{
            Short theWeight = (weight!=null && !weight.isEmpty())?Short.parseShort(weight):-1;
            bean.setWeight(theWeight);
        }catch (Exception e){
            errors.addIfNotNull("Weight: Should be an integer greater than 0");
        }
    }

    private static void addMultiple(ObstetricOVBean bean, String multiple) {
        try{
            Byte theMultiple = multiple!=null && !multiple.isEmpty()?Byte.parseByte(multiple):-1;
            bean.setMultiplet(theMultiple);
        }catch (Exception e){
            errors.addIfNotNull("Multiplet: Should be an integer greater than 0");
        }
    }

    private static void addFhr(ObstetricOVBean bean, String fhr) {
        try{
            Short theFHR = fhr!=null && !fhr.isEmpty()?Short.parseShort(fhr):-1;
            bean.setFhr(theFHR);
        }catch (Exception e){
            errors.addIfNotNull("Fetal Heart Rate: Should be an integer greater than 0");
        }
    }

    private static void addNumDaysPreg(ObstetricOVBean bean, String numDaysPreg) {
        try{
            Short theDays = numDaysPreg!=null && !numDaysPreg.isEmpty()?Short.parseShort(numDaysPreg):-1;
            bean.setNumberDaysPregnant(theDays);
        }catch (Exception e){
            errors.addIfNotNull("Number Of Days Pregnant: Should be an integer greater than 0");
        }
    }



}
