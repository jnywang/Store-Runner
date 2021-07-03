package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// Represents an item with a name, cost and price (in dollars)
public class Item implements Writable {
    private final String name;     // the item's name
    private final double cost;     // the unit cost ($) of the item
    private double price;          // the retail price ($) of the item
    private int quantity;          // the quantity of the item in store

    /*
     * REQUIRES: itemName has a non-zero length; itemCost > 0.0
     * EFFECTS: item's name is set to itemName; item's cost is set to itemCost;
     *          item's price is initialized to 0, meaning it is not yet set
     */
    public Item(String itemName, double itemCost) {
        name = itemName;
        cost = itemCost;
        price = 0.0;
        quantity = 0;
    }

    /*
     * REQUIRES: amount > 0.0
     * MODIFIES: this
     * EFFECTS: item's price is set to amount
     */
    public void setPrice(double amount) {
        price = amount;
    }

    // EFFECTS: returns true if price has been set, false if not
    public boolean priceIsSet() {
        return price != 0.0;
    }

    // REQUIRES: n is a positive integer
    // MODIFIES: this
    // EFFECTS: increases item's quantity by n
    public void increaseQ(int n) {
        quantity += n;
    }

    // REQUIRES: n is a positive integer
    // MODIFIES: this
    // EFFECTS: decreases item's quantity by n
    public void decreaseQ(int n) {
        quantity -= n;
    }

// Getters:

    //EFFECTS: returns item's price
    public double getPrice() {
        return price;
    }

    // EFFECTS: returns item's cost
    public double getCost() {
        return cost;
    }

    // EFFECTS: returns item's name
    public String getName() {
        return name;
    }

    // EFFECTS: returns item's quantity
    public int getQuantity() {
        return quantity;
    }

    @Override
    //EFFECTS: returns string representation of this item
    public String toString() {
        String theName = name.substring(0, 1).toUpperCase() + name.substring(1);
        return " " + theName;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("cost", cost);
        json.put("price", price);
        json.put("quantity", quantity);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Double.compare(item.cost, cost) == 0 && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }
}
