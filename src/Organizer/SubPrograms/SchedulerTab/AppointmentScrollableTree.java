package Organizer.SubPrograms.SchedulerTab;

import Organizer.Database.SchedulerTable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class AppointmentScrollableTree extends JScrollPane {

    private final Scheduler motherPane;

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Appointments");
    private final DefaultTreeModel treeModel = new DefaultTreeModel(root);
    private final JTree tree = new JTree(treeModel);
    private final TreeSelectionListener listener = new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if (e.isAddedPath()) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.isLeaf()) {
                    // Führen Sie hier die Aktionen aus, die Sie durchführen möchten, wenn ein Knoten ausgewählt wird.
                    SchedulerEntry schedulerEntry = (SchedulerEntry) selectedNode.getUserObject();
                    AppointmentDialog dialog = new AppointmentDialog(motherPane, schedulerEntry);
                    dialog.setVisible(true);
                }
            }

            tree.clearSelection();
        }
    };

    public AppointmentScrollableTree(Scheduler motherPane) {
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

        LinkedList<SchedulerEntry> schedulerEntries = SchedulerTable.loadFullTable();

        TreeMap<Integer, TreeMap<Month, TreeMap<Integer, DefaultMutableTreeNode>>> yearMap = new TreeMap<>();

        for (SchedulerEntry schedulerEntry : schedulerEntries) {
            int year = schedulerEntry.getStartDate().getYear();
            Month month = schedulerEntry.getStartDate().getMonth();
            int day = schedulerEntry.getStartDate().getDayOfMonth();

            // Erstellen oder Abrufen der Tages-Map für den Monat und das Jahr
            yearMap.computeIfAbsent(year, y -> new TreeMap<>())
                    .computeIfAbsent(month, m -> new TreeMap<>())
                    .computeIfAbsent(day, d -> new DefaultMutableTreeNode(day))
                    .add(new DefaultMutableTreeNode(schedulerEntry));
        }

        // Hinzufügen der sortierten Jahr-, Monats- und Tagesknoten zum Baum
        for (Map.Entry<Integer, TreeMap<Month, TreeMap<Integer, DefaultMutableTreeNode>>> yearEntry : yearMap.entrySet()) {
            DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(yearEntry.getKey());
            root.add(yearNode);

            for (Map.Entry<Month, TreeMap<Integer, DefaultMutableTreeNode>> monthEntry : yearEntry.getValue().entrySet()) {
                String monthName = monthEntry.getKey().toString();
                DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(monthName);
                yearNode.add(monthNode);

                for (Map.Entry<Integer, DefaultMutableTreeNode> dayEntry : monthEntry.getValue().entrySet()) {
                    monthNode.add(dayEntry.getValue());
                }
            }
        }

        expandCurrentYearAndMonth();
    }

    // Methode zur Umwandlung eines Month-Objekts in einen formatierten Monatsnamen
    private String getMonthName(Month month) {
        return month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault());
    }

    // Hilfsmethode zum Suchen oder Erstellen eines Knotens
    private DefaultMutableTreeNode findOrCreateNode(DefaultMutableTreeNode parent, Object userObject) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parent.getChildAt(i);
            if (childNode.getUserObject().equals(userObject)) {
                return childNode;
            }
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(userObject);
        parent.add(newNode);
        return newNode;
    }

    public void actualizeAppointmentScrollTree() {
        // clear current tree and repopulate it
        tree.removeTreeSelectionListener(listener);
        root.removeAllChildren();
        populateTree();
        treeModel.reload();
        expandCurrentYearAndMonth();
        tree.addTreeSelectionListener(listener);
    }

    private void expandCurrentYearAndMonth() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        String currentMonth = today.getMonth().toString();

        DefaultMutableTreeNode yearNode = findChildNode(root, currentYear);
        if (yearNode != null) {
            tree.expandPath(new TreePath(yearNode.getPath()));  // Jahrknoten ausklappen

            DefaultMutableTreeNode monthNode = findChildNode(yearNode, currentMonth);
            if (monthNode != null) {
                tree.expandPath(new TreePath(monthNode.getPath()));  // Monatsknoten ausklappen

                // Alle Tage des aktuellen Monats ausklappen
                for (int i = 0; i < monthNode.getChildCount(); i++) {
                    TreeNode dayNode = monthNode.getChildAt(i);
                    tree.expandPath(new TreePath(((DefaultMutableTreeNode) dayNode).getPath()));
                }
            }
        }
    }

    private static DefaultMutableTreeNode findChildNode(DefaultMutableTreeNode parent, Object userObject) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parent.getChildAt(i);
            if (childNode.getUserObject().equals(userObject)) {
                return childNode;
            }
        }
        return null;
    }
}
