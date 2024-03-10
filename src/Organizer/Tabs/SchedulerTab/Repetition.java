package Organizer.Tabs.SchedulerTab;

public enum Repetition {
    NONE,
    DAILY,
    WEEKLY,
    YEARLY;

    public boolean hasRepetition() {
        return (this != NONE);
    }
}
