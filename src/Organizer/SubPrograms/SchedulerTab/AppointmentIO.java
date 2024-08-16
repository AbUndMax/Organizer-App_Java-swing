package Organizer.SubPrograms.SchedulerTab;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;

public class AppointmentIO {

    private final String pathToAppointmentFolder = "Files/SchedulerFiles";
    private final AppointmentCollection appointmentCollection;

    public AppointmentIO(AppointmentCollection appointmentCollection) {
        this.appointmentCollection = appointmentCollection;
        loadAppointmentsIntoSystem();
    }

    // this Method loads all Appointments of one Year into the System
    public void loadAppointmentsIntoSystem() {

        File directory = new File(pathToAppointmentFolder);

        File[] files = directory.listFiles();

        for (File file : files) {
            try (BufferedReader bReader = new BufferedReader(new FileReader(file))) {

                String line = bReader.readLine();

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
                Repetition repetition = Repetition.parse(line);
                appointment.setRepetition(repetition);
                //read and set number of repetitions
                line = bReader.readLine().substring(1);
                int numberOfRepetition = Integer.parseInt(line);
                appointment.setNumberOfRepetition(numberOfRepetition);
                //read and set Description
                String description = bReader.readLine().substring(1);
                while ((line = bReader.readLine()) != null) {
                    description = description + "\n" + line;
                }
                appointment.setDescription(description);

                appointmentCollection.loadAppointment(appointment);
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
        int part2H = appointment.getStartTime().getHour();
        int part2M = appointment.getStartTime().getMinute();
        String part3 = appointment.getEndDate().toString();
        int part4H = appointment.getEndTime().getHour();
        int part4M = appointment.getEndTime().getMinute();


        String appointmentFileString =  part1 + "_" + part2H + "-" + part2M + "_" + part3 + "_" + part4H + "-" + part4M;

        return  pathToAppointmentFolder + "/" + appointmentFileString;
    }

    public void writeAppointmentToFile(Appointment appointment) {

        File file = new File(getAppointmentPath(appointment));

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(">" + appointment.getTitle());
            pw.println(">" + appointment.getStartDate().toString());
            pw.println(">" + appointment.getEndDate().toString());
            pw.println(">" + appointment.getStartTime().toString());
            pw.println(">" + appointment.getEndTime().toString());
            pw.println(">" + appointment.getRepetition().toString());
            pw.println(">" + appointment.getNumberOfRepetition());
            pw.println(">" + appointment.getDescription());
        }
        catch (Exception expt) {
            expt.printStackTrace();
        }
    }
}
