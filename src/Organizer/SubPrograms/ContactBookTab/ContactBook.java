package Organizer.SubPrograms.ContactBookTab;

import Organizer.Database.ContactBookTable;
import Organizer.Tools.NewTableEntryDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ContactBook extends JPanel {

    private static final ContactBookTable contactBook = new ContactBookTable();
    private static final String[] colNames = {"Name", "Surname" ,"Phone Number", "Street", "House Number", "City", "Postal Code", "Country"};
    private final JTable table = new JTable(new ContactBookTableModel(contactBook.loadFullTable()));
    private final JComboBox<String> columnChooser = new JComboBox<>(colNames);

    private final JTextField searchBar = new JTextField("Search..."){{
        getDocument().addDocumentListener(new DocumentListener() {

            private void showSearchResults(){
                LinkedList<ContactBookEntry> matchingEntries = contactBook.searchInDB(columnChooser.getSelectedItem().toString(), searchBar.getText());
                ((ContactBookTableModel) table.getModel()).contacts.clear();
                ((ContactBookTableModel) table.getModel()).contacts.addAll(matchingEntries);
                ((ContactBookTableModel) table.getModel()).fireTableDataChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                showSearchResults();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showSearchResults();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showSearchResults();
            }
        });
    }};

    private final JButton newEntryButton = new JButton("new entry") {{
        addActionListener( e -> {
            String[] userInput = new String[colNames.length];
            new NewTableEntryDialog(colNames, userInput);

            boolean anyNull = userInput == null || userInput.length == 0 || userInput[0] == null;

            if (!anyNull) {
                ContactBookEntry newEntry = contactBook.newDBTuple(userInput[0], userInput[1], userInput[2], userInput[3], userInput[4], userInput[5], userInput[6], userInput[7]);
                ((ContactBookTableModel) table.getModel()).addContact(newEntry);
            }
        });
    }};

    private final JButton delEntryButton = new JButton("delete selected entry") {{
        addActionListener( e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                ContactBookEntry contact = ((ContactBookTableModel) table.getModel()).getContactAt(selectedRow);
                contactBook.deleteDBTuple(contact.id());
                ((ContactBookTableModel) table.getModel()).removeContact(selectedRow);
            }
        });
    }};

    /**
     * Constructor
     */
    public ContactBook() {
        setLayout(new BorderLayout());

        add(controlPane(), BorderLayout.NORTH);
        add(scrollTable(), BorderLayout.CENTER);
    }

    /**
     * generates the JPanel with the control buttons "newEntry", "DeleteEntry" and the searchbar as well as the searchIn chooser
     * @return JPanel for Control Buttons
     */
    private JPanel controlPane() {
        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.X_AXIS));

        searchBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        Dimension searchBarDimension = new Dimension(200, searchBar.getMinimumSize().height);
        searchBar.setMaximumSize(searchBarDimension);
        searchBar.setPreferredSize(searchBarDimension);

        for (String columnName: colNames) {
            columnChooser.addItem(columnName);
        }

        columnChooser.setMaximumSize(new Dimension(200, columnChooser.getMinimumSize().height));

        controlPane.add(Box.createHorizontalStrut(10));
        controlPane.add(delEntryButton);
        controlPane.add(Box.createHorizontalStrut(10));
        controlPane.add(newEntryButton);
        controlPane.add(Box.createHorizontalGlue());
        controlPane.add(searchBar);
        controlPane.add(Box.createHorizontalStrut(10));
        controlPane.add(columnChooser);
        controlPane.add(Box.createHorizontalStrut(10));

        controlPane.setMaximumSize(new Dimension(controlPane.getMaximumSize().width, controlPane.getMinimumSize().height));

        return controlPane;
    }

    /**
     * Generates the Table with all the entries of the DB inside of it
     * @return the ScrollTable
     */
    private JScrollPane scrollTable() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        return scrollPane;
    }

    public class ContactBookTableModel extends AbstractTableModel {

        private final List<ContactBookEntry> contacts;

        public ContactBookTableModel(List<ContactBookEntry> contacts) {
            this.contacts = contacts;
        }

        @Override
        public int getRowCount() {
            return contacts.size();
        }

        @Override
        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ContactBookEntry contact = contacts.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> contact.name();
                case 1 -> contact.surname();
                case 2 -> contact.phoneNumber();
                case 3 -> contact.street();
                case 4 -> contact.houseNumber();
                case 5 -> contact.city();
                case 6 -> contact.postalCode();
                case 7 -> contact.country();
                default -> null;
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ContactBookEntry contact = contacts.get(rowIndex);

            ContactBookEntry updatedContact = switch (columnIndex) {
                case 0 -> new ContactBookEntry(contact.id(), (String) aValue, contact.surname(), contact.phoneNumber(), contact.street(), contact.houseNumber(), contact.city(), contact.postalCode(), contact.country());
                case 1 -> new ContactBookEntry(contact.id(), contact.name(), (String) aValue, contact.phoneNumber(), contact.street(), contact.houseNumber(), contact.city(), contact.postalCode(), contact.country());
                case 2 -> new ContactBookEntry(contact.id(), contact.name(), contact.surname(), (String) aValue, contact.street(), contact.houseNumber(), contact.city(), contact.postalCode(), contact.country());
                case 3 -> new ContactBookEntry(contact.id(), contact.name(), contact.surname(), contact.phoneNumber(), (String) aValue, contact.houseNumber(), contact.city(), contact.postalCode(), contact.country());
                case 4 -> new ContactBookEntry(contact.id(), contact.name(), contact.surname(), contact.phoneNumber(), contact.street(), (String) aValue, contact.city(), contact.postalCode(), contact.country());
                case 5 -> new ContactBookEntry(contact.id(), contact.name(), contact.surname(), contact.phoneNumber(), contact.street(), contact.houseNumber(), (String) aValue, contact.postalCode(), contact.country());
                case 6 -> new ContactBookEntry(contact.id(), contact.name(), contact.surname(), contact.phoneNumber(), contact.street(), contact.houseNumber(), contact.city(), (String) aValue, contact.country());
                case 7 -> new ContactBookEntry(contact.id(), contact.name(), contact.surname(), contact.phoneNumber(), contact.street(), contact.houseNumber(), contact.city(), contact.postalCode(), (String) aValue);
                default -> contact;
            };

            contacts.set(rowIndex, updatedContact);
            contactBook.updateDB(updatedContact);
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        public void addContact(ContactBookEntry contact) {
            contacts.add(contact);
            fireTableRowsInserted(contacts.size() - 1, contacts.size() - 1);
        }

        public void removeContact(int rowIndex) {
            contacts.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }

        public ContactBookEntry getContactAt(int rowIndex) {
            return contacts.get(rowIndex);
        }
    }

}
