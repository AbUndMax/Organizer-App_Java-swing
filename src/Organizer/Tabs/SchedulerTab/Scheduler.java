package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.TreeSet;

public class Scheduler extends JSplitPane {

    private final LocalDate currentDate = LocalDate.now();
    private final AppointmentIO appointmentIO = new AppointmentIO();
    private final AppointmentMap appointmentMap = appointmentIO.getAppointmentMapInstance();

    public Scheduler() {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        setLeftComponent(leftPane());
        setRightComponent(rightPane());
    }

    private JPanel leftPane() {
        // Panel for the whole left side of the Split Pane (Appointment overview & control Buttons)
        JPanel leftSpacePane = new JPanel(new BorderLayout());

        // add control buttons for Appointments
        JPanel appointmentButtonPane = new JPanel(new GridLayout(2, 1));
        JButton newAppointmentButton = new JButton("new appointment");
        JButton delAppointmentButton = new JButton("delete appointment");

        //TODO: Add appointment button listeners

        appointmentButtonPane.add(newAppointmentButton);
        appointmentButtonPane.add(delAppointmentButton);

        // setup the Appointment overview (by using a modified JTree embedded in JScrollPane
        JScrollPane scrollTree = new JScrollPane();
        //TODO: add JTree into JScrollPane

        // put Buttons and JTree inside one final JPane which gets added to the left side.
        leftSpacePane.add(scrollTree, BorderLayout.CENTER);
        leftSpacePane.add(appointmentButtonPane, BorderLayout.SOUTH);

        return leftSpacePane;
    }

    private JPanel rightPane() {
        // Panel for the right Space of the SpiltPane (Calendar Monthly View & Calendar control Buttons)
        JPanel rightSpacePane = new JPanel(new BorderLayout());

        // Calendar Control elements:
        // Button Control Panel
        JPanel calendarButtonPane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(calendarButtonPane, BoxLayout.X_AXIS);
        calendarButtonPane.setLayout(boxLayout);

        // Buttons to "scroll" through the Months
        JButton prevMonth = new JButton("⏴ previous Month");
        JButton nextMonth = new JButton("next Month ⏵");

        //TODO: add calendar control listeners

        // JComboBox to choose easily between years and months
        Year currentYear = Year.of(currentDate.getYear());
        Month currentMonth = Month.of(currentDate.getMonthValue());
        JComboBox yearChooser = new JComboBox();
        // calendar will always start with 1999
        Year i = Year.of(1999);
        // there will be always +30 years from the year the application gets opened
        while (i.getValue() != currentYear.plusYears(30).getValue()) {
            yearChooser.addItem(i);
            i = i.plusYears(1);
        }
        JComboBox monthChooser = new JComboBox();
        for (Month month : Month.values()) monthChooser.addItem(month);
        // set current month and year as selected
        yearChooser.setSelectedItem(currentYear);
        monthChooser.setSelectedItem(currentMonth);

        //add the control elements to the Panel
        calendarButtonPane.add(Box.createHorizontalStrut(5));
        calendarButtonPane.add(prevMonth);
        calendarButtonPane.add(Box.createHorizontalGlue());
        calendarButtonPane.add(yearChooser);
        calendarButtonPane.add(Box.createHorizontalStrut(5));
        calendarButtonPane.add(monthChooser);
        calendarButtonPane.add(Box.createHorizontalGlue());
        calendarButtonPane.add(nextMonth);
        calendarButtonPane.add(Box.createHorizontalStrut(5));


        // Calendar:
        //TODO: add Calendar to rightSpacePane
        Year yearToCreate = (Year) yearChooser.getSelectedItem();
        Month monthToCreate = (Month) monthChooser.getSelectedItem();
        TreeSet<Appointment>[] appointmentsOfThisMonth = appointmentMap.getAppointmentsOfMonth(yearToCreate, monthToCreate);
        MonthPanel monthPanel = new MonthPanel(yearToCreate, monthToCreate, appointmentsOfThisMonth);

        rightSpacePane.add(calendarButtonPane, BorderLayout.NORTH);
        rightSpacePane.add(monthPanel, BorderLayout.CENTER);

        return rightSpacePane;
    }
}
