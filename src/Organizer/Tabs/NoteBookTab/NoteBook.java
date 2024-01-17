package Organizer.Tabs.NoteBookTab;

import Organizer.Frames.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class NoteBook extends JSplitPane {

    private final JScrollPane listScrollPane = new JScrollPane();
    private final JPanel listPane = new JPanel();
    private final JButton newFileButton = new JButton("create new Note");
    private final JButton saveButton = new JButton("save changes");
    private final JButton deleteFileButton = new JButton("delete Note");
    private final JScrollPane textScrollPane = new JScrollPane();
    private final JList list = new JList<>();
    private final JTextArea textArea = new JTextArea();
    private List<String> areaContent;
    private Path currentFile;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final NoteButtonListener buttonListener = new NoteButtonListener(textArea);

    public NoteBook () {
    setOrientation(JSplitPane.HORIZONTAL_SPLIT);

    setupList();
    setupTextArea();

    list.addListSelectionListener(new ListListener(textArea));

    setLeftComponent(listPane);
    setRightComponent(textScrollPane);

    }

    private void setupList(){

        File folder = new File("Files/NoteBookFiles");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    listModel.addElement(file.getName());
                }
            }
        }

        list.setModel(listModel);
        listScrollPane.setViewportView(list);

        listPane.setLayout(new BorderLayout());

        listPane.add(listScrollPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new GridLayout(3,1));

        newFileButton.setActionCommand("newFile");
        saveButton.setActionCommand("saveNote");
        deleteFileButton.setActionCommand("delNote");

        newFileButton.addActionListener(buttonListener);
        saveButton.addActionListener(buttonListener);
        deleteFileButton.addActionListener(buttonListener);;

        buttonPane.add(saveButton);
        buttonPane.add(newFileButton);
        buttonPane.add(deleteFileButton);

        listPane.add(buttonPane, BorderLayout.SOUTH);

    }

    private void setupTextArea() {
        textScrollPane.setViewportView(textArea);
    }

    public void actualizeList(){

        listModel.clear();
        File folder = new File("Files/NoteBookFiles");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    listModel.addElement(file.getName());
                }
            }
        }
    }

    public List<String> getAreaContent() {
        return areaContent;
    }

    public void setAreaContent(List<String> areaContent) {
        this.areaContent = areaContent;
    }

    public Path getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(Path currentFile) {
        this.currentFile = currentFile;
    }

    public static void importFile(String filePath) {
        Path sourcePath = Paths.get(filePath);
        Path targetPath = Paths.get("Files/NoteBookFiles");

        try {
            Files.copy(sourcePath, targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportFile(String filePath) {
        Path targetPath = Paths.get(filePath);
        Path sourcePath = MainFrame.noteBookPane.getCurrentFile();

        if (sourcePath != null) {
            try {
                Files.copy(sourcePath, targetPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}