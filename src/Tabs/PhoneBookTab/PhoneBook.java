package Tabs.PhoneBookTab;

import Tools.ScrollTable;

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
    private static final ScrollTable phoneBookScrollTable = new ScrollTable(phoneBookColNames, phoneBookEntries, () -> saveToFile());
    private static String currentFilePath;

    public PhoneBook() {
        setLayout(new BorderLayout());

        add(phoneBookScrollTable, BorderLayout.CENTER);

        // Load default file into the table
        loadDefaultPhoneBookTable();
    }

    // gets file -> loads Data into system i.e. instantiates PhoneBook Objects
    private void createPhoneBookEntries(String phoneBookFilePath) {
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
        }
    }

    // opens the default PhoneBookFile
    private void loadDefaultPhoneBookTable(){
        currentFilePath = "Files/PhoneBookFiles/phone_book_default.txt";
        createPhoneBookEntries(currentFilePath);
        phoneBookScrollTable.actualizeTable(phoneBookEntries);
    }

    // opens FileChooser and lets user select a file
    public void loadUserPhoneBookFile(String filePath){

        // delete current table entries
        phoneBookEntries.clear();

        // load new file
        createPhoneBookEntries(filePath);

        // actualize table
        phoneBookScrollTable.actualizeTable(phoneBookEntries);
    }

    private static void saveToFile() {
        try (BufferedWriter bufferedOUT = new BufferedWriter(new FileWriter(currentFilePath, false))) {

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
