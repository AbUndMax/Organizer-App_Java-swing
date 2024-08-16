package Organizer.SubPrograms.SchedulerTab;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.TreeSet;

public class AppointmentScrollableTree extends JScrollPane {

    private AppointmentCollection appointmentCollection;
    private HashMap<Year, TreeSet<Appointment>[][]> appointmentMap;
    private final Scheduler motherPane;

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Years");
    private final DefaultTreeModel treeModel = new DefaultTreeModel(root);
    private final JTree tree = new JTree(treeModel);
    private final TreeSelectionListener listener = new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if (e.isAddedPath()) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.isLeaf()) {
                    // Führen Sie hier die Aktionen aus, die Sie durchführen möchten, wenn ein Knoten ausgewählt wird.
                    Appointment appointment = (Appointment) selectedNode.getUserObject();
                    AppointmentDialog dialog = new AppointmentDialog(motherPane, appointmentCollection, appointment);
                    dialog.setVisible(true);
                }
            }

            tree.clearSelection();
        }
    };

    public AppointmentScrollableTree(AppointmentCollection appointmentCollection, Scheduler motherPane) {
        this.appointmentCollection = appointmentCollection;
        this.appointmentMap = appointmentCollection.getAppointmentMap();
        this.motherPane = motherPane;

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);

        tree.setCellRenderer(renderer);
        populateTree();
        add(tree);
        setViewportView(tree);

        tree.addTreeSelectionListener(listener);
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
                    String dayName = month.toString().substring(0, 3).toLowerCase() + " " + dayValue + ".";
                    DefaultMutableTreeNode dayNode = new DefaultMutableTreeNode(dayName);
                    monthNode.add(dayNode);

                    for (Appointment appointment : appointmentMap.get(year)[m][d]) {
                        DefaultMutableTreeNode appointmentNode = new DefaultMutableTreeNode(appointment);
                        dayNode.add(appointmentNode);
                    }
                }
            }
        }

        expandTree();

        tree.setRootVisible(false);
    }

    public void actualizeAppointmentScrollTree() {
        // clear current tree and repopulate it
        tree.removeTreeSelectionListener(listener);
        root.removeAllChildren();
        populateTree();
        treeModel.reload();
        expandTree();
        tree.addTreeSelectionListener(listener);
    }

    private void expandTree() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
