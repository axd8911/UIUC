package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;

public class AddObstetricOfficeVisitValidator extends BeanValidator<ObstetricOVBean>{

    @Override
    public void validate(ObstetricOVBean bean) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        errorList.addIfNotNull(checkFormat("Blood Pressure",bean.getBloodPressure(),ValidationFormat.BLOOD_PRESSURE_OV,false));
        errorList.addIfNotNull(checkFormat("FHR",String.valueOf(bean.getFhr()),ValidationFormat.FHR,false));
        errorList.addIfNotNull(checkFormat("Multiple",String.valueOf(bean.getMultiplet()),ValidationFormat.MULTIPLE,false));
        errorList.addIfNotNull(checkFormat("Number of Days Pregnant",String.valueOf(bean.getNumberDaysPregnant()),ValidationFormat.NUMBER_OF_DAYS_PREGNANT,false));
        errorList.addIfNotNull(checkFormat("Visit Date",String.valueOf(bean.getVisitDate()),ValidationFormat.DATETIME,true));
        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }
    }
}
