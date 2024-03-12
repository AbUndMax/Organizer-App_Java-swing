package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
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
    private final JComboBox<String> startHoursCBox = new JComboBox();
    private final JComboBox<String> startMinutesCBox = new JComboBox();
    private final JComboBox<String> endHoursCBox = new JComboBox();
    private final JComboBox<String> endMinutesCBox = new JComboBox();
    private final JComboBox<Repetition> repetitionCBox = new JComboBox();
    private final JSpinner numberOfRepetitionsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
    private final JRadioButton selectRepetition = new JRadioButton();
    private final JRadioButton unSelectRepetition = new JRadioButton();
    private final ButtonGroup radioGroup = new ButtonGroup();
    private final JTextArea descriptionArea = new JTextArea();
    private final JButton deleteButton = new JButton("delete");
    private final JButton saveButton = new JButton("save");

    public Appointment getNewAppointment() {
        Appointment appointment = new Appointment();

        return appointment;
    }

    public AppointmentDialog(Component component, Appointment appointment) {
        setSize(425, 310);
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
        getRootPane().setBorder(new EmptyBorder(0, 2, 0, 2));
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

        BoxLayout boxLayout = new BoxLayout(comboBoxPane, BoxLayout.Y_AXIS);
        comboBoxPane.setLayout(boxLayout);

        JPanel startDateBoxPane = comboBoxLabelPane("start date:", selectDatePane(startYearCBox,
                                                                                      startMonthCBox,
                                                                                      startDayCBox,
                                                                                      startDate), false);

        JPanel endDateBoxPane = comboBoxLabelPane("end date:", selectDatePane(endYearCBox,
                                                                                  endMonthCBox,
                                                                                  endDayCBox,
                                                                                  endDate), false);

        JPanel selectTimeComboPane = new JPanel();
        BoxLayout boxLayout1 = new BoxLayout(selectTimeComboPane, BoxLayout.Y_AXIS);
        selectTimeComboPane.setLayout(boxLayout1);

        JPanel startTimeBoxPane = comboBoxLabelPane("start time:", selectTimePane(startHoursCBox,
                                                                                      startMinutesCBox,
                                                                                      startTime), true);

        JPanel endTimeBoxPane = comboBoxLabelPane("end time:", selectTimePane(endHoursCBox,
                                                                                  endMinutesCBox,
                                                                                  endTime), true);

        selectTimeComboPane.add(Box.createVerticalStrut(0));
        selectTimeComboPane.add(startTimeBoxPane);
        selectTimeComboPane.add(Box.createVerticalStrut(0));
        selectTimeComboPane.add(endTimeBoxPane);
        selectTimeComboPane.add(Box.createVerticalStrut(0));

        selectTimeComboPane.setMaximumSize(selectTimeComboPane.getMinimumSize());
        selectTimeComboPane.setPreferredSize(selectTimeComboPane.getMinimumSize());

        JPanel radioButtonRepetitionPane = radioButtonRepetitionPane(repetition, numberOfRepetitions);

        JPanel timeAndRadioButtonPanel = new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(timeAndRadioButtonPanel, BoxLayout.X_AXIS);
        timeAndRadioButtonPanel.setLayout(boxLayout2);

        radioButtonRepetitionPane.setBorder(new MatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        timeAndRadioButtonPanel.add(Box.createHorizontalStrut(0));
        timeAndRadioButtonPanel.add(selectTimeComboPane);
        timeAndRadioButtonPanel.add(Box.createHorizontalStrut(0));
        timeAndRadioButtonPanel.add(radioButtonRepetitionPane);
        timeAndRadioButtonPanel.add(Box.createHorizontalGlue());



        comboBoxPane.add(startDateBoxPane);
        comboBoxPane.add(endDateBoxPane);
        comboBoxPane.add(timeAndRadioButtonPanel);

        return comboBoxPane;
    }

    private JPanel comboBoxLabelPane(String labelText, JPanel comboPane, boolean pack) {
        Dimension labelSize = new Dimension(73, 30);

        JPanel comboBoxPane = new JPanel();
        BoxLayout startTimeLayout = new BoxLayout(comboBoxPane, BoxLayout.X_AXIS);
        comboBoxPane.setLayout(startTimeLayout);
        JLabel startTimeLabel = new JLabel(labelText);
        JPanel LabelPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        LabelPane.add(startTimeLabel);
        LabelPane.setPreferredSize(labelSize);
        LabelPane.setMinimumSize(labelSize);
        LabelPane.setMaximumSize(labelSize);
        if (pack) {
            comboPane.setPreferredSize(comboPane.getMinimumSize());
            comboPane.setMaximumSize(comboPane.getMinimumSize());
        }
        comboBoxPane.add(Box.createHorizontalStrut(5));
        comboBoxPane.add(LabelPane);
        comboBoxPane.add(Box.createHorizontalStrut(10));
        comboBoxPane.add(comboPane);
        comboBoxPane.add(Box.createHorizontalStrut(0));

        return comboBoxPane;
    }

    private JPanel selectDatePane(JComboBox<Year> yearBox, JComboBox<Month> monthBox, JComboBox<Integer> dayBox,
                                  LocalDate date) {
        JPanel selectDatePane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(selectDatePane, BoxLayout.X_AXIS);
        selectDatePane.setLayout(boxLayout);

        Year y = Year.of(1999);
        while(y.getValue() < date.getYear() + 30) {
            yearBox.addItem(y);
            y = y.plusYears(1);
        }
        yearBox.setSelectedItem(Year.of(date.getYear()));
        // the number of days inside the dayBox denpend on the year (in leap, feb will go from 28 to 29)
        yearBox.addItemListener(e -> fillDayBoxAtEvent(yearBox, monthBox, dayBox));
        selectDatePane.add(yearBox);

        for (Month month : Month.values()) monthBox.addItem(month);
        // the number of days inside the dayBox depend also on the month
        monthBox.addItemListener(e -> fillDayBoxAtEvent(yearBox, monthBox, dayBox));

        monthBox.setSelectedItem(date.getMonth()); //this will fire an Event and fill the dayBox
        selectDatePane.add(monthBox);
        selectDatePane.add(dayBox);

        return selectDatePane;
    }

    private void fillDayBoxAtEvent(JComboBox<Year> yearBox, JComboBox<Month> monthBox, JComboBox<Integer> dayBox) {
        Month selectedMonth = (Month) monthBox.getSelectedItem();
        Year selectedYear = (Year) yearBox.getSelectedItem();
        dayBox.removeAllItems();
        for (int d = 1; d <= selectedMonth.length(selectedYear.isLeap()); d++) {
            dayBox.addItem(d);
        }
    }

    private JPanel selectTimePane(JComboBox<String> hourBox, JComboBox<String> minuteBox, LocalTime time) {
        JPanel selectTimePane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(selectTimePane, BoxLayout.X_AXIS);
        selectTimePane.setLayout(boxLayout);

        outerLoop:
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 9; j++) {
                if (i == 2 && j > 3) break outerLoop;
                String hour = String.valueOf(i) + j;
                hourBox.addItem(hour);
            }
        }
        hourBox.setSelectedItem(time.getHour());

        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 9; j++) {
                String hour = String.valueOf(i) + j;
                minuteBox.addItem(hour);
            }
        }
        minuteBox.setSelectedItem(time.getMinute());

        selectTimePane.add(hourBox);
        selectTimePane.add(minuteBox);

        return selectTimePane;
    }

    private JPanel radioButtonRepetitionPane(Repetition repetition, int numberOfRepetitions) {
        JPanel radioButtonPane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(radioButtonPane, BoxLayout.Y_AXIS);
        radioButtonPane.setLayout(boxLayout);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("no repetition");
        labelPanel.add(label);

        // group the radio Buttons
        radioGroup.add(unSelectRepetition);
        radioGroup.add(selectRepetition);

        JPanel upperRadioButton = radioButtonPane(unSelectRepetition, labelPanel);

        JPanel repetitionChooserPane = new JPanel();
        BoxLayout boxLayout1 = new BoxLayout(repetitionChooserPane, BoxLayout.X_AXIS);
        repetitionChooserPane.setLayout(boxLayout1);
        JLabel xLabel = new JLabel("X");


        for (Repetition rep : Repetition.hasRepetitionValues()) {
            repetitionCBox.addItem(rep);
        }

        ((JSpinner.DefaultEditor) numberOfRepetitionsSpinner.getEditor()).getTextField().setFocusable(false);
        Dimension prefferedSpinnerSize = new Dimension(45, numberOfRepetitionsSpinner.getPreferredSize().height);
        numberOfRepetitionsSpinner.setPreferredSize(prefferedSpinnerSize);
        numberOfRepetitionsSpinner.setMinimumSize(prefferedSpinnerSize);
        numberOfRepetitionsSpinner.setMaximumSize(prefferedSpinnerSize);

        //pack radioButtons
        selectRepetition.setPreferredSize(selectRepetition.getMinimumSize());
        selectRepetition.setMaximumSize(selectRepetition.getMinimumSize());
        unSelectRepetition.setMaximumSize(unSelectRepetition.getMinimumSize());
        unSelectRepetition.setPreferredSize(unSelectRepetition.getMinimumSize());

        repetitionChooserPane.add(Box.createHorizontalStrut(0));
        repetitionChooserPane.add(repetitionCBox);
        repetitionChooserPane.add(Box.createHorizontalStrut(0));
        repetitionChooserPane.add(xLabel);
        repetitionChooserPane.add(Box.createHorizontalStrut(0));
        repetitionChooserPane.add(numberOfRepetitionsSpinner);
        repetitionChooserPane.add(Box.createHorizontalGlue());

        JPanel lowerRadioButton = radioButtonPane(selectRepetition, repetitionChooserPane);

        radioButtonPane.add(upperRadioButton);
        radioButtonPane.add(lowerRadioButton);

        if (repetition.hasRepetition()) {
            selectRepetition.setSelected(true);
            repetitionCBox.setSelectedItem(repetition);
            numberOfRepetitionsSpinner.setValue(numberOfRepetitions);
        }
        else unSelectRepetition.setSelected(true);

        radioButtonPane.setMaximumSize(radioButtonPane.getMinimumSize());
        radioButtonPane.setPreferredSize(radioButtonPane.getMinimumSize());

        return radioButtonPane;
    }

    private JPanel radioButtonPane(JRadioButton radioButton, JPanel panel) {
        JPanel radioButtonPane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(radioButtonPane, BoxLayout.X_AXIS);
        radioButtonPane.setLayout(boxLayout);

        radioButtonPane.add(Box.createHorizontalStrut(0));
        radioButtonPane.add(radioButton);
        radioButtonPane.add(Box.createHorizontalStrut(0));
        radioButtonPane.add(panel);
        radioButtonPane.add(Box.createHorizontalGlue());

        return radioButtonPane;
    }

    private JScrollPane descriptionScrollPane(String description) {
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionArea.setText(description);
        descriptionScrollPane.setViewportView(descriptionArea);
        return descriptionScrollPane;
    }

    private JPanel controlButtonPane() {
        JPanel controlButtonPane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(controlButtonPane, BoxLayout.X_AXIS);
        controlButtonPane.setLayout(boxLayout);

        //TODO: add buttonListener

        controlButtonPane.add(Box.createHorizontalStrut(5));
        controlButtonPane.add(deleteButton);
        controlButtonPane.add(Box.createHorizontalGlue());
        controlButtonPane.add(saveButton);
        controlButtonPane.add(Box.createHorizontalStrut(5));

        return controlButtonPane;
    }
}
