package Organizer.SubPrograms.NoteBookTab;

import Organizer.Frames.MainFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ListListener implements ListSelectionListener {

    private JTextArea textArea;

    public ListListener(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {  // don't react twice on a single click

            // reset Area
            textArea.setText("");

            // get selected value
            JList sourceList = (JList) e.getSource();
            String selectedValue = (String) sourceList.getSelectedValue();

            if (selectedValue == null) {
                return;
            }

            // set current file
            MainFrame.noteBookPane.setCurrentFile(Paths.get("Files/NoteBookFiles/" + selectedValue));

            // read file and set content
            try {
                MainFrame.noteBookPane.setAreaContent(Files.readAllLines(MainFrame.noteBookPane.getCurrentFile()));
                for (String line : MainFrame.noteBookPane.getAreaContent()) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
