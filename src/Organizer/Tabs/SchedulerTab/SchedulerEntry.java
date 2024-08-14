package Organizer.Tabs.SchedulerTab;

import java.time.LocalDate;
import java.time.LocalTime;

public record SchedulerEntry (Integer id, String title, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                              Repetition repetition, int numberOfRepetition, String description) {

    public SchedulerEntry {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }

    // this method is used by the JTree Model to get the Appointment-Title
    @Override
    public String toString(){
        return startTime + "' " + title;
    }
}