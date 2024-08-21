package Organizer.Frames;

import Organizer.SubPrograms.ContactBookTab.ContactBook;
import Organizer.SubPrograms.NoteBookTab.NoteBook;
import Organizer.SubPrograms.SchedulerTab.Scheduler;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final JTabbedPane organizerTabs = new JTabbedPane();

    public static final ContactBook contactBook = new ContactBook();
    public static final NoteBook noteBookPane = new NoteBook();
    private static final Scheduler schedulerPane = new Scheduler();

    public MainFrame(){
        setTitle("Organizer");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setSize(1000, 700);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(Box.createVerticalStrut(5));

        organizerTabs.setAlignmentX(Component.CENTER_ALIGNMENT);
        organizerTabs.addTab("Contact Book", contactBook);
        organizerTabs.addTab("Notebook", noteBookPane);
        organizerTabs.addTab("Scheduler", schedulerPane);
        add(organizerTabs);

    }

}
