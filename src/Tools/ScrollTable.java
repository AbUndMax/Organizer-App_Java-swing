package Tools;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/*
Goals:
Visualize in ScrollPane
Allow to add entrys
provide save/open functionality
search

 */

public class ScrollTable extends JPanel {

    private String[] colNames;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTextField searchBar = new JTextField("Search...");
    private final JComboBox columnChooser = new JComboBox();
    private final JPanel searchBarPane = new JPanel();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private ArrayList<String[]> tableContent;

    public ScrollTable(String[] colNames, ArrayList<String[]> tableContent) {
        this.colNames = colNames;
        this.tableContent = tableContent;
        searchBar.getDocument().addDocumentListener(new SearchBarList());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setupSearchBar();

        setupScrollTextArea();

        add(Box.createVerticalStrut(10));
        add(searchBarPane);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
    }

    private void setupSearchBar() {
        searchBarPane.setLayout(new BoxLayout(searchBarPane, BoxLayout.X_AXIS));

        searchBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchBar.setMaximumSize(new Dimension(200, searchBar.getMinimumSize().height));

        for (String columnName: colNames) {
            columnChooser.addItem(columnName);
        }

        columnChooser.setMaximumSize(new Dimension(200, columnChooser.getMinimumSize().height));

        searchBarPane.add(searchBar);
        searchBarPane.add(Box.createHorizontalStrut(10));
        searchBarPane.add(columnChooser);

        searchBarPane.setMaximumSize(new Dimension(searchBarPane.getMaximumSize().width, searchBarPane.getMinimumSize().height));
    }

    private void setupScrollTextArea() {

        for (String name : colNames) {
            tableModel.addColumn(name);
        }

        table.setModel(tableModel);

        scrollPane.setViewportView(table);
    }

    // input searchPattern -> output matching entries
    public ArrayList<String[]> searchTable(String searchPattern, int columnToSearch) {

        ArrayList<String[]> searchedTableContent = new ArrayList<>();

        for (String[] array : tableContent) {
            if (array[columnToSearch].contains(searchPattern)) {
                searchedTableContent.add(array);
            }
        }

        return searchedTableContent;
    }

    // input ArrayList<String> -> draws new table
    public void actualizeTable(ArrayList<String[]> tableContent) {

        //zurücksetzen des table
        tableModel.setRowCount(0);

        // draw table based on input new
        for (String[] array : tableContent){
            tableModel.addRow(array);
        }
    }

    private void reactToSearchBar(){
        actualizeTable(searchTable(searchBar.getText(), columnChooser.getSelectedIndex()));
    }

    class SearchBarList implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            reactToSearchBar();
        }
        public void removeUpdate(DocumentEvent e) {
            reactToSearchBar();
        }
        public void insertUpdate(DocumentEvent e) {
            reactToSearchBar();
        }
    }
}
