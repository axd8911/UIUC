package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.UltraSoundBean;

public class AddUltraSoundValidator extends BeanValidator<UltraSoundBean> {

    @Override
    public void validate(UltraSoundBean bean) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        errorList.addIfNotNull(checkFormat("Abdominal Circumference", bean.getAbdominalCircumference(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Biparietal Diameter", bean.getBiparietalDiameter(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Crown Rump Length", bean.getCrownRumpLength(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Estimated Fetal Weight", bean.getEstimatedFetalWeight(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Femur Length", bean.getFemurLength(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Head Circumference", bean.getHeadCircumference(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Humerus Circumference", bean.getHumerusLength(), ValidationFormat.HeadCircumference, true));
        errorList.addIfNotNull(checkFormat("Abdominal Circumference", bean.getOccipitofrontalDiameter(), ValidationFormat.HeadCircumference, true));
    }
}
