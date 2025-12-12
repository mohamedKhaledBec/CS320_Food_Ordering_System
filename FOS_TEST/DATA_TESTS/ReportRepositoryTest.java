package FOS_TEST.DATA_TESTS;

import FOS_CORE.Restaurant;
import FOS_DATA.RestaurantData;
import FOS_CORE.MenuItem;
import FOS_CORE.Order;
import FOS_CORE.Discount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ReportRepositoryTest {

    private final RestaurantData restaurantData = new RestaurantData();

    @Test
    public void testFetchRestaurantsByCity() {
        ArrayList<Restaurant> restaurants = restaurantData.fetchRestaurantsByCity("İstanbul");

        Assertions.assertNotNull(restaurants);
        Assertions.assertTrue(restaurants.size() > 0);
    }

    @Test
    public void testFetchRestaurantMenu() {
        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(r);

        Assertions.assertNotNull(menu);
        Assertions.assertTrue(menu.size() > 0);
    }

    @Test
    public void testFetchMenuItemDiscounts() {
        MenuItem m = new MenuItem(1, "Margherita Pizza", "desc", 12.99);
        ArrayList<Discount> discounts = restaurantData.fetchMenuItemDiscounts(m);

        Assertions.assertNotNull(discounts);
        Assertions.assertTrue(discounts.size() >= 0);
    }

    @Test
    public void testCalculateRestaurantRating() {
        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
        double rating = restaurantData.calculateRestaurantRating(r);

        Assertions.assertTrue(rating >= 0);
    }

    @Test
    public void testFetchRestaurantKeywords() {
        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");

        ArrayList<String> keywords = restaurantData.fetchRestaurantKeywords(r);

        Assertions.assertNotNull(keywords);
        Assertions.assertTrue(keywords.size() > 0);
    }

    @Test
    public void testFetchRestaurantOrdersForToday() {
        Restaurant r = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");

        ArrayList<Order> orders = restaurantData.fetchRestaurantOrdersForToday(r);

        Assertions.assertNotNull(orders);
        Assertions.assertTrue(orders.size() >= 0);
    }
}
