package Organizer.Tabs.SchedulerTab;

public enum Repetition {
    NONE,
    DAILY,
    WEEKLY,
    YEARLY;

    public boolean isRepeating() {
        return (this != NONE);
    }

    public boolean isDaily() {
        return (this == DAILY);
    }

    public boolean isWeekly() {
        return (this == WEEKLY);
    }

    public boolean isYearly() {
        return (this == YEARLY);
    }

    public static Repetition[] isRepeatingValues(){
        Repetition[] array = {DAILY, WEEKLY, YEARLY};
        return array;
    }

    public static Repetition StringToRepetition(String string) {
        switch (string) {
            case "NONE":
                return NONE;
            case "DAILY":
                return DAILY;
            case "WEEKLY":
                return WEEKLY;
            case "YEARLY":
                return YEARLY;
            default:
                throw new IllegalArgumentException("given String is no Repetition");
        }
    }
}
