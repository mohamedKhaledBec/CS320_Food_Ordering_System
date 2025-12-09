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

    public ArrayList<Order> fetchRestaurantOrders(Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_date, o.status, o.phone_number, o.card_no\n" +
                "                       a.address_id,\n" +
                "                       rt.rating_value, rt.rating_comment\n" +
                "                FROM Order o\n" +
                "                JOIN Restaurant r ON o.restaurant_id = r.restaurant_id\n" +
                "                JOIN Address a ON o.delivery_address_id = a.address_id\n" +
                "                LEFT JOIN Rating rt ON o.order_id = rt.cart_id\n" +
                "                WHERE o.restaurant_id = ?\n" +
                "                GROUP BY o.order_id, o.order_date, o.status, r.name, rt.rating, rt.comment\n" +
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



    //private functions start here
    private String fetchAddressDetails(int addressId){
        final String sql = "SELECT street, city, state, zip_code, country FROM Address WHERE address_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, addressId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String street = resultSet.getString("street");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zipCode = resultSet.getString("zip_code");
                String country = resultSet.getString("country");
                return street + ", " + city + ", " + state + " " + zipCode + ", " + country;
            }
        }catch (SQLException e) {
            System.out.println("Database failed to fetch address details: " + e.getMessage());
        }
        return null;
    }

    private ArrayList<CartItem> fetchOrderItemsByOrderID(int orderID) {
        ArrayList<CartItem> items = new ArrayList<>();
        final String sql = "SELECT mi.menu_item_id, mi.item_name, mi.description, mi.price, oi.quantity, ci.price as cart_price " +
                "FROM CartItem oi " +
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
                items.add(new CartItem(menuItem, quantity,cartPrice));
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch order items: " + e.getMessage());
        }
        return items;
    }

    private ArrayList<Discount> fetchMenuItemDiscounts(MenuItem menuItem) {
        ArrayList<Discount> discounts = new ArrayList<>();
        final String sql = "SELECT * FROM Discount WHERE MenuItemID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int discountID = resultSet.getInt("discount_id");
                String discountName = resultSet.getString("discount_name");
                String discountDescription = resultSet.getString("discount_description");
                double percentage = resultSet.getDouble("discount_percentage");
                Timestamp startDate = resultSet.getTimestamp("start_date");
                Timestamp endDate = resultSet.getTimestamp("end_date");
                long millis= System.currentTimeMillis();
                Timestamp currentDate = new Timestamp(millis);
                Discount discount = new Discount(discountID, discountName, discountDescription, percentage, startDate, endDate);
                discounts.add(discount);
                }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch Menu Item Discounts: " + e.getMessage());
        }
        return discounts;
    }

}

