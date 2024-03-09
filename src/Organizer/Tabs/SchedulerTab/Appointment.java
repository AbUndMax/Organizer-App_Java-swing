package Organizer.Tabs.SchedulerTab;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    // the title of the appointment
    private String title;

    // date of the appointment
    private LocalDate startDate;

    private LocalDate endDate;

    // time of the appointment
    private LocalTime startTime;

    private LocalTime endTime;

    // description provided by the user for the appointment
    private String description;


    // getters and setters:

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
