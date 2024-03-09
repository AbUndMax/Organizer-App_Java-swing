package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDate;

public class DayPanel extends JPanel {

    private LocalDate dateOfDay;

    public DayPanel(LocalDate date) {

        dateOfDay = date;

        setBackground(Color.WHITE);
        setBorder(LineBorder.createBlackLineBorder());
        setLayout(new BorderLayout());

        add(dateOfDayLabel(), BorderLayout.NORTH);

    }

    private JLabel dateOfDayLabel() {
        // make a String with current Day number of Month
        int intOfDay = dateOfDay.getDayOfMonth();
        String dateNumber;
        if (intOfDay < 10) dateNumber = " 0" + intOfDay + " ";
        else dateNumber = " " + intOfDay + " ";

        JLabel dateOfDayLabel = new JLabel(dateNumber);
        dateOfDayLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

        return dateOfDayLabel;
    }

    private JScrollPane appointmentsOfDay() {
        JScrollPane appointmentScrollPane = new JScrollPane();
        JPanel appointmentButtonPane = new JPanel();


        return appointmentScrollPane;
    }
}
