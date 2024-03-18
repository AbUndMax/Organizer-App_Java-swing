package Organizer.Tabs.NoteBookTab;

import Organizer.Frames.MainFrame;
import Organizer.Main;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;


public class NoteButtonListener implements ActionListener {

    private JTextArea textArea;

    NoteButtonListener(JTextArea textArea){
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch(command) {
            case "newFile":
                new NewNoteDialog();
                break;

            case "saveNote":
                saveFile();
                break;

            case "delNote":
                deleteFile();
                break;
        }
    }

    private void saveFile() {
        try {

            List<String> content = Arrays.asList(textArea.getText().split("\\n"));
            // wrote the content to the file
            Files.write(MainFrame.noteBookPane.getCurrentFile(), content, StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(Main.mainFrame, "File successfully saved!", "SAVED",
                                          JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Main.mainFrame, "Error: something went wrong :(", "ERROR",
                                          JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteFile() {
        try {
            int response = JOptionPane.showConfirmDialog(Main.mainFrame, "Do you really want to delete this Note?",
                                                         "CONFIRM DELETION", JOptionPane.YES_NO_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return;
            } else if (response == JOptionPane.YES_OPTION) {
                Files.delete(MainFrame.noteBookPane.getCurrentFile());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        MainFrame.noteBookPane.actualizeList();
    }
}