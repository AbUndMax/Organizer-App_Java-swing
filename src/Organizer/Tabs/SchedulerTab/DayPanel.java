package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.time.LocalDate;
import java.util.TreeSet;

public class DayPanel extends JPanel {

    private final LocalDate dateOfDay;
    private TreeSet<Appointment> appointmentsOfTheDay;
    private final DefaultListModel<Appointment> listModel = new DefaultListModel<>();
    private final JList<Appointment> list = new JList<>(listModel);
    private final Scheduler motherPane;
    private final AppointmentCollection collection;
    private final ListSelectionListener listener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList<Appointment> list = (JList) e.getSource();
            if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                AppointmentDialog appointmentDialog = new AppointmentDialog(motherPane, collection, list.getSelectedValue());
                appointmentDialog.setVisible(true);

                list.clearSelection();
            }
        }
    };

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

        list.addListSelectionListener(listener);
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

        //TODO: make the blue selection indicator go away if something different is selected

        appointmentScrollPane.setViewportView(list);

        appointmentScrollPane.setPreferredSize(new Dimension(10, appointmentScrollPane.getHeight()));

        return appointmentScrollPane;
    }

    private void fillList() {
        // add all Appointments to the listModel
        if (appointmentsOfTheDay != null) {
            for (Appointment appointment : appointmentsOfTheDay) {
                listModel.addElement(appointment);
            }
        }
    }

    public void actualizeDayPane(TreeSet<Appointment> newAppointmentsOfTheDay) {
        list.removeListSelectionListener(listener);
        appointmentsOfTheDay = newAppointmentsOfTheDay;
        listModel.removeAllElements();
        fillList();
        list.clearSelection();
        list.addListSelectionListener(listener);
    }
}
