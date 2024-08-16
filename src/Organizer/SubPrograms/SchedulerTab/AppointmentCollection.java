package Organizer.SubPrograms.SchedulerTab;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

public class AppointmentCollection {

    /*
    This Map holds all Appointments.
    As key, the years are used. The value is a matrix in shape of [month][day] with a TreeSet as output.
    The TreeSet is used to store all Appointments of one day sorted by their startTime.
     */
    private final HashMap<Year, TreeSet<Appointment>[][]> appointmentMap = new HashMap<>();

    private final AppointmentIO appointmentIO = new AppointmentIO(this);

    private void addAppointmentToMap(Appointment appointment, LinkedList<LocalDate> occurrences) {
        //add all occurrences in the List to the Map
        for (LocalDate occurrence : occurrences) {
            Year year = Year.of(occurrence.getYear());
            Month month = occurrence.getMonth();
            int dayValue = occurrence.getDayOfMonth();
            int monthIndex = month.getValue() - 1;
            int dayIndex = dayValue - 1;

            // if the year-key doesn't exist, make a completly new year and TreeSet<Appointment>[month][day] matrix
            if (!appointmentMap.containsKey(year)) {
                TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
                TreeSet<Appointment>[][] AppointmentMatrix = new TreeSet[12][31];

                AppointmentMatrix[monthIndex][dayIndex] = appointments;
                appointmentMap.put(year, AppointmentMatrix);
            }
            // if the year exists, but the month is null, make a new TreeSet<Appointment>[day] array for this month
            else if (appointmentMap.get(year)[monthIndex] == null) {
                TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
                TreeSet<Appointment>[] treeSetArray = new TreeSet[month.length(year.isLeap())];

                treeSetArray[dayIndex] = appointments;
                appointmentMap.get(year)[monthIndex] = treeSetArray;
            }
            // if year and month exist and only the day is null, make a new TreeSet<Appointment>
            else if (appointmentMap.get(year)[monthIndex][dayIndex] == null) {
                TreeSet<Appointment> appointments = new TreeSet<>(Comparator.comparing(Appointment::getStartTime));
                appointmentMap.get(year)[monthIndex][dayIndex] = appointments;
            }
            // finally, year, month and day exist (i.e. TreeSet<Appointment>[month][day]), add the appointment
            appointmentMap.get(year)[monthIndex][dayIndex].add(appointment);
        }
    }

    private LinkedList<LocalDate> getAllOccurrences(Appointment appointment) {
        LinkedList<LocalDate> occurrences = new LinkedList<>();

        LocalDate startDate = appointment.getStartDate();
        LocalDate endDate = appointment.getEndDate();
        occurrences.add(startDate);

        LocalDate nextDate = startDate;

        // add all occurrences between start and end date:
        if (!startDate.isEqual(endDate)) {
            do {
                nextDate = nextDate.plusDays(1);
                occurrences.add(nextDate);
            } while (!nextDate.equals(endDate));
        }

        // add all repetition dates to the occurrence List
        Repetition repetition = appointment.getRepetition();
        int numberOfRepetitions = appointment.getNumberOfRepetition();
        nextDate = startDate;

        if (repetition.isDaily()) {
            for (int i = numberOfRepetitions; i != 0; i-- ) {
                nextDate = nextDate.plusDays(1);
                occurrences.add(nextDate);
            }
        }
        else if (repetition.isWeekly()) {
            for (int i = numberOfRepetitions; i != 0; i-- ) {
                nextDate = nextDate.plusMonths(1);
                occurrences.add(nextDate);
            }
        }
        else if (repetition.isYearly()) {
            for (int i = numberOfRepetitions; i != 0; i-- ) {
                nextDate = nextDate.plusYears(1);
                occurrences.add(nextDate);
            }
        }

        return occurrences;
    }

    //this class is used to load an appointment from the file to the map 8IT DOESNT CALL APPOINTMENTIO)
    public void loadAppointment(Appointment appointment) {
        addAppointmentToMap(appointment, getAllOccurrences(appointment));
    }

    public void addAppointment(Appointment appointment) {
        addAppointmentToMap(appointment, getAllOccurrences(appointment));
        // write new Appointment to File
        appointmentIO.writeAppointmentToFile(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        LinkedList<LocalDate> occurrences = getAllOccurrences(appointment);

        for (LocalDate occurrence : occurrences) {
            Year occurrenceYear = Year.of(occurrence.getYear());
            int occurrenceMonthIndex = occurrence.getMonth().getValue() - 1;
            int occurrenceDayIndex = occurrence.getDayOfMonth() - 1;

            TreeSet<Appointment> appointmentsOfDay =
                    appointmentMap.get(occurrenceYear)[occurrenceMonthIndex][occurrenceDayIndex];
            appointmentsOfDay.remove(appointment);

            if (appointmentsOfDay.isEmpty()) {
                appointmentMap.get(occurrenceYear)[occurrenceMonthIndex][occurrenceDayIndex] = null;
            }
        }

        appointmentIO.deleteAppointmentFile(appointment);
    }

    public void actualizeAppointment(Appointment oldAppointment, Appointment actualizedAppointment) {
        // delete old appointment in map
        deleteAppointment(oldAppointment);
        // add new appointment to map
        addAppointmentToMap(actualizedAppointment, getAllOccurrences(actualizedAppointment));
        // actualize AppointmentFile
        appointmentIO.actualizeAppointmentFile(oldAppointment, actualizedAppointment);
    }

    public HashMap<Year, TreeSet<Appointment>[][]> getAppointmentMap() {
        return appointmentMap;
    }

    public TreeSet<Appointment>[] getAppointmentsOfMonth(Year year, Month month) {
        int monthIndex = month.getValue() - 1;

        if (!appointmentMap.containsKey(year)) appointmentMap.put(year, new TreeSet[12][31]);
        return appointmentMap.get(year)[monthIndex];
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

    //DEBUG method:
    public void printAppointments() {
        System.out.println("<<<<<<<< current collection >>>>>>>>");

        for (Year yearKey : appointmentMap.keySet()) {
            System.out.println("### Year: " + yearKey);

            for (int m = 0; m < 12; m++) {
                TreeSet<Appointment>[][] yearArray = appointmentMap.get(yearKey);
                System.out.println("## month: " + Month.of(m + 1));
                System.out.println(Arrays.toString(yearArray[m]));

                for (int d = 0; d < Month.of(m + 1).length(yearKey.isLeap()); d++) {
                    if (yearArray[m][d] != null) {
                        System.out.println("# day: " + (d + 1));
                        System.out.println(yearArray[m][d]);
                    }
                }
            }
        }

        System.out.println(">>>>>> END OF COLLECTION <<<<<<");
    }
}
