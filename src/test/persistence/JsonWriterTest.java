package persistence;

import model.Item;
import model.Store;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// ACKNOWLEDGEMENT: The codes below are based on the on the ones in JsonSerializationDemo

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Store s = new Store(99999);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyStore() {
        try {
            Store s = new Store(99999);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyStore.json");
            writer.open();
            writer.write(s);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyStore.json");
            s = reader.read();
            assertEquals(99999, s.getAsset());
            assertEquals(0, s.getItemsStocked().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStore() {
        try {
            Store s = new Store(99999);
            Item i1 = new Item("cd", 7.7);
            Item i2 = new Item("book", 30);
            s.stock(i1, 7);
            s.stock(i2, 3);
            i2.setPrice(39.89);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralStore.json");
            writer.open();
            writer.write(s);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralStore.json");
            s = reader.read();
            assertEquals(99999 - 7.7*7 - 30*3, s.getAsset());
            List<Item> items = s.getItemsStocked();
            assertEquals(2, items.size());
            checkItem("cd", 7.7, 0.0, 7, items.get(0));
            checkItem("book", 30, 39.89, 3, items.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}