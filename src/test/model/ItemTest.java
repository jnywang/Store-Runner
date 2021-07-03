package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    Item item1;
    Item item2;

    @BeforeEach
    public void setUp() {
        item1 = new Item("wand", 12.5);
        item2 = new Item("wand", 12.5);
    }

    @Test
    public void testSetPrice() {
        item1.setPrice(234.5);
        assertEquals(234.5, item1.getPrice());
    }

    @Test
    public void testPriceIsSet() {
        assertFalse(item1.priceIsSet());

        item1.setPrice(0.2);
        assertTrue(item1.priceIsSet());
    }

    @Test
    public void testGetName() {
        assertEquals("wand", item1.getName());
    }

    @Test
    public void testToString() {
        assertEquals(" Wand", item1.toString());
    }

    @Test
    public void testEquals() {
        assertEquals(item2, item1);
        assertFalse(item1.equals(new Item("wand", 125)));
        assertFalse(item1.equals(new Item("land", 12.5)));
        assertFalse(item1.equals(new Store(1)));
        assertFalse(item1.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(item1.hashCode(), item2.hashCode());
    }
}
