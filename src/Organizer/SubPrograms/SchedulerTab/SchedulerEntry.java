package Organizer.SubPrograms.SchedulerTab;

import java.time.LocalDate;
import java.time.LocalTime;

public class SchedulerEntry {

    private final int id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Repetition repetition;
    private int numberOfRepetition;
    private String description;

    public SchedulerEntry(int id, String title, LocalDate startDate, LocalTime startTime) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
    }

    // getters and setters:

    public int getId() {
        return id;
    }

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
