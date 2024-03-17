package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeSet;

public class DayPanel extends JPanel {

    private final LocalDate dateOfDay;
    private TreeSet<Appointment> appointmentsOfTheDay;
    private final DefaultListModel<Appointment> listModel = new DefaultListModel<>();
    private final JList<Appointment> list = new JList<>(listModel);
    private final Scheduler motherPane;
    private final AppointmentCollection collection;

    public DayPanel(LocalDate date, TreeSet<Appointment> appointments, Scheduler motherPane, AppointmentCollection collection) {

        dateOfDay = date;
        appointmentsOfTheDay = appointments;
        this.motherPane = motherPane;
        this.collection = collection;

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

        fillList();
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickHandler(e);
            }
        });

        //TODO: make the blue selection indicator go away if something different is selected

        appointmentScrollPane.setViewportView(list);

        appointmentScrollPane.setPreferredSize(new Dimension(10, appointmentScrollPane.getHeight()));

        return appointmentScrollPane;
    }

    private void fillList() {
        // add all Appointments to the listModel
        System.out.println("all appointemnts of day: " + dateOfDay);
        System.out.println(appointmentsOfTheDay);
        if (appointmentsOfTheDay != null) {
            for (Appointment appointment : appointmentsOfTheDay) {
                listModel.addElement(appointment);
            }
        }
    }

    private void clickHandler(MouseEvent e) {
        if (e.getClickCount() == 1) {
            JList<Appointment> list = (JList) e.getSource();
            AppointmentDialog appointmentDialog = new AppointmentDialog(motherPane, collection, list.getSelectedValue());
            appointmentDialog.setVisible(true);
        }
    }

    public void actualizeDayPane(TreeSet<Appointment> newAppointmentsOfTheDay) {
        appointmentsOfTheDay = newAppointmentsOfTheDay;
        listModel.removeAllElements();
        fillList();
    }
}
