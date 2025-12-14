package FOS_TEST.DATA_TESTS;

import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantRepositoryTest
{

    private RestaurantData restaurantData;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp()
    {
        restaurantData = new RestaurantData();
        testRestaurant = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
    }

    @Test
    void testFetchRestaurantsByCity_Found()
    {
        ArrayList<Restaurant> restaurants = restaurantData.fetchRestaurantsByCity("İstanbul");
        assertNotNull(restaurants, "Result list should not be null");
        assertTrue(restaurants.size() > 0, "Should find at least one restaurant in İstanbul");
        boolean found = restaurants.stream().anyMatch(r -> r.getRestaurantID() == 1);
        assertTrue(found, "Pizza Palace (ID 1) should be found in İstanbul");
    }

    @Test
    void testFetchRestaurantsByCity_NotFound()
    {
        ArrayList<Restaurant> restaurants = restaurantData.fetchRestaurantsByCity("NonexistentCity");
        assertNotNull(restaurants, "Result list should not be null");
        assertEquals(0, restaurants.size(), "Should find zero restaurants in a non-existent city");
    }

    @Test
    void testFetchRestaurantMenu()
    {
        ArrayList<MenuItem> menu = restaurantData.fetchRestaurantMenu(testRestaurant);

        assertNotNull(menu, "Menu should not be null");
        assertTrue(menu.size() > 0, "Restaurant should have menu items (e.g., Margherita Pizza)");
    }

    @Test
    void testFetchRestaurantKeywords()
    {
        ArrayList<String> keywords = restaurantData.fetchRestaurantKeywords(testRestaurant);
        assertNotNull(keywords, "Keywords list should not be null");
        assertTrue(keywords.contains("pizza") && keywords.contains("italian"), "Keywords should contain 'pizza' and 'italian'");
    }
}