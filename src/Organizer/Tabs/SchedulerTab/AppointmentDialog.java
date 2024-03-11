package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;

import static Organizer.Tabs.SchedulerTab.Repetition.NONE;

public class AppointmentDialog extends JDialog {

    private final JTextField titleField = new JTextField();
    private final JComboBox<Year> startYearCBox = new JComboBox();
    private final JComboBox<Month> startMonthCBox = new JComboBox();
    private final JComboBox<Integer> startDayCBox = new JComboBox();
    private final JComboBox<Year> endYearCBox = new JComboBox();
    private final JComboBox<Month> endMonthCBox = new JComboBox();
    private final JComboBox<Integer> endDayCBox = new JComboBox();
    private final JComboBox<Integer> startHoursCBox = new JComboBox();
    private final JComboBox<Integer> startMinutesCBox = new JComboBox();
    private final JComboBox<Integer> endHoursCBox = new JComboBox();
    private final JComboBox<Integer> endMinutesCBox = new JComboBox();
    private final JComboBox<Repetition> repetitionCBox = new JComboBox();
    private final JSpinner numberOfRepetitionsSpinner = new JSpinner();
    private final JTextArea descriptionArea = new JTextArea();
    private final JButton closeButton = new JButton();
    private final JButton saveButton = new JButton();

    public AppointmentDialog(Component component, Appointment appointment) {
        setSize(600, 350);
        setLocationRelativeTo(component);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (appointment == null) {
            setTitle("new Appointment");
            setupPanels("Title", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1),
                        NONE, 0, "Appointment Description");
        }
        else {
            setTitle("edit Appointment");
            setupPanels(appointment.getTitle(), appointment.getStartDate(), appointment.getEndDate(),
                        appointment.getStartTime(), appointment.getEndTime(), appointment.getRepetition(),
                        appointment.getNumberOfRepetition(), appointment.getDescription());
        }
    }

    private void setupPanels(String title, LocalDate startDate, LocalDate endDate, LocalTime startTime,
                             LocalTime endTime, Repetition repetition, int numberOfRepetitions, String description) {

        setLayout(new BorderLayout());

        JPanel northPane = northPane(title, startDate, endDate, startTime, endTime, repetition, numberOfRepetitions);
        JScrollPane descriptionScrollPane = descriptionScrollPane(description);
        JPanel controlButtonPane = controlButtonPane();

        add(northPane, BorderLayout.NORTH);
        add(descriptionScrollPane, BorderLayout.CENTER);
        add(controlButtonPane, BorderLayout.SOUTH);
    }

    private JPanel northPane(String title, LocalDate startDate, LocalDate endDate,
                             LocalTime startTime, LocalTime endTime,
                             Repetition repetition, int numberOfRepetitions) {
        JPanel northPane = new JPanel();
        northPane.setLayout(new BorderLayout());
        titleField.setText(title);
        titleField.setPreferredSize(new Dimension(titleField.getWidth(), 30));

        northPane.add(titleField, BorderLayout.NORTH);
        northPane.add(comboBoxPane(startDate, endDate, startTime, endTime, repetition, numberOfRepetitions), BorderLayout.CENTER);

        return northPane;
    }

    private JPanel comboBoxPane(LocalDate startDate, LocalDate endDate,
                                   LocalTime startTime, LocalTime endTime,
                                   Repetition repetition, int numberOfRepetitions) {
        JPanel comboBoxPane = new JPanel();
        //TODO: setup all comboBoxes
        return comboBoxPane;
    }

    private JScrollPane descriptionScrollPane(String description) {
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionArea.setText(description);
        descriptionScrollPane.setViewportView(descriptionArea);
        return descriptionScrollPane;
    }

    private JPanel controlButtonPane() {
        JPanel controlButtonPane = new JPanel();
        //TODO: setup the buttons for saving / closing
        return controlButtonPane;
    }
}
