package FOS_DATA;

import FOS_CORE.*;

import java.sql.*;
import java.util.ArrayList;

public class RestaurantData implements IRestaurantData {

    public ArrayList<Discount> fetchMenuItemDiscounts(MenuItem menuItem) {
        int menuItemID = menuItem.getMenuItemID();
        ArrayList<Discount> discounts = new ArrayList<>();
        final String sql = "SELECT * FROM Discount WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItemID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int discountID = resultSet.getInt("discount_id");
                String discountName = resultSet.getString("discount_name");
                String discountDescription = resultSet.getString("discount_description");
                double percentage = resultSet.getDouble("discount_percentage");
                Timestamp startDate = resultSet.getTimestamp("start_date");
                Timestamp endDate = resultSet.getTimestamp("end_date");
                long millis = System.currentTimeMillis();
                Date currentDate = new Date(millis);
                if (startDate.before(currentDate) && endDate.after(currentDate)) {
                    Discount discount = new Discount(discountID, discountName, discountDescription, percentage, startDate, endDate);
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch Menu Item Discounts: " + e.getMessage());
        }
        return discounts;
    }

    public ArrayList<Restaurant> fetchRestaurantsByCity(String city) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT restaurant_id, name, cuisine_type, city, manager_id FROM Restaurant WHERE city = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int restaurantId = resultSet.getInt("restaurant_id");
                String name = resultSet.getString("name");
                String cuisineType = resultSet.getString("cuisine_type");
                Restaurant restaurant = new Restaurant(restaurantId, name, cuisineType, city);
                restaurants.add(restaurant);
            }
            return restaurants;
        } catch (SQLException e) {
            System.out.println("Database failed to fetch restaurants by city: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        ArrayList<MenuItem> menu = new ArrayList<>();
        final String sql = "SELECT menu_item_id, item_name, description, price FROM MenuItem WHERE restaurant_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt("menu_item_id");
                String itemName = resultSet.getString("item_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                MenuItem menuItem = new MenuItem(menuItemId, itemName, description, price);
                menu.add(menuItem);
            }
            return menu;
        } catch (SQLException e) {
            System.out.println("Database failed to fetch restaurant menu items: " + e.getMessage());
        }
        return null;
    }

    @Override
    public double calculateRestaurantRating(Restaurant restaurant) {
        int restaurantID = restaurant.getRestaurantID();
        final String sql = " SELECT AVG(rt.rating_value) as avg_rating " +
                "FROM Rating rt JOIN `Order` o ON rt.order_id = o.order_id WHERE o.restaurant_id = ? ";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("avg_rating");
            }

        } catch (SQLException e) {
            System.out.println("Database failed to fetch restaurant ratings: " + e.getMessage());
        }
        return 0;
    }

    public ArrayList<String> fetchRestaurantKeywords(Restaurant restaurant) {
        final String sql = "SELECT keyword FROM RestaurantKeyword WHERE restaurant_id = ?";
        ArrayList<String> keywords = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurant.getRestaurantID());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String keyword = resultSet.getString("keyword");
                keywords.add(keyword);
            }
            return keywords;
        } catch (SQLException e) {
            System.out.println("Database failed to fetch restaurant keywords: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Order> fetchRestaurantOrdersForToday(Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_date, o.order_status, o.phone_number, o.card_no, a.address_id," +
                "                       rt.rating_value, rt.rating_comment\n" +
                "                FROM Order o\n" +
                "                JOIN Restaurant r ON o.restaurant_id = r.restaurant_id\n" +
                "                JOIN Address a ON o.delivery_address_id = a.address_id\n" +
                "                LEFT JOIN Rating rt ON o.order_id = rt.order_id\n" +
                "                WHERE o.restaurant_id = ?\n and order_date BETWEEN CURDATE() AND CURDATE() + INTERVAL 1 DAY" +
                "                GROUP BY o.order_id, o.order_date, o.order_status, r.name, rt.rating_value, rt.rating_comment\n" +
                "                ORDER BY o.order_date DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int order_id = resultSet.getInt("order_id");
                    Timestamp date = resultSet.getTimestamp("order_date");
                    OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
                    int ratingValue = resultSet.getInt("rating_value");
                    String ratingComment = resultSet.getString("rating_comment");
                    Rating rating = new Rating(ratingValue, ratingComment);
                    int addressId = resultSet.getInt("address_id");
                    String phoneNumber = resultSet.getString("phone_number");
                    String cardNo = resultSet.getString("card_no");
                    String deliveryAddress = fetchAddressDetails(addressId);
                    String restaurantName = restaurant.getRestaurantName();
                    ArrayList<CartItem> items = fetchOrderItemsByOrderID(order_id);
                    orders.add(new Order(deliveryAddress, items, date, status, restaurantName, order_id, rating, phoneNumber, cardNo));
                }
            } catch (SQLException e) {
                System.out.println("Database failed to fetch Restaurant orders");
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //private fuctions start here
    private String fetchAddressDetails(int addressId) {
        final String sql = "SELECT address_line, city, state, zip FROM Address WHERE address_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, addressId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String street = resultSet.getString("address_line");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zipCode = resultSet.getString("zip");
                Address address = new Address(addressId, street, city, state, zipCode);
                return address.toString();
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch address details: " + e.getMessage());
        }
        return null;
    }

    private ArrayList<CartItem> fetchOrderItemsByOrderID(int orderID) {
        ArrayList<CartItem> items = new ArrayList<>();
        final String sql = "SELECT mi.menu_item_id, mi.item_name, mi.description, mi.price, ci.quantity, ci.price as cart_price " +
                "FROM CartItem ci " +
                "JOIN MenuItem mi ON ci.menu_item_id = mi.menu_item_id " +
                "WHERE ci.order_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt("menu_item_id");
                String itemName = resultSet.getString("item_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                int cartPrice = resultSet.getInt("cart_price");
                MenuItem menuItem = new MenuItem(menuItemId, itemName, description, price);
                items.add(new CartItem(menuItem, quantity, cartPrice));
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch order items: " + e.getMessage());
        }
        return items;
    }
}
