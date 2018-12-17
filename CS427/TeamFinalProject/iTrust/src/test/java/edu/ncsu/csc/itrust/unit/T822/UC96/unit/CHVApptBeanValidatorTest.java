package edu.ncsu.csc.itrust.unit.T822.UC96.unit;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.CHVApptBean;
import edu.ncsu.csc.itrust.model.old.validate.CHVApptBeanValidator;
import junit.framework.TestCase;

public class CHVApptBeanValidatorTest extends TestCase {
    CHVApptBeanValidator validator = new CHVApptBeanValidator();
    CHVApptBean bean = new CHVApptBean();



    public void testValidateNullBean () {

        bean.setComment(null);

        try{
            validator.validate(bean);
        } catch (FormValidationException e){
            fail();
        }

    }

    public void testValidateGoodBean(){
        bean.setComment("This is a comment");

        try{
            validator.validate(bean);
        } catch (FormValidationException e){
            fail();
        }
    }

    public void testValidateBadBean(){
        bean.setComment("This is a comment@#$%#%$^#");

        try{
            validator.validate(bean);
            fail();
        } catch (FormValidationException e){
            assertTrue(e.getMessage().contains("[Appointment Comment: Between 0 and 1000 alphanumerics with space, and other punctuation]"));
        }
    }
}
