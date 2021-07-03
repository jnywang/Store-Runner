package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Item;
import model.Store;
import org.json.*;

// ACKNOWLEDGEMENT: The codes below are based on the on the ones in JsonSerializationDemo

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Store read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseStore(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses store from JSON object and returns it
    private Store parseStore(JSONObject jsonObject) {
        double asset = jsonObject.getDouble("asset");
        double revenue = jsonObject.getDouble("revenue");
        double expenditure = jsonObject.getDouble("expenditure");
        Store s = new Store(asset);
        s.setRevenue(revenue);
        s.setExpenditure(expenditure);
        addItems(s, jsonObject);
        return s;
    }

    // MODIFIES: s
    // EFFECTS: parses items from JSON object and adds them to store
    private void addItems(Store s, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (Object json : jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            addItem(s, nextItem);
        }
    }

    // MODIFIES: s
    // EFFECTS: parses item from JSON object and adds it to store
    private void addItem(Store s, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double cost = jsonObject.getDouble("cost");
        double price = jsonObject.getDouble("price");
        int quantity = jsonObject.getInt("quantity");
        Item item = new Item(name, cost);
        if (price != 0.0) {
            item.setPrice(price);
        }
        s.addItemToStore(item, quantity);
    }
}
