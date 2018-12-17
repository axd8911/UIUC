package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;

/**
 * A bean builder to populate bean from database result, or construct a database prepared statement
 * @author brycelin
 *
 */
public class CHVApptBeanLoader implements BeanLoader<CHVApptBean> {

	@Override
	public List<CHVApptBean> loadList(ResultSet rs) throws SQLException {
		List<CHVApptBean> list = new ArrayList<CHVApptBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	/**
	 * Construct a database prepared statement from bean values
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, CHVApptBean bean) throws SQLException {
		ps.setString(1, bean.getApptType());
		ps.setLong(2, bean.getPatient());
		ps.setLong(3, bean.getHcp());
		ps.setTimestamp(4, bean.getDate());
		ps.setString(5, bean.getComment());
		ps.setString(6, bean.getPreferDMethod());
		ps.setString(7, bean.getWhenScheduled());
		return ps;
	}

	/**
	 * Construct a childbirth hospital visit appointment bean from datbase search result
	 */
	@Override
	public CHVApptBean loadSingle(ResultSet rs) throws SQLException {
		CHVApptBean bean = new CHVApptBean();
		bean.setApptID(rs.getInt("appt_id"));
		bean.setApptType(rs.getString("appt_type"));
		bean.setPatient(rs.getLong("patient_id"));
		bean.setHcp(rs.getLong("doctor_id"));
		bean.setDate(rs.getTimestamp("sched_date"));
		bean.setPreferDMethod(rs.getString("prefer_dmethod"));
		bean.setWhenScheduled(rs.getString("when_scheduled"));
		
		String comment = rs.getString("comment");
		if (comment == null)
			bean.setComment("");
		else
			bean.setComment(comment);
		return bean;
	}

}
