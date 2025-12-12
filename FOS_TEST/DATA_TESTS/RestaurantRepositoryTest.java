package FOS_TEST.DATA_TESTS;

import FOS_CORE.*;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestaurantRepositoryTest {

    private static RestaurantData restaurantData;

    @BeforeAll
    public static void setup() {
        restaurantData = new RestaurantData();
    }

    // ------------------------------
    // 1. fetchRestaurantsByCity()
    // ------------------------------
    @Test
    @Order(1)
    public void testFetchRestaurantsByCity() {
        ArrayList<Restaurant> list = restaurantData.fetchRestaurantsByCity("İstanbul");

        assertNotNull(list);
        assertTrue(list.size() >= 3); // Pizza Palace, Sushi Stop, Burger Bonanza

        Restaurant r = list.get(0);
        assertNotNull(r.getRestaurantName());
        assertNotEquals(0, r.getRestaurantID());
    }

    // ------------------------------
    // 2. fetchRestaurantMenu()
    // ------------------------------
    @Test
    @Order(2)
    public void testFetchRestaurantMenu() {

        // Restaurant 1 -> Pizza Palace
        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");

        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(r);

        assertNotNull(menu);
        assertTrue(menu.size() >= 3); // 3 items inserted in SQL

        MenuItem item = menu.get(0);
        assertNotEquals(0, item.getMenuItemID());
    }

    // ------------------------------
    // 3. fetchMenuItemDiscounts()
    // ------------------------------
    @Test
    @Order(3)
    public void testFetchMenuItemDiscounts() {

        // MenuItem 1 has a discount in your data
        MenuItem item = new MenuItem(1, "Margherita", "test", 12.99);

        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(item);

        assertNotNull(discounts);
        assertTrue(discounts.size() >= 1);

        Discount d = discounts.get(0);
        assertEquals("Pizza Deal", d.getDiscountName());
    }

    // ------------------------------
    // 4. calculateRestaurantRating()
    // ------------------------------
    @Test
    @Order(4)
    public void testCalculateRestaurantRating() {

        // Restaurant 1 has delivered orders in your SQL
        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");

        double rating = restaurantData.calculateRestaurantRating(r);

        assertTrue(rating > 0); // Pizza Palace has ratings in SQL
        assertTrue(rating <= 5);
    }

    // ------------------------------
    // 5. fetchRestaurantKeywords()
    // ------------------------------
    @Test
    @Order(5)
    public void testFetchRestaurantKeywords() {

        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");

        ArrayList<String> keywords = restaurantData.fetchRestaurantKeywords(r);

        assertNotNull(keywords);
        assertTrue(keywords.contains("pizza"));
        assertTrue(keywords.contains("italian"));
    }

    // ------------------------------
    // 6. fetchRestaurantOrdersForToday()
    // ------------------------------
    @Test
    @Order(6)
    public void testFetchRestaurantOrdersForToday() {

        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");

        ArrayList<Order> orders = restaurantData.fetchRestaurantOrdersForToday(r);

        assertNotNull(orders);

        // You might have 0 today if run on another date, so no assertTrue(size > 0)
        if (!orders.isEmpty()) {
            Order o = orders.get(0);
            assertNotEquals(0, o.getOrderID());
            assertNotNull(o.getOrderStatus());
            assertNotNull(o.getDeliveryAddress());
        }
    }
}
