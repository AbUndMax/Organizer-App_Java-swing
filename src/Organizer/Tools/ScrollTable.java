package Organizer.Tools;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrollTable extends JPanel {

    private String[] colNames;
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTextField searchBar = new JTextField("Search...");
    private final JComboBox columnChooser = new JComboBox();
    private final JPanel searchBarPane = new JPanel();
    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane();
    private ArrayList<String[]> tableContent;
    private Runnable writeToFileFunction;

    public ScrollTable(String[] colNames, ArrayList<String[]> tableContent, Runnable writeToFileFunction) {
        this.colNames = colNames;
        this.tableContent = tableContent;
        this.writeToFileFunction = writeToFileFunction;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setupSearchBar();

        setupScrollableTable();

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

        searchBar.getDocument().addDocumentListener(new SearchBarList());

        searchBarPane.setMaximumSize(new Dimension(searchBarPane.getMaximumSize().width, searchBarPane.getMinimumSize().height));
    }

    private void setupScrollableTable() {

        for (String name : colNames) {
            tableModel.addColumn(name);
        }

        table.setModel(tableModel);
        table.getModel().addTableModelListener(new TableListener());

        scrollPane.setViewportView(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    // input searchPattern -> output matching entries
    public ArrayList<String[]> searchTable(String searchPattern, int columnToSearch) {

        ArrayList<String[]> searchedTableContent = new ArrayList<>();
        Pattern compiledPattern = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);

        for (String[] array : tableContent) {
            Matcher matcher = compiledPattern.matcher(array[columnToSearch]);
            if (matcher.find()) {
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

    // listens to changes inside the table, and saves these changes immediately to the file.
    class TableListener implements TableModelListener{
        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (e.getType() == TableModelEvent.UPDATE) {
                TableModel model = (TableModel)e.getSource();
                String columnName = model.getColumnName(column);
                Object data = model.getValueAt(row, column);

                String[] newRow = tableContent.get(row);
                newRow[column] = data.toString();

                tableContent.set(row, newRow);

                /* Debugging Line
                for (String[] array : tableContent){
                    System.out.println(Arrays.toString(array));
                }

                System.out.println("Row: " + row + " Column: " + column);
                System.out.println("Column Name: " + columnName);
                System.out.println("Data: " + data);
                */


                // Änderungen an der Tabelle werden direkt gespeichert !!!!
                writeToFileFunction.run();
            }
        }
    }


    // finds the minimum column size for each column based on the textsizes in each row
    public void actualizeColSize() {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
            }
            tableColumn.setMinWidth(preferredWidth + 20);
        }
    }
}