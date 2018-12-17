package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;

public class PriorPregBeanLoader implements BeanLoader<ObstetPriorPregBean> {
	private static ErrorList errors = new ErrorList();
//table
//obstetpriorpreg_id		INT UNSIGNED AUTO_INCREMENT primary key,
//patientMID 			BIGINT(20) UNSIGNED NOT NULL,
//conception_year     INT(4),
//pregnant_weeks		INT,
//pregnant_days		INT,
//labor_hours			FLOAT,
//weight_gain			FLOAT,
//delivery_type		varchar(30),
//multiplet			INT,

	@Override
	public List<ObstetPriorPregBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetPriorPregBean> list = new ArrayList<ObstetPriorPregBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetPriorPregBean bean) throws SQLException {
		ps.setLong(1, bean.getMid());
		ps.setInt(2, bean.getConceptionYear());
		ps.setInt(3, bean.getPregWeeks());
		ps.setInt(4,  bean.getPregDays());
		ps.setFloat(5, bean.getLaborHours());
		ps.setFloat(6, bean.getWeightGain());
		ps.setString(7, bean.getDeliveryType());
		ps.setInt(8, bean.getMultiplet());
		
		return ps;
	}
	
	@Override
	public ObstetPriorPregBean loadSingle(ResultSet rs) throws SQLException {
		ObstetPriorPregBean bean = new ObstetPriorPregBean();
		bean.setPriorPregRecID(rs.getInt("obstetpriorpreg_id"));
		bean.setMid(rs.getLong("patientMID"));
		bean.setConceptionYear(rs.getInt("conception_year"));
		bean.setPregWeeks(rs.getInt("pregnant_weeks"));
		bean.setPregDays(rs.getInt("pregnant_days"));
		bean.setLaborHours(rs.getFloat("labor_hours"));
		bean.setWeightGain(rs.getFloat("weight_gain"));
		bean.setDeliveryType(rs.getString("delivery_type"));
		bean.setMultiplet(rs.getInt("multiplet"));
		
		return bean;
	}
	
	public static ObstetPriorPregBean beanBuilder(Map<String, String[]> map) throws FormValidationException {
		ObstetPriorPregBean bean = new ObstetPriorPregBean();
		errors.getMessageList().clear();

		String yearOfConception = map.get("conceptionYear")[0];
		String weeksPreg	    = map.get("pregnant_weeks")[0];
		String daysPreg         = map.get("pregnant_days")[0];
		String hoursInLabor	    = map.get("laborHours")[0];
		String weightGained     = map.get("weightGain")[0];
		String deliveryType	    = map.get("deliveryType")[0];
		String multiplet        = map.get("multiplet")[0];

		addYearConceptionToBean(bean, yearOfConception);

		addweeksPreg( bean,  weeksPreg);

		adddaysPreg( bean,  daysPreg);

		addhoursInLabor( bean,  hoursInLabor);

		addweightGained( bean,  weightGained);

		adddeliveryType( bean, deliveryType);

		addmultiplet( bean,  multiplet);


		if (errors.hasErrors())
			throw new FormValidationException(errors);

		return bean;
	}

	private static void addYearConceptionToBean(ObstetPriorPregBean bean, String yearOfConception){
		try{
			Calendar c1 = Calendar.getInstance();
			bean.setConceptionYear(Integer.parseInt(yearOfConception));

			if (c1.getTime().getYear()+1900 < bean.getConceptionYear()){ //can't be in future
				errors.addIfNotNull("Year of Conception: Can't be a future year");
			}

			if (bean.getConceptionYear() < 1900){
				errors.addIfNotNull("Year of Conception: Past year not possible.");
			}

		} catch (Exception e){
			errors.addIfNotNull("Year of Conception: Should be integer of form YYYY");
		}
	}

	private static void addweeksPreg(ObstetPriorPregBean bean, String weeksPreg){
		try{
			bean.setPregWeeks(Integer.parseInt(weeksPreg));

			if (bean.getPregWeeks()<0 || bean.getPregWeeks() > 52){
				errors.addIfNotNull("Pregnant Weeks: Should be an integer between 0 and 52");
			}
		} catch (Exception e){
			errors.addIfNotNull("Pregnant Weeks: Should be an integer between 0 and 52");
		}
	}

	private static void adddaysPreg(ObstetPriorPregBean bean, String daysPreg){
		try{
			bean.setPregDays(Integer.parseInt(daysPreg));

			if (bean.getPregDays() < 0 || bean.getPregDays() > 6){
				errors.addIfNotNull("Days Pregnant : Should be an integer between 0 and 6");
			}
		} catch (Exception e){
			errors.addIfNotNull("Days Pregnant : Should be an integer between 0 and 6");
		}
	}

	private static void addhoursInLabor(ObstetPriorPregBean bean, String hoursInLabor){
		try{
			bean.setLaborHours(Float.parseFloat(hoursInLabor));

			if (bean.getLaborHours() < 0){
				errors.addIfNotNull("Hours in Labor: Should be a nonzero decimal");
			}
			if (bean.getLaborHours() > 1000){
				errors.addIfNotNull("Hours in Labor: Not physically possible, use smaller decimal");
			}
		} catch (Exception e){
			errors.addIfNotNull("Hours in Labor: Should be a nonzero decimal");
		}
	}

	private static void addweightGained(ObstetPriorPregBean bean, String weightGained){
		try{
			bean.setWeightGain(Float.parseFloat(weightGained));

			if (bean.getWeightGain() < 0){
				errors.addIfNotNull("Weight Gained: Should be a decimal greater than 0");
			}
			if (bean.getWeightGain() > 1000){
				errors.addIfNotNull("Weight Gained: Not physically possible, use smaller decimal");
			}
		} catch (Exception e){
			errors.addIfNotNull("Weight Gained: Should be a decimal greater than 0");
		}
	}

	private static void addmultiplet(ObstetPriorPregBean bean, String multiplet){
		try{
			bean.setMultiplet(Integer.parseInt(multiplet));

			if (bean.getMultiplet() < 1){
				errors.addIfNotNull("Multiplet: Should be an integer greater than 0");
			}

			if (bean.getMultiplet() > 50){
				errors.addIfNotNull("Multiplet: Not physically possible, use smaller integer");
			}

		} catch (Exception e){
			errors.addIfNotNull("Multiplet: Should be an integer greater than 0");
		}
	}

	private static void adddeliveryType(ObstetPriorPregBean bean, String deliveryType){
		try{
			bean.setDeliveryType(deliveryType);

			if (  !(deliveryType.equals("vaginal_delivery") ||
			        deliveryType.equals("vaginal_delivery_vacuum_assist") ||
					deliveryType.equals("vaginal_delivery_forceps_assist") ||
					deliveryType.equals("caesarean_section") ||
					deliveryType.equals("miscarriage"))){
				errors.addIfNotNull("Delivery Type: Invalid selection.");
			}
		} catch (Exception e){
			errors.addIfNotNull("Delivery Type: Invalid selection.");
		}
	}



}
