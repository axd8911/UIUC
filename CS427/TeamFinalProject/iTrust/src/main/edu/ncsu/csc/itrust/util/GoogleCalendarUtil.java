package edu.ncsu.csc.itrust.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

public class GoogleCalendarUtil {

    private static final String APPLICATION_NAME = "iTrust Office Visit Calender";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "/tokens";

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    /**
     * Helper method for Google calendar resource mapping and getting the OAUTH mapping done.
     * @param HTTP_TRANSPORT
     * @return
     * @throws IOException
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        // Load client secrets from file rather than hardcode above.
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline").build();

        LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, localServerReceiver).authorize("user");

    }

    /**
     * Print to console the event details.
     * @param items
     */
    private void printItemsToLog(List<Event> items) {
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }

    /**
     * Helper method to get google calendar events.
     * @param inputMap
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public List<Event> getCalendarEvents(Map<String, String> inputMap) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        if (inputMap != null && !inputMap.isEmpty()) {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = service.events().list(inputMap.get("user") != null ? inputMap.get("user") : "primary")
                    .setMaxResults(inputMap.get("max_results") != null ? Integer.parseInt(inputMap.get("max_results")) : 10)
                    .setTimeMin(now)
                    .setOrderBy(inputMap.get("order_by") != null ? inputMap.get("order_by") : "startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();
            printItemsToLog(items);
            return items;
        }
        return null;
    }

    /**
     * Create a new event in the google calendar API
     * @param inputMap
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public Event createNewEvent(Map<String, String> inputMap) throws IOException, GeneralSecurityException {
	if (inputMap != null && !inputMap.isEmpty()) {
	    try {

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

		Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
			.setApplicationName(APPLICATION_NAME).build();

		// basic event details. Generic location set. These are not mentioned part of the requirement.
		Event event = new Event()
			.setSummary(inputMap.get("summary") != null ? inputMap.get("summary")
				: "iTrust Obstetric Office Visit")
			.setLocation(inputMap.get("location") != null ? inputMap.get("location")
				: "800 Howard St., San Francisco, CA 94103")
			.setDescription(inputMap.get("description") != null ? inputMap.get("description")
				: "The next appointment (office visit "
					+ "or delivery visit) will be scheduled for the patient at the end of the appointment. "
					+ "Appointments are scheduled "
					+ "between 9am and 4pm, Monday - Friday");

		// event patient details
		String patientAptStartTimeTime = inputMap.get("patientAptStartTime");
		EventDateTime start = createDateAndTime(patientAptStartTimeTime);
		event.setStart(start);

		String patientAptEndTime = inputMap.get("patientAptEndTime");
		EventDateTime endTime = createDateAndTime(patientAptEndTime);
		event.setEnd(endTime);

		// frequency.
		String[] recurrence = new String[] {
			inputMap.get("recurrence_rule") != null ? inputMap.get("recurrence_rule")
				: "RRULE:FREQ=MONTHLY;COUNT=1" };
		event.setRecurrence(Arrays.asList(recurrence));

		// attendees
		EventAttendee[] attendees = new EventAttendee[] {
			new EventAttendee()
				.setEmail(inputMap.get("patient_email") != null ? inputMap.get("patient_email")
					: "itrustcommon@gmail.com"),
			new EventAttendee().setEmail("itrustcommon@gmail.com") };
		event.setAttendees(Arrays.asList(attendees));

		// reminder
		EventReminder[] reminderOverrides = new EventReminder[] {
			new EventReminder().setMethod("email").setMinutes(24 * 60),
			new EventReminder().setMethod("popup").setMinutes(10), };

		Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
			.setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);

		String calendarId = "primary";
		event = service.events().insert(calendarId, event).execute(); // insert.

		System.out.printf("Event created: %s\n", event.getHtmlLink());
	    } catch (Exception ex) {
		System.out.println(
			"GoogleCalendarUtil.createNewEvent ... Error occurred while " + "trying to add a new event");
		System.out.println(ex.getMessage());
	    }
	}
	return null;
    }

    /**
     * Helper method to add date and time.
     * @param date
     * @return
     */
    private EventDateTime createDateAndTime(String date) {
        EventDateTime eventDateTime = null;
        if (date != null) {
            DateTime endDateTime = new DateTime(Long.parseLong(date));
            eventDateTime = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Los_Angeles");
        } else {
            DateTime endDateTime = new DateTime(System.currentTimeMillis());
            eventDateTime = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Los_Angeles");
        }
        return eventDateTime;
    }

    public Event deleteExistingEvent(Event existingEvent, Map<String, String> inputs) {
        throw new UnsupportedOperationException("This operation is not yet supported");
    }

    public Event updateExistingEvent(Event existingEvent, Map<String, String> inputs) {
        throw new UnsupportedOperationException("This operation is not yet supported");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCalendarUtil util = new GoogleCalendarUtil();

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, util.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        util.printItemsToLog(items);

        java.util.Calendar utilCalendar1 = java.util.Calendar.getInstance();
        utilCalendar1.add(java.util.Calendar.HOUR, 1);

        java.util.Calendar utilCalendar2 = java.util.Calendar.getInstance();
        utilCalendar2.add(java.util.Calendar.HOUR, 2);

        Map<String, String> eventInputMap = new HashMap<>();
        eventInputMap.put("patientAptStartTime", utilCalendar1.getTimeInMillis() + "");
        eventInputMap.put("patientAptEndTime", utilCalendar2.getTimeInMillis() + "");
        util.createNewEvent(eventInputMap);
    }

}
