package office;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * User: sameer
 * Date: 16/05/2013
 * Time: 14:09
 */
public class MeetingSchedulePrinter {

    private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

    private MeetingScheduler meetingScheduler;

    public MeetingSchedulePrinter(MeetingScheduler meetingScheduler) {
        this.meetingScheduler = meetingScheduler;
    }

    public String print(String meetingRequest) {
        MeetingsSchedule meetingsScheduleBooked = meetingScheduler.schedule(meetingRequest);

        return buildMeetingScheduleString(meetingsScheduleBooked);

    }

    private String buildMeetingScheduleString(MeetingsSchedule meetingsScheduleBooked) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, SortedSet<Meeting>> meetingEntry : meetingsScheduleBooked.getMeetings().entrySet()) {
            LocalDate meetingDate = meetingEntry.getKey();
            sb.append(dateFormatter.print(meetingDate)).append("\n");
            SortedSet<Meeting> meetings = meetingEntry.getValue();
            for (Meeting meeting : meetings) {
                sb.append(timeFormatter.print(meeting.getStartTime())).append(" ");
                sb.append(timeFormatter.print(meeting.getFinishTime())).append(" ");
                sb.append(meeting.getEmployeeId()).append("\n");
            }

        }
        return sb.toString();
    }
}
