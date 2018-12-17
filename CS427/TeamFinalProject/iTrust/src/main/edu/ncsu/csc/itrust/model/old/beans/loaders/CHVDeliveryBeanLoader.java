package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.CHVDeliveryBean;

public class CHVDeliveryBeanLoader implements BeanLoader<CHVDeliveryBean> {

	@Override
	public List<CHVDeliveryBean> loadList(ResultSet rs) throws SQLException {
		List<CHVDeliveryBean> list = new ArrayList<CHVDeliveryBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, CHVDeliveryBean bean) throws SQLException {
		ps.setLong(1, bean.getPatient());
		ps.setLong(2, bean.getHcp());
		ps.setTimestamp(3, bean.getDate());
		ps.setString(4, bean.getDeliveryMethod());
		ps.setInt(5,  bean.getDosPitocin());
		ps.setInt(6,  bean.getDosNitrous());
		ps.setInt(7,  bean.getDosPethidine());
		ps.setInt(8,  bean.getDosEpidural());
		ps.setInt(9,  bean.getDosMagnesium());
		ps .setInt(10,  bean.getDosRHimmune());
		return ps;
	}

	@Override
	public CHVDeliveryBean loadSingle(ResultSet rs) throws SQLException {
		CHVDeliveryBean bean = new CHVDeliveryBean();
		bean.setDeliveryID(rs.getInt("delivery_id"));
		bean.setPatient(rs.getLong("patient_id"));
		bean.setHcp(rs.getLong("doctor_id"));
		bean.setDate(rs.getTimestamp("delivery_date"));
		bean.setDeliveryMethod(rs.getString("delivery_method"));
		bean.setDosPitocin(rs.getInt("dosPitocin"));
		bean.setDosNitrous(rs.getInt("dosNitrous"));
		bean.setDosPethidine(rs.getInt("dosPethidine"));
		bean.setDosEpidural(rs.getInt("dosEpidural"));
		bean.setDosMagnesium(rs.getInt("dosMagnesium"));
		bean.setDosRHimmune(rs.getInt("dosRHimmune"));
		
		return bean;
	}

}
