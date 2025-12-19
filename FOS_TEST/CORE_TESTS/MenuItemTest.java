/**
 * This test class is written by Mohamed Khaled Becetti
 */

package FOS_TEST.CORE_TESTS;

import FOS_CORE.Discount;
import FOS_CORE.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Date;
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

    /* @brief Verifies basic getters/setters work
     * @tests Ensures setter/getter contract for id, name, description, price. */
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

    /* @brief Confirms discounts list is what we injected
     * @tests Validates that getDiscounts() returns the injected list reference. */
    @Test
    void getDiscountsReturnsInjectedList() {
        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(1, "Summer Sale", "50% off", 50, 
            Date.valueOf("2025-06-01"),
            Date.valueOf("2025-08-31")));

        setField(menuItem, "discounts", discounts);

        assertSame(discounts, menuItem.getDiscounts());
        assertEquals(1, menuItem.getDiscounts().size());
    }

    /* @brief Replacing discounts list should take effect immediately
     * @tests Verifies setDiscounts() replaces list and state reflects new contents. */
    @Test
    void setDiscountsReplacesList() {
        ArrayList<Discount> newDiscounts = new ArrayList<>();
        newDiscounts.add(new Discount(2, "Winter Sale", "25% off", 25,
            Date.valueOf("2025-12-01"),
            Date.valueOf("2025-12-31")));

        menuItem.setDiscounts(newDiscounts);

        assertSame(newDiscounts, menuItem.getDiscounts());
        assertEquals(1, menuItem.getDiscounts().size());
    }

    /* @brief Price can be zero
     * @tests Documents acceptance of zero price values. */
    @Test
    void priceCanBeZero() {
        menuItem.setPrice(0);
        assertEquals(0, menuItem.getPrice());
    }

    /* @brief Price can be negative
     * @tests Captures current behavior for negative inputs (no validation yet). */
    @Test
    void priceCanBeNegative() {
        menuItem.setPrice(-5.50);
        assertEquals(-5.50, menuItem.getPrice());
    }

    /* @brief Empty item name is allowed
     * @tests Records present behavior where empty names are stored. */
    @Test
    void itemNameCanBeEmpty() {
        menuItem.setItemName("");
        assertEquals("", menuItem.getItemName());
    }

    /* @brief Description may be null
     * @tests Ensures null assignment is retained and safe to read back. */
    @Test
    void descriptionCanBeNull() {
        menuItem.setDescription(null);
        assertNull(menuItem.getDescription());
    }

    /* @brief Negative IDs are stored
     * @tests Documents storage behavior for invalid/negative identifiers. */
    @Test
    void menuItemIDCanBeNegative() {
        menuItem.setMenuItemID(-1);
        assertEquals(-1, menuItem.getMenuItemID());
    }

    /* @brief Constructor state setup is coherent
     * @tests Validates initialized fields produce a consistent readable state. */
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

    /* @brief Multiple discounts can coexist
     * @tests Confirms list retains multiple Discount entries without loss. */
    @Test
    void multipleDiscountsCanBeAdded() {
        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(1, "D1", "desc1", 10,
            Date.valueOf("2025-01-01"),
            Date.valueOf("2025-01-31")));
        discounts.add(new Discount(2, "D2", "desc2", 20,
            Date.valueOf("2025-02-01"),
            Date.valueOf("2025-02-28")));

        menuItem.setDiscounts(discounts);

        assertEquals(2, menuItem.getDiscounts().size());
    }

    /* @brief Mutating returned discounts list changes object state
     * @tests Validates returned list is live (mutations reflect on the object). */
    @Test
    void mutatingReturnedDiscountListReflectsOnObject() {
        ArrayList<Discount> discounts = new ArrayList<>();
        menuItem.setDiscounts(discounts);
        assertSame(discounts, menuItem.getDiscounts());

        menuItem.getDiscounts().add(new Discount(3, "D3", "desc3", 5,
                Date.valueOf("2025-03-01"),
                Date.valueOf("2025-03-31")));

        assertEquals(1, menuItem.getDiscounts().size());
    }

    /* @brief Item name may be null
     * @tests Documents current nullable acceptance for name field. */
    @Test
    void itemNameCanBeNull() {
        menuItem.setItemName(null);
        assertNull(menuItem.getItemName());
    }

    /* @brief Empty description is handled
     * @tests Verifies empty strings are stored and returned unchanged. */
    @Test
    void descriptionEmptyStringHandled() {
        menuItem.setDescription("");
        assertEquals("", menuItem.getDescription());
    }

    /* @brief High-precision prices are retained
     * @tests Asserts double precision retention within tight tolerance. */
    @Test
    void highPrecisionPriceIsRetained() {
        double precise = 12.123456789;
        menuItem.setPrice(precise);
        assertEquals(precise, menuItem.getPrice(), 1e-12);
    }

    /* @brief Very large prices are stored
     * @tests Validates handling of large magnitude double values. */
    @Test
    void veryLargePriceIsStored() {
        double big = 1e12;
        menuItem.setPrice(big);
        assertEquals(big, menuItem.getPrice());
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
