package Organizer.SubPrograms.NoteBookTab;

import Organizer.Database.NoteBookTable;

import javax.swing.*;
import java.awt.*;

public class NoteBook extends JSplitPane {

    private final JTextArea textArea = new JTextArea();

    private final DefaultListModel<NoteBookEntry> listModel = new DefaultListModel<>();
    private final JList<NoteBookEntry> noteTitleList = new JList<>(listModel) {{
        setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.title());
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setOpaque(true);
            return label;
        });

        addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                NoteBookEntry entry = getSelectedValue();
                if (entry != null) {
                    textArea.setText(NoteBookTable.getNoteContent(entry.id()));
                }
            }
        });
    }};

    private final JButton newFileButton = new JButton("create new Note") {{
        addActionListener(e -> {
            new NewNoteDialog(listModel);
        });
    }};

    private final JButton saveButton = new JButton("save changes") {{
        addActionListener(e -> {
            NoteBookTable.updateDB(noteTitleList.getSelectedValue(), textArea.getText());
        });
    }};

    private final JButton deleteFileButton = new JButton("delete Note") {{
        addActionListener(e -> {
            NoteBookTable.deleteDBTuple(noteTitleList.getSelectedValue().id());
            listModel.removeElementAt(noteTitleList.getSelectedIndex());
            noteTitleList.setSelectedIndex(0);
        });
    }};

    public NoteBook () {
    setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    JScrollPane textScrollPane = new JScrollPane();
    textScrollPane.setViewportView(textArea);

    setLeftComponent(setupListPane());
    setRightComponent(textScrollPane);
    }

    private JPanel setupListPane(){
        JPanel listPane = new JPanel(new BorderLayout());
        listPane.setLayout(new BorderLayout());

        JScrollPane listScrollPane = new JScrollPane();
        JPanel buttonPane = new JPanel(new GridLayout(3,1));

        fillList();

        listScrollPane.setViewportView(noteTitleList);
        listPane.add(listScrollPane, BorderLayout.CENTER);

        buttonPane.add(saveButton);
        buttonPane.add(newFileButton);
        buttonPane.add(deleteFileButton);
        listPane.add(buttonPane, BorderLayout.SOUTH);

        return listPane;
    }

    private void fillList() {
        for (NoteBookEntry entry : NoteBookTable.loadFullTable()) {
            listModel.addElement(entry);
        }
    }

}