package ui;

import javax.swing.*;

public class Main {

    // EFFECTS: run the store
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StoreGUI::new);
    }
}
