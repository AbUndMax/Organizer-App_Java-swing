package Tabs.AddressBookTab;

import javax.swing.*;
import java.awt.*;

public class AddressBook extends JPanel {
    private static final String[] colNames = {"name", "street & number", "city", "postal-code", "country"};
    //private static final ScrollTable scrollTable = new ScrollTable(colNames);

    public AddressBook() {
        setLayout(new BorderLayout());

        //add(scrollTable, BorderLayout.CENTER);

    }
}
