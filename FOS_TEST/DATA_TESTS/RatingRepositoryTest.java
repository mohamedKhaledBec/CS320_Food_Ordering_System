package FOS_TEST.DATA_TESTS;

import FOS_CORE.Restaurant;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RatingRepositoryTest {

    private RestaurantData restaurantData;
    private Restaurant ratedRestaurant;
    private Restaurant unratedRestaurant;

    @BeforeEach
    void setUp()
    {
        restaurantData = new RestaurantData();
        ratedRestaurant = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
        unratedRestaurant = new Restaurant(999, "Nonexistent", "Unknown", "Unknown");
    }

    @Test
    void testCalculateRestaurantRating_ValidRange()
    {

        double rating = restaurantData.calculateRestaurantRating(ratedRestaurant);
        assertTrue(rating > 0.0, "Rated restaurant should have a rating greater than 0");
        assertTrue(rating <= 5.0, "Rating should not exceed 5.0");
    }

    @Test
    void testCalculateRestaurantRating_NonExistentRestaurant()
    {
        double rating = restaurantData.calculateRestaurantRating(unratedRestaurant);
        assertEquals(0.0, rating, "Rating for non-existent restaurant should be 0.0");
    }
}