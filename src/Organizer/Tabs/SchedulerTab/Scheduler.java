package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.TreeSet;

public class Scheduler extends JSplitPane {

    private final LocalDate currentDate = LocalDate.now();
    private final AppointmentCollection appointmentCollection = new AppointmentCollection();
    private final JComboBox<Year> yearChooser = new JComboBox();
    private final JComboBox<Month> monthChooser = new JComboBox();
    private final JPanel rightSpacePane = new JPanel(new BorderLayout());
    private MonthPanel currentMonthPane;
    private final AppointmentScrollableTree scrollTree = new AppointmentScrollableTree(appointmentCollection, this);

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

        leftSpacePane.setMinimumSize(new Dimension(250, leftSpacePane.getPreferredSize().height));

        return leftSpacePane;
    }

    //Appointment List-view Panel:
    private JScrollPane appointmentListViewPane() {
        // setup the Appointment overview (by using a modified JTree embedded in JScrollPane
        scrollTree.setPreferredSize(new Dimension(200, scrollTree.getHeight()));

        return scrollTree;
    }

    //Appointment control buttons:
    private JPanel appointmentButtonPane() {
        JPanel appointmentButtonPane = new JPanel(new GridLayout(1, 1));
        JButton newAppointmentButton = new JButton("new appointment");
        //JButton delAppointmentButton = new JButton("delete appointment");
        newAppointmentButton.addActionListener(e -> newAppointmentHandler());


        appointmentButtonPane.add(newAppointmentButton);
        //appointmentButtonPane.add(delAppointmentButton);

        return appointmentButtonPane;
    }


    // Panel for the right Space consisting of Calendar Monthly View & Calendar control Buttons
    private JPanel rightPane() {
        // Calendar Control elements:
        rightSpacePane.add(calendarButtonPane(), BorderLayout.NORTH);
        // Calendar:
        rightSpacePane.add(calendarPane(0), BorderLayout.CENTER);

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

        // JComboBox to choose easily between years and months
        Year currentYear = Year.of(currentDate.getYear());
        Month currentMonth = Month.of(currentDate.getMonthValue());
        // calendar will always start with 1999
        Year y = Year.of(1999);
        // there will be always +30 years from the year the application gets opened
        while (y.getValue() != currentYear.plusYears(30).getValue()) {
            yearChooser.addItem(y);
            y = y.plusYears(1);
        }
        for (Month month : Month.values()) monthChooser.addItem(month);
        // set current month and year as selected
        yearChooser.setSelectedItem(currentYear);
        monthChooser.setSelectedItem(currentMonth);

        prevMonth.addActionListener(e -> previousMonthHandler());
        nextMonth.addActionListener(e -> nextMonthHandler());

        yearChooser.addActionListener(e -> newMonthHandler());
        monthChooser.addActionListener(e -> newMonthHandler());

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
    private JPanel calendarPane(int plusMonths) {
        // if month should go up, check if that changed the month to jan -> which means we go also one year up
        if (plusMonths == 1) {
            monthChooser.setSelectedItem(((Month) monthChooser.getSelectedItem()).plus(1));
            if (monthChooser.getSelectedItem() == Month.JANUARY) yearChooser.setSelectedIndex(yearChooser.getSelectedIndex() + 1);
        } // same for substraction -> if month drops from jan to december -> increment the year
        else if (plusMonths == -1) {
            monthChooser.setSelectedItem(((Month) monthChooser.getSelectedItem()).minus(1));
            if ((Month) monthChooser.getSelectedItem() == Month.DECEMBER) yearChooser.setSelectedIndex(yearChooser.getSelectedIndex() - 1);
        }
        Year yearToCreate = (Year) yearChooser.getSelectedItem();
        Month monthToCreate = (Month) monthChooser.getSelectedItem();

        TreeSet<Appointment>[] appointmentsOfThisMonth = appointmentCollection.getAppointmentsOfMonth(yearToCreate,
                                                                                                      monthToCreate);

        currentMonthPane = new MonthPanel(yearToCreate, monthToCreate, appointmentsOfThisMonth, this, appointmentCollection);

        return (currentMonthPane);
    }

    // ###################################################################################
    // ################################# Action Handlers #################################
    // #################################                 #################################

    private void newAppointmentHandler() {
        AppointmentDialog appointmentDialog = new AppointmentDialog(this, appointmentCollection, null);
        appointmentDialog.setVisible(true);
    }

    public void actualizeSchedulerPane() {
        currentMonthPane.actualizeMonthPane();
        scrollTree.actualizeAppointmentScrollTree();
    }

    private void nextMonthHandler() {
        rightSpacePane.remove(currentMonthPane);
        rightSpacePane.add(calendarPane(1));
        rightSpacePane.revalidate();
        rightSpacePane.repaint();
    }

    private void previousMonthHandler() {
        rightSpacePane.remove(currentMonthPane);
        rightSpacePane.add(calendarPane(-1));
        rightSpacePane.revalidate();
        rightSpacePane.repaint();
    }

    private void newMonthHandler() {
        rightSpacePane.remove(currentMonthPane);
        rightSpacePane.add(calendarPane(0));
        rightSpacePane.revalidate();
        rightSpacePane.repaint();
    }
}
