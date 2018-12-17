package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetRecBean;
import edu.ncsu.csc.itrust.model.old.beans.PostnatalCareRecordBean;

public class PostnatalCareBeanLoader implements BeanLoader<PostnatalCareRecordBean> {

	/**
	 * Load the whole list of postnatal care records
	 * 
	 * @return the list of records
	 */
	@Override
	public List<PostnatalCareRecordBean> loadList(ResultSet rs) throws SQLException {
		List<PostnatalCareRecordBean> list = new ArrayList<PostnatalCareRecordBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;

	}

	/**
	 * Helper method to load the parameters for sql
	 * 
	 * @return a preared statement for sql execution
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, PostnatalCareRecordBean bean) throws SQLException {
		ps.setLong(1, bean.getMid());
		ps.setDate(2, new java.sql.Date(bean.getRecordDate().getTime()));
		ps.setDate(3, new java.sql.Date(bean.getChildbirthDate().getTime()));
		ps.setString(4, bean.getDeliveryType());
		ps.setString(5, bean.getAdditionalComment());
		if (bean.getReleaseDate() == null) {
			ps.setDate(6, null);
		} else {
			ps.setDate(6, new java.sql.Date(bean.getReleaseDate().getTime()));
		}

		return ps;
	}

	/**
	 * Load a single result for postnatal care record
	 * 
	 * @return postnatal record bean
	 */
	@Override
	public PostnatalCareRecordBean loadSingle(ResultSet rs) throws SQLException {
		PostnatalCareRecordBean bean = new PostnatalCareRecordBean();

		bean.setPostnatalCareRecordId(rs.getInt("postnatal_care_record_id"));
		bean.setMid(rs.getInt("patient_mid"));
		bean.setDeliveryType(rs.getString("delivery_type"));
		bean.setAdditionalComment(rs.getString("comment"));
		// bean.setFirstName(rs.getString(""));

		java.util.Date recordDate = new java.util.Date(rs.getDate("record_date").getTime());
		java.util.Date childbirthDate = new java.util.Date(rs.getDate("childbirth_date").getTime());
		if (rs.getDate("release_date") != null) {
			java.util.Date releaseDate = new java.util.Date(rs.getDate("release_date").getTime());
			bean.setReleaseDate(releaseDate);
		}
		bean.setRecordDate(recordDate);
		bean.setChildbirthDate(childbirthDate);

		return bean;
	}

	/**
	 * Get the parameters from the web page to create a bean
	 * 
	 * @param map
	 * @return postnatal record bean
	 * @throws FormValidationException
	 */
	public static PostnatalCareRecordBean beanBuilder(Map<String, String[]> map) throws FormValidationException {
		PostnatalCareRecordBean bean = new PostnatalCareRecordBean();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			bean.setRecordDate(sdf.parse(map.get("record_date")[0]));
			bean.setChildbirthDate(sdf.parse(map.get("childbirth_date")[0]));
			bean.setDeliveryType(map.get("delivery_type")[0]);
			bean.setAdditionalComment(map.get("comment")[0]);
			if (map.get("release_date")[0] != null && map.get("release_date")[0] != "") {
				bean.setReleaseDate(sdf.parse(map.get("release_date")[0]));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FormValidationException(e.getMessage());
		}

		return bean;
	}

}
