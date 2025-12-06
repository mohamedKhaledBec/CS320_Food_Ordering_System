package FOS_CORE;

import java.sql.Date;
import java.util.ArrayList;

public class ManagerService implements IManagerService {

    private final FOS_DATA.ManagerService DB = new FOS_DATA.ManagerService();

    @Override
    public Restaurant getRestaurantDetails(Manager manager) {
        // TODO: IMPLEMENT
        return null;
    }

    @Override
    public void updateRestaurantInfo(Restaurant restaurant) {
        // TODO
    }

    @Override
    public void addMenuItem(Restaurant restaurant, MenuItem item) {
        // TODO
    }

    @Override
    public void editMenuItem(Restaurant restaurant, MenuItem item) {
         // TODO
    }

    @Override
    public void removeMenuItem(Restaurant restaurant, MenuItem item) {
        // TODO
    }

    @Override
    public void updateRestaurantInfo(Manager manager, Restaurant details) {
        if (manager == null || details == null) {
            throw new IllegalArgumentException("Manager and restaurant details must not be null");
        }
        if (details.getRestaurantName() == null || details.getRestaurantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        details.setRestaurantID(manager.getUserID());
        boolean ok = DB.saveRestaurantInfo(details);
        if (!ok) throw new RuntimeException("Failed to save restaurant info");
        saveRestaurantChanges(manager, details);
    }

    @Override
    public void addMenuItem(Manager manager, MenuItem item) {
        if (manager == null || item == null) {
            throw new IllegalArgumentException("Manager and menu item must not be null");
        }
        validateMenuItem(item);
        Restaurant restaurant = getRestaurantDetails(manager);
        if (restaurant == null) {
            throw new IllegalStateException("Manager has no associated restaurant");
        }
        boolean added = DB.addMenuItem(item, restaurant);
        if (!added) throw new RuntimeException("Failed to add menu item");
    }

    @Override
    public void editMenuItem(Manager manager, MenuItem item) {
        if (manager == null || item == null) {
            throw new IllegalArgumentException("Manager and menu item must not be null");
        }
        validateMenuItem(item);
        boolean ok = DB.updateMenuItem(item);
        if (!ok) throw new RuntimeException("Failed to update menu item");
    }

    @Override
    public void removeMenuItem(Manager manager, MenuItem item) {
        if (manager == null || item == null) {
            throw new IllegalArgumentException("Manager and menu item must not be null");
        }
        boolean ok = DB.removeMenuItem(item);
        if (!ok) throw new RuntimeException("Failed to remove menu item");
    }

    @Override
    public ArrayList<Order> viewIncomingOrders(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        Restaurant restaurant = getRestaurantDetails(manager);
        if (restaurant == null) {
            return new ArrayList<>();
        }
        ArrayList<Order> orders = DB.fetchRestaurantOrders(restaurant);
        return orders != null ? orders : new ArrayList<>();
    }

    @Override
    public void updateOrderStatus(Order order, String status) {

    }

    @Override
    public void updateOrderStatus(Manager manager, Order order, String status) {
        if (manager == null || order == null || status == null) {
            throw new IllegalArgumentException("Manager, order and status must not be null");
        }
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    /**
     
     * Output Format (pipe-separated):
     * restaurantName|managerID|date|revenue|totalOrders|deliveredOrders|avgRating

     */
    @Override
    public String generateMonthlyReport(Manager manager, Restaurant restaurant, Date date) {
        if (manager == null || restaurant == null || date == null) {
            throw new IllegalArgumentException("Manager, restaurant, and date must not be null");
        }

        // Get all orders for the restaurant
        ArrayList<Order> allOrders = DB.fetchRestaurantOrders(restaurant);
        if (allOrders == null || allOrders.isEmpty()) {
            return restaurant.getRestaurantName() + "|" + manager.getUserID() + "|" + date.toString() + "|0.00|0|0|0.0";
        }

        // Filter orders for the specified month (simple date comparison)
        int targetYear = date.getYear() + 1900; // getYear() returns year - 1900
        int targetMonth = date.getMonth() + 1;   // getMonth() returns 0-11

        ArrayList<Order> monthlyOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getCreationDate() == null) continue;
            Date orderDate = order.getCreationDate();
            int orderYear = orderDate.getYear() + 1900;
            int orderMonth = orderDate.getMonth() + 1;
            
            if (orderYear == targetYear && orderMonth == targetMonth) {
                monthlyOrders.add(order);
            }
        }

        if (monthlyOrders.isEmpty()) {
            return restaurant.getRestaurantName() + "|" + manager.getUserID() + "|" + date.toString() + "|0.00|0|0|0.0";
        }

        // Calculate revenue
        double totalRevenue = 0.0;
        int totalOrders = monthlyOrders.size();
        int deliveredOrders = 0;
        double totalRating = 0.0;
        int ratedOrders = 0;

        for (Order order : monthlyOrders) {
            // Calculate order total
            if (order.getItems() != null) {
                for (CartItem item : order.getItems()) {
                    if (item != null) {
                        totalRevenue += item.calculateItemTotal();
                    }
                }
            }

            if (order.getStatus() == OrderStatus.DELIVERED) {
                deliveredOrders++;
            }

            if (order.getRating() != null && order.getRating().getRatingValue() > 0) {
                totalRating += order.getRating().getRatingValue();
                ratedOrders++;
            }
        }

        double averageRating = ratedOrders > 0 ? totalRating / ratedOrders : 0.0;

        // Simple pipe-separated format: restaurant|managerID|date|revenue|totalOrders|deliveredOrders|avgRating
        return restaurant.getRestaurantName() + "|" + 
               manager.getUserID() + "|" + 
               date.toString() + "|" + 
               String.format("%.2f", totalRevenue) + "|" + 
               totalOrders + "|" + 
               deliveredOrders + "|" + 
               String.format("%.1f", averageRating);
    }

    @Override
    public void createDiscount(Manager manager, MenuItem item, String description, double percentage, Date startDate, Date endDate) {
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
            java.sql.Date exStart = existing.getStartDate();
            java.sql.Date exEnd = existing.getEndDate();
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

    private double calculateDiscount(MenuItem item) {
        if (item == null || item.getDiscounts() == null) return 0.0;
        java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
        for (Discount d : item.getDiscounts()) {
            if (d == null) continue;
            java.sql.Date start = d.getStartDate();
            java.sql.Date end = d.getEndDate();
            if (start != null && end != null && !now.before(start) && !now.after(end)) {
                return d.getDiscountPercentage();
            }
        }
        return 0.0;
    }

    private void saveRestaurantChanges(Manager manager, Restaurant restaurant) {
        if (manager == null || restaurant == null) return;
        DB.saveRestaurantInfo(restaurant);
    }
}