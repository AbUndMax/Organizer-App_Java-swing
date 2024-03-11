package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.TreeSet;

public class Scheduler extends JSplitPane {

    private final LocalDate currentDate = LocalDate.now();
    private final AppointmentIO appointmentIO = new AppointmentIO();
    private final AppointmentCollection appointmentCollection = appointmentIO.getAppointmentMapInstance();
    private final JComboBox yearChooser = new JComboBox();
    private final JComboBox monthChooser = new JComboBox();

    public Scheduler() {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        setLeftComponent(leftPane());
        setRightComponent(rightPane());
    }

    // in the left Split Pane we have the Appointment - List view component and the appointment control buttons
    private JPanel leftPane() {
        // Panel for the whole left side of the Split Pane (Appointment overview & control Buttons)
        JPanel leftSpacePane = new JPanel(new BorderLayout());

        // List-view of all Appointments:
        leftSpacePane.add(appointmentListViewPane(), BorderLayout.CENTER);
        // add control buttons for Appointments
        leftSpacePane.add(appointmentButtonPane(), BorderLayout.SOUTH);

        return leftSpacePane;
    }

    //Appointment List-view Panel:
    private JScrollPane appointmentListViewPane() {
        // setup the Appointment overview (by using a modified JTree embedded in JScrollPane
        JScrollPane scrollTree = new JScrollPane();
        //TODO: add JTree into JScrollPane

        return scrollTree;
    }

    //Appointment control buttons:
    private JPanel appointmentButtonPane() {
        JPanel appointmentButtonPane = new JPanel(new GridLayout(2, 1));
        JButton newAppointmentButton = new JButton("new appointment");
        JButton delAppointmentButton = new JButton("delete appointment");

        //TODO: Add appointment button listeners

        appointmentButtonPane.add(newAppointmentButton);
        appointmentButtonPane.add(delAppointmentButton);

        return appointmentButtonPane;
    }


    // Panel for the right Space consisting of Calendar Monthly View & Calendar control Buttons
    private JPanel rightPane() {

        JPanel rightSpacePane = new JPanel(new BorderLayout());

        // Calendar Control elements:
        rightSpacePane.add(calendarButtonPane(), BorderLayout.NORTH);
        // Calendar:
        rightSpacePane.add(calendarPane(), BorderLayout.CENTER);

        return rightSpacePane;
    }

    // Panel for the control buttons of the calendar
    private JPanel calendarButtonPane() {
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
        // calendar will always start with 1999
        Year i = Year.of(1999);
        // there will be always +30 years from the year the application gets opened
        while (i.getValue() != currentYear.plusYears(30).getValue()) {
            yearChooser.addItem(i);
            i = i.plusYears(1);
        }
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

        return calendarButtonPane;
    }

    // Panel for the Calendar
    private JPanel calendarPane() {
        Year yearToCreate = (Year) yearChooser.getSelectedItem();
        Month monthToCreate = (Month) monthChooser.getSelectedItem();
        TreeSet<Appointment>[] appointmentsOfThisMonth = appointmentCollection.getAppointmentsOfMonth(yearToCreate,
                                                                                                      monthToCreate);

        return (new MonthPanel(yearToCreate, monthToCreate, appointmentsOfThisMonth));
    }
}
