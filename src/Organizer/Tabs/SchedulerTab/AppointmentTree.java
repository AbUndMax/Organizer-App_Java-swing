package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import javax.swing.tree.*;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.TreeSet;

public class AppointmentTree extends JScrollPane {

    private AppointmentCollection appointmentCollection;
    private HashMap<Year, TreeSet<Appointment>[][]> appointmentMap;

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Years");
    private final DefaultTreeModel treeModel = new DefaultTreeModel(root);
    private final JTree tree = new JTree(treeModel);

    public AppointmentTree(AppointmentCollection appointmentCollection) {
        this.appointmentCollection = appointmentCollection;
        this.appointmentMap = appointmentCollection.getAppointmentMap();

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);

        tree.setCellRenderer(renderer);
        populateTree();
        add(tree);
        setViewportView(tree);
    }

    private void populateTree() {
        // Populate the tree (add all existing Appointments in the appointmentMap to
        for (Year year : appointmentMap.keySet()) {
            DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(year);
            root.add(yearNode);

            for (int m = 0; m < 12; m++) {
                Month month = Month.of(m + 1);
                //if month doesn't have any appointments skip
                if (!appointmentCollection.monthHasAppointments(year, month)) continue;
                DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(month);
                yearNode.add(monthNode);

                for (int d = 0; d < month.length(year.isLeap()); d++) {
                    Integer dayValue = d + 1;
                    // if day doesn't have any appointments skip
                    if (appointmentCollection.dayHasAppointments(year, month, dayValue)) continue;
                    DefaultMutableTreeNode dayNode = new DefaultMutableTreeNode(dayValue);
                    monthNode.add(dayNode);

                    for (Appointment appointment : appointmentMap.get(year)[m][d]) {
                        DefaultMutableTreeNode appointmentNode = new DefaultMutableTreeNode(appointment);
                        dayNode.add(appointmentNode);
                    }
                }
            }
        }

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        tree.setRootVisible(false);
    }
}
