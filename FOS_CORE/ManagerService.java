package FOS_CORE;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class ManagerService implements IManagerService {

    @Override
    public Restaurant getRestaurantDetails(Manager manager) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void updateRestaurantInfo(Manager manager, Restaurant details) {
        // TODO: Implementation
    }

    @Override
    public void addMenuItem(Manager manager, MenuItem item) {
        // TODO: Implementation
    }

    @Override
    public void editMenuItem(Manager manager, MenuItem item) {
        // TODO: Implementation
    }

    @Override
    public void removeMenuItem(Manager manager, MenuItem item) {
        // TODO: Implementation
    }

    @Override
    public List<Order> viewIncomingOrders(Manager manager) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void updateOrderStatus(Manager manager, Order order, String status) {
        // TODO: Implementation
    }

    @Override
    public Map<String, Object> generateMonthlyReport(Manager manager) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void createDiscount(Manager manager, MenuItem item, double percentage, LocalDate startDate, LocalDate endDate) {
        // TODO: Implementation
    }

    private void validateMenuItem(MenuItem item) {
        // TODO: Implementation
    }

    private double calculateDiscount(MenuItem item) {
        // TODO: Implementation
        return 0.0;
    }

    private void saveRestaurantChanges(Manager manager, Restaurant restaurant) {
        // TODO: Implementation
    }
}