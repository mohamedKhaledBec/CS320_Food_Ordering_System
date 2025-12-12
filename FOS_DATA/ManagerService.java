package FOS_DATA;

import FOS_CORE.*;
import java.sql.*;
import java.util.ArrayList;

public class ManagerService extends UserData implements IManagerService {
    public boolean saveRestaurantInfo(Restaurant restaurant) {
        String sql = "INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurant.getRestaurantID());
            statement.setString(2, restaurant.getRestaurantName());
            statement.setString(3, restaurant.getCuisineType());
            statement.setString(4, restaurant.getCity());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to save restaurant info: " + e.getMessage());
            return false;
        }
    }

    public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        String sql = "INSERT INTO MenuItem (restaurant_id, item_name, description, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            statement.setString(2, menuItem.getItemName());
            statement.setString(3, menuItem.getDescription());
            statement.setDouble(4, menuItem.getPrice());
            int rowsAffected = statement.executeUpdate();
            sql = "SELECT LAST_INSERT_ID() AS menu_item_id";
            ResultSet resultSet = statement.executeQuery(sql);
            menuItem.setMenuItemID(resultSet.getInt("menu_item_id"));
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add menu Item to restaurant: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuItem(MenuItem menuItem) {
        final String sql = "UPDATE MenuItem SET item_name = ?, description = ?, price = ? WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, menuItem.getItemName());
            statement.setString(2, menuItem.getDescription());
            statement.setDouble(3, menuItem.getPrice());
            statement.setInt(4, menuItem.getMenuItemID());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to update menu Item: " + e.getMessage());
            return false;
        }
    }

    public boolean removeMenuItem(MenuItem menuItem) {
        final String sql = "DELETE FROM MenuItem WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItem.getMenuItemID());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to remove menu Item: " + e.getMessage());
            return false;
        }
    }

    public boolean createDiscount(Discount discount, MenuItem menuItem) {
        String sql = "INSERT INTO Discount (menu_item_id, discount_name, discount_description, discount_percentage, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItem.getMenuItemID());
            statement.setString(2, discount.getDiscountName());
            statement.setString(3, discount.getDescription());
            statement.setDouble(4, discount.getDiscountPercentage());
            statement.setTimestamp(5, new Timestamp(discount.getStartDate().getTime()));
            statement.setTimestamp(6, new Timestamp(discount.getEndDate().getTime()));
            int rowsAffected = statement.executeUpdate();
            sql = "SELECT LAST_INSERT_ID() AS discount_id";
            ResultSet resultSet = statement.executeQuery(sql);
            discount.setDiscountID(resultSet.getInt("discount_id"));
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to create discount: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Restaurant> fetchManagerRestaurants(Manager manager) {
        int managerId = manager.getUserID();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT restaurant_id, name, cuisine_type, city FROM Restaurant WHERE manager_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, managerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int restaurantId = resultSet.getInt("restaurant_id");
                    String name = resultSet.getString("name");
                    String cuisineType = resultSet.getString("cuisine_type");
                    String city = resultSet.getString("city");
                    restaurants.add(new Restaurant(restaurantId, name, cuisineType, city));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch manager restaurants");
        }
        return restaurants;
    }

    public String generateMonthlyReport(Restaurant restaurant, Date date) {
        StringBuilder report = new StringBuilder();
        int restaurantId = restaurant.getRestaurantID();

        // Calculate start and end dates for the month
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        Timestamp startDate = new Timestamp(cal.getTimeInMillis());

        cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 59);
        Timestamp endDate = new Timestamp(cal.getTimeInMillis());

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Get total revenue
            String revenueSql = "SELECT COALESCE(SUM(ci.price * ci.quantity), 0) as total_revenue " +
                    "FROM `Order` o " +
                    "JOIN CartItem ci ON o.order_id = ci.order_id " +
                    "WHERE o.restaurant_id = ? AND o.order_date BETWEEN ? AND ?";

            double totalRevenue = 0;
            try (PreparedStatement stmt = connection.prepareStatement(revenueSql)) {
                stmt.setInt(1, restaurantId);
                stmt.setTimestamp(2, startDate);
                stmt.setTimestamp(3, endDate);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalRevenue = rs.getDouble("total_revenue");
                }
            }

            // Get number of orders
            String orderCountSql = "SELECT COUNT(*) as order_count " +
                    "FROM `Order` WHERE restaurant_id = ? AND order_date BETWEEN ? AND ?";

            int orderCount = 0;
            try (PreparedStatement stmt = connection.prepareStatement(orderCountSql)) {
                stmt.setInt(1, restaurantId);
                stmt.setTimestamp(2, startDate);
                stmt.setTimestamp(3, endDate);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    orderCount = rs.getInt("order_count");
                }
            }

            // Get most popular menu items
            String popularItemsSql = "SELECT mi.item_name, SUM(ci.quantity) as total_quantity, " +
                    "SUM(ci.price * ci.quantity) as item_revenue " +
                    "FROM `Order` o " +
                    "JOIN CartItem ci ON o.order_id = ci.order_id " +
                    "JOIN MenuItem mi ON ci.menu_item_id = mi.menu_item_id " +
                    "WHERE o.restaurant_id = ? AND o.order_date BETWEEN ? AND ? " +
                    "GROUP BY mi.menu_item_id, mi.item_name " +
                    "ORDER BY total_quantity DESC " +
                    "LIMIT 5";

            ArrayList<String> popularItems = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(popularItemsSql)) {
                stmt.setInt(1, restaurantId);
                stmt.setTimestamp(2, startDate);
                stmt.setTimestamp(3, endDate);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("total_quantity");
                    double revenue = rs.getDouble("item_revenue");
                    popularItems.add(String.format("  %s - %d orders ($%.2f)", itemName, quantity, revenue));
                }
            }

            // Get average rating
            String ratingSql = "SELECT AVG(rt.rating_value) as avg_rating, COUNT(rt.rating_value) as rating_count " +
                    "FROM `Order` o " +
                    "LEFT JOIN Rating rt ON o.order_id = rt.order_id " +
                    "WHERE o.restaurant_id = ? AND o.order_date BETWEEN ? AND ?";

            double avgRating = 0;
            int ratingCount = 0;
            try (PreparedStatement stmt = connection.prepareStatement(ratingSql)) {
                stmt.setInt(1, restaurantId);
                stmt.setTimestamp(2, startDate);
                stmt.setTimestamp(3, endDate);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    avgRating = rs.getDouble("avg_rating");
                    ratingCount = rs.getInt("rating_count");
                }
            }

            // Build the report
            report.append("========================================\n");
            report.append("       MONTHLY REPORT\n");
            report.append("========================================\n");
            report.append(String.format("Restaurant: %s\n", restaurant.getRestaurantName()));
            report.append(String.format("Period: %s to %s\n\n",
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate),
                    new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate)));

            report.append("SUMMARY\n");
            report.append("----------------------------------------\n");
            report.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
            report.append(String.format("Total Orders: %d\n", orderCount));
            report.append(String.format("Average Order Value: $%.2f\n",
                    orderCount > 0 ? totalRevenue / orderCount : 0));
            report.append(String.format("Average Rating: %.2f (%d ratings)\n\n", avgRating, ratingCount));

            report.append("TOP 5 MENU ITEMS\n");
            report.append("----------------------------------------\n");
            if (popularItems.isEmpty()) {
                report.append("  No orders this month\n");
            } else {
                for (String item : popularItems) {
                    report.append(item).append("\n");
                }
            }
            report.append("\n========================================\n");

        } catch (SQLException e) {
            System.out.println("Database failed to generate monthly report: " + e.getMessage());
            e.printStackTrace();
            return "Error generating report: " + e.getMessage();
        }

        return report.toString();
    }

}

