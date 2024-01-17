package Organizer;

import Organizer.Frames.MainFrame;

public class Main {

    public static MainFrame mainFrame;

    public static void main(String[] args) {
        newMainFrame();
        mainFrame.setVisible(true);
    }

    public static void newMainFrame() {
        mainFrame = new MainFrame();
    }
}