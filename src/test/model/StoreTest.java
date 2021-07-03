package model;

import model.exceptions.InsufficientQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {
    Store testStore;
    Item item1;
    Item item2;

    @BeforeEach
    public void setUp() {
        testStore = new Store(2468.35);
        item1 = new Item("covid vaccine", 777);
        item2 = new Item("covid vaccine", 777);
        item1.setPrice(5849.89);
    }

    @Test
    public void testStockFail() {
        assertFalse(testStore.stock(item1, 4));
        assertEquals(0, testStore.getItemsStocked().size());
        assertEquals(0.0, testStore.getExpenditure());
        assertEquals(2468.35, testStore.getAsset());
    }

    @Test
    public void testStockSuccess() {
        assertTrue(testStore.stock(item1, 3));
        assertEquals(2331, testStore.getExpenditure());
        assertEquals(2468.35-2331, testStore.getAsset());
        assertEquals(1, testStore.getItemsStocked().size());

        Item item2 = new Item("toilet paper", 0.5);
        item2.setPrice(2.99);
        assertTrue(testStore.stock(item2, 4));
        assertEquals(2331+4*0.5, testStore.getExpenditure());
        assertEquals(2468.35-2331-4*0.5, testStore.getAsset());
        assertEquals(2, testStore.getItemsStocked().size());

        assertTrue(testStore.stock(item2, 6));
        Item item3 = new Item("surgical mask", testStore.getAsset());
        item3.setPrice(50);
        assertTrue(testStore.stock(item3, 1));
        assertEquals(0, testStore.getAsset());
        assertEquals(3, testStore.getItemsStocked().size());
        assertEquals(item1, testStore.getItemsStocked().get(0));
        assertEquals(3, item1.getQuantity());
        assertEquals(item2, testStore.getItemsStocked().get(1));
        assertEquals(4+6, testStore.getItemsStocked().get(1).getQuantity());
        assertEquals(item3, testStore.getItemsStocked().get(2));
        assertEquals(1, item3.getQuantity());
    }

    @Test
    public void testSellFail() {
        assertTrue(testStore.stock(item1, 3));
        Item item2 = new Item("pen", 0.65);
        item2.setPrice(1.99);
        Item item3 = new Item("notebook", 2);
        assertTrue(testStore.stock(item3, 7));
        Item item4 = new Item("happiness", 9999999);
        double asset0 = testStore.getAsset();

        // insufficient quantity
        try {
            assertFalse(testStore.sell(item1, 4));
            fail();
        } catch (InsufficientQuantityException e) {
            // expected
        }
        assertEquals(0, testStore.getRevenue());
        assertEquals(asset0, testStore.getAsset());
        assertEquals(2, testStore.getItemsStocked().size());

        // not in stock
        try {
            assertFalse(testStore.sell(item2, 1));
            assertEquals(0, testStore.getRevenue());
            assertEquals(asset0, testStore.getAsset());
            assertEquals(2, testStore.getItemsStocked().size());
        } catch (InsufficientQuantityException e) {
            fail();
        }

        // price not set
        try {
            assertFalse(testStore.sell(item3, 6));
        } catch (InsufficientQuantityException e) {
            fail();
        }

        // insufficient quantity and price not set
        try {
            assertFalse(testStore.sell(item3, 20));
        } catch (InsufficientQuantityException e) {
            fail();
        }

        // not in stock and price not set
        try {
            assertFalse(testStore.sell(item4, 1));
        } catch (InsufficientQuantityException e) {
            fail();
        }
    }

    @Test
    public void testSellSuccess() {
        Item item2 = new Item("pen", 0.45);
        item2.setPrice(1.99);
        assertTrue(testStore.stock(item1, 2));
        assertTrue(testStore.stock(item2, 74));
        double asset0 = testStore.getAsset();

        try {
            assertTrue(testStore.sell(item2, 43));
        } catch (InsufficientQuantityException e) {
            fail();
        }
        double rev1 = 43 * item2.getPrice();
        double asset1 = asset0 + 43 * item2.getPrice();
        assertEquals(rev1, testStore.getRevenue());
        assertEquals(asset1, testStore.getAsset());
        assertEquals(2, testStore.getItemsStocked().size());
        assertEquals(item1, testStore.getItemsStocked().get(0));
        assertEquals(item2, testStore.getItemsStocked().get(1));
        assertEquals(74-43, item2.getQuantity());

        try {
            assertTrue(testStore.sell(item1, 2));
        } catch (InsufficientQuantityException e) {
            fail();
        }
        double rev2 = rev1 + 2 * item1.getPrice();
        double asset2 = asset1 + 2 * item1.getPrice();
        assertEquals(rev2, testStore.getRevenue());
        assertEquals(asset2, testStore.getAsset());
        assertEquals(2, testStore.getItemsStocked().size());
        assertEquals(item1, testStore.getItemsStocked().get(0));
        assertEquals(0, item1.getQuantity());
        assertEquals(item2, testStore.getItemsStocked().get(1));
        assertEquals(74-43, item2.getQuantity());

        try {
            assertTrue(testStore.sell(item2, 74-43));
        } catch (InsufficientQuantityException e) {
            fail();
        }
        double rev3 = rev2 + (74-43) * item2.getPrice();
        double asset3 = asset2 + (74-43) * item2.getPrice();
        assertEquals(rev3, testStore.getRevenue());
        assertEquals(asset3, testStore.getAsset());
        assertEquals(2, testStore.getItemsStocked().size());
        assertEquals(item1, testStore.getItemsStocked().get(0));
        assertEquals(0, item1.getQuantity());
        assertEquals(item2, testStore.getItemsStocked().get(1));
        assertEquals(0, item2.getQuantity());
    }

    @Test
    public void testGetIndexOf() {
        Item item2 = new Item("pen", 0.75);
        assertTrue(testStore.stock(item2, 13));
        assertTrue(testStore.stock(item1, 2));

        assertEquals(0, testStore.getIndexOf(item2));
        assertEquals(1, testStore.getIndexOf(item1));
    }

    @Test
    public void testSameItems() {
        testStore.stock(item1, 1);
        testStore.stock(item2, 1);
        assertEquals(1, testStore.getItemsStocked().size());
        assertEquals(item1, testStore.getItemsStocked().get(0));
        assertEquals(item2, testStore.getItemsStocked().get(0));
    }
}
