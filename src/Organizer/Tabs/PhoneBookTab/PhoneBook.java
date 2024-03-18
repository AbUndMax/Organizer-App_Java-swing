package Organizer.Tabs.PhoneBookTab;

import Organizer.Main;
import Organizer.Tools.NewTableEntryDialog;
import Organizer.Tools.ScrollTable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PhoneBook extends JPanel {

    private static final String[] phoneBookColNames = {"name", "surname", "phone-number"};
    private static ArrayList<String[]> phoneBookEntries = new ArrayList<>();
    private static String currentFilePath;
    private static final ScrollTable phoneBookScrollTable = new ScrollTable(phoneBookColNames, phoneBookEntries,
                                                                            () -> saveToFile(currentFilePath));

    public PhoneBook() {
        setLayout(new BorderLayout());

        add(phoneBookScrollTable, BorderLayout.CENTER);

        // Load default file into the table
        loadDefaultPhoneBookTable();
        phoneBookScrollTable.actualizeColSize();
    }

    // gets file -> loads Data into system
    private static void createPhoneBookEntries(String phoneBookFilePath) {
        // File Structure: name surname, number

        try (BufferedReader bufferedIN = new BufferedReader(new FileReader(phoneBookFilePath));) {
            // Creating a BufferedReader to read the file
            String line = bufferedIN.readLine();


            do {
                // Splitting the line into name, surname and number
                String[] lineArray = line.split(",\\s|\\s");

                phoneBookEntries.add(lineArray);

            } while ((line = bufferedIN.readLine()) != null);
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Main.mainFrame, "Error: File not Compatible", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    // opens the default PhoneBookFile
    private void loadDefaultPhoneBookTable(){
        currentFilePath = "Files/PhoneBookFiles/phone_book_default.txt";
        createPhoneBookEntries(currentFilePath);
        phoneBookScrollTable.actualizeTable();
    }

    // opens FileChooser and lets user select a file
    public static void loadUserPhoneBookFile(String filePath){

        //set current FilePath
        currentFilePath = filePath;

        // delete current table entries
        phoneBookEntries.clear();

        // load new file
        createPhoneBookEntries(filePath);

        // actualize table
        phoneBookScrollTable.actualizeTable();
    }

    public static void saveToFile(String filePath) {
        try (BufferedWriter bufferedOUT = new BufferedWriter(new FileWriter(filePath, false))) {

            // Writing each entry in the HashMap to the file
            for (String[] array : phoneBookEntries) {
                bufferedOUT.write(array[0] + " " + array[1] + ", " + array[2] + "\n");
            }
        }

        catch (Exception xptn){
            System.out.println(xptn);
        }
    }
}
