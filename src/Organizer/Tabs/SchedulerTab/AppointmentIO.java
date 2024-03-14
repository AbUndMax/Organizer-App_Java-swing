package Organizer.Tabs.SchedulerTab;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;

public class AppointmentIO {

    private final String pathToAppointmentFolder = "Files/SchedulerFiles";
    private final AppointmentCollection appointmentCollection;

    public AppointmentIO(AppointmentCollection appointmentCollection) {
        this.appointmentCollection = appointmentCollection;
        Year currentYear = Year.of(LocalDate.now().getYear());
        createNewYearFolder(currentYear);
        loadYear(currentYear);
    }

    // this Method loads all Appointments of one Year into the System
    public void loadYear(Year year) {

        File directory = new File(pathToAppointmentFolder + "/" + year.getValue());

        File[] files = directory.listFiles();

        for (File file : files) {
            try (BufferedReader bReader = new BufferedReader(new FileReader(file))) {

                String line;

                while ((line = bReader.readLine()) != null) {

                    // new Appointment Instance
                    Appointment appointment = new Appointment();

                    //set all Fields:

                    //read and set Title
                    line = line.substring(1);
                    appointment.setTitle(line);
                    //read and set startDate
                    line = bReader.readLine().substring(1);
                    String[] startDate = line.split("-");
                    appointment.setStartDate(getLocalDate(startDate));
                    //read and set EndDate
                    line = bReader.readLine().substring(1);
                    String[] endDate = line.split("-");
                    appointment.setEndDate(getLocalDate(endDate));
                    //read and set StartTime
                    line = bReader.readLine().substring(1);
                    String[] startTime = line.split(":");
                    appointment.setStartTime(getLocalTime(startTime));
                    //read and set EndTime
                    line = bReader.readLine().substring(1);
                    String[] endTime = line.split(":");
                    appointment.setEndTime(getLocalTime(endTime));
                    //read and set Repetition
                    line = bReader.readLine().substring(1);
                    Repetition repetition = Repetition.StringToRepetition(line);
                    appointment.setRepetition(repetition);
                    //read and set number of repetitions
                    line = bReader.readLine().substring(1);
                    int numberOfRepetition = Integer.parseInt(line);
                    appointment.setNumberOfRepetition(numberOfRepetition);
                    //read and set Description
                    line = bReader.readLine().substring(1);
                    appointment.setDescription(line);

                    appointmentCollection.addAppointment(appointment.getStartDate(), appointment);
                }
            }
            catch (Exception expt) {
                expt.printStackTrace();
            }
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
    private void createNewYearFolder(Year year) {

        File file = new File(pathToAppointmentFolder + "/" + year.getValue());

        try {
            file.mkdir();
        }
        catch (Exception expt) {
            expt.printStackTrace();
        }
    }

    public void NewAppointmentFile(Appointment appointment) {

        Year appointmentYear = Year.of(appointment.getStartDate().getYear());
        String pathToDirectory = pathToAppointmentFolder + "/" + appointmentYear;
        File directory = new File(pathToDirectory);

        if (directory.exists()) {
            writeAppointmentToFile(appointment);
        }
        else {
            createNewYearFolder(appointmentYear);
            writeAppointmentToFile(appointment);
        }
    }

    public void actualizeAppointmentFile(Appointment oldAppointment, Appointment actualizedAppointment) {
        File oldFile = new File(getAppointmentPath(oldAppointment));
        File newFile = new File(getAppointmentPath(actualizedAppointment));

        oldFile.renameTo(newFile);

        writeAppointmentToFile(actualizedAppointment);
    }

    public void deleteAppointmentFile(Appointment appointment) {
        File delFile = new File(getAppointmentPath(appointment));

        delFile.delete();
    }

    private String getAppointmentPath(Appointment appointment) {
        String part1 = appointment.getStartDate().toString();
        String part2 = appointment.getStartTime().toString();
        String part3 = appointment.getEndDate().toString();
        String part4 = appointment.getEndTime().toString();

        String appointmentFileString =  part1 + "_" + part2 + "_" + part3 + "_" + part4;

        String appointmentYear =  String.valueOf(appointment.getStartDate().getYear());

        return  pathToAppointmentFolder + "/" + appointmentYear + "/" + appointmentFileString;
    }

    private void writeAppointmentToFile(Appointment appointment) {

        File file = new File(getAppointmentPath(appointment));

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(appointment.getTitle());
            pw.write(">" + appointment.getStartDate().toString());
            pw.write(">" + appointment.getEndDate().toString());
            pw.write(">" + appointment.getStartTime().toString());
            pw.write(">" + appointment.getEndTime().toString());
            pw.write(">" + appointment.getRepetition().toString());
            pw.write(">" + appointment.getNumberOfRepetition());
            pw.write(">" + appointment.getDescription());
        }
        catch (Exception expt) {
            expt.printStackTrace();
        }
    }

}
