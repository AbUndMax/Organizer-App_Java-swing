package Organizer.SubPrograms.NoteBookTab;

import Organizer.Frames.MainFrame;
import Organizer.Main;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NewNoteDialog extends JDialog {

    private static final JPanel messagePane = new JPanel();
    private final JTextField getNoteName = new JTextField("name your note...");
    private JButton createButton = new JButton("create note");

    public NewNoteDialog() {
        super(Main.mainFrame, "new note", true);
        setSize(new Dimension(300, 150));
        setResizable(false);
        setLocationRelativeTo(Main.mainFrame);
        setLayout(new GridBagLayout());


        messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.Y_AXIS));


        createButton.setActionCommand("createNote");
        createButton.addActionListener(e -> createFile());

        getNoteName.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        getNoteName.setPreferredSize(new Dimension(230, 35));

        messagePane.add(Box.createVerticalGlue());
        messagePane.add(getNoteName);
        messagePane.add(Box.createVerticalStrut(5));
        messagePane.add(createButton);
        messagePane.add(Box.createVerticalGlue());

        add(messagePane);
        this.setVisible(true);
    }

    private void createFile() {
        try {
            Files.createFile(Paths.get("Files/NoteBookFiles/" + getNoteName.getText()));
            this.dispose();
            MainFrame.noteBookPane.actualizeList();
        }
        catch (Exception expt) {
            JOptionPane.showMessageDialog(Main.mainFrame, "Error: " + expt.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            expt.printStackTrace();
        }
    }
}
