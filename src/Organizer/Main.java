package Organizer;

import Organizer.Frames.MainFrame;
import Organizer.Database.Database;

public class Main {

    public static MainFrame mainFrame;

    public static void main(String[] args) {
        Database.createDatabase();
        newMainFrame();
        mainFrame.setVisible(true);
    }

    public static void newMainFrame() {
        mainFrame = new MainFrame();
    }
}