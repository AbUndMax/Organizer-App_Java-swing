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

        // if appointment is part of a year already existing in the Map, check if month already has appointments
        if (AppointmentMap.containsKey(year)) {

            // if month already has appointments, check if day already has appointments
            if (AppointmentMap.get(year)[monthIndex] != null) {

                // if day has already appointments (i.e. has a TreeSet), add appointment to the TreeSet
                if (AppointmentMap.get(year)[monthIndex][dayIndex] != null) {
                    AppointmentMap.get(year)[monthIndex][dayIndex].add(appointment);

                }// if day has no appointments:
                else {// make a new TreeSet and place it accordingly into the matrix
                    TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
                    appointments.add(appointment);
                    AppointmentMap.get(year)[monthIndex][dayIndex] = appointments;
                }

            }// if month doesn't have any appointments:
            else { //make a new TreeSet array and add it to the Month
                TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
                TreeSet<Appointment>[] treeSetArray = new TreeSet[month.length(year.isLeap())];

                appointments.add(appointment);
                treeSetArray[dayIndex] = appointments;

                AppointmentMap.get(year)[monthIndex] = treeSetArray;
            }

        }// if the year doesn't exist in the map:
        else { //make a new matrix in the form of [months][days] add a new TreeSet with appointment
            TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
            TreeSet<Appointment>[][] AppointmentMatrix = new TreeSet[12][month.length(year.isLeap())];

            appointments.add(appointment);
            AppointmentMatrix[monthIndex][dayIndex] = appointments;
            AppointmentMap.put(year, AppointmentMatrix);
        }
    }

    public HashMap<Year, TreeSet<Appointment>[][]> getAppointmentMap() {
        return AppointmentMap;
    }

    public TreeSet<Appointment>[] getAppointmentsOfMonth(Year year, Month month) {
        int monthIndex = month.getValue() - 1;
        return AppointmentMap.get(year)[monthIndex];
    }
}
