package Organizer.Tools;

import Organizer.Frames.MainFrame;
import Organizer.Main;
import Organizer.Tabs.AddressBookTab.AddressBook;
import Organizer.Tabs.NoteBookTab.NoteBook;
import Organizer.Tabs.PhoneBookTab.PhoneBook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuBarListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch(command) {
            case "Import":
                String filePath;
                if ((filePath = getFilePath()) != null) {
                    // on phoneBook Tab
                    if (MainFrame.organizerTabs.getSelectedIndex() == 0){
                        PhoneBook.loadUserPhoneBookFile(filePath);
                    }
                    // on AddressBook Tab
                    else if (MainFrame.organizerTabs.getSelectedIndex() == 1) {
                        AddressBook.loadUserAddressBookFile(filePath);
                    }
                    // on NoteBook Tab
                    else if (MainFrame.organizerTabs.getSelectedIndex() == 2) {
                        NoteBook.importFile(filePath);
                    }
                    //on Scheduler Tab
                    else if (MainFrame.organizerTabs.getSelectedIndex() == 3) {

                    }
                }

                break;
            case "Export":
                String savePath;
                if ((savePath = getSavePath()) != null) {
                    // on phoneBook Tab
                    if (MainFrame.organizerTabs.getSelectedIndex() == 0){
                        PhoneBook.saveToFile(savePath);
                    }
                    // on AddressBook Tab
                    else if (MainFrame.organizerTabs.getSelectedIndex() == 1) {
                        AddressBook.saveToFile(savePath);
                    }
                    // on NoteBook Tab
                    else if (MainFrame.organizerTabs.getSelectedIndex() == 2) {
                        NoteBook.exportFile(savePath);
                    }
                    //on Scheduler Tab
                    else if (MainFrame.organizerTabs.getSelectedIndex() == 3) {

                    }
                    break;
                }
        }
    }

    // returns the path of the file that should be imported
    private String getFilePath() {

        JFileChooser chooser = new JFileChooser();
        Boolean isFileSelected = false;
        String filePath = null;

        do {
            // open FileChooser
            try {
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = chooser.showOpenDialog(Main.mainFrame);

                // check if file is selected
                if (result == JFileChooser.APPROVE_OPTION) {
                    filePath = chooser.getSelectedFile().getAbsolutePath();
                    isFileSelected = true;

                    // check File if Format is correct & Load it into the system-> else throw error


                } else if (result == JFileChooser.CANCEL_OPTION){
                    return null;
                }
            } catch (Exception xptn) {
                isFileSelected = false;
                JOptionPane.showMessageDialog(Main.mainFrame, "Error: " + xptn.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } while (!isFileSelected);

        return filePath;
    }

    // returns the path where the file should be saved
    private String getSavePath() {
        String saveFilePath = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(Main.mainFrame);

        // check if file is selected
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File chosenFile = fileChooser.getSelectedFile();
            saveFilePath = chosenFile.getAbsolutePath();
            System.out.println("Selected file: " + saveFilePath);
        }
        else if (userSelection == JFileChooser.CANCEL_OPTION) {
            return null;
        }
        return saveFilePath;
    }

}
