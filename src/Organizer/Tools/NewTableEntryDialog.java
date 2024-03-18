package Organizer.Tools;

import Organizer.Main;

import javax.swing.*;
import java.awt.*;

public class NewTableEntryDialog extends JDialog {

    private final JLabel[] labels;
    private final JTextField[] fields;
    private final JButton addButton = new JButton("add");
    private final JButton closeButton = new JButton("close");
    private final String[] userInput;

    public NewTableEntryDialog(String[] fieldNames, String[] userInput) {
        setLocationRelativeTo(Main.mainFrame);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        setModal(true);

        this.userInput = userInput;

        int arrayLength = fieldNames.length;
        // largestWidth is used to set all JTextField Widths to the largest TextField
        int largestWidth = 0;
        // frameWidth is used to set the frames width (based on the largest Panel composed of Label and TextField
        int frameWidth = 0;

        setLayout(new GridLayout(arrayLength + 1, 1));

        // initialize all Labels
        labels = new JLabel[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            labels[i] = new JLabel(fieldNames[i] + ": ");
        }

        // initialize all TextFields & determine the largest
        fields = new JTextField[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            fields[i] = new JTextField(fieldNames[i] + "...");
            JTextField field = fields[i];
            int width = field.getPreferredSize().width;

            if (width > largestWidth) {
                largestWidth = width;
            }
        }

        // if none of the TextFields were larger than 200, we set the minimum to 200
        if (largestWidth < 200) largestWidth = 200;

        // set all TextFields width to the largestWidth
        for (JTextField field : fields) {
            field.setPreferredSize(new Dimension(largestWidth, field.getPreferredSize().height));
        }

        // for each Label and TextField a panel is build and added to the Dialog
        for (int i = 0; i < arrayLength; i++) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.add(labels[i]);
            panel.add(fields[i]);

            add(panel);
            int panelWidth = panel.getPreferredSize().width;
            if (panelWidth > frameWidth) frameWidth = panelWidth;
        }

        // setup the controlButtons:
        JPanel buttonPane = new JPanel();
        BoxLayout buttonBoxLayout = new BoxLayout(buttonPane, BoxLayout.X_AXIS);
        buttonPane.setLayout(buttonBoxLayout);

        buttonPane.add(closeButton);
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(addButton);

        add(buttonPane);

        // set window height based on number of fields
        setSize(frameWidth, this.getPreferredSize().height);

        closeButton.addActionListener(e -> this.dispose());
        addButton.addActionListener(e -> saveUserInput());

        setVisible(true);
    }

    // gets all texts in the TextFields in one String array.
    public void saveUserInput() {
        for (int i = 0; i < fields.length; i++) {
            userInput[i] = fields[i].getText();
        }

        this.dispose();
    }
}
