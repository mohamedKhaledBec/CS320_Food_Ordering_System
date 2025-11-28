package FOS_CORE;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public interface IManagerService {
    Restaurant getRestaurantDetails(Manager manager);
    void updateRestaurantInfo(Manager manager, Restaurant details);
    void addMenuItem(Manager manager, MenuItem item);
    void editMenuItem(Manager manager, MenuItem item);
    void removeMenuItem(Manager manager, MenuItem item);
    List<Order> viewIncomingOrders(Manager manager);
    void updateOrderStatus(Manager manager, Order order, String status);
    Map<String, Object> generateMonthlyReport(Manager manager);
    void createDiscount(Manager manager, MenuItem item, double percentage, LocalDate startDate, LocalDate endDate);
}