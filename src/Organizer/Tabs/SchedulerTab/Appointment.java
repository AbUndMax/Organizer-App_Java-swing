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

    private Repetition repetition;

    private int numberOfRepetition;

    // description provided by the user for the appointment
    private String description;

    public Appointment() {
    }

    public Appointment(String title, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                       Repetition repetition, int numberOfRepetition, String description) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repetition = repetition;
        this.numberOfRepetition = numberOfRepetition;
        this.description = description;
    }


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

    public Repetition getRepetition() {
        return repetition;
    }

    public void setRepetition(Repetition repetition) {
        this.repetition = repetition;
    }

    public int getNumberOfRepetition() {
        return numberOfRepetition;
    }

    public void setNumberOfRepetition(int numberOfRepetition) {
        this.numberOfRepetition = numberOfRepetition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // this method is used by the JTree Model to get the Appointment-Title
    @Override
    public String toString(){
        return startTime + "' " + title;
    }
}
