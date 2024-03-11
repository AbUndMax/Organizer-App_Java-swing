package Organizer.Tabs.SchedulerTab;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class AppointmentCollection {

    /*
    This Map holds all Appointments.
    As key, the years are used. The value is a matrix in shape of [month][day] with a TreeSet as output.
    The TreeSet is used to store all Appointments of one day sorted by their startTime.
     */
    private final HashMap<Year, TreeSet<Appointment>[][]> AppointmentMap = new HashMap<>();

    public void addAppointment(LocalDate date, Appointment appointment) {

        Year year = Year.of(date.getYear());
        Month month = Month.of(date.getMonth().getValue());
        int dayValue = date.getDayOfMonth();
        int monthIndex = month.getValue() - 1;
        int dayIndex = dayValue - 1;

        // if the year-key doesn't exist, make a completly new year and TreeSet<Appointment>[month][day] matrix
        if (!AppointmentMap.containsKey(year)) {
            TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
            TreeSet<Appointment>[][] AppointmentMatrix = new TreeSet[12][month.length(year.isLeap())];

            AppointmentMatrix[monthIndex][dayIndex] = appointments;
            AppointmentMap.put(year, AppointmentMatrix);
        }
        // if the year exists, but the month is null, make a new TreeSet<Appointment>[day] array for this month
        else if (AppointmentMap.get(year)[monthIndex] == null) {
            TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
            TreeSet<Appointment>[] treeSetArray = new TreeSet[month.length(year.isLeap())];

            treeSetArray[dayIndex] = appointments;
            AppointmentMap.get(year)[monthIndex] = treeSetArray;
        }
        // if year and month exist and only the day is null, make a new TreeSet<Appointment>
        else if (AppointmentMap.get(year)[monthIndex][dayIndex] == null) {
            TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
            AppointmentMap.get(year)[monthIndex][dayIndex] = appointments;
        }
        // finally year, month and day exist (i.e. TreeSet<Appointment>[month][day]), add the appointment
        AppointmentMap.get(year)[monthIndex][dayIndex].add(appointment);
    }

    public HashMap<Year, TreeSet<Appointment>[][]> getAppointmentMap() {
        return AppointmentMap;
    }

    public TreeSet<Appointment>[] getAppointmentsOfMonth(Year year, Month month) {
        int monthIndex = month.getValue() - 1;

        if (AppointmentMap.containsKey(year)) return AppointmentMap.get(year)[monthIndex];
        else return null;
    }

    // checks if a given month has any appointments
    public boolean monthHasAppointments(Year year, Month month) {
        TreeSet<Appointment>[] monthArray = getAppointmentsOfMonth(year, month);

        for (TreeSet<Appointment> day : monthArray) {
            if (day != null) return true;
        }

        return false;
    }

    // checks if a given day has any appointments
    public boolean dayHasAppointments(Year year, Month month, int dayValue) {
        int dayIndex = dayValue - 1;
        TreeSet<Appointment>[] monthArray = getAppointmentsOfMonth(year, month);

        return (monthArray[dayIndex] == null);
    }
}
