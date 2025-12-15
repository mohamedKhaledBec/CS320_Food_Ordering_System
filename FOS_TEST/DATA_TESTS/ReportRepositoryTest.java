package FOS_TEST.DATA_TESTS;

import FOS_CORE.Order;
import FOS_CORE.Restaurant;
import FOS_DATA.RestaurantData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

public class ReportRepositoryTest {

    private RestaurantData restaurantData;
    private Restaurant testRestaurant;
    private Restaurant nonExistentRestaurant;

    @BeforeEach
    void setUp()
    {
        restaurantData = new RestaurantData();
        // Using existing Restaurant ID 1 (Pizza Palace)
        testRestaurant = new Restaurant(1, "Pizza Palace", "Italian", "İstanbul");
        nonExistentRestaurant = new Restaurant(999, "Ghost Eatery", "Unknown", "Unknown");
    }

    @Test
    void testFetchRestaurantOrdersForToday_NotNull()
    {
        ArrayList<Order> dailyReport = restaurantData.fetchRestaurantOrdersForToday(testRestaurant);

        assertNotNull(dailyReport, "Daily report list should not be null");
    }

    @Test
    void testFetchRestaurantOrdersForToday_DateBoundary()
    {
        ArrayList<Order> dailyReport = restaurantData.fetchRestaurantOrdersForToday(testRestaurant);
        if (!dailyReport.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Timestamp todayStart = new Timestamp(cal.getTimeInMillis());

            cal.add(Calendar.DAY_OF_MONTH, 1);
            Timestamp tomorrowStart = new Timestamp(cal.getTimeInMillis());

            for (Order order : dailyReport) {
                Timestamp orderDate = order.getCreationDate();
                assertTrue(orderDate.compareTo(todayStart) >= 0, "Order date must be on or after today's start");
                assertTrue(orderDate.compareTo(tomorrowStart) < 0, "Order date must be before tomorrow's start");
            }
        }
    }

    @Test
    void testFetchRestaurantOrdersForToday_NonExistentRestaurant()
    {
        assertThrows(RuntimeException.class, () -> {
            restaurantData.fetchRestaurantOrdersForToday(nonExistentRestaurant);
        }, "Fetching orders for non-existent restaurant should throw a RuntimeException if underlying DB connection fails");
    }
}