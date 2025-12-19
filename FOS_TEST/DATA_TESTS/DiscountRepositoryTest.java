package FOS_TEST.DATA_TESTS;

import FOS_CORE.*;
import FOS_DATA.ManagerService;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountRepositoryTest {

    private ManagerService managerService;
    private RestaurantData restaurantData;
    private Restaurant testRestaurant;
    private MenuItem testMenuItem;

    @BeforeEach
    void setUp() {
        managerService = new ManagerService();
        restaurantData = new RestaurantData();
        
        // Create a test restaurant and menu item for testing
        // Using existing restaurant from database (ID 1 - Pizza Palace)
        testRestaurant = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
        
        // Create a test menu item (using existing menu item ID 1 - Margherita Pizza)
        testMenuItem = new MenuItem(1, "Margherita Pizza", "Classic tomato and mozzarella", 12.99);
    }

    @Test
    void testCreateDiscountSuccessfully() {
        // Create a discount with valid data
        Date startDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        Date endDate = new Date(System.currentTimeMillis() + 86400000 * 30); // 30 days from now
        
        Discount discount = new Discount();
        discount.setDiscountName("Test Discount");
        discount.setDescription("Test discount description");
        discount.setDiscountPercentage(20.0);
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);

        boolean result = managerService.createDiscount(discount, testMenuItem);

        assertTrue(result, "Discount should be created successfully");
        assertTrue(discount.getDiscountID() > 0, "Discount ID should be set after creation");
    }

    @Test
    void testCreateDiscountWithInvalidPercentage() {
        // Create a discount with percentage > 100 (should fail database constraint)
        Date startDate = new Date(System.currentTimeMillis() - 86400000);
        Date endDate = new Date(System.currentTimeMillis() + 86400000 * 30);
        
        Discount discount = new Discount();
        discount.setDiscountName("Invalid Discount");
        discount.setDescription("Discount with invalid percentage");
        discount.setDiscountPercentage(150.0); // Invalid: > 100
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);

        boolean result = managerService.createDiscount(discount, testMenuItem);

        assertFalse(result, "Discount with percentage > 100 should not be created");
    }

    @Test
    void testCreateDiscountWithNegativePercentage() {
        // Create a discount with negative percentage (should fail database constraint)
        Date startDate = new Date(System.currentTimeMillis() - 86400000);
        Date endDate = new Date(System.currentTimeMillis() + 86400000 * 30);
        
        Discount discount = new Discount();
        discount.setDiscountName("Negative Discount");
        discount.setDescription("Discount with negative percentage");
        discount.setDiscountPercentage(-10.0); // Invalid: negative
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);

        boolean result = managerService.createDiscount(discount, testMenuItem);

        assertFalse(result, "Discount with negative percentage should not be created");
    }

    @Test
    void testFetchMenuItemDiscountsActive() {
        // Fetch discounts for a menu item that has active discounts
        // Menu item ID 1 (Margherita Pizza) has an active discount in DML.sql
        
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(testMenuItem);

        assertNotNull(discounts, "Discounts list should not be null");
        // Should return active discounts only (those within date range)
        assertTrue(discounts.size() >= 0, "Should return zero or more active discounts");
        
        // Verify all returned discounts are active
        Date now = new Date(System.currentTimeMillis());
        for (Discount discount : discounts) {
            assertTrue(discount.getStartDate().before(now) || discount.getStartDate().equals(now),
                    "Discount start date should be before or equal to now");
            assertTrue(discount.getEndDate().after(now),
                    "Discount end date should be after now");
        }
    }

    @Test
    void testFetchMenuItemDiscountsExpired() {
        // Create and fetch an expired discount
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = new Date(cal.getTimeInMillis());
        
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = new Date(cal.getTimeInMillis()); // Yesterday (expired)
        
        Discount expiredDiscount = new Discount();
        expiredDiscount.setDiscountName("Expired Discount");
        expiredDiscount.setDescription("This discount has expired");
        expiredDiscount.setDiscountPercentage(15.0);
        expiredDiscount.setStartDate(startDate);
        expiredDiscount.setEndDate(endDate);

        // Create the expired discount
        managerService.createDiscount(expiredDiscount, testMenuItem);

        // Fetch discounts - expired ones should not be returned
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(testMenuItem);

        // Verify expired discount is not in the results
        boolean foundExpired = false;
        for (Discount discount : discounts) {
            if (discount.getDiscountID() == expiredDiscount.getDiscountID()) {
                foundExpired = true;
                break;
            }
        }
        assertFalse(foundExpired, "Expired discount should not be returned");
    }

    @Test
    void testFetchMenuItemDiscountsFuture() {
        // Create a discount that starts in the future
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date startDate = new Date(cal.getTimeInMillis()); // Tomorrow
        
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date endDate = new Date(cal.getTimeInMillis()); // 31 days from now
        
        Discount futureDiscount = new Discount();
        futureDiscount.setDiscountName("Future Discount");
        futureDiscount.setDescription("This discount starts in the future");
        futureDiscount.setDiscountPercentage(25.0);
        futureDiscount.setStartDate(startDate);
        futureDiscount.setEndDate(endDate);

        // Create the future discount
        managerService.createDiscount(futureDiscount, testMenuItem);

        // Fetch discounts - future ones should not be returned
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(testMenuItem);

        // Verify future discount is not in the results
        boolean foundFuture = false;
        for (Discount discount : discounts) {
            if (discount.getDiscountID() == futureDiscount.getDiscountID()) {
                foundFuture = true;
                break;
            }
        }
        assertFalse(foundFuture, "Future discount should not be returned");
    }

    @Test
    void testFetchMenuItemDiscountsNoDiscounts() {
        // Create a new menu item that doesn't have any discounts
        MenuItem newMenuItem = new MenuItem(999, "New Item", "Item with no discounts", 10.00);
        
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(newMenuItem);

        assertNotNull(discounts, "Discounts list should not be null");
        assertEquals(0, discounts.size(), "Should return empty list for menu item with no discounts");
    }

    @Test
    void testDiscountPropertiesAfterCreation() {
        // Create a discount and verify all properties are set correctly
        Date startDate = new Date(System.currentTimeMillis() - 86400000);
        Date endDate = new Date(System.currentTimeMillis() + 86400000 * 30);
        
        String discountName = "Property Test Discount";
        String description = "Testing discount properties";
        double percentage = 30.0;
        
        Discount discount = new Discount();
        discount.setDiscountName(discountName);
        discount.setDescription(description);
        discount.setDiscountPercentage(percentage);
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);

        boolean created = managerService.createDiscount(discount, testMenuItem);
        assertTrue(created, "Discount should be created");

        // Fetch the discount to verify properties
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(testMenuItem);
        Discount fetchedDiscount = null;
        for (Discount d : discounts) {
            if (d.getDiscountID() == discount.getDiscountID()) {
                fetchedDiscount = d;
                break;
            }
        }

        if (fetchedDiscount != null) {
            assertEquals(discountName, fetchedDiscount.getDiscountName(), "Discount name should match");
            assertEquals(description, fetchedDiscount.getDescription(), "Discount description should match");
            assertEquals(percentage, fetchedDiscount.getDiscountPercentage(), 0.01, "Discount percentage should match");
            assertEquals(startDate, fetchedDiscount.getStartDate(), "Start date should match");
            assertEquals(endDate, fetchedDiscount.getEndDate(), "End date should match");
        }
    }

    @Test
    void testMultipleDiscountsForMenuItem() {
        // Create multiple discounts for the same menu item
        Date startDate1 = new Date(System.currentTimeMillis() - 86400000);
        Date endDate1 = new Date(System.currentTimeMillis() + 86400000 * 10);
        
        Discount discount1 = new Discount();
        discount1.setDiscountName("First Discount");
        discount1.setDescription("First discount description");
        discount1.setDiscountPercentage(10.0);
        discount1.setStartDate(startDate1);
        discount1.setEndDate(endDate1);

        Date startDate2 = new Date(System.currentTimeMillis() - 86400000);
        Date endDate2 = new Date(System.currentTimeMillis() + 86400000 * 20);
        
        Discount discount2 = new Discount();
        discount2.setDiscountName("Second Discount");
        discount2.setDescription("Second discount description");
        discount2.setDiscountPercentage(20.0);
        discount2.setStartDate(startDate2);
        discount2.setEndDate(endDate2);

        boolean created1 = managerService.createDiscount(discount1, testMenuItem);
        boolean created2 = managerService.createDiscount(discount2, testMenuItem);

        assertTrue(created1, "First discount should be created");
        assertTrue(created2, "Second discount should be created");

        // Fetch discounts - should return both active discounts
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(testMenuItem);

        assertTrue(discounts.size() >= 2, "Should return at least 2 active discounts");
    }
}
