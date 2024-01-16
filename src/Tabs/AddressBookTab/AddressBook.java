package Tabs.AddressBookTab;

import Tools.ScrollTable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class AddressBook extends JPanel {
    private static final String[] colNamesAddresses = {"name", "street & number", "city", "postal-code", "country"};
    //private static final ScrollTable scrollTable = new ScrollTable(colNames);
    private static ArrayList<String[]> AddressEntries = new ArrayList<>();
    private static final ScrollTable AddressScrollTable = new ScrollTable(colNamesAddresses, AddressEntries);

    public AddressBook() {
        setLayout(new BorderLayout());

        add(AddressScrollTable, BorderLayout.CENTER);

        // Load default file into the table
        loadDefaultAddressBookTable();
    }

    // gets file -> loads Data into system i.e. instantiates PhoneBook Objects
    private void createAddressBookEntry(String phoneBookFilePath) {
        // File Structure: name surname, number

        try (BufferedReader bufferedIN = new BufferedReader(new FileReader(phoneBookFilePath));) {
            // Creating a BufferedReader to read the file
            String line = bufferedIN.readLine();


            do {
                // Splitting the line into name, surname and number
                String[] lineArray = line.split(",\\s");

                AddressEntries.add(lineArray);

            } while ((line = bufferedIN.readLine()) != null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // opens the default PhoneBookFile
    private void loadDefaultAddressBookTable(){
        createAddressBookEntry("Files/AddressBookFiles/address_book_default.txt");
        AddressScrollTable.actualizeTable(AddressEntries);
    }

    // opens FileChooser and lets user select a file
    public void loadUserAddressBookFile(String filePath){

        // delete current table entries
        AddressEntries.clear();

        // load new file
        createAddressBookEntry(filePath);

        // actualize table
        AddressScrollTable.actualizeTable(AddressEntries);
    }
}
