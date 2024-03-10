package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.TreeSet;

public class MonthPanel extends JPanel {

    private Year currentYear;
    private Month currentMonth;
    private TreeSet<Appointment>[] appointmentsOfThisMonth;

    public MonthPanel(Year year, Month month, TreeSet<Appointment>[] appointments) {

        currentYear = year;
        currentMonth = month;
        appointmentsOfThisMonth = appointments;

        System.out.println(Arrays.toString(appointments));

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
        LocalDate firstDayOfMonth = LocalDate.of(currentYear.getValue(), month, 1);
        int indexOfFirstWeekdayOfMonth = firstDayOfMonth.getDayOfWeek().getValue() - 1;
        int lengthOfMonth = currentMonth.length(year.isLeap());

        gbc.weighty = 1;
        int y = 1;
        int daysToAdd = 0;
        for (int x = indexOfFirstWeekdayOfMonth; x < indexOfFirstWeekdayOfMonth + lengthOfMonth; x++) {
            if (x % 7 == 0) y++;
            gbc.gridy = y;
            gbc.gridx = x % 7;
            // we have to increment daysToAdd and use it then again, because appointmentsOfThisMonth is actually
            // using the real day value (first day in month is 1, second is 2 and so on).
            // AppointmentMap is handling the correct indexing
            TreeSet<Appointment> appointmentSet;
            if (appointmentsOfThisMonth == null) appointmentSet = null;
            else appointmentSet = appointmentsOfThisMonth[daysToAdd];
            DayPanel day = new DayPanel(firstDayOfMonth.plusDays(daysToAdd++), appointmentSet);
            add(day, gbc);
        }

    }
}
