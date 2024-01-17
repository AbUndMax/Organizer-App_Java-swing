package Organizer.Tabs.NoteBookTab;

import Organizer.Frames.MainFrame;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile() {
        try {
            Files.delete(MainFrame.noteBookPane.getCurrentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainFrame.noteBookPane.actualizeList();
    }
}