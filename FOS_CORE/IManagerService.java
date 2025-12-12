package FOS_CORE;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;

public interface IManagerService {;
    ArrayList<Restaurant> getManagerRestaurants(Manager manager);
    void updateRestaurantInfo(Restaurant restaurant);
    void addMenuItem(Restaurant restaurant, MenuItem item) ;
    void editMenuItem(Restaurant restaurant, MenuItem item);
    void removeMenuItem(Restaurant restaurant, MenuItem item);
    void updateOrderStatus(Order order, String status);
    String generateMonthlyReport(Manager manager, Restaurant restaurant, Date date);
    void createDiscount(Manager manager, MenuItem item, String description, double percentage, Timestamp startDate, Timestamp endDate);
}