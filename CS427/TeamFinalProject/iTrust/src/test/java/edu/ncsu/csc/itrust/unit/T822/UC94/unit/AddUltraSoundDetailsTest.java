package edu.ncsu.csc.itrust.unit.T822.UC94.unit;

import edu.ncsu.csc.itrust.action.AddUltraSoundDetails;
import edu.ncsu.csc.itrust.action.ObstetAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.UltraSoundBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;

import java.io.File;
import java.sql.Timestamp;

// FileName: AddUltraSoundDetailsTest.java

public class AddUltraSoundDetailsTest extends TestCase{

	private ObstetAction action;
	private DAOFactory factory;
	private long hcpId = 9000000012L;
	TestDataGenerator gen = new TestDataGenerator();
	
	@Override
	protected void setUp() throws Exception {
		gen.uc94();
		this.factory = TestDAOFactory.getTestInstance();
	}

	@Override
	protected void tearDown() throws Exception {
		gen.uc94Clean();
	}

	public void testGetPatientExistingUltraSoundRecord() throws FormValidationException, ITrustException{
    	String path = ".\\WebRoot\\image\\ultrasound";
    	File picture = new File(path + "\\1.png");

    	long currentTimeToMilliseconds = System.currentTimeMillis();
    	long currentTimeToSeconds = currentTimeToMilliseconds - (currentTimeToMilliseconds % 1000);

    	Timestamp nTimeStamp = new Timestamp( currentTimeToSeconds );
    	UltraSoundBean bean = new UltraSoundBean(2,402,nTimeStamp,2,
    			10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00);
    	AddUltraSoundDetails input = new AddUltraSoundDetails(this.factory, "402", this.hcpId);
    	input.addOrUpdateUltraSoundRecord(bean);
    	bean.setImage(picture);
    	bean.setImageLocation(path);
    	input.addOrUpdateUltraSoundRecord(bean);

    	Long patientIdCheck = 402L;
    	Long officeVisitCheck = 2L;
    	UltraSoundBean bean2 = input.getPatientExistingUltraSoundRecord(patientIdCheck, officeVisitCheck);
    	// The input bean contains a file object and the database does not. All elements except file object need to be compared
    	assertEquals(bean.getPatientMid(),bean2.getPatientMid());
    	assertEquals(bean.getCreateDate(),bean2.getCreateDate());
    	assertEquals(bean.getObstetricOfficeVisitId(),bean2.getObstetricOfficeVisitId());
    	assertEquals(bean.getImageLocation(),bean2.getImageLocation());
    	assertEquals(bean.getCrownRumpLength(),bean2.getCrownRumpLength());
    	assertEquals(bean.getBiparietalDiameter(),bean2.getBiparietalDiameter());
    	assertEquals(bean.getHeadCircumference(),bean2.getHeadCircumference());
    	assertEquals(bean.getFemurLength(),bean2.getFemurLength());
    	assertEquals(bean.getOccipitofrontalDiameter(),bean2.getOccipitofrontalDiameter());
    	assertEquals(bean.getAbdominalCircumference(),bean2.getAbdominalCircumference());
    	assertEquals(bean.getHumerusLength(),bean2.getHumerusLength());
    	assertEquals(bean.getEstimatedFetalWeight(),bean2.getEstimatedFetalWeight());
    	
    }

    public void testGetPatientExistingUltraSoundRecordNullPID() throws ITrustException, FormValidationException{
		AddUltraSoundDetails input = new AddUltraSoundDetails(this.factory, "402", this.hcpId);

		try{
			UltraSoundBean bean2 = input.getPatientExistingUltraSoundRecord(null, null);
		} catch (FormValidationException e){
			assertEquals(e.getMessage(), "This form has not been validated correctly. The following fields are not properly filled in: \n" +
					"[patient id or hcp id cannot be empty/null]");
		}
	}
   
}
