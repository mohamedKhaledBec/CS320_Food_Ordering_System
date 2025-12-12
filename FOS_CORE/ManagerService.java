package FOS_CORE;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ManagerService implements IManagerService {

    private final FOS_DATA.ManagerService DB = new FOS_DATA.ManagerService();

    @Override
    public ArrayList<Restaurant> getManagerRestaurants(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        return DB.fetchManagerRestaurants(manager);
    }

    @Override
    public void updateRestaurantInfo(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant details must not be null");
        }
        if (restaurant.getRestaurantName() == null || restaurant.getRestaurantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        boolean ok = DB.saveRestaurantInfo(restaurant);
        if (!ok) throw new RuntimeException("Failed to save restaurant info");
        saveRestaurantChanges(restaurant);
    }

    @Override
    public void addMenuItem(Restaurant restaurant, MenuItem item) {
        if (restaurant == null || item == null) {
            throw new IllegalArgumentException("Restaurant and menu item must not be null");
        }
        validateMenuItem(item);
        boolean added = DB.addMenuItem(item, restaurant);
        if (!added) throw new RuntimeException("Failed to add menu item");
    }

    @Override
    public void editMenuItem(Restaurant restaurant, MenuItem item) {
        if (restaurant == null || item == null) {
            throw new IllegalArgumentException("Restaurant and menu item must not be null");
        }
        validateMenuItem(item);
        boolean ok = DB.updateMenuItem(item);
        if (!ok) throw new RuntimeException("Failed to update menu item");
    }

    @Override
    public void removeMenuItem(Restaurant restaurant, MenuItem item) {
        if (restaurant == null || item == null) {
            throw new IllegalArgumentException("Restaurant and menu item must not be null");
        }
        boolean ok = DB.removeMenuItem(item);
        if (!ok) throw new RuntimeException("Failed to remove menu item");
    }

    @Override
    public void updateOrderStatus(Order order, String status) {
        if (order == null || status == null) {
            throw new IllegalArgumentException("Order and status must not be null");
        }
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    @Override
    public String generateMonthlyReport(Manager manager, Restaurant restaurant, Date date) {
        if (manager == null || restaurant == null || date == null) {
            throw new IllegalArgumentException("Manager, restaurant, and date must not be null");
        }
        return DB.generateMonthlyReport(restaurant, date);
    }

    @Override
    public void createDiscount(Manager manager, MenuItem item, String description, double percentage, Timestamp startDate, Timestamp endDate) {
        if (manager == null || item == null) {
            throw new IllegalArgumentException("Manager and menu item must not be null");
        }
        if (startDate == null || endDate == null || endDate.before(startDate)) {
            throw new IllegalArgumentException("Invalid date range for discount");
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        if (description == null) description = "";
        // Ensure no overlapping discounts for this menu item
        for (Discount existing : item.getDiscounts()) {
            if (existing == null) continue;
            Timestamp exStart = existing.getStartDate();
            Timestamp exEnd = existing.getEndDate();
            if (exStart == null || exEnd == null) continue;
            // overlap if newStart <= exEnd && newEnd >= exStart
            if (!endDate.before(exStart) && !startDate.after(exEnd)) {
                throw new IllegalArgumentException("New discount period overlaps an existing discount for this menu item");
            }
        }

        Discount d = new Discount(-1, "Discount", description, percentage, startDate, endDate);
        if(!DB.createDiscount(d, item)) {
            throw new RuntimeException("Failed to create discount in database");
        }
        item.getDiscounts().add(d);
    }

    private void validateMenuItem(MenuItem item) {
        if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name is required");
        }
        if (item.getPrice() < 0) {
            throw new IllegalArgumentException("Menu item price cannot be negative");
        }
    }

    private void saveRestaurantChanges(Restaurant restaurant) {
        if (restaurant == null) return;
        DB.saveRestaurantInfo(restaurant);
    }
}