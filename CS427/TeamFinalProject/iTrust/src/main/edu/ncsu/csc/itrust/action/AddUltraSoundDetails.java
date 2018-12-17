package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.UltraSoundBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricOfficeVisitDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.AddUltraSoundValidator;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AddUltraSoundDetails {

    public DAOFactory daoFactory;
    public String patientId;
    public Long hcpId;
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    public AddUltraSoundDetails(DAOFactory daoFactory, String patientId, Long hcpId) {
        this.daoFactory = daoFactory;
        this.patientId = patientId;
        this.hcpId = hcpId;
    }

    public UltraSoundBean getPatientExistingUltraSoundRecord(Long patientId, Long officeVisitId) throws FormValidationException, ITrustException {
        ObstetricOfficeVisitDAO obstetricOfficeVisitDAO = daoFactory.getObstetricOfficeVisitDAO();
        if (obstetricOfficeVisitDAO !=null) {
            if (patientId!=null && officeVisitId != null) {
                List<UltraSoundBean> beanStalk = obstetricOfficeVisitDAO.getPatientUltraSoundRecords(patientId, officeVisitId);
                UltraSoundBean magicBean = null;
                if (beanStalk!=null && !beanStalk.isEmpty()) {
                    magicBean = beanStalk.get(0);
                }
                return magicBean;
            } else {
                throw new FormValidationException("patient id or hcp id cannot be empty/null");
            }
        }
        return null;
    }

    public UltraSoundBean addOrUpdateUltraSoundRecord(UltraSoundBean ultraSoundBean)throws FormValidationException, ITrustException {

        ObstetricOfficeVisitDAO obstetricOfficeVisitDAO = daoFactory.getObstetricOfficeVisitDAO();
        if (obstetricOfficeVisitDAO != null) {
            if (!StringUtils.isEmpty(ultraSoundBean.getImageLocation())
                && ultraSoundBean.getImage() !=null) {
                Long ultraSoundBeanId = obstetricOfficeVisitDAO.addOrUpdatePatientUltraSoundImage(ultraSoundBean);		// Wrong bean id is return than expected
                if (ultraSoundBeanId != null && ultraSoundBeanId > 0) {
                    ultraSoundBean.setUltraSoundRecordId(ultraSoundBeanId);		// Information is added to the database here
                }
            }else {
                new AddUltraSoundValidator().validate(ultraSoundBean);
                if (obstetricOfficeVisitDAO != null) {
                    Long ultraSoundBeanId = obstetricOfficeVisitDAO.addOrUpdatePatientUltraSoundReport(ultraSoundBean);
                    if (ultraSoundBeanId != null && ultraSoundBeanId > 0) {
                        ultraSoundBean.setUltraSoundRecordId(ultraSoundBeanId);
                    }
                }
            }
        }
        TransactionLogger.getInstance().logTransaction(TransactionType.ULTRASOUND_CREATE_ADD, hcpId,Long.parseLong(patientId),"Office Visit ID : "+ultraSoundBean.getObstetricOfficeVisitId());
        return ultraSoundBean;
    }
}
