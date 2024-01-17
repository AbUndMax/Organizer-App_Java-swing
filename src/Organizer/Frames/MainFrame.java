package Organizer.Frames;

import Organizer.Tabs.AddressBookTab.AddressBook;
import Organizer.Tabs.NoteBookTab.NoteBook;
import Organizer.Tabs.PhoneBookTab.PhoneBook;
import Organizer.Tabs.SchedulerTab.Scheduler;
import Organizer.Tools.MenuBarListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final JTabbedPane organizerTabs = new JTabbedPane();

    private static final PhoneBook phoneBookPane = new PhoneBook();
    private static final AddressBook addressBookPane = new AddressBook();
    public static final NoteBook noteBookPane = new NoteBook();
    private static final Scheduler schedulerPane = new Scheduler();
    private static final JMenuBar menuBar = new JMenuBar();
    private static final JMenu IOMenu = new JMenu("menu");
    private static final JMenuItem menuImport = new JMenuItem("import");
    private static final JMenuItem menuExport = new JMenuItem("export");
    private static final MenuBarListener menuBarListener = new MenuBarListener();

    public MainFrame(){
        setTitle("Organizer");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setSize(800, 700);
        setMinimumSize(new Dimension(300, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuImport.addActionListener(menuBarListener);
        menuImport.setActionCommand("Import");
        menuExport.addActionListener(menuBarListener);
        menuExport.setActionCommand("Export");

        IOMenu.add(menuImport);
        IOMenu.add(menuExport);
        menuBar.add(IOMenu);
        setJMenuBar(menuBar);

        add(Box.createVerticalStrut(5));

        organizerTabs.setAlignmentX(Component.CENTER_ALIGNMENT);
        organizerTabs.addTab("Phone Book", phoneBookPane);
        organizerTabs.addTab("Address Book", addressBookPane);
        organizerTabs.addTab("Notebook", noteBookPane);
        organizerTabs.addTab("Scheduler", schedulerPane);
        add(organizerTabs);

    }

}
