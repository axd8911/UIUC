package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;

public class CHVApptBeanValidator extends BeanValidator<CHVApptBean>{

	@Override
	public void validate(CHVApptBean bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		if(bean.getComment() == null)
			return;
		errorList.addIfNotNull(checkFormat("Appointment Comment", bean.getComment(), ValidationFormat.APPT_COMMENT, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
