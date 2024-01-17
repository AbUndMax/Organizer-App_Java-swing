package Organizer.Tabs.AddressBookTab;

import Organizer.Main;
import Organizer.Tools.ScrollTable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class AddressBook extends JPanel {
    private static final String[] colNamesAddresses = {"name", "street & number", "city", "postal-code", "country"};
    //private static final ScrollTable scrollTable = new ScrollTable(colNames);
    private static ArrayList<String[]> AddressEntries = new ArrayList<>();
    private static String currentFilePath;
    private static final ScrollTable AddressScrollTable = new ScrollTable(colNamesAddresses, AddressEntries, () -> saveToFile(currentFilePath));

    public AddressBook() {
        setLayout(new BorderLayout());

        add(AddressScrollTable, BorderLayout.CENTER);

        // Load default file into the table
        loadDefaultAddressBookTable();
        AddressScrollTable.actualizeColSize();
    }

    // gets file -> loads Data into system i.e. instantiates PhoneBook Objects
    private static void createAddressBookEntry(String phoneBookFilePath) {
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
            JOptionPane.showMessageDialog(Main.mainFrame, "Error: File not Compatible", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    // opens the default PhoneBookFile
    private void loadDefaultAddressBookTable(){
        currentFilePath = "Files/AddressBookFiles/address_book_default.txt";
        createAddressBookEntry(currentFilePath);

        AddressScrollTable.actualizeTable(AddressEntries);
    }

    // opens FileChooser and lets user select a file
    public static void loadUserAddressBookFile(String filePath){

        currentFilePath = filePath;

        // delete current table entries
        AddressEntries.clear();

        // load new file
        createAddressBookEntry(currentFilePath);

        // actualize table
        AddressScrollTable.actualizeTable(AddressEntries);
    }

    public static void saveToFile(String filePath) {
        try (BufferedWriter bufferedOUT = new BufferedWriter(new FileWriter(filePath, false))) {

            // Writing each entry in the HashMap to the file
            for (String[] array : AddressEntries) {
                bufferedOUT.write(array[0] + ", " + array[1] + ", " +
                        array[2] + ", " + array[3] + ", " + array[3] + ", " + "\n");
            }
        }

        catch (Exception xptn){
            System.out.println(xptn);
        }
    }

}
