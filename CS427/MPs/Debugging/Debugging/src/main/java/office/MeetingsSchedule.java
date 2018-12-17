package office;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.*;

/**
 * User: sameer
 * Date: 16/05/2013
 * Time: 11:01
 */
public class MeetingsSchedule {

    private LocalTime officeStartTime;

    private LocalTime officeFinishTime;

    private SortedMap<LocalDate, SortedSet<Meeting>> meetings;

    public MeetingsSchedule(LocalTime officeStartTime, LocalTime officeFinishTime, SortedMap<LocalDate, SortedSet<Meeting>> meetings) {
        this.officeStartTime = officeStartTime;
        this.officeFinishTime = officeFinishTime;
        this.meetings = meetings;
    }

    public LocalTime getOfficeStartTime() {
        return officeStartTime;
    }

    public LocalTime getOfficeFinishTime() {
        return officeFinishTime;
    }

    public Map<LocalDate, SortedSet<Meeting>> getMeetings() {
        return meetings;
    }

}
