package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.LinkedList;
import java.util.TreeSet;

public class MonthPanel extends JPanel {

    private final Year yearOfThisPanel;
    private final Month monthOfThisPanel;
    private final TreeSet<Appointment>[] appointmentsOfThisMonth;
    private final Scheduler motherPane;
    private final AppointmentCollection collection;

    private final LinkedList<DayPanel> dayPanelList = new LinkedList<>();

    public MonthPanel(Year year, Month month, TreeSet<Appointment>[] appointments, Scheduler motherPane, AppointmentCollection collection) {

        yearOfThisPanel = year;
        monthOfThisPanel = month;
        appointmentsOfThisMonth = appointments;
        this.motherPane = motherPane;
        this.collection = collection;

        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        //create the calendar header with its week days:
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.gridy = 0;
        for (int i = 0; i <= 6; i++) {
            gbc.gridx = i;
            JLabel weekDayLabel = new JLabel(DayOfWeek.values()[i].name());
            weekDayLabel.setPreferredSize(new Dimension(10, weekDayLabel.getPreferredSize().height));
            weekDayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            weekDayLabel.setBorder(LineBorder.createBlackLineBorder());
            add(weekDayLabel, gbc);
        }

        // add each day to the calendar:
        // get the Day of the week as an index & the length of the month
        LocalDate firstDayOfMonth = LocalDate.of(yearOfThisPanel.getValue(), month, 1);
        int indexOfFirstWeekdayOfMonth = firstDayOfMonth.getDayOfWeek().getValue() - 1;
        int lengthOfMonth = monthOfThisPanel.length(year.isLeap());

        gbc.weighty = 1;
        int y = 1;
        int daysToAdd = 0;
        for (int x = indexOfFirstWeekdayOfMonth; x < indexOfFirstWeekdayOfMonth + lengthOfMonth; x++) {
            if (x % 7 == 0) y++;
            gbc.gridy = y;
            gbc.gridx = x % 7;
            // instantiate all days of this month and hand it over the current appointments of that day
            TreeSet<Appointment> appointmentSet = appointmentsOfThisMonth[daysToAdd];
            DayPanel day = new DayPanel(firstDayOfMonth.plusDays(daysToAdd++), appointmentSet, motherPane, collection);
            dayPanelList.add(day);
            add(day, gbc);
        }
    }

    public void actualizeMonthPane() {
        int d = 0;
        for(DayPanel day : dayPanelList) {
            day.actualizeDayPane(appointmentsOfThisMonth[d++]);
        }
    }

    //TODO: fill also the days which are not belonging to this month
    //  (write Month name to the day header e.g. Feb. 29 and make it somehow different in color)
}
