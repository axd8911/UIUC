package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.sql.Date;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetPriorPregBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;

public class ObstetBeanLoader implements BeanLoader<ObstetRecBean> {

	@Override
	public List<ObstetRecBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetRecBean> list = new ArrayList<ObstetRecBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetRecBean bean) throws SQLException {
		ps.setLong(1, bean.getMID());
		ps.setDate(2, new java.sql.Date(bean.getLmpDate().getTime()));
		ps.setDate(3, new java.sql.Date(bean.getInitDate().getTime()));
		return ps;
	}
	
	@Override
	public ObstetRecBean loadSingle(ResultSet rs) throws SQLException {
		ObstetRecBean bean = new ObstetRecBean();
		bean.setObstetRecID(rs.getInt("obstetrec_id"));
		bean.setMID(rs.getInt("patientMID"));
		java.util.Date lmpDate = new java.util.Date(rs.getDate("lmp_date").getTime());
		java.util.Date initDate = new java.util.Date(rs.getDate("init_date").getTime());
		
		bean.setLmpDate(lmpDate);
		bean.setInitDate(initDate);
		
		bean = calculateDates(bean, lmpDate, initDate);
		return bean;
	}
	
	public ObstetRecBean calculateDates(ObstetRecBean bean, java.util.Date lmpDate, java.util.Date initDate) {
	
		Calendar c1 = Calendar.getInstance();
		c1.setTime(lmpDate);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(initDate);
		
		long milliseconds1 = c1.getTimeInMillis();
		long milliseconds2 = c2.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		
		long dayCount = (long) diff / (24 * 60 * 60 * 1000);
		int weeks = Long.valueOf(dayCount).intValue() / 7;
		bean.setPregWeeks(weeks);
		int days = Long.valueOf(dayCount).intValue() - weeks * 7;
		bean.setPregDays(days);
		
		// Calculate EDD
		c1.add(Calendar.DATE, 280);  // number of days to add
		//bean.setEddDate(sdf.format(c1.getTime()));
		bean.setEddDate(c1.getTime());
		
		return bean;
	}

	public ObstetRecBean calculateDates(ObstetRecBean bean) {

		return calculateDates(bean, bean.getLmpDate(), bean.getInitDate());
	}

	public static ObstetRecBean beanBuilder(Map<String, String[]> map) throws FormValidationException {
		ObstetRecBean bean = new ObstetRecBean();
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//
			if (!sdf.format(sdf.parse(map.get("lmpDate")[0])).equals(map.get("lmpDate")[0])){
				throw new Exception();
			}
			bean.setLmpDate(sdf.parse(map.get("lmpDate")[0]));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FormValidationException(e.getMessage());
		}
		
		return bean;
	}


}
