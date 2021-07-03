/*
  REFERENCES:
  https://www.youtube.com/watch?v=KOI1WbkKUpQ&ab_channel=LazicB.
  https://www.youtube.com/watch?v=Kmgo00avvEw&t=1225s&ab_channel=BroCode
  https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
  https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
 */

package ui;

import model.Item;
import model.Store;
import model.exceptions.InsufficientQuantityException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StoreGUI {

    private final JFrame frame = new JFrame("Store");
    private final JLabel label = new JLabel();
    private final JSplitPane splitPane = new JSplitPane();
    private final JList<Item> list = new JList<>();
    private final DefaultListModel<Item> model = new DefaultListModel<>();

    private final Item printer = new Item("printer", 150);
    private final Item laptop = new Item("laptop", 200);
    private final Item tv = new Item("tv", 375);
    private final Item headphone = new Item("headphone", 30.5);
    private Store store = new Store(50000);

    private static final String JSON_STORE = "./data/store.json";

    private ImageIcon tvImg;
    private ImageIcon printerImg;
    private ImageIcon headphoneImg;
    private ImageIcon laptopImg;

    // EFFECTS: sets up window in which store will be run
    public StoreGUI() {
        JPanel panel = new JPanel();

        loadImages();

        list.setModel(model);
        list.getSelectionModel().addListSelectionListener(e -> {
            Item item = list.getSelectedValue();
            drawImg(item);
            showInfo(item);
        });

        splitPane.setLeftComponent(new JScrollPane(list));
        panel.add(label);
        splitPane.setRightComponent(new JScrollPane(panel));

        frameSetUp();
    }

    // MODIFIES: this
    // EFFECTS: displays image and info of selected item
    private void showInfo(Item item) {
        double price = item.getPrice();
        if (price == 0) {
            label.setText("Quantity: " + item.getQuantity() + "            \n"
                    + "Price: --");
        } else {
            label.setText("Quantity: " + item.getQuantity() + "            \n"
                    + "Price: $" + price);
        }
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setIconTextGap(22);
        label.setFont(new Font("Palatino Linotype", Font.PLAIN, 18));
    }

    // MODIFIES: this
    // EFFECTS: displays image of selected item
    private void drawImg(Item item) {
        if (item.equals(printer)) {
            label.setIcon(printerImg);
        } else if (item.equals(headphone)) {
            label.setIcon(headphoneImg);
        } else if (item.equals(laptop)) {
            label.setIcon(laptopImg);
        } else if (item.equals(tv)) {
            label.setIcon(tvImg);
        }
    }

    // EFFECTS: loads images
    private void loadImages() {
        String sep = System.getProperty("file.separator");
        tvImg = new ImageIcon(System.getProperty("user.dir") + sep
                + "images" + sep + "tv.jpg");
        headphoneImg = new ImageIcon(System.getProperty("user.dir") + sep
                + "images" + sep + "hp.jpg");
        printerImg = new ImageIcon(System.getProperty("user.dir") + sep
                + "images" + sep + "pt.jpg");
        laptopImg = new ImageIcon(System.getProperty("user.dir") + sep
                + "images" + sep + "lt.jpg");
    }

    // EFFECTS: sets up frame of the window
    private void frameSetUp() {
        // menu
        frame.setJMenuBar(createMenuBar());
        frame.setContentPane(createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(splitPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // EFFECTS: creates menu bar
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        fileMenu(menuBar);
        stockMenu(menuBar);
        setPriceMenu(menuBar);
        sellMenu(menuBar);
        storeInfoMenu(menuBar);
        return menuBar;
    }

    // MODIFIES: this, store
    // EFFECTS: creates file menu with action events associated
    private void fileMenu(JMenuBar menuBar) {
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        // Save & Load
        menuItem = new JMenuItem("Save", KeyEvent.VK_S);
        menuItem.addActionListener(e -> saveStore());
        menu.add(menuItem);
        menuItem = new JMenuItem("Load", KeyEvent.VK_L);
        menuItem.addActionListener(e -> loadStore());
        menu.add(menuItem);
    }

    // MODIFIES: this, store, items
    // EFFECTS: creates stock menu with action events associated
    private void stockMenu(JMenuBar menuBar) {
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("Stock");
        menu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(menu);
        // stock items
        menuItem = new JMenuItem("printer");
        menuItem.addActionListener(e -> stockItem(printer));
        menu.add(menuItem);
        menuItem = new JMenuItem("headphone");
        menuItem.addActionListener(e -> stockItem(headphone));
        menu.add(menuItem);
        menuItem = new JMenuItem("laptop");
        menuItem.addActionListener(e -> stockItem(laptop));
        menu.add(menuItem);
        menuItem = new JMenuItem("tv");
        menuItem.addActionListener(e -> stockItem(tv));
        menu.add(menuItem);
    }

    // MODIFIES: this, items
    // EFFECTS: creates set price menu with action events associated
    private void setPriceMenu(JMenuBar menuBar) {
        JMenuItem menuItem;
        JMenu menu;
        menu = new JMenu("Set Price");
        menu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(menu);
        // set prices of items
        menuItem = new JMenuItem("printer");
        menuItem.addActionListener(e -> setPriceOfItem(printer));
        menu.add(menuItem);
        menuItem = new JMenuItem("headphone");
        menuItem.addActionListener(e -> setPriceOfItem(headphone));
        menu.add(menuItem);
        menuItem = new JMenuItem("laptop");
        menuItem.addActionListener(e -> setPriceOfItem(laptop));
        menu.add(menuItem);
        menuItem = new JMenuItem("tv");
        menuItem.addActionListener(e -> setPriceOfItem(tv));
        menu.add(menuItem);
    }

    // MODIFIES: this, store, items
    // EFFECTS: creates sell menu with action events associated
    private void sellMenu(JMenuBar menuBar) {
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("Sell");
        menu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(menu);
        // sell items
        menuItem = new JMenuItem("printer");
        menuItem.addActionListener(e -> sellItem(printer));
        menu.add(menuItem);
        menuItem = new JMenuItem("headphone");
        menuItem.addActionListener(e -> sellItem(headphone));
        menu.add(menuItem);
        menuItem = new JMenuItem("laptop");
        menuItem.addActionListener(e -> sellItem(laptop));
        menu.add(menuItem);
        menuItem = new JMenuItem("tv");
        menuItem.addActionListener(e -> sellItem(tv));
        menu.add(menuItem);
    }

    // EFFECTS: creates store info menu
    private void storeInfoMenu(JMenuBar menuBar) {
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(menu);
        menuItem = new JMenuItem("Store Info");
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "\nCurrent Asset: " + store.getAsset()
                        + "\n\n Total Expenditure: $" + store.getExpenditure()
                        + "\n Total Revenue: $" + store.getRevenue() + "\n\n",
                "Store Info",
                JOptionPane.INFORMATION_MESSAGE));
        menu.add(menuItem);
    }

    // EFFECTS: creates content pane
    private Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        JTextArea output = new JTextArea(5, 30);
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);

        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);

        return contentPane;
    }

    // MODIFIES: this, store, item
    // EFFECTS: stocks q items to store
    private void stockItem(Item item) {
        int q = quantityToStock(item.getName(), item.getCost());
        if (q > 0) {
            if (store.getAsset() >= item.getCost() * q) {
                store.stock(item, q);
            } else {
                errorMsg("Insufficient Asset\n Current asset: $" + store.getAsset());
            }
        } else {
            errorMsg("Invalid input");
        }
        stockPerformed();
    }

    // EFFECTS: lets user enter quantity to stock and parses it as integer
    private int quantityToStock(String name, double cost) {
        String s = JOptionPane.showInputDialog(
                frame,
                "Each " + name + " costs $" + cost
                        + "\n\nEnter quantity to stock:\n",
                "Stock",
                JOptionPane.PLAIN_MESSAGE);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // MODIFIES: this
    // EFFECTS: append changes after stock is performed
    private void stockPerformed() {
        for (Item item : store.getItemsStocked()) {
            if (!model.contains(item)) {
                model.addElement(item);
            }
            if (item.equals(list.getSelectedValue())) {
                showInfo(item);
            }
        }
    }

    // MODIFIES: this, item
    // EFFECTS: sets item's price
    private void setPriceOfItem(Item item) {
        if (store.getItemsStocked().contains(item)) {
            double price = priceToSet();
            if (price > 0) {
                item.setPrice(price);
            } else {
                errorMsg("Invalid input");
            }
            updateInfo();
        } else {
            errorMsg("This item is not in stock");
        }
    }

    // EFFECTS: lets user enter price to set and parses it as double
    private double priceToSet() {
        String s = JOptionPane.showInputDialog(
                frame,
                "Enter new price:\n",
                "Set Price",
                JOptionPane.PLAIN_MESSAGE);
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // MODIFIES: this, store, item
    // EFFECTS: sells q items
    private void sellItem(Item item) {
        if (store.getItemsStocked().contains(item)) {
            if (item.priceIsSet()) {
                int q = quantityToSell();
                if (q > 0) {
                    try {
                        store.sell(item, q);
                    } catch (InsufficientQuantityException e) {
                        errorMsg("Insufficient Quantity");
                    }
                } else {
                    errorMsg("Invalid input");
                }
                updateInfo();
            } else {
                errorMsg("The price of this item is not set");
            }
        } else {
            errorMsg("This item is not in stock");
        }
    }

    // EFFECTS: lets user enter quantity to sell and parse it as integer
    private int quantityToSell() {
        String s = JOptionPane.showInputDialog(
                frame,
                "Enter quantity to sell:\n",
                "Sell",
                JOptionPane.PLAIN_MESSAGE);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // MODIFIES: this
    // EFFECTS: update label of select item
    private void updateInfo() {
        for (Item item : store.getItemsStocked()) {
            if (item.equals(list.getSelectedValue())) {
                showInfo(item);
            }
        }
    }

    // EFFECTS: pops up error message s
    private void errorMsg(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // REQUIRES: model is empty
    // MODIFIES: this, store
    // EFFECTS: loads store from file
    private void loadStore() {
        JsonReader jsonReader = new JsonReader(JSON_STORE);
        try {
            store = jsonReader.read();
            for (Item item : store.getItemsStocked()) {
                if (!model.contains(item)) {
                    model.addElement(item);
                }
            }
        } catch (IOException e) {
            errorMsg("Unable to load from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the store to file
    private void saveStore() {
        JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(store);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            errorMsg("Unable to save to file: " + JSON_STORE);
        }
    }
}
