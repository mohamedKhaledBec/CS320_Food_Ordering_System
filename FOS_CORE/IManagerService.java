package FOS_CORE;

import java.util.ArrayList;
import java.sql.Date;

public interface IManagerService {
    Restaurant getRestaurantDetails(Manager manager);
    void updateRestaurantInfo(Restaurant restaurant);
    void addMenuItem(Restaurant restaurant, MenuItem item) ;
    void editMenuItem(Restaurant restaurant, MenuItem item);
    void removeMenuItem(Restaurant restaurant, MenuItem item);
    ArrayList<Order> viewIncomingOrders(Manager manager);
    void updateOrderStatus(Order order, String status);
    String generateMonthlyReport(Manager manager, Restaurant restaurant, Date date);
    void createDiscount(Manager manager, MenuItem item, String description, double percentage, Date startDate, Date endDate);
}