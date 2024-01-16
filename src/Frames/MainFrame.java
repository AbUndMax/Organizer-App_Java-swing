package Frames;

import Tabs.AddressBookTab.AddressBook;
import Tabs.NoteBookTab.NoteBook;
import Tabs.PhoneBookTab.PhoneBook;
import Tabs.SchedulerTab.Scheduler;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final JTabbedPane organizerTabs = new JTabbedPane();

    private static final PhoneBook phoneBookPane = new PhoneBook();
    private static final AddressBook addressBookPane = new AddressBook();
    private static final NoteBook noteBookPane = new NoteBook();
    private static final Scheduler schedulerPane = new Scheduler();

    public MainFrame(){
        setTitle("Organizer");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setSize(800, 700);
        setMinimumSize(new Dimension(300, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(Box.createVerticalStrut(5));

        organizerTabs.setAlignmentX(Component.CENTER_ALIGNMENT);
        organizerTabs.addTab("Phone Book", phoneBookPane);
        organizerTabs.addTab("Address Book", addressBookPane);
        organizerTabs.addTab("Notebook", noteBookPane);
        organizerTabs.addTab("Scheduler", schedulerPane);
        add(organizerTabs);

    }
}
