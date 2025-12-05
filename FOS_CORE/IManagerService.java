package FOS_CORE;

import java.util.ArrayList;
import java.util.Map;
import java.sql.Date;

public interface IManagerService {
    Restaurant getRestaurantDetails(Manager manager);
    void updateRestaurantInfo(Manager manager, Restaurant details);
    void addMenuItem(Manager manager, MenuItem item);
    void editMenuItem(Manager manager, MenuItem item);
    void removeMenuItem(Manager manager, MenuItem item);
    ArrayList<Order> viewIncomingOrders(Manager manager);
    void updateOrderStatus(Manager manager, Order order, String status);
    String generateMonthlyReport(Manager manager, Restaurant restaurant, Date date);
    void createDiscount(Manager manager, MenuItem item, String description, double percentage, Date startDate, Date endDate);
}