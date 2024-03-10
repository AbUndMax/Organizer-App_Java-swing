package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.TreeSet;

public class DayPanel extends JPanel {

    private LocalDate dateOfDay;
    private TreeSet<Appointment> appointmentsOfTheDay;

    public DayPanel(LocalDate date, TreeSet<Appointment> appointments) {

        dateOfDay = date;
        appointmentsOfTheDay = appointments;

        setBackground(Color.WHITE);
        setBorder(LineBorder.createBlackLineBorder());
        setLayout(new BorderLayout());

        add(dateOfDayLabel(), BorderLayout.NORTH);
        add(appointmentPane(), BorderLayout.CENTER);
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

    private JScrollPane appointmentPane() {
        // This scrollPane contains a List with all Appointments of the day
        JScrollPane appointmentScrollPane = new JScrollPane();
        appointmentScrollPane.setBorder(null);

        JList list = new JList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // add all Appointments to the listModel
        if (appointmentsOfTheDay != null) {
            for (Appointment appointment : appointmentsOfTheDay) {
                listModel.addElement(appointment.getTitle());
            }
        }

        list.setModel(listModel);

        appointmentScrollPane.setViewportView(list);

        appointmentScrollPane.setPreferredSize(new Dimension(10, appointmentScrollPane.getHeight()));

        return appointmentScrollPane;
    }
}
