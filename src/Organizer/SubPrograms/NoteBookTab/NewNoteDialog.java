package Organizer.SubPrograms.NoteBookTab;

import Organizer.Database.NoteBookTable;
import Organizer.Main;

import javax.swing.*;
import java.awt.*;

public class NewNoteDialog extends JDialog {

    private static final JPanel messagePane = new JPanel();
    private final JTextField getNoteName = new JTextField("name your note...");
    private final DefaultListModel<NoteBookEntry> listModel;

    public NewNoteDialog(DefaultListModel<NoteBookEntry> listModel) {
        super(Main.mainFrame, "new note", true);
        this.listModel = listModel;
        setSize(new Dimension(300, 150));
        setResizable(false);
        setLocationRelativeTo(Main.mainFrame);
        setLayout(new GridBagLayout());


        messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.Y_AXIS));


        JButton createButton = new JButton("create note");
        createButton.setActionCommand("createNote");
        createButton.addActionListener(e -> createNewNoteInDB());

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

    private void createNewNoteInDB() {
        if (getNoteName.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name for your note.");

        } else {
            NoteBookEntry newEntry = NoteBookTable.newDBTuple(getNoteName.getText(), "");
            listModel.addElement(newEntry);
        }

        this.dispose();
    }
}
