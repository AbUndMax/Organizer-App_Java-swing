package Organizer.Tabs.SchedulerTab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;

public class AppointmentIO {

    private Path pathToAppointmentFolder = Paths.get("Files/SchedulerFiles");
    private AppointmentMap appointmentMap = new AppointmentMap();

    public AppointmentIO() {
        Year currentYear = Year.of(LocalDate.now().getYear());
        createNewYearFile(currentYear);
        loadYear(currentYear);
    }

    public AppointmentMap getAppointmentMapInstance() {
        return appointmentMap;
    }

    // this Method loads all Appointments of one Year into the System
    public void loadYear(Year year) {

        File file = new File(pathToAppointmentFolder + "Appointments_" + year.getValue());

        try (BufferedReader bReader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = bReader.readLine()) != null) {

                if (line.charAt(0) == '#') {

                    // new Appointment Instance
                    Appointment appointment = new Appointment();

                    //set all Fields:

                    //set Title
                    line = line.substring(1);
                    appointment.setTitle(line);
                    //set startDate
                    line = bReader.readLine().substring(1);
                    String[] startDate = line.split("-");
                    appointment.setStartDate(getLocalDate(startDate));
                    //setEndDate
                    line = bReader.readLine().substring(1);
                    String[] endDate = line.split("-");
                    appointment.setEndDate(getLocalDate(endDate));
                    //set StartTime
                    line = bReader.readLine().substring(1);
                    String[] startTime = line.split(":");
                    appointment.setStartTime(getLocalTime(startTime));
                    //set EndTime
                    line = bReader.readLine().substring(1);
                    String[] endTime = line.split(":");
                    appointment.setEndTime(getLocalTime(endTime));
                    //set Repetition
                    line = bReader.readLine().substring(1);
                    Repetition repetition = Repetition.repetitionOf(line);
                    appointment.setRepetition(repetition);
                    //set number of repetitions
                    line = bReader.readLine().substring(1);
                    int numberOfRepetition = Integer.parseInt(line);
                    appointment.setNumberOfRepetition(numberOfRepetition);
                    //set Description
                    line = bReader.readLine().substring(1);
                    appointment.setDescription(line);

                    appointmentMap.addAppointment(appointment.getStartDate(), appointment);

                }
            }

        }
        catch (Exception expt) {
            expt.printStackTrace();
        }
    }

    // easy StringArray to LocalDate converter helperMethod
    private LocalDate getLocalDate(String[] stringArray) {
        return (LocalDate.of(Integer.parseInt(stringArray[0]),
                             Integer.parseInt(stringArray[1]),
                             Integer.parseInt(stringArray[2])));
    }

    // easy StringArray to LocalTime converter helperMethod
    private LocalTime getLocalTime(String[] stringArray) {
        return (LocalTime.of(Integer.parseInt(stringArray[0]),
                             Integer.parseInt(stringArray[1])));
    }

    // creates a new file of the current year, in case no such file exists.
    private void createNewYearFile(Year year) {

        File file = new File(pathToAppointmentFolder + "/Appointments_" + year.getValue());

        try {
            file.createNewFile();
        }
        catch (Exception expt) {
            expt.printStackTrace();
        }
    }
}
