package Organizer.Tabs.SchedulerTab;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TestScheduler {
    private static final Scheduler schedulerPane = new Scheduler();
    private static JFrame frame = new JFrame();
    private static final JTabbedPane organizerTabs = new JTabbedPane();
    public static void main(String[] args) {
        setupFrame();
    }

    private static void setupFrame() {
        frame.setTitle("Scheduler Tester");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(300, 400));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        organizerTabs.setAlignmentX(Component.CENTER_ALIGNMENT);
        organizerTabs.addTab("Scheduler", schedulerPane);
        frame.add(organizerTabs);

        frame.setVisible(true);
    }
}
