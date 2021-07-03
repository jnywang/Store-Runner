package persistence;

import model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

// ACKNOWLEDGEMENT: The codes below are based on the on the ones in JsonSerializationDemo

public class JsonTest {
    protected void checkItem(String name, double cost, double price, int quantity, Item item) {
        assertEquals(name, item.getName());
        assertEquals(price, item.getPrice());
        assertEquals(cost, item.getCost());
        assertEquals(quantity, item.getQuantity());
    }
}