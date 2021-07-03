package persistence;

import model.Item;
import model.Store;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// ACKNOWLEDGEMENT: The codes below are based on the on the ones in JsonSerializationDemo

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Store s = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyStore() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyStore.json");
        try {
            Store s = reader.read();
            assertEquals(99999, s.getAsset());
            assertEquals(0, s.getItemsStocked().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralStore() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralStore.json");
        try {
            Store s = reader.read();
            assertEquals(99999 - 7.7*7 - 30*3, s.getAsset());
            List<Item> items = s.getItemsStocked();
            assertEquals(2, items.size());
            checkItem("cd", 7.7, 0.0, 7, items.get(0));
            checkItem("book", 30, 39.89, 3, items.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}