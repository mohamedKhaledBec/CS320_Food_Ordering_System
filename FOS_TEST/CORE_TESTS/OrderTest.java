/**
 * This test class is written by Mohamed Khaled Becetti
 */

package FOS_TEST.CORE_TESTS;

import FOS_CORE.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class OrderTest {
    private Order order;
    private ArrayList<CartItem> items;

 
    @BeforeEach
    public void setUp() throws Exception {
        
        items = new ArrayList<>();
        order = new Order("123 Main St", items, "Test Restaurant", "555-1234", "4111111111111111");

        setField(order, "items", new ArrayList<>());
    }

    // ========== OrderID Tests ==========

    /**
     * @brief Test getOrderID returns correct value.
     * @tests Validates orderID getter after field injection.
     */
    @Test
    public void testGetOrderID() throws Exception {
        setField(order, "orderID", 12345);
        assertEquals(12345, order.getOrderID());
    }

    /**
     * @brief Test setOrderID updates orderID correctly.
     * @tests Validates orderID setter modifies internal state.
     */
    @Test
    public void testSetOrderID() {
        order.setOrderID(99999);
        assertEquals(99999, order.getOrderID());
    }

    /**
     * @brief Test setOrderID accepts zero.
     * @tests Validates orderID can be set to 0 (no validation enforced).
     */
    @Test
    public void testSetOrderIDAcceptsZero() {
        order.setOrderID(0);
        assertEquals(0, order.getOrderID());
    }

    /**
     * @brief Test setOrderID accepts negative values.
     * @tests Validates orderID can be negative (no validation enforced).
     */
    @Test
    public void testSetOrderIDAcceptsNegative() {
        order.setOrderID(-100);
        assertEquals(-100, order.getOrderID());
    }

    // ========== OrderStatus Tests ==========

    /**
     * @brief Test getStatus returns correct OrderStatus.
     * @tests Validates status getter reflects injected value.
     */
    @Test
    public void testGetStatus() throws Exception {
        setField(order, "status", OrderStatus.PREPARING);
        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }

    /**
     * @brief Test setStatus updates status correctly.
     * @tests Validates status setter modifies internal state to different OrderStatus values.
     */
    @Test
    public void testSetStatusToPreparing() {
        order.setStatus(OrderStatus.PREPARING);
        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }

    /**
     * @brief Test setStatus to DELIVERED.
     * @tests Validates status transition to terminal state.
     */
    @Test
    public void testSetStatusToDelivered() {
        order.setStatus(OrderStatus.DELIVERED);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    /**
     * @brief Test setStatus to SENT.
     * @tests Validates status can be set to sent state.
     */
    @Test
    public void testSetStatusToSent() {
        order.setStatus(OrderStatus.SENT);
        assertEquals(OrderStatus.SENT, order.getStatus());
    }

    /**
     * @brief Test setStatus accepts null (no validation).
     * @tests Validates current implementation allows null status (may be design bug).
     */
    @Test
    public void testSetStatusAcceptsNull() {
        order.setStatus(null);
        assertNull(order.getStatus());
    }

    // ========== CreationDate Tests ==========

    /**
     * @brief Test getCreationDate returns correct Timestamp.
     * @tests Validates creation date getter reflects injected value.
     */
    @Test
    public void testGetCreationDate() throws Exception {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        setField(order, "creationDate", now);
        assertEquals(now, order.getCreationDate());
    }

    /**
     * @brief Test setCreationDate updates date correctly.
     * @tests Validates creation date setter modifies internal state.
     */
    @Test
    public void testSetCreationDate() {
        Timestamp futureDate = new Timestamp(System.currentTimeMillis() + 86400000); // +1 day
        order.setCreationDate(futureDate);
        assertEquals(futureDate, order.getCreationDate());
    }

    /**
     * @brief Test setDate (alias for setCreationDate).
     * @tests Validates alternative setter method works identically.
     */
    @Test
    public void testSetDate() {
        Timestamp specificDate = new Timestamp(1640000000000L);
        order.setDate(specificDate);
        assertEquals(specificDate, order.getCreationDate());
    }

    /**
     * @brief Test setCreationDate accepts null.
     * @tests Validates creation date can be set to null (no validation).
     */
    @Test
    public void testSetCreationDateAcceptsNull() {
        order.setCreationDate(null);
        assertNull(order.getCreationDate());
    }

    // ========== Rating Tests ==========

    /**
     * @brief Test getRating returns correct Rating.
     * @tests Validates rating getter reflects injected value.
     */
    @Test
    public void testGetRating() throws Exception {
        Rating mockRating = new Rating(5, "Excellent service!");
        setField(order, "rating", mockRating);
        assertEquals(mockRating, order.getRating());
    }

    /**
     * @brief Test setRating updates rating correctly.
     * @tests Validates rating setter modifies internal state.
     */
    @Test
    public void testSetRating() {
        Rating newRating = new Rating(4, "Good food");
        order.setRating(newRating);
        assertEquals(newRating, order.getRating());
    }

    /**
     * @brief Test setRating accepts null.
     * @tests Validates rating can be null initially (no validation).
     */
    @Test
    public void testSetRatingAcceptsNull() {
        order.setRating(null);
        assertNull(order.getRating());
    }

    // ========== DeliveryAddress Tests ==========

    /**
     * @brief Test getDeliveryAddress returns correct address string.
     * @tests Validates delivery address getter reflects injected value.
     */
    @Test
    public void testGetDeliveryAddress() throws Exception {
        setField(order, "deliveryAddress", "456 Oak Ave, Springfield");
        assertEquals("456 Oak Ave, Springfield", order.getDeliveryAddress());
    }

    /**
     * @brief Test setDeliveryAddress updates address correctly.
     * @tests Validates delivery address setter modifies internal state.
     */
    @Test
    public void testSetDeliveryAddress() {
        order.setDeliveryAddress("789 Elm St, Shelbyville");
        assertEquals("789 Elm St, Shelbyville", order.getDeliveryAddress());
    }

    /**
     * @brief Test setDeliveryAddress accepts empty string.
     * @tests Validates address can be empty (no validation enforced).
     */
    @Test
    public void testSetDeliveryAddressAcceptsEmpty() {
        order.setDeliveryAddress("");
        assertEquals("", order.getDeliveryAddress());
    }

    /**
     * @brief Test setDeliveryAddress accepts null.
     * @tests Validates address can be null (no validation enforced).
     */
    @Test
    public void testSetDeliveryAddressAcceptsNull() {
        order.setDeliveryAddress(null);
        assertNull(order.getDeliveryAddress());
    }

    // ========== RestaurantName Tests ==========

    /**
     * @brief Test getRestaurantName returns correct restaurant name.
     * @tests Validates restaurant name getter reflects injected value.
     */
    @Test
    public void testGetRestaurantName() throws Exception {
        setField(order, "restaurantName", "Pizza Palace");
        assertEquals("Pizza Palace", order.getRestaurantName());
    }

    /**
     * @brief Test setRestaurantName updates name correctly.
     * @tests Validates restaurant name setter modifies internal state.
     */
    @Test
    public void testSetRestaurantName() {
        order.setRestaurantName("Burger Barn");
        assertEquals("Burger Barn", order.getRestaurantName());
    }

    /**
     * @brief Test setRestaurantName accepts empty string.
     * @tests Validates name can be empty (no validation enforced).
     */
    @Test
    public void testSetRestaurantNameAcceptsEmpty() {
        order.setRestaurantName("");
        assertEquals("", order.getRestaurantName());
    }

    /**
     * @brief Test setRestaurantName accepts null.
     * @tests Validates name can be null (no validation enforced).
     */
    @Test
    public void testSetRestaurantNameAcceptsNull() {
        order.setRestaurantName(null);
        assertNull(order.getRestaurantName());
    }

    // ========== PhoneNumber Tests ==========

    /**
     * @brief Test getPhoneNumber returns correct phone number.
     * @tests Validates phone number getter reflects injected value.
     */
    @Test
    public void testGetPhoneNumber() throws Exception {
        setField(order, "phoneNumber", "555-9876");
        assertEquals("555-9876", order.getPhoneNumber());
    }

    /**
     * @brief Test setPhoneNumber updates phone number correctly.
     * @tests Validates phone number setter modifies internal state.
     */
    @Test
    public void testSetPhoneNumber() {
        order.setPhoneNumber("555-4321");
        assertEquals("555-4321", order.getPhoneNumber());
    }

    /**
     * @brief Test setPhoneNumber accepts empty string.
     * @tests Validates phone number can be empty (no validation enforced).
     */
    @Test
    public void testSetPhoneNumberAcceptsEmpty() {
        order.setPhoneNumber("");
        assertEquals("", order.getPhoneNumber());
    }

    /**
     * @brief Test setPhoneNumber accepts null.
     * @tests Validates phone number can be null (no validation enforced).
     */
    @Test
    public void testSetPhoneNumberAcceptsNull() {
        order.setPhoneNumber(null);
        assertNull(order.getPhoneNumber());
    }

    // ========== CardNumber Tests ==========

    /**
     * @brief Test getCardNumber returns correct card number.
     * @tests Validates card number getter reflects injected value.
     */
    @Test
    public void testGetCardNumber() throws Exception {
        setField(order, "CardNumber", "4532123456789010");
        assertEquals("4532123456789010", order.getCardNumber());
    }

    /**
     * @brief Test setCardNumber updates card number correctly.
     * @tests Validates card number setter modifies internal state (note: parameter name is cardNumber, field is CardNumber).
     */
    @Test
    public void testSetCardNumber() {
        order.setCardNumber("5425123456789012");
        assertEquals("5425123456789012", order.getCardNumber());
    }

    /**
     * @brief Test setCardNumber accepts empty string.
     * @tests Validates card number can be empty (no validation enforced).
     */
    @Test
    public void testSetCardNumberAcceptsEmpty() {
        order.setCardNumber("");
        assertEquals("", order.getCardNumber());
    }

    /**
     * @brief Test setCardNumber accepts null.
     * @tests Validates card number can be null (no validation enforced).
     */
    @Test
    public void testSetCardNumberAcceptsNull() {
        order.setCardNumber(null);
        assertNull(order.getCardNumber());
    }

    // ========== Items Collection Tests ==========

    /**
     * @brief Test getItems returns ArrayList of CartItem.
     * @tests Validates items getter returns reference to internal collection.
     */
    @Test
    public void testGetItems() throws Exception {
        ArrayList<CartItem> testItems = new ArrayList<>();
        setField(order, "items", testItems);
        assertSame(testItems, order.getItems());
    }

    /**
     * @brief Test getItems reflects mutations via list reference.
     * @tests Validates mutations to returned list affect internal state (defensive copy not implemented).
     */
    @Test
    public void testGetItemsMutationReflectsInternally() throws Exception {
        ArrayList<CartItem> testItems = new ArrayList<>();
        MenuItem burgerItem = allocateMenuItem(1, "Burger", "Delicious burger", 9.99);
        CartItem item = new CartItem(burgerItem, 2, 9.99);
        testItems.add(item);
        setField(order, "items", testItems);

        ArrayList<CartItem> retrievedItems = order.getItems();
        MenuItem friesItem = allocateMenuItem(2, "Fries", "Golden fries", 3.99);
        retrievedItems.add(new CartItem(friesItem, 1, 3.99));

        // Verify mutation is visible via original getter
        assertEquals(2, order.getItems().size());
    }

    /**
     * @brief Test setItems replaces entire items collection.
     * @tests Validates items setter replaces internal ArrayList reference.
     */
    @Test
    public void testSetItems() throws Exception {
        ArrayList<CartItem> newItems = new ArrayList<>();
        MenuItem pizzaItem = allocateMenuItem(3, "Pizza", "Fresh pizza", 12.99);
        MenuItem saladItem = allocateMenuItem(4, "Salad", "Healthy salad", 7.99);
        newItems.add(new CartItem(pizzaItem, 1, 12.99));
        newItems.add(new CartItem(saladItem, 2, 7.99));
        order.setItems(newItems);

        assertEquals(2, order.getItems().size());
        assertSame(newItems, order.getItems());
    }

    /**
     * @brief Test setItems accepts empty ArrayList.
     * @tests Validates items can be set to empty collection (valid edge case).
     */
    @Test
    public void testSetItemsAcceptsEmpty() {
        order.setItems(new ArrayList<>());
        assertEquals(0, order.getItems().size());
    }

    /**
     * @brief Test setItems accepts null (no validation).
     * @tests Validates current implementation allows null items list (possible design issue).
     */
    @Test
    public void testSetItemsAcceptsNull() {
        order.setItems(null);
        assertNull(order.getItems());
    }

    /**
     * @brief Test multiple CartItems in collection.
     * @tests Validates order can store multiple distinct items.
     */
    @Test
    public void testMultipleItemsInCollection() throws Exception {
        ArrayList<CartItem> multiItems = new ArrayList<>();
        MenuItem chickenItem = allocateMenuItem(5, "Chicken", "Grilled chicken", 15.99);
        MenuItem pastaItem = allocateMenuItem(6, "Pasta", "Italian pasta", 11.99);
        MenuItem dessertItem = allocateMenuItem(7, "Dessert", "Sweet dessert", 5.99);
        multiItems.add(new CartItem(chickenItem, 3, 15.99));
        multiItems.add(new CartItem(pastaItem, 2, 11.99));
        multiItems.add(new CartItem(dessertItem, 1, 5.99));
        order.setItems(multiItems);

        assertEquals(3, order.getItems().size());
    }

    // ========== Constructor Tests ==========

    /**
     * @brief Test constructor initializes order with delivery address and items.
     * @tests Validates primary constructor sets basic properties and defaults status to PENDING.
     */
    @Test
    public void testConstructorInitializesBasicProperties() throws Exception {
        ArrayList<CartItem> constructorItems = new ArrayList<>();
        MenuItem testMenuItem = allocateMenuItem(8, "Test Item", "Test description", 9.99);
        constructorItems.add(new CartItem(testMenuItem, 1, 9.99));
        Order newOrder = new Order("999 Test Lane", constructorItems, "Test Restaurant", "555-0000", "1111222233334444");

        assertEquals("999 Test Lane", newOrder.getDeliveryAddress());
        assertEquals("Test Restaurant", newOrder.getRestaurantName());
        assertEquals("555-0000", newOrder.getPhoneNumber());
        assertEquals("1111222233334444", newOrder.getCardNumber());
        assertSame(constructorItems, newOrder.getItems());
    }

    /**
     * @brief Test constructor sets status to PENDING by default.
     * @tests Validates default status initialization in constructor.
     */
    @Test
    public void testConstructorDefaultStatusPending() throws Exception {
        ArrayList<CartItem> constructorItems = new ArrayList<>();
        Order newOrder = new Order("123 Street", constructorItems, "Restaurant", "555-1111", "1234567890123456");
        assertEquals(OrderStatus.PENDING, newOrder.getStatus());
    }

    /**
     * @brief Test full constructor with all parameters.
     * @tests Validates constructor accepting orderID, status, rating, and date.
     */
    @Test
    public void testFullConstructorWithAllParameters() throws Exception {
        ArrayList<CartItem> allItems = new ArrayList<>();
        MenuItem item1 = allocateMenuItem(9, "Item1", "First item", 5.0);
        allItems.add(new CartItem(item1, 2, 5.0));
        Timestamp specificDate = new Timestamp(1640000000000L);
        Rating rating = new Rating(5, "Perfect!");
        Order fullOrder = new Order("456 Avenue", allItems, specificDate, OrderStatus.DELIVERED, "Cuisine Place", 1001, rating, "555-2222", "9999888877776666");

        assertEquals(1001, fullOrder.getOrderID());
        assertEquals(OrderStatus.DELIVERED, fullOrder.getStatus());
        assertEquals(specificDate, fullOrder.getCreationDate());
        assertEquals(rating, fullOrder.getRating());
        assertEquals("456 Avenue", fullOrder.getDeliveryAddress());
        assertEquals("Cuisine Place", fullOrder.getRestaurantName());
        assertEquals("555-2222", fullOrder.getPhoneNumber());
        assertEquals("9999888877776666", fullOrder.getCardNumber());
        assertSame(allItems, fullOrder.getItems());
    }






    // ========== Helper Methods ==========

    /**
     * Helper method to inject values into private fields using reflection.
     * Enables field mutation without public setters.
     */
    private void setField(Order order, String fieldName, Object value) throws Exception {
        Field field = Order.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(order, value);
    }

    /**
     * Bypasses DB access via RestaurantService.fetchMenuItemDiscounts().
     * Directly injects all fields to avoid initialization side effects.
     * Used to avoid DB related dependencies in unit tests.
     */
    private MenuItem allocateMenuItem(int id, String name, String description, double price) throws Exception {
        MenuItem item = new MenuItem(); // Use no-arg constructor
        setField(item, "menuItemID", id);
        setField(item, "itemName", name);
        setField(item, "description", description);
        setField(item, "price", price);
        setField(item, "discounts", new ArrayList<>()); // Empty discounts list
        return item;
    }

   
    private void setField(MenuItem item, String fieldName, Object value) throws Exception {
        Field field = MenuItem.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(item, value);
    }
}
