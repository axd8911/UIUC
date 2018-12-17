package edu.ncsu.csc.itrust.unit.T822.UC94.unit;

import edu.ncsu.csc.itrust.util.GoogleCalendarUtil;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GoogleCalendarUtilTest extends TestCase {

    private GoogleCalendarUtil util = null;
    private Map<String,String> inputMap = new HashMap<>();

    @Override
    protected void setUp() throws Exception {
        util = new GoogleCalendarUtil();
    }

    @Test //"Adding a new event to the google calendar"
    public void testAddEventToGoogleCalendar() {
        try {
            inputMap.put("summary","testing google calendar");
            inputMap.put("location","google calendar");
            inputMap.put("description","google calendar testing");
            inputMap.put("patient_email","itrustcommon@gmail.com");

            java.util.Calendar utilCalendar1 = java.util.Calendar.getInstance();
            utilCalendar1.add(java.util.Calendar.HOUR, 1);
            java.util.Calendar utilCalendar2 = java.util.Calendar.getInstance();
            utilCalendar2.add(java.util.Calendar.HOUR, 2);

            Map<String, String> eventInputMap = new HashMap<>();
            eventInputMap.put("patientAptStartTime", utilCalendar1.getTimeInMillis() + "");
            eventInputMap.put("patientAptEndTime", utilCalendar2.getTimeInMillis() + "");
            util.createNewEvent(inputMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
