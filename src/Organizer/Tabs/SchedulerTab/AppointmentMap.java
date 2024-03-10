package Organizer.Tabs.SchedulerTab;

import java.time.Month;
import java.time.Year;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class AppointmentMap {

    /*
    This Map holds all Appointments.
    As key, the years are used. The value is a matrix in shape of [month][day] with a TreeSet as output.
    The TreeSet is used to store all Appointments of one day sorted by their startTime.
     */
    private final HashMap<Year, TreeSet<Appointment>[][]> AppointmentMap = new HashMap<>();

    public void addAppointment(Year year, Month month, Integer dayValue, Appointment appointment) {

        int monthIndex = month.getValue() - 1;
        int dayIndex = dayValue - 1;

        // if appointment is part of a year alredy existing in the Map, pass it to the Matrix
        if (AppointmentMap.containsKey(year)) AppointmentMap.get(year)[monthIndex][dayIndex].add(appointment);

        // else create a new TreeSet for all appointments of one day, sorted by their start time
        // then create a new Matrix with [month][day] order.
        else {
            TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
            TreeSet<Appointment>[][] AppointmentMatrix = new TreeSet[12][31];

            appointments.add(appointment);
            AppointmentMatrix[monthIndex][dayIndex] = appointments;
            AppointmentMap.put(year, AppointmentMatrix);
        }
    }

    public HashMap<Year, TreeSet<Appointment>[][]> getAppointmentMap() {
        return AppointmentMap;
    }

    public TreeSet[] getAppointmentsOfMonth(Year year, Month month) {
        int monthIndex = month.getValue() - 1;
        return AppointmentMap.get(year)[monthIndex];
    }
}
