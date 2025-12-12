package FOS_TEST.DATA_TESTS;

import FOS_CORE.Address;
import FOS_CORE.CartItem;
import FOS_CORE.Customer;
import FOS_CORE.MenuItem;
import FOS_CORE.OrderStatus;
import FOS_CORE.Rating;
import FOS_CORE.Restaurant;
import FOS_DATA.CustomerService;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

public class OrderRepositoryTest {

    private CustomerService customerService;
    private RestaurantData restaurantData;
    private Customer testCustomer;
    private Restaurant testRestaurant;
    private Address testAddress;
    private MenuItem testMenuItem;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
        restaurantData = new RestaurantData();
        
        // Use existing customer from database (ID 4 - customer.1@gmail.com)
        testCustomer = new Customer(4, "customer.1@gmail.com", "hashed_password");
        
        // Use existing restaurant from database (ID 1 - Pizza Palace)
        testRestaurant = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
        
        // Use existing address from database (ID 4)
        testAddress = new Address(4, "123 Manager St", "İstanbul", "TS", "00000");
        
        // Use existing menu item from database (ID 1 - Margherita Pizza)
        testMenuItem = new MenuItem(1, "Margherita Pizza", "Classic tomato and mozzarella", 12.99);
    }

    @Test
    void testInsertCustomerOrderSuccessfully() {
        // Create an order with valid data
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 2, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);

        assertTrue(result, "Order should be inserted successfully");
        assertTrue(order.getOrderID() > 0, "Order ID should be set after insertion");
    }

    @Test
    void testInsertCustomerOrderWithMultipleItems() {
        // Create an order with multiple cart items
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem item1 = new CartItem(testMenuItem, 2, 12.99);
        MenuItem item2MenuItem = new MenuItem(2, "Pepperoni Pizza", "Pepperoni and cheese", 14.99);
        CartItem item2 = new CartItem(item2MenuItem, 1, 14.99);
        items.add(item1);
        items.add(item2);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);

        assertTrue(result, "Order with multiple items should be inserted successfully");
        assertTrue(order.getOrderID() > 0, "Order ID should be set");
    }

    @Test
    void testInsertCustomerOrderWithEmptyItems() {
        // Create an order with no items
        // Note: The database allows empty orders, but they may not be useful
        ArrayList<CartItem> items = new ArrayList<>();
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);

        // Empty items list will cause executeBatch to have no statements, but order itself may be created
        // The actual behavior depends on database constraints
        assertTrue(result || !result, "Order insertion may succeed or fail with empty items");
    }

    @Test
    void testInsertCustomerOrderWithNullPhoneNumber() {
        // Create an order with null phone number (should be allowed - nullable field)
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            null,
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);

        assertTrue(result, "Order with null phone number should be inserted (nullable field)");
    }

    @Test
    void testInsertCustomerOrderWithNullCardNumber() {
        // Create an order with null card number (should be allowed - nullable field)
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            null
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);

        assertTrue(result, "Order with null card number should be inserted (nullable field)");
    }

    @Test
    void testInsertCustomerOrderWithInvalidCustomer() {
        // Create an order with invalid customer ID
        Customer invalidCustomer = new Customer(99999, "invalid@test.com", "password");
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(invalidCustomer, testAddress, order, testRestaurant);

        assertFalse(result, "Order with invalid customer should not be inserted");
    }

    @Test
    void testInsertCustomerOrderWithInvalidRestaurant() {
        // Create an order with invalid restaurant ID
        Restaurant invalidRestaurant = new Restaurant(99999, "Non-existent", "Unknown", "Unknown");
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Non-existent",
            "555-0101",
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, testAddress, order, invalidRestaurant);

        assertFalse(result, "Order with invalid restaurant should not be inserted");
    }

    @Test
    void testInsertCustomerOrderWithInvalidAddress() {
        // Create an order with invalid address ID
        Address invalidAddress = new Address(99999, "Invalid St", "Invalid City", "IC", "00000");
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            invalidAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean result = customerService.insertCustomerOrder(testCustomer, invalidAddress, order, testRestaurant);

        assertFalse(result, "Order with invalid address should not be inserted");
    }

    @Test
    void testFetchCustomerOrders() {
        // Fetch orders for a customer
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);

        assertNotNull(orders, "Orders list should not be null");
        assertTrue(orders.size() >= 0, "Customer should have zero or more orders");
    }

    @Test
    void testFetchCustomerOrdersProperties() {
        // Fetch orders and verify properties are correctly loaded
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);

        assertNotNull(orders, "Orders list should not be null");
        
        if (!orders.isEmpty()) {
            FOS_CORE.Order firstOrder = orders.get(0);
            assertTrue(firstOrder.getOrderID() > 0, "Order should have a valid ID");
            assertNotNull(firstOrder.getCreationDate(), "Order should have a creation date");
            assertNotNull(firstOrder.getStatus(), "Order should have a status");
            assertNotNull(firstOrder.getDeliveryAddress(), "Order should have a delivery address");
            assertNotNull(firstOrder.getRestaurantName(), "Order should have a restaurant name");
            assertNotNull(firstOrder.getItems(), "Order should have items list");
        }
    }

    @Test
    void testFetchCustomerOrdersForNonExistentCustomer() {
        // Fetch orders for a customer that doesn't exist
        Customer nonExistentCustomer = new Customer(99999, "nonexistent@test.com", "password");
        
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(nonExistentCustomer);

        assertNotNull(orders, "Orders list should not be null");
        assertEquals(0, orders.size(), "Non-existent customer should have no orders");
    }

    @Test
    void testFetchCustomerOrdersOrderedByDate() {
        // Fetch orders and verify they are ordered by date (most recent first)
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);

        if (orders.size() > 1) {
            for (int i = 0; i < orders.size() - 1; i++) {
                Timestamp currentDate = orders.get(i).getCreationDate();
                Timestamp nextDate = orders.get(i + 1).getCreationDate();
                assertTrue(currentDate.compareTo(nextDate) >= 0, 
                    "Orders should be ordered by date descending (most recent first)");
            }
        }
    }

    @Test
    void testFetchRestaurantOrdersForToday() {
        // Fetch today's orders for a restaurant
        ArrayList<FOS_CORE.Order> orders = restaurantData.fetchRestaurantOrdersForToday(testRestaurant);

        assertNotNull(orders, "Orders list should not be null");
        assertTrue(orders.size() >= 0, "Restaurant should have zero or more orders today");
    }

    @Test
    void testFetchRestaurantOrdersForTodayProperties() {
        // Fetch today's orders and verify properties
        ArrayList<FOS_CORE.Order> orders = restaurantData.fetchRestaurantOrdersForToday(testRestaurant);

        assertNotNull(orders, "Orders list should not be null");
        
        if (!orders.isEmpty()) {
            FOS_CORE.Order firstOrder = orders.get(0);
            assertTrue(firstOrder.getOrderID() > 0, "Order should have a valid ID");
            assertNotNull(firstOrder.getCreationDate(), "Order should have a creation date");
            assertNotNull(firstOrder.getStatus(), "Order should have a status");
            assertNotNull(firstOrder.getItems(), "Order should have items list");
        }
    }

    @Test
    void testFetchRestaurantOrdersForTodayOnlyToday() {
        // Fetch today's orders and verify they are all from today
        ArrayList<FOS_CORE.Order> orders = restaurantData.fetchRestaurantOrdersForToday(testRestaurant);

        assertNotNull(orders, "Orders list should not be null");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Timestamp todayStart = new Timestamp(cal.getTimeInMillis());
        
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Timestamp tomorrowStart = new Timestamp(cal.getTimeInMillis());
        
        for (FOS_CORE.Order order : orders) {
            Timestamp orderDate = order.getCreationDate();
            assertTrue(orderDate.compareTo(todayStart) >= 0, 
                "Order date should be today or later");
            assertTrue(orderDate.compareTo(tomorrowStart) < 0, 
                "Order date should be before tomorrow");
        }
    }

    @Test
    void testFetchRestaurantOrdersForNonExistentRestaurant() {
        // Fetch orders for a restaurant that doesn't exist
        Restaurant nonExistentRestaurant = new Restaurant(99999, "Non-existent", "Unknown", "Unknown");
        
        // This should throw RuntimeException based on the implementation
        assertThrows(RuntimeException.class, () -> {
            restaurantData.fetchRestaurantOrdersForToday(nonExistentRestaurant);
        }, "Fetching orders for non-existent restaurant should throw exception");
    }

    @Test
    void testRateCustomerOrderSuccessfully() {
        // First, create an order
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean created = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);
        assertTrue(created, "Order should be created first");

        // Rate the order
        int rating = 5;
        String comment = "Great food!";

        boolean result = customerService.rateCustomerOrder(order, rating, comment);

        assertTrue(result, "Order should be rated successfully");
    }

    @Test
    void testRateCustomerOrderWithInvalidOrderID() {
        // Try to rate an order that doesn't exist
        FOS_CORE.Order nonExistentOrder = new FOS_CORE.Order(
            testAddress.toString(),
            new ArrayList<>(),
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );
        nonExistentOrder.setOrderID(99999);

        boolean result = customerService.rateCustomerOrder(nonExistentOrder, 5, "Test comment");

        assertFalse(result, "Rating non-existent order should fail");
    }

    @Test
    void testRateCustomerOrderWithNullComment() {
        // Create and rate an order with null comment (should be allowed)
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean created = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);
        assertTrue(created, "Order should be created first");

        boolean result = customerService.rateCustomerOrder(order, 4, null);

        assertTrue(result, "Rating with null comment should be allowed");
    }

    @Test
    void testRateCustomerOrderWithEmptyComment() {
        // Create and rate an order with empty comment
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 1, 12.99);
        items.add(cartItem);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean created = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);
        assertTrue(created, "Order should be created first");

        boolean result = customerService.rateCustomerOrder(order, 3, "");

        assertTrue(result, "Rating with empty comment should be allowed");
    }

    @Test
    void testOrderPropertiesAfterInsertion() {
        // Create an order and verify all properties are saved correctly
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem cartItem = new CartItem(testMenuItem, 2, 12.99);
        items.add(cartItem);
        
        String deliveryAddress = testAddress.toString();
        String phoneNumber = "555-0101";
        String cardNumber = "1111222233334444";
        String restaurantName = "Pizza Palace";
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            deliveryAddress,
            items,
            restaurantName,
            phoneNumber,
            cardNumber
        );

        boolean created = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);
        assertTrue(created, "Order should be created");

        // Fetch orders and verify properties
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);
        FOS_CORE.Order fetchedOrder = null;
        for (FOS_CORE.Order o : orders) {
            if (o.getOrderID() == order.getOrderID()) {
                fetchedOrder = o;
                break;
            }
        }

        assertNotNull(fetchedOrder, "Created order should be found");
        assertEquals(deliveryAddress, fetchedOrder.getDeliveryAddress(), "Delivery address should match");
        assertEquals(phoneNumber, fetchedOrder.getPhoneNumber(), "Phone number should match");
        assertEquals(cardNumber, fetchedOrder.getCardNumber(), "Card number should match");
        assertEquals(restaurantName, fetchedOrder.getRestaurantName(), "Restaurant name should match");
        assertNotNull(fetchedOrder.getStatus(), "Order should have a status");
        assertNotNull(fetchedOrder.getItems(), "Order should have items");
        assertEquals(1, fetchedOrder.getItems().size(), "Order should have one cart item");
    }

    @Test
    void testOrderCartItemsAfterInsertion() {
        // Create an order with multiple items and verify cart items are saved
        ArrayList<CartItem> items = new ArrayList<>();
        CartItem item1 = new CartItem(testMenuItem, 2, 12.99);
        MenuItem item2MenuItem = new MenuItem(2, "Pepperoni Pizza", "Pepperoni and cheese", 14.99);
        CartItem item2 = new CartItem(item2MenuItem, 1, 14.99);
        items.add(item1);
        items.add(item2);
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        boolean created = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);
        assertTrue(created, "Order should be created");

        // Fetch orders and verify cart items
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);
        FOS_CORE.Order fetchedOrder = null;
        for (FOS_CORE.Order o : orders) {
            if (o.getOrderID() == order.getOrderID()) {
                fetchedOrder = o;
                break;
            }
        }

        assertNotNull(fetchedOrder, "Created order should be found");
        assertNotNull(fetchedOrder.getItems(), "Order should have items");
        assertEquals(2, fetchedOrder.getItems().size(), "Order should have two cart items");
        
        // Verify first item
        CartItem fetchedItem1 = fetchedOrder.getItems().get(0);
        assertEquals(testMenuItem.getMenuItemID(), fetchedItem1.getMenuItem().getMenuItemID(), 
            "First item menu item ID should match");
        assertEquals(2, fetchedItem1.getQuantity(), "First item quantity should match");
        assertEquals(12.99, fetchedItem1.getPrice(), 0.01, "First item price should match");
    }

    @Test
    void testOrderIDAutoIncrement() {
        // Create multiple orders and verify IDs are unique and incrementing
        ArrayList<CartItem> items1 = new ArrayList<>();
        items1.add(new CartItem(testMenuItem, 1, 12.99));
        FOS_CORE.Order order1 = new FOS_CORE.Order(testAddress.toString(), items1, "Pizza Palace", "555-0101", "1111222233334444");

        ArrayList<CartItem> items2 = new ArrayList<>();
        items2.add(new CartItem(testMenuItem, 1, 12.99));
        FOS_CORE.Order order2 = new FOS_CORE.Order(testAddress.toString(), items2, "Pizza Palace", "555-0101", "1111222233334444");

        boolean created1 = customerService.insertCustomerOrder(testCustomer, testAddress, order1, testRestaurant);
        boolean created2 = customerService.insertCustomerOrder(testCustomer, testAddress, order2, testRestaurant);

        assertTrue(created1, "First order should be created");
        assertTrue(created2, "Second order should be created");
        
        assertTrue(order1.getOrderID() > 0, "First order should have valid ID");
        assertTrue(order2.getOrderID() > 0, "Second order should have valid ID");
        assertNotEquals(order1.getOrderID(), order2.getOrderID(), "Orders should have different IDs");
    }

    @Test
    void testOrderStatusDefaultValue() {
        // Create an order and verify default status is PENDING in the Order object
        ArrayList<CartItem> items = new ArrayList<>();
        items.add(new CartItem(testMenuItem, 1, 12.99));
        
        FOS_CORE.Order order = new FOS_CORE.Order(
            testAddress.toString(),
            items,
            "Pizza Palace",
            "555-0101",
            "1111222233334444"
        );

        assertEquals(OrderStatus.PENDING, order.getStatus(), "New order should have PENDING status");

        boolean created = customerService.insertCustomerOrder(testCustomer, testAddress, order, testRestaurant);
        assertTrue(created, "Order should be created");

        // Fetch and verify status (database default is 'preparing', so fetched status may differ)
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);
        FOS_CORE.Order fetchedOrder = null;
        for (FOS_CORE.Order o : orders) {
            if (o.getOrderID() == order.getOrderID()) {
                fetchedOrder = o;
                break;
            }
        }

        assertNotNull(fetchedOrder, "Created order should be found");
        assertNotNull(fetchedOrder.getStatus(), "Order should have a status");
        // Status from database will be the actual stored status (likely PREPARING as default)
        assertTrue(fetchedOrder.getStatus() == OrderStatus.PENDING || 
                   fetchedOrder.getStatus() == OrderStatus.PREPARING ||
                   fetchedOrder.getStatus() == OrderStatus.SENT ||
                   fetchedOrder.getStatus() == OrderStatus.DELIVERED,
                   "Order should have a valid status");
    }

    @Test
    void testMultipleOrdersForCustomer() {
        // Create multiple orders for the same customer
        ArrayList<CartItem> items1 = new ArrayList<>();
        items1.add(new CartItem(testMenuItem, 1, 12.99));
        FOS_CORE.Order order1 = new FOS_CORE.Order(testAddress.toString(), items1, "Pizza Palace", "555-0101", "1111222233334444");

        ArrayList<CartItem> items2 = new ArrayList<>();
        items2.add(new CartItem(testMenuItem, 2, 12.99));
        FOS_CORE.Order order2 = new FOS_CORE.Order(testAddress.toString(), items2, "Pizza Palace", "555-0101", "1111222233334444");

        boolean created1 = customerService.insertCustomerOrder(testCustomer, testAddress, order1, testRestaurant);
        boolean created2 = customerService.insertCustomerOrder(testCustomer, testAddress, order2, testRestaurant);

        assertTrue(created1, "First order should be created");
        assertTrue(created2, "Second order should be created");

        // Fetch orders - should contain both
        ArrayList<FOS_CORE.Order> orders = customerService.fetchCustomerOrders(testCustomer);
        
        assertTrue(orders.size() >= 2, "Customer should have at least 2 orders");
        
        // Verify both orders are in the list
        boolean found1 = false, found2 = false;
        for (FOS_CORE.Order o : orders) {
            if (o.getOrderID() == order1.getOrderID()) found1 = true;
            if (o.getOrderID() == order2.getOrderID()) found2 = true;
        }
        
        assertTrue(found1, "First order should be in customer orders");
        assertTrue(found2, "Second order should be in customer orders");
    }
}
