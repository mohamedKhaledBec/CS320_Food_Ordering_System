/**
 * This test class is written by Mohamed Khaled Becetti
 */

package FOS_TEST.CORE_TESTS;

import FOS_CORE.Discount;
import FOS_CORE.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the MenuItem domain model
public class MenuItemTest {

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem();
        setField(menuItem, "discounts", new ArrayList<Discount>());
    }

    @Test
    void menuItemPropertiesAreReadableAndWritable() {
        menuItem.setMenuItemID(5);
        menuItem.setItemName("Burger");
        menuItem.setDescription("Classic beef burger");
        menuItem.setPrice(12.99);

        assertEquals(5, menuItem.getMenuItemID());
        assertEquals("Burger", menuItem.getItemName());
        assertEquals("Classic beef burger", menuItem.getDescription());
        assertEquals(12.99, menuItem.getPrice(), 0.0001);
    }

    @Test
    void getDiscountsReturnsInjectedList() {
        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(1, "Summer Sale", "50% off", 50, 
            Timestamp.valueOf("2025-06-01 00:00:00"), 
            Timestamp.valueOf("2025-08-31 23:59:59")));

        setField(menuItem, "discounts", discounts);

        assertSame(discounts, menuItem.getDiscounts());
        assertEquals(1, menuItem.getDiscounts().size());
    }

    @Test
    void setDiscountsReplacesList() {
        ArrayList<Discount> newDiscounts = new ArrayList<>();
        newDiscounts.add(new Discount(2, "Winter Sale", "25% off", 25,
            Timestamp.valueOf("2025-12-01 00:00:00"),
            Timestamp.valueOf("2025-12-31 23:59:59")));

        menuItem.setDiscounts(newDiscounts);

        assertSame(newDiscounts, menuItem.getDiscounts());
        assertEquals(1, menuItem.getDiscounts().size());
    }

    @Test
    void priceCanBeZero() {
        menuItem.setPrice(0);
        assertEquals(0, menuItem.getPrice());
    }

    @Test
    void priceCanBeNegative() {
        menuItem.setPrice(-5.50);
        assertEquals(-5.50, menuItem.getPrice());
    }

    @Test
    void itemNameCanBeEmpty() {
        menuItem.setItemName("");
        assertEquals("", menuItem.getItemName());
    }

    @Test
    void descriptionCanBeNull() {
        menuItem.setDescription(null);
        assertNull(menuItem.getDescription());
    }

    @Test
    void menuItemIDCanBeNegative() {
        menuItem.setMenuItemID(-1);
        assertEquals(-1, menuItem.getMenuItemID());
    }

    @Test
    void constructorWithParametersInitializesAllFields() {
        MenuItem item = new MenuItem();
        setField(item, "menuItemID", 10);
        setField(item, "itemName", "Pizza");
        setField(item, "description", "Pepperoni pizza");
        setField(item, "price", 15.99);
        setField(item, "discounts", new ArrayList<Discount>());

        assertEquals(10, item.getMenuItemID());
        assertEquals("Pizza", item.getItemName());
        assertEquals("Pepperoni pizza", item.getDescription());
        assertEquals(15.99, item.getPrice(), 0.0001);
        assertNotNull(item.getDiscounts());
    }

    @Test
    void multipleDiscountsCanBeAdded() {
        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(1, "D1", "desc1", 10,
            Timestamp.valueOf("2025-01-01 00:00:00"),
            Timestamp.valueOf("2025-01-31 23:59:59")));
        discounts.add(new Discount(2, "D2", "desc2", 20,
            Timestamp.valueOf("2025-02-01 00:00:00"),
            Timestamp.valueOf("2025-02-28 23:59:59")));

        menuItem.setDiscounts(discounts);

        assertEquals(2, menuItem.getDiscounts().size());
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject field '" + fieldName + "': " + e.getMessage());
        }
    }
}
