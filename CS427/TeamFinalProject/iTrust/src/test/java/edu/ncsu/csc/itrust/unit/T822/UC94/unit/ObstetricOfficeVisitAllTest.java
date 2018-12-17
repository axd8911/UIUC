package edu.ncsu.csc.itrust.unit.T822.UC94.unit;

import edu.ncsu.csc.itrust.action.SearchUsersAction;
import edu.ncsu.csc.itrust.action.ViewObstetricOfficeVisitsAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricOVBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("Should pass ObstetricOfficeVisitAllTest's using the method parameters provided by the " +
        "data provider() method")
public class ObstetricOfficeVisitAllTest {

    private DAOFactory factory = TestDAOFactory.getTestInstance();
    private TransactionLogger transactionLogger = TransactionLogger.getInstance();

    @BeforeAll
    static void setup() throws Exception{
        TestDataGenerator gen = new TestDataGenerator();
        gen.uc94DataGen(); // generate only what is required for the tests.
    }

    @AfterAll
    static void tearDown() throws Exception {
        TestDataGenerator gen = new TestDataGenerator();
        gen.uc94ClearTables(); // clear tables of what was generated for the tests.
    }

    /*######################################################################################*/
    /**********************************Argument providers************************************/
    /*######################################################################################*/

    static class InvalidArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(null,null),
                    Arguments.of("    ",null),
                    Arguments.of(null,""),
                    Arguments.of("    ","    ")
            );
        }
    }

    static class OBGynHCPArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(402L, 9000000012L),
                    Arguments.of(401L, 9000000012L)
            );
        }
    }

    static class GeneralHCPArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(402L, 9000000013L),
                    Arguments.of(401L, 9000000013L),
                    Arguments.of(100000L, 9000000013L),
                    Arguments.of(-1L, 9000000013L),
                    Arguments.of(0L,  0L),
                    Arguments.of(99999999999999L, 99999999999999L)
            );
        }
    }

    static class ValidatePatientArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(400L),
                    Arguments.of(401L),
                    Arguments.of(402L),
                    Arguments.of(403L),
                    Arguments.of(404L),
                    Arguments.of(405L)
            );
        }
    }

    static class ValidatePatientRhShotArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("itrustcommon-test1","itrustcommon-test",null,9000000012L),
                    Arguments.of("itrustcommon-test2","itrustcommon-test",null,9000000012L)
            );
        }
    }

    static class NewObstetricOfficeVisitProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

            byte shotTaken = 1;
            byte multiplePositive = 2;
            short shortPositive = 1;
            Date d = new Date();
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            d = format.parse(format.format(d));

            ObstetricOVBean obstetricOVBean1 = new ObstetricOVBean();
            obstetricOVBean1.setPatientMID(400);
            obstetricOVBean1.setoBhcpMID(9000000012L);
            obstetricOVBean1.setVisitDate(new Timestamp(d.getTime()));
            obstetricOVBean1.setBloodPressure("120/129");
            obstetricOVBean1.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean2 = new ObstetricOVBean();
            obstetricOVBean2.setPatientMID(402);
            obstetricOVBean2.setoBhcpMID(9000000012L);
            obstetricOVBean2.setVisitDate(new Timestamp(d.getTime()));
            obstetricOVBean2.setBloodPressure("1/1");
            obstetricOVBean2.setNumberDaysPregnant(shortPositive);
            obstetricOVBean2.setMultiplet(multiplePositive);
            obstetricOVBean2.setRhShotTaken(shotTaken);

            ObstetricOVBean obstetricOVBean3 = new ObstetricOVBean();
            obstetricOVBean3.setPatientMID(403);
            obstetricOVBean3.setoBhcpMID(9000000012L);
            obstetricOVBean3.setVisitDate(new Timestamp(d.getTime()));
            obstetricOVBean3.setBloodPressure("1/1");
            obstetricOVBean3.setNumberDaysPregnant(shortPositive);
            obstetricOVBean3.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean4 = new ObstetricOVBean();
            obstetricOVBean4.setPatientMID(404);
            obstetricOVBean4.setoBhcpMID(9000000012L);
            obstetricOVBean4.setVisitDate(new Timestamp(d.getTime()));
            obstetricOVBean4.setBloodPressure("1/1");
            obstetricOVBean4.setNumberDaysPregnant(shortPositive);
            obstetricOVBean4.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean5 = new ObstetricOVBean();
            obstetricOVBean5.setPatientMID(405);
            obstetricOVBean5.setoBhcpMID(9000000012L);
            obstetricOVBean5.setVisitDate(new Timestamp(d.getTime()));
            obstetricOVBean5.setBloodPressure("1/1");
            obstetricOVBean5.setNumberDaysPregnant(shortPositive);
            obstetricOVBean5.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean6 = new ObstetricOVBean();
            obstetricOVBean6.setPatientMID(406);
            obstetricOVBean6.setoBhcpMID(9000000012L);
            obstetricOVBean6.setVisitDate(new Timestamp(d.getTime()));
            obstetricOVBean6.setBloodPressure("1/1");
            obstetricOVBean6.setNumberDaysPregnant(shortPositive);
            obstetricOVBean6.setMultiplet(multiplePositive);

            return Stream.of(
                    Arguments.of(obstetricOVBean1),
                    Arguments.of(obstetricOVBean2),
                    Arguments.of(obstetricOVBean3),
                    Arguments.of(obstetricOVBean4),
                    Arguments.of(obstetricOVBean5),
                    Arguments.of(obstetricOVBean6)
            );
        }
    }

    static class InvalidObstetricOfficeVisitProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

            byte multiplePositive = 2;
            short shortPositive = 1;
            DateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = format.parse("2017-11-05 03:00:00");
            Date d2 = format.parse("2019-11-05 04:00:00");
            Date d3 = format.parse("2017-11-05 03:00:00");
            Date d4 = format.parse("3333-11-05 03:00:00");
            Date d5 = format.parse("3030-11-05 05:00:00");
            Date d6 = format.parse("9999-12-05 06:00:00");


            ObstetricOVBean obstetricOVBean1 = new ObstetricOVBean();
            obstetricOVBean1.setPatientMID(401);
            obstetricOVBean1.setoBhcpMID(9000000012L);
            obstetricOVBean1.setVisitDate(new Timestamp(d1.getTime()));
            obstetricOVBean1.setBloodPressure("120/129");
            obstetricOVBean1.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean2 = new ObstetricOVBean();
            obstetricOVBean2.setPatientMID(402);
            obstetricOVBean2.setoBhcpMID(9000000012L);
            obstetricOVBean2.setVisitDate(new Timestamp(d2.getTime()));
            obstetricOVBean2.setBloodPressure("1/1");
            obstetricOVBean2.setNumberDaysPregnant(shortPositive);
            obstetricOVBean2.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean3 = new ObstetricOVBean();
            obstetricOVBean3.setPatientMID(403);
            obstetricOVBean3.setoBhcpMID(9000000013L);
            obstetricOVBean3.setVisitDate(new Timestamp(d3.getTime()));
            obstetricOVBean3.setBloodPressure("1/1");
            obstetricOVBean3.setNumberDaysPregnant(shortPositive);
            obstetricOVBean3.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean4 = new ObstetricOVBean();
            obstetricOVBean4.setPatientMID(404);
            obstetricOVBean4.setoBhcpMID(9000000013L);
            obstetricOVBean4.setVisitDate(new Timestamp(d4.getTime()));
            obstetricOVBean4.setBloodPressure("1/1");
            obstetricOVBean4.setNumberDaysPregnant(shortPositive);
            obstetricOVBean4.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean5 = new ObstetricOVBean();
            obstetricOVBean5.setPatientMID(405);
            obstetricOVBean5.setoBhcpMID(9000000013L);
            obstetricOVBean5.setVisitDate(new Timestamp(d5.getTime()));
            obstetricOVBean5.setBloodPressure("1/1");
            obstetricOVBean5.setNumberDaysPregnant(shortPositive);
            obstetricOVBean5.setMultiplet(multiplePositive);

            ObstetricOVBean obstetricOVBean6 = new ObstetricOVBean();
            obstetricOVBean6.setPatientMID(406);
            obstetricOVBean6.setoBhcpMID(9000000013L);
            obstetricOVBean6.setVisitDate(new Timestamp(d6.getTime()));
            obstetricOVBean6.setBloodPressure("1/1");
            obstetricOVBean6.setNumberDaysPregnant(shortPositive);
            obstetricOVBean6.setMultiplet(multiplePositive);

            return Stream.of(
                    Arguments.of(obstetricOVBean1),
                    Arguments.of(obstetricOVBean2),
                    Arguments.of(obstetricOVBean3),
                    Arguments.of(obstetricOVBean4),
                    Arguments.of(obstetricOVBean5),
                    Arguments.of(obstetricOVBean6)
            );
        }
    }

    static class GoogleRecurrenceArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            LocalDate date1= LocalDate.now().minusDays(300);
            LocalDate date2= LocalDate.now().minusDays(294);
            LocalDate date3= LocalDate.now().minusDays(200);
            LocalDate date4= LocalDate.now().minusDays(100);
            LocalDate date5= LocalDate.now().minusDays(50);
            LocalDate date6= LocalDate.now().minusDays(10);
            LocalDate date7= LocalDate.now().minusDays(1);
            LocalDate date8= LocalDate.now().minusDays(0); //current day

            return Stream.of(
                    Arguments.of(402L,9000000012L,date1),
                    Arguments.of(402L,9000000012L,date2),
                    Arguments.of(402L,9000000012L,date3),
                    Arguments.of(402L,9000000012L,date4),
                    Arguments.of(402L,9000000012L,date5),
                    Arguments.of(402L,9000000012L,date6),
                    Arguments.of(402L,9000000012L,date7),
                    Arguments.of(402L,9000000012L,date8),

                    Arguments.of(404L,9000000012L,date1),
                    Arguments.of(405L,9000000012L,date2),
                    Arguments.of(406L,9000000012L,date3),
                    Arguments.of(403L,9000000012L,date4),
                    Arguments.of(401L,9000000012L,date5),
                    Arguments.of(402L,9000000012L,date6),
                    Arguments.of(403L,9000000012L,date7),
                    Arguments.of(400L,9000000012L,date8)
            );
        }
    }

    /*######################################################################################*/
    /**********************************Test Runners****************************************/
    /*######################################################################################*/

    @DisplayName("Should handle exception's for invalid arguments")
    @ParameterizedTest(name = "{index} => patientId={0}, hcpId={1}")
    @ArgumentsSource(InvalidArgumentsProvider.class)
    void testGetOBOVRecordsExceptionTesting(String patientId,String hcpId) {
        ViewObstetricOfficeVisitsAction action1 =
                new ViewObstetricOfficeVisitsAction(factory,patientId);
        Assertions.assertThrows(FormValidationException.class,
                () -> action1.getOBOVRecords());

        ViewObstetricOfficeVisitsAction action2 =
                new ViewObstetricOfficeVisitsAction(null,patientId);
        Assertions.assertThrows(ITrustException.class,
                () -> action2.getOBOVRecords());

    }

    @DisplayName("Should retrieve Obstetric office visit for the OB/GYN HCP")
    @ParameterizedTest(name = "{index} => patientId={0}, hcpId={1}")
    @ArgumentsSource(OBGynHCPArgumentProvider.class)
    void testGetOBOfficeVisitsByPidAndOBGynHcpId(long patientId, long hcpId) {
        ViewObstetricOfficeVisitsAction action =
                new ViewObstetricOfficeVisitsAction(factory,String.valueOf(patientId),hcpId);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(action.getOBOVRecords()),
                () -> Assertions.assertFalse(action.getOBOVRecords().isEmpty()),
                () -> Assertions.assertFalse(action.obstetricVisitMap.isEmpty()),
                () -> Assertions.assertFalse(action.getObstetricVisitMap().isEmpty())

        );
    }

    @DisplayName("Should not retrieve Obstetric office visit for the general HCP")
    @ParameterizedTest(name = "{index} => patientId={0}, hcpId={1}")
    @ArgumentsSource(GeneralHCPArgumentProvider.class)
    void testGetOBOfficeVisitsByPidAndGenHcpId(long patientId, long hcpId) {
        ViewObstetricOfficeVisitsAction action =
                new ViewObstetricOfficeVisitsAction(factory,String.valueOf(patientId),hcpId);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(action.getOBOVRecords()),
                () -> Assertions.assertTrue(action.getOBOVRecords().isEmpty()),
                () -> Assertions.assertTrue(action.obstetricVisitMap == null)
        );
    }

    @DisplayName("Should be able to validate all patients")
    @ParameterizedTest(name = "{index} => patientId={0}")
    @ArgumentsSource(ValidatePatientArgumentProvider.class)
    void testPatientForOfficeVisit(long patientId) {

        ViewObstetricOfficeVisitsAction action =
                new ViewObstetricOfficeVisitsAction(factory,String.valueOf(patientId));
        ObstetricOVBean ovBean = new ObstetricOVBean();
        ovBean.setPatientMID(patientId);
        ovBean.setObVisitID(1);
        try {
            boolean valStatus = action.validateOBOVPatientRecord(ovBean);
            Assertions.assertTrue(valStatus);
        } catch (Exception ex) {}
    }


    @DisplayName("Should be able to add a new obstetric office visit, edit it and remove it at " +
            "the end of process")
    @ParameterizedTest(name = "{index} => obstetricOVBean={0}")
    @ArgumentsSource(NewObstetricOfficeVisitProvider.class)
    void testObstetricOfficeVisit(ObstetricOVBean obstetricOVBean) {

        Assumptions.assumingThat(obstetricOVBean !=null,
                () -> {
                    Assertions.assertAll(
                            () -> Assertions.assertTrue(obstetricOVBean.getVisitDate()!=null),
                            () -> Assertions.assertTrue(obstetricOVBean.getPatientMID()>0),
                            () -> Assertions.assertTrue(obstetricOVBean.getoBhcpMID()>0));
                });

        ViewObstetricOfficeVisitsAction action =
                new ViewObstetricOfficeVisitsAction(factory, String.valueOf(obstetricOVBean.getPatientMID()),
                        obstetricOVBean.getoBhcpMID());
        try {
            List<ObstetricOVBean> obstetricOVBeanList = action.getOBOVRecords();

            action.addOBOVPatientRecord(obstetricOVBean);

            Assertions.assertAll(
                    () -> Assertions.assertTrue(obstetricOVBean!=null),
                    () -> Assertions.assertTrue(obstetricOVBean.getObVisitID() > 0)
            );

            obstetricOVBean.setVisitDate(new Timestamp(System.currentTimeMillis()));
            action.editOBOVPatientRecord(obstetricOVBean);

            final List<ObstetricOVBean> obstetricOVBeanList1  = action.getOBOVRecords();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(obstetricOVBeanList1),
                    () -> Assertions.assertFalse(obstetricOVBeanList1.isEmpty())
            );

            for (ObstetricOVBean ovBean :obstetricOVBeanList1) {
                if(ovBean.getObVisitID() == obstetricOVBean.getObVisitID()) {
                    Assertions.assertTrue(action.removeOBOVPatientRecord(ovBean));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @DisplayName("Should be able to handle invalid obstetric office visit dates. Assuming few " +
            "Mandatory inputs")
    @ParameterizedTest(name = "{index} => obstetricOVBean={0}")
    @ArgumentsSource(InvalidObstetricOfficeVisitProvider.class)
    void testInvalidObstetricOfficeVisit(ObstetricOVBean obstetricOVBean) {
        Assumptions.assumingThat(obstetricOVBean !=null,
                () -> {
                    Assertions.assertAll(
                            () -> Assertions.assertTrue(obstetricOVBean.getVisitDate()!=null),
                            () -> Assertions.assertTrue(obstetricOVBean.getPatientMID()>0),
                            () -> Assertions.assertTrue(obstetricOVBean.getoBhcpMID()>0));
                });

        ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(factory,
                String.valueOf(obstetricOVBean.getPatientMID()),obstetricOVBean.getoBhcpMID());

        Assertions.assertThrows(FormValidationException.class, () -> action.addOBOVPatientRecord(obstetricOVBean));

    }

    @DisplayName("Should be able to validate all patients")
    @ParameterizedTest(name = "{index} => patientId={0}, isRhShotTaken={1}")
    @ArgumentsSource(ValidatePatientRhShotArgumentProvider.class)
    void testPatientForRhShots(String patientFirstName,String patientLastName,
                                   String isRhShotTaken, long loggedInMid) {

        SearchUsersAction searchUsersAction = new SearchUsersAction(factory,loggedInMid);
        Assumptions.assumeTrue(searchUsersAction!=null);
        List<PatientBean> patients = searchUsersAction.searchForPatientsWithName(patientFirstName,
                patientLastName);
        Assumptions.assumeTrue(patients!=null && !patients.isEmpty());
        Assumptions.assumeTrue(patients.get(0).getMID()>0);

        ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(factory,
                String.valueOf(patients.get(0).getMID()),loggedInMid);
        try {

            Assertions.assertThrows(FormValidationException.class,
                    () -> action.checkIfRhShotRequired(String.valueOf(patients.get(0).getMID()),
                            isRhShotTaken));

            //invalid number
            Assertions.assertThrows(FormValidationException.class,
                    () -> action.checkIfRhShotRequired("@1212@@1--434",isRhShotTaken));

        } catch (Exception ex) {
            System.out.println("ObstetricOfficeVisitAllTest.validatePatientForRhShots : " + ex.getMessage());
        }
    }

    @DisplayName("Should be able to return or throw error for invalid inputs")
    @ParameterizedTest(name = "{index} => patientId={0}, loggedInMid={1}, localDate={2}")
    @ArgumentsSource(GoogleRecurrenceArgumentProvider.class)
    void testInvalidGoogleAppointment(long patientId,long loggedInMid,
                                                 LocalDate localDate) {
        Assumptions.assumeTrue(patientId>0 && loggedInMid>0);
        ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(factory,
                String.valueOf(patientId),loggedInMid);

        try {
            Assertions.assertFalse(action.addAppointmentToGoogleCalendar(null,null));
        } catch (Exception ex) {
            System.out.println("ObstetricOfficeVisitAllTest.validatePatientForRhShots : " + ex.getMessage());
        }

    }

    @DisplayName("Should be able to return Google Calendar Recurrence Rules")
    @ParameterizedTest(name = "{index} => patientId={0}, loggedInMid={1}, localDate={2}")
    @ArgumentsSource(GoogleRecurrenceArgumentProvider.class)
    void testGoogleCalendarRecurrenceRules(long patientId,long loggedInMid,
                                                 LocalDate localDate) {

        Assumptions.assumeTrue(patientId>0 && loggedInMid>0);
        ViewObstetricOfficeVisitsAction action = new ViewObstetricOfficeVisitsAction(factory,
                String.valueOf(patientId),loggedInMid);

        try {
            String val = action.calculateRecurrenceRule(String.valueOf(patientId),
                    Timestamp.valueOf(localDate.atStartOfDay()));
            Assumptions.assumeTrue(val!=null && !val.isEmpty() && val.contains("RRULE:FREQ"));

        } catch (Exception ex) {
            System.out.println("ObstetricOfficeVisitAllTest.validatePatientForRhShots : " + ex.getMessage());
        }
    }

}
