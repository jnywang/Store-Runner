package model;

import model.exceptions.InsufficientQuantityException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a store with items in stock; asset, revenue, and expenditure (in dollars)
public class Store implements Writable {
    private final List<Item> itemsStocked;     // the items in stock
    private double asset;                      // the store's asset ($)
    private double revenue;                    // the store's total revenue ($)
    private double expenditure;                // the store's total expenditure ($)

    /*
     * REQUIRES: initialAsset > 0.0
     * EFFECTS: store's asset is set to initialAsset; store's stock is empty;
     *          store's revenue and expenditure are set to 0.0
     */
    public Store(double initialAsset) {
        asset = initialAsset;
        itemsStocked = new ArrayList<>();
        revenue = 0.0;
        expenditure = 0.0;
    }

    /*
     * REQUIRES: quantity > 0
     * MODIFIES: this and item
     * EFFECTS: if transaction cost > asset, returns false;
     *          otherwise: if item already exists in itemsStocked,
     *                          adds quantity to its original quantity;
     *                     otherwise,
     *                          adds item with given quantity to itemsStocked;
     *                     adds transaction cost to expenditure; deducts it from asset;
     *                     and returns true.
     */
    public boolean stock(Item item, int quantity) {
        double totalCost = quantity * item.getCost();
        if (totalCost <= asset) {
            addItemToStore(item, quantity);
            expenditure += totalCost;
            asset -= totalCost;
            return true;
        } else {
            return false;
        }
    }

    /*
     * REQUIRES: quantity > 0
     * MODIFIES: this and item
     * EFFECTS: if there are > quantity of items in stock and its price is set,
     *          deducts quantity from item's original quantity,
     *          adds the money earned to revenue and asset, and returns true;
     *          otherwise returns false
     */
    public boolean sell(Item item, int quantity) throws InsufficientQuantityException {
        if (itemsStocked.contains(item) && item.priceIsSet()) {
            if (quantity <= item.getQuantity()) {
                item.decreaseQ(quantity);
                revenue += quantity * item.getPrice();
                asset += quantity * item.getPrice();
                return true;
            } else {
                throw new InsufficientQuantityException();
            }
        } else {
            return false;
        }
    }

// Getters:

    // REQUIRES: item is contained in itemsStocked
    // EFFECTS: returns the index of given item in itemsStocked
    public int getIndexOf(Item item) {
        return itemsStocked.indexOf(item);
    }

    // EFFECTS: returns store's current asset
    public double getAsset() {
        return asset;
    }

    // EFFECTS: returns store's total expenditure
    public double getExpenditure() {
        return expenditure;
    }

    // EFFECTS: returns store's total revenue
    public double getRevenue() {
        return revenue;
    }

    // EFFECTS: returns list of items in stock
    public List<Item> getItemsStocked() {
        return itemsStocked;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("asset", asset);
        json.put("revenue", revenue);
        json.put("expenditure", expenditure);
        json.put("items", itemsToJson());
        return json;
    }

    // EFFECTS: returns items in this store as a JSON array
    private JSONArray itemsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Item item : itemsStocked) {
            jsonArray.put(item.toJson());
        }
        return jsonArray;
    }

    public void addItemToStore(Item item, int quantity) {
        if (itemsStocked.contains(item)) {
            for (Item existingItem : itemsStocked) {
                if (item == existingItem) {
                    existingItem.increaseQ(quantity);
                }
            }
        } else {
            item.increaseQ(quantity);
            itemsStocked.add(item);
        }
    }

    public void setRevenue(double rev) {
        revenue = rev;
    }

    public void setExpenditure(double exp) {
        expenditure = exp;
    }
}
