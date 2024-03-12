package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;

import static Organizer.Tabs.SchedulerTab.Repetition.NONE;

public class AppointmentDialog extends JDialog {

    private final AppointmentCollection collection;
    private final Appointment initialAppointment;
    private final boolean isNewAppointment;

    //Appointment values of the appointment instance that was given to the constructor:
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Repetition repetition;
    private final int numberOfRepetitions;
    private final String description;

    //JComponents
    private final JTextField titleField = new JTextField();
    private final JComboBox<Year> startYearCBox;
    private final JComboBox<Month> startMonthCBox;
    private final JComboBox<Integer> startDayCBox;
    private final JComboBox<Year> endYearCBox;
    private final JComboBox<Month> endMonthCBox;
    private final JComboBox<Integer> endDayCBox;
    private final JComboBox<String> startHoursCBox;
    private final JComboBox<String> startMinutesCBox;
    private final JComboBox<String> endHoursCBox;
    private final JComboBox<String> endMinutesCBox;
    private final JComboBox<Repetition> repetitionCBox = new JComboBox(Repetition.isRepeatingValues());
    private final JSpinner numberOfRepetitionsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
    private final JRadioButton selectRepetition = new JRadioButton();
    private final JRadioButton unSelectRepetition = new JRadioButton();
    private final ButtonGroup radioGroup = new ButtonGroup();
    private final JTextArea descriptionArea = new JTextArea();
    private final JButton deleteButton = new JButton("delete");
    private final JButton saveButton = new JButton("save");

    private final LocalDate currentStartDate() {
        return LocalDate.of(((Year) startYearCBox.getSelectedItem()).getValue(),
                            (Month) startMonthCBox.getSelectedItem(),
                            (int) startDayCBox.getSelectedItem());
    }
    private final LocalDate currentEndDate() {
        return LocalDate.of(((Year) endYearCBox.getSelectedItem()).getValue(),
                            (Month) endMonthCBox.getSelectedItem(),
                            (int) endDayCBox.getSelectedItem());
    }
    private final LocalTime currentStartTime() {
        return LocalTime.of(startHoursCBox.getSelectedIndex(), startMinutesCBox.getSelectedIndex());
    }
    private final LocalTime currentEndTime() {
        return LocalTime.of(endHoursCBox.getSelectedIndex(), endMinutesCBox.getSelectedIndex());
    }

    private Appointment getNewAppointment() {
        String title = titleField.getText();
        Repetition repetition;
        if (unSelectRepetition.isSelected()) repetition = NONE;
        else repetition = (Repetition) repetitionCBox.getSelectedItem();
        int numberOfRepetitions = (int) numberOfRepetitionsSpinner.getValue();
        String description = descriptionArea.getText();

        Appointment appointment = new Appointment(title, currentStartDate(), currentEndDate(), currentStartTime(),
                                                  currentEndTime(), repetition, numberOfRepetitions, description);

        return appointment;
    }

    private void saveAppointment() {
        Appointment appointment = getNewAppointment();

        //TODO: make a loop to create repetitions
        // also create a option to make a appointment over several dates long
        collection.addAppointment(currentStartDate(), appointment);
    }

    private void actualizeAppointment(Appointment appointment) {
        appointment.setTitle(titleField.getText());
        appointment.setStartDate(currentStartDate());
        appointment.setEndDate(currentEndDate());
        appointment.setStartTime(currentStartTime());
        appointment.setEndTime(currentEndTime());
        Repetition repetition;
        if (unSelectRepetition.isSelected()) repetition = NONE;
        else repetition = (Repetition) repetitionCBox.getSelectedItem();
        appointment.setRepetition(repetition);
        appointment.setNumberOfRepetition((int) numberOfRepetitionsSpinner.getValue());
        appointment.setDescription(descriptionArea.getText());

        collection.actualizeAppointment(appointment);
    }

    public AppointmentDialog(Component component, AppointmentCollection collection, Appointment appointment) {
        setSize(425, 310);
        setLocationRelativeTo(component);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        this.collection = collection;
        this.initialAppointment = appointment;

        if (appointment == null) {
            this.isNewAppointment = true;

            setTitle("new Appointment");
            this.title = "Title";
            this.startDate = LocalDate.now();
            this.endDate = LocalDate.now();
            this.startTime = LocalTime.now();
            this.endTime = LocalTime.now();
            this.repetition = NONE;
            this.numberOfRepetitions = 0;
            this.description = "Appointment Description";

            endHoursCBox = newHourComboBox(endTime.plusHours(1));
        }
        else {
            this.isNewAppointment = false;

            setTitle("edit Appointment");
            this.title = appointment.getTitle();
            this.startDate = appointment.getStartDate();
            this.endDate = appointment.getEndDate();
            this.startTime = appointment.getStartTime();
            this.endTime = appointment.getEndTime();
            this.repetition = appointment.getRepetition();
            this.numberOfRepetitions = appointment.getNumberOfRepetition();
            this.description = appointment.getDescription();

            endHoursCBox = newHourComboBox(endTime);
        }

        startYearCBox = newYearComboBox(startDate, Year.of(1999));
        startYearCBox.addItemListener(this::startBoxHandler);
        startMonthCBox = newMonthComboBox(startDate, Month.of(1));
        startMonthCBox.addItemListener(this::startBoxHandler);
        startDayCBox = newDayComboBox(startYearCBox, startMonthCBox, startDate, 1);
        startDayCBox.addItemListener(this::startBoxHandler);
        endYearCBox = newYearComboBox(endDate, (Year) startYearCBox.getSelectedItem());
        endYearCBox.addItemListener(this::endYearBoxHandler);
        endMonthCBox = newMonthComboBox(endDate, (Month) startMonthCBox.getSelectedItem());
        endDayCBox = newDayComboBox(endYearCBox, endMonthCBox, endDate, (Integer) startDayCBox.getSelectedItem());
        startHoursCBox = newHourComboBox(startTime);
        startMinutesCBox = newMinuteComboBox(startTime);
        endMinutesCBox = newMinuteComboBox(endTime);
        deleteButton.addActionListener(e -> deleteAppointmentHandler());
        saveButton.addActionListener(e -> saveAppointmentHandler());

        setupPanels();
    }

    // #################################################################################################
    // ################################# GUI elements placed on Dialog #################################
    // #################################                               #################################
    private void setupPanels() {

        setLayout(new BorderLayout());

        JPanel northPane = northPane();
        JScrollPane descriptionScrollPane = descriptionScrollPane(description);
        JPanel controlButtonPane = controlButtonPane();

        add(northPane, BorderLayout.NORTH);
        add(descriptionScrollPane, BorderLayout.CENTER);
        add(controlButtonPane, BorderLayout.SOUTH);
        getRootPane().setBorder(new EmptyBorder(0, 2, 0, 2));
    }

    private JPanel northPane() {
        JPanel northPane = new JPanel();
        northPane.setLayout(new BorderLayout());
        titleField.setText(title);
        titleField.setPreferredSize(new Dimension(titleField.getWidth(), 30));

        northPane.add(titleField, BorderLayout.NORTH);
        northPane.add(comboBoxPane(), BorderLayout.CENTER);

        return northPane;
    }

    private JPanel comboBoxPane() {
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

        JPanel radioButtonRepetitionPane = radioButtonRepetitionPane();

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

        // the number of days inside the dayBox denpend on the year (in leap, feb will go from 28 to 29)
        //yearBox.addItemListener(e -> fillDayBox(yearBox, monthBox, dayBox, 1));
        selectDatePane.add(yearBox);

        // the number of days inside the dayBox depend also on the month
        //monthBox.addItemListener(e -> fillDayBox(yearBox, monthBox, dayBox, 1));

        selectDatePane.add(monthBox);
        selectDatePane.add(dayBox);

        return selectDatePane;
    }

    private JPanel selectTimePane(JComboBox<String> hourBox, JComboBox<String> minuteBox, LocalTime time) {
        JPanel selectTimePane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(selectTimePane, BoxLayout.X_AXIS);
        selectTimePane.setLayout(boxLayout);

        selectTimePane.add(hourBox);
        selectTimePane.add(minuteBox);

        return selectTimePane;
    }

    private JPanel radioButtonRepetitionPane() {
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

        if (repetition.isRepeating()) {
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

        controlButtonPane.add(Box.createHorizontalStrut(5));
        controlButtonPane.add(deleteButton);
        controlButtonPane.add(Box.createHorizontalGlue());
        controlButtonPane.add(saveButton);
        controlButtonPane.add(Box.createHorizontalStrut(5));

        return controlButtonPane;
    }

    // ##########################################################################################
    // ################################# JComboBox initializers #################################
    // #################################                        #################################

    private JComboBox<Year> newYearComboBox(LocalDate setSelectedDate, Year startPoint) {
        JComboBox<Year> yearJComboBox = new JComboBox<>();

        fillYearBox(yearJComboBox, startPoint);

        yearJComboBox.setSelectedItem(Year.of(setSelectedDate.getYear()));

        return yearJComboBox;
    }

    private JComboBox<Month> newMonthComboBox(LocalDate setSelectedDate, Month startPoint) {
        JComboBox<Month> monthJComboBox = new JComboBox<>();

        fillMonthBox(monthJComboBox, startPoint);

        monthJComboBox.setSelectedItem(setSelectedDate.getMonth());

        return monthJComboBox;
    }

    private JComboBox<Integer> newDayComboBox(JComboBox<Year> yearBox, JComboBox<Month> monthBox, LocalDate setSelectedDate, int startPoint) {
        JComboBox<Integer> dayJComboBox = new JComboBox<>();

        fillDayBox(yearBox, monthBox, dayJComboBox, startPoint);

        dayJComboBox.setSelectedItem(setSelectedDate.getDayOfMonth());

        return dayJComboBox;
    }

    private JComboBox<String> newHourComboBox(LocalTime setSelectedTime) {
        JComboBox<String> hourJCBox = new JComboBox<>();
        outerLoop:
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 9; j++) {
                if (i == 2 && j > 3) break outerLoop;
                String hour = String.valueOf(i) + j;
                hourJCBox.addItem(hour);
            }
        }

        hourJCBox.setSelectedIndex(setSelectedTime.getHour());

        hourJCBox.addItemListener(e -> adjustDate());

        return hourJCBox;
    }

    private JComboBox<String> newMinuteComboBox(LocalTime setSelectedTime) {
        JComboBox<String> minuteJCBox = new JComboBox<>();
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 9; j++) {
                String hour = String.valueOf(i) + j;
                minuteJCBox.addItem(hour);
            }
        }

        minuteJCBox.setSelectedIndex(setSelectedTime.getMinute());

        minuteJCBox.addItemListener(e -> adjustDate());

        return minuteJCBox;
    }

    // ###################################################################################
    // ################################# Action Handlers #################################
    // #################################                 #################################

    private void adjustDate() {
        //if the endTime < startTime AND endDate <= startDate -> make endDate = startDate + 1 day

        if (currentEndTime().isBefore(currentStartTime())
                &&
                (currentEndDate().isBefore(currentStartDate()) || currentEndDate().isEqual(currentStartDate()))) {
            endYearCBox.setSelectedItem(startYearCBox.getSelectedItem());
            endMonthCBox.setSelectedItem(startMonthCBox.getSelectedItem());
            endDayCBox.setSelectedItem((Integer) startDayCBox.getSelectedItem() + 1);
        }
    }

    // action listener for endyear comboBox -> the year of the endBox cannot go under the startYear comboBox
    // this Listener get added to the startYear ComboBox
    private void fillYearBox(JComboBox<Year> yearComboBox, Year startPoint) {

        ItemListener[] listenerList = removeAllComboListeners(yearComboBox);

        yearComboBox.removeAllItems();

        Year y = startPoint;
        while(y.getValue() < LocalDate.now().getYear() + 30) {
            yearComboBox.addItem(y);
            y = y.plusYears(1);
        }

        addAllComboListeners(listenerList, yearComboBox);
    }

    private void fillMonthBox(JComboBox<Month> monthComboBox, Month startPoint) {

        ItemListener[] listenerList = removeAllComboListeners(monthComboBox);

        monthComboBox.removeAllItems();

        Month m = startPoint;
        do {
            monthComboBox.addItem(m);
            m = m.plus(1);
        } while (m != Month.DECEMBER);

        addAllComboListeners(listenerList, monthComboBox);
    }

    private void fillDayBox(JComboBox<Year> yearBox, JComboBox<Month> monthBox, JComboBox<Integer> dayBox, int startPoint) {
        Month selectedMonth = (Month) monthBox.getSelectedItem();
        Year selectedYear = (Year) yearBox.getSelectedItem();

        ItemListener[] listenerList = removeAllComboListeners(dayBox);

        dayBox.removeAllItems();

        // start day is influenced by start date and end date -> if start Year and start month
        // equal end year and end month, the end days can't go under the start days
        for (int d = startPoint; d <= selectedMonth.length(selectedYear.isLeap()); d++) {
            dayBox.addItem(d);
        }

        addAllComboListeners(listenerList, dayBox);
    }

    private void endYearBoxHandler(ItemEvent e) {
        Year startYear = (Year) startYearCBox.getSelectedItem();
        Month startMonth = Month.JANUARY;
        int startDay = 1;
        int startMonthValue = ((Month) startMonthCBox.getSelectedItem()).getValue();
        int endMonthValue = ((Month) endMonthCBox.getSelectedItem()).getValue();
        int startYearValue = startYear.getValue();
        int endYearValue;
        if (e.getSource().equals(startYearCBox)) endYearValue = ((Year) startYearCBox.getSelectedItem()).getValue();
        else endYearValue = ((Year) endYearCBox.getSelectedItem()).getValue();

        if (startYearValue == endYearValue) {
            startMonth = (Month) startMonthCBox.getSelectedItem();

            if (startMonthValue == endMonthValue) {
                startDay = (Integer) startDayCBox.getSelectedItem();
            }
        }
        fillMonthBox(endMonthCBox, startMonth);
        fillDayBox(endYearCBox, endMonthCBox, endDayCBox, startDay);
    }

    private void startBoxHandler(ItemEvent e) {
        Year startYear = (Year) startYearCBox.getSelectedItem();
        endYearBoxHandler(e);
        fillYearBox(endYearCBox, startYear);
    }

    private ItemListener[] removeAllComboListeners(JComboBox comboBox) {
        ItemListener[] itemListeners = comboBox.getItemListeners();

        for (ItemListener itemListener : itemListeners) {
            comboBox.removeItemListener(itemListener);
        }

        return itemListeners;
    }

    private void addAllComboListeners(ItemListener[] listenerList, JComboBox comboBox) {
        for (ItemListener itemListener : listenerList) {
            comboBox.addItemListener(itemListener);
        }
    }

    private void deleteAppointmentHandler() {
        if (isNewAppointment) {
            this.dispose();
        }
        else {
            collection.deleteAppointment(initialAppointment);
            this.dispose();
        }
    }

    private void saveAppointmentHandler() {
        if (isNewAppointment) {
            saveAppointment();
            this.dispose();
        }
        else {
            actualizeAppointment(initialAppointment);
            this.dispose();
        }
    }
}
