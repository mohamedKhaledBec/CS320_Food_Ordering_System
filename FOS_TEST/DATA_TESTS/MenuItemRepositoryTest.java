package FOS_TEST.DATA_TESTS;

import FOS_CORE.*;
import FOS_DATA.ManagerService;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MenuItemRepositoryTest {

    private ManagerService managerService;
    private RestaurantData restaurantData;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        managerService = new ManagerService();
        restaurantData = new RestaurantData();
        
        // Use existing restaurant from database (ID 1 - Pizza Palace)
        testRestaurant = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
    }

    @Test
    void testAddMenuItemSuccessfully() {
        // Create a new menu item with valid data
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Test Pizza");
        menuItem.setDescription("A test pizza item");
        menuItem.setPrice(15.99);

        boolean result = managerService.addMenuItem(menuItem, testRestaurant);

        assertTrue(result, "Menu item should be added successfully");
        assertTrue(menuItem.getMenuItemID() > 0, "Menu item ID should be set after creation");
    }

    @Test
    void testAddMenuItemWithNegativePrice() {
        // Create a menu item with negative price (should fail database constraint)
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Invalid Price Item");
        menuItem.setDescription("Item with negative price");
        menuItem.setPrice(-10.0); // Invalid: negative price

        boolean result = managerService.addMenuItem(menuItem, testRestaurant);

        assertFalse(result, "Menu item with negative price should not be added");
    }

    @Test
    void testAddMenuItemWithZeroPrice() {
        // Create a menu item with zero price (should be valid based on constraint >= 0)
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Free Item");
        menuItem.setDescription("Item with zero price");
        menuItem.setPrice(0.0);

        boolean result = managerService.addMenuItem(menuItem, testRestaurant);

        assertTrue(result, "Menu item with zero price should be added (price >= 0)");
    }

    @Test
    void testAddMenuItemWithNullName() {
        // Create a menu item with null name (should fail database constraint)
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName(null);
        menuItem.setDescription("Item with null name");
        menuItem.setPrice(10.0);

        boolean result = managerService.addMenuItem(menuItem, testRestaurant);

        assertFalse(result, "Menu item with null name should not be added");
    }

    @Test
    void testAddMenuItemWithEmptyName() {
        // Create a menu item with empty name (should fail database constraint)
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("");
        menuItem.setDescription("Item with empty name");
        menuItem.setPrice(10.0);

        boolean result = managerService.addMenuItem(menuItem, testRestaurant);

        assertFalse(result, "Menu item with empty name should not be added");
    }

    @Test
    void testUpdateMenuItemSuccessfully() {
        // First, create a menu item
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Original Name");
        menuItem.setDescription("Original description");
        menuItem.setPrice(10.0);
        
        boolean created = managerService.addMenuItem(menuItem, testRestaurant);
        assertTrue(created, "Menu item should be created first");

        // Update the menu item
        menuItem.setItemName("Updated Name");
        menuItem.setDescription("Updated description");
        menuItem.setPrice(12.99);

        boolean result = managerService.updateMenuItem(menuItem);

        assertTrue(result, "Menu item should be updated successfully");
    }

    @Test
    void testUpdateMenuItemWithInvalidID() {
        // Try to update a menu item that doesn't exist
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuItemID(99999); // Non-existent ID
        menuItem.setItemName("Non-existent Item");
        menuItem.setDescription("This item doesn't exist");
        menuItem.setPrice(10.0);

        boolean result = managerService.updateMenuItem(menuItem);

        assertFalse(result, "Updating non-existent menu item should fail");
    }

    @Test
    void testUpdateMenuItemWithNegativePrice() {
        // Create a menu item first
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Item to Update");
        menuItem.setDescription("Description");
        menuItem.setPrice(10.0);
        
        boolean created = managerService.addMenuItem(menuItem, testRestaurant);
        assertTrue(created, "Menu item should be created first");

        // Try to update with negative price
        menuItem.setPrice(-5.0);

        boolean result = managerService.updateMenuItem(menuItem);

        assertFalse(result, "Updating menu item with negative price should fail");
    }

    @Test
    void testRemoveMenuItemSuccessfully() {
        // First, create a menu item
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Item to Remove");
        menuItem.setDescription("This item will be removed");
        menuItem.setPrice(10.0);
        
        boolean created = managerService.addMenuItem(menuItem, testRestaurant);
        assertTrue(created, "Menu item should be created first");
        int menuItemId = menuItem.getMenuItemID();

        // Remove the menu item
        boolean result = managerService.removeMenuItem(menuItem);

        assertTrue(result, "Menu item should be removed successfully");
        
        // Verify it's actually removed by trying to fetch menu
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);
        boolean found = false;
        for (MenuItem item : menu) {
            if (item.getMenuItemID() == menuItemId) {
                found = true;
                break;
            }
        }
        assertFalse(found, "Removed menu item should not be in the menu");
    }

    @Test
    void testRemoveMenuItemWithInvalidID() {
        // Try to remove a menu item that doesn't exist
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuItemID(99999); // Non-existent ID
        menuItem.setItemName("Non-existent Item");

        boolean result = managerService.removeMenuItem(menuItem);

        assertFalse(result, "Removing non-existent menu item should fail");
    }

    @Test
    void testFetchRestaurantMenu() {
        // Fetch menu items for a restaurant
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);

        assertNotNull(menu, "Menu list should not be null");
        assertTrue(menu.size() > 0, "Restaurant should have at least one menu item");
    }

    @Test
    void testFetchRestaurantMenuProperties() {
        // Fetch menu and verify properties are correctly loaded
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);

        assertNotNull(menu, "Menu list should not be null");
        
        if (!menu.isEmpty()) {
            MenuItem firstItem = menu.get(0);
            assertTrue(firstItem.getMenuItemID() > 0, "Menu item should have a valid ID");
            assertNotNull(firstItem.getItemName(), "Menu item should have a name");
            assertFalse(firstItem.getItemName().isEmpty(), "Menu item name should not be empty");
            assertTrue(firstItem.getPrice() >= 0, "Menu item price should be non-negative");
        }
    }

    @Test
    void testFetchRestaurantMenuForNonExistentRestaurant() {
        // Fetch menu for a restaurant that doesn't exist
        Restaurant nonExistentRestaurant = new Restaurant(99999, "Non-existent", "Unknown", "Unknown");
        
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(nonExistentRestaurant);

        assertNotNull(menu, "Menu list should not be null");
        assertEquals(0, menu.size(), "Non-existent restaurant should have no menu items");
    }

    @Test
    void testMenuItemPropertiesAfterCreation() {
        // Create a menu item and verify all properties are set correctly
        String itemName = "Property Test Item";
        String description = "Testing menu item properties";
        double price = 19.99;
        
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName(itemName);
        menuItem.setDescription(description);
        menuItem.setPrice(price);

        boolean created = managerService.addMenuItem(menuItem, testRestaurant);
        assertTrue(created, "Menu item should be created");

        // Fetch the menu and verify properties
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);
        MenuItem fetchedItem = null;
        for (MenuItem item : menu) {
            if (item.getMenuItemID() == menuItem.getMenuItemID()) {
                fetchedItem = item;
                break;
            }
        }

        assertNotNull(fetchedItem, "Created menu item should be found in menu");
        assertEquals(itemName, fetchedItem.getItemName(), "Item name should match");
        assertEquals(description, fetchedItem.getDescription(), "Description should match");
        assertEquals(price, fetchedItem.getPrice(), 0.01, "Price should match");
    }

    @Test
    void testMultipleMenuItemsForRestaurant() {
        // Create multiple menu items for the same restaurant
        MenuItem item1 = new MenuItem();
        item1.setItemName("First Item");
        item1.setDescription("First item description");
        item1.setPrice(10.0);

        MenuItem item2 = new MenuItem();
        item2.setItemName("Second Item");
        item2.setDescription("Second item description");
        item2.setPrice(15.0);

        MenuItem item3 = new MenuItem();
        item3.setItemName("Third Item");
        item3.setDescription("Third item description");
        item3.setPrice(20.0);

        boolean created1 = managerService.addMenuItem(item1, testRestaurant);
        boolean created2 = managerService.addMenuItem(item2, testRestaurant);
        boolean created3 = managerService.addMenuItem(item3, testRestaurant);

        assertTrue(created1, "First menu item should be created");
        assertTrue(created2, "Second menu item should be created");
        assertTrue(created3, "Third menu item should be created");

        // Fetch menu - should contain all items
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);
        
        assertTrue(menu.size() >= 3, "Menu should contain at least 3 items");
        
        // Verify all created items are in the menu
        boolean found1 = false, found2 = false, found3 = false;
        for (MenuItem item : menu) {
            if (item.getMenuItemID() == item1.getMenuItemID()) found1 = true;
            if (item.getMenuItemID() == item2.getMenuItemID()) found2 = true;
            if (item.getMenuItemID() == item3.getMenuItemID()) found3 = true;
        }
        
        assertTrue(found1, "First item should be in menu");
        assertTrue(found2, "Second item should be in menu");
        assertTrue(found3, "Third item should be in menu");
    }

    @Test
    void testUpdateMenuItemAllProperties() {
        // Create a menu item
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Original Item");
        menuItem.setDescription("Original description");
        menuItem.setPrice(10.0);
        
        boolean created = managerService.addMenuItem(menuItem, testRestaurant);
        assertTrue(created, "Menu item should be created first");

        // Update all properties
        String newName = "Completely New Name";
        String newDescription = "Completely new description";
        double newPrice = 25.99;
        
        menuItem.setItemName(newName);
        menuItem.setDescription(newDescription);
        menuItem.setPrice(newPrice);

        boolean updated = managerService.updateMenuItem(menuItem);
        assertTrue(updated, "Menu item should be updated");

        // Fetch and verify all properties were updated
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);
        MenuItem fetchedItem = null;
        for (MenuItem item : menu) {
            if (item.getMenuItemID() == menuItem.getMenuItemID()) {
                fetchedItem = item;
                break;
            }
        }

        assertNotNull(fetchedItem, "Updated menu item should be found");
        assertEquals(newName, fetchedItem.getItemName(), "Name should be updated");
        assertEquals(newDescription, fetchedItem.getDescription(), "Description should be updated");
        assertEquals(newPrice, fetchedItem.getPrice(), 0.01, "Price should be updated");
    }

    @Test
    void testMenuItemIDAutoIncrement() {
        // Create multiple menu items and verify IDs are unique and incrementing
        MenuItem item1 = new MenuItem();
        item1.setItemName("Item One");
        item1.setDescription("First item");
        item1.setPrice(10.0);

        MenuItem item2 = new MenuItem();
        item2.setItemName("Item Two");
        item2.setDescription("Second item");
        item2.setPrice(15.0);

        boolean created1 = managerService.addMenuItem(item1, testRestaurant);
        boolean created2 = managerService.addMenuItem(item2, testRestaurant);

        assertTrue(created1, "First item should be created");
        assertTrue(created2, "Second item should be created");
        
        assertTrue(item1.getMenuItemID() > 0, "First item should have valid ID");
        assertTrue(item2.getMenuItemID() > 0, "Second item should have valid ID");
        assertNotEquals(item1.getMenuItemID(), item2.getMenuItemID(), "Menu items should have different IDs");
    }
}
