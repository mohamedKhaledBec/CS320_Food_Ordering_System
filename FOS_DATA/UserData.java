package FOS_DATA;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

import java.sql.*;
import java.util.ArrayList;

public class UserData implements IUserData {
    public boolean addNewCustomer(User user){
        final String sql = "INSERT INTO User (username, password, user_type) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, "customer");
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database failed to save user");
        }
        return false;
    }
    public User getUserByEmail(String email) {
        final String sql = "SELECT user_id, password, user_type FROM User WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String password = resultSet.getString("password");
                    String userType = resultSet.getString("user_type");
                    if(userType.equals("manager")){
                        return new Manager(id, email, password);
                    }else{
                        return new Customer(id, email, password);
                    }

                }
            } catch (SQLException e) {
                System.out.println("No user found with the corresponding username");
            }

        } catch (SQLException e) {
            System.out.println("Database failed to fetch user by username");
        }
        return null;
    }
    public boolean changeUserPassword(User user, String newHashedPassword){
        int userId = user.getUserID();
        final String sql = "UPDATE User SET password = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newHashedPassword);
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to update user password");
            return false;
        }
    }

    public ArrayList<Card> fetchCustomerCards(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<Card> cards = new ArrayList<>();
        final String sql = "SELECT card_no, expiry_date, cardholder_name, cvv FROM Card WHERE customer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int cardNumber = resultSet.getInt("card_no");
                    Date expiryDate = resultSet.getDate("expiry_date");
                    String cardholderName = resultSet.getString("cardholder_name");
                    int cvv = resultSet.getInt("cvv");
                    cards.add(new Card(cardNumber, cardholderName, expiryDate, cvv));
                }
            } catch (SQLException e) {
                System.out.println("No cards found for the customer");
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch customer cards");
        }
        return cards;
    }
    public boolean addCardToCustomer(Customer customer, Card card) {
        int customerId = customer.getUserID();
        final String sql = "INSERT INTO Card (customer_id, card_no, expiry_date, cardholder_name, cvv) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setInt(2, card.getCardNumber());
            statement.setDate(3, card.getExpiryDate());
            statement.setString(4, card.getCardHolderName());
            statement.setInt(5, card.getCvv());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add card to customer");
            return false;
        }
    }
    public ArrayList<Address> fetchCustomerAddresses(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<Address> addresses = new ArrayList<>();
        final String sql = "SELECT address_id, address_line, city, state, zip_code FROM Address WHERE customer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int addressId = resultSet.getInt("address_id");
                    String addressLine = resultSet.getString("address_line");
                    String city = resultSet.getString("city");
                    String state = resultSet.getString("state");
                    String zipCode = resultSet.getString("zip_code");
                    addresses.add(new Address(addressId, addressLine, city, state, zipCode));
                }
            } catch (SQLException e) {
                System.out.println("No addresses found for the customer");
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch customer addresses");
        }
        return addresses;
    }
    public boolean addAddress(Customer customer, Address address) {
        int customerId = customer.getUserID();
        final String sql = "INSERT INTO Address (customer_id, address_line, city, state, zip_code) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, address.getAddressLine());
            statement.setString(3, address.getCity());
            statement.setString(4, address.getState());
            statement.setString(5, address.getZipCode());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add address to customer");
            return false;
        }
    }
    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        final String sql = "SELECT phone_number FROM PhoneNumber WHERE customer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String phoneNumber = resultSet.getString("phone_number");
                    phoneNumbers.add(phoneNumber);
                }
            } catch (SQLException e) {
                System.out.println("No phone numbers found for the customer");
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch customer phone numbers");
        }
        return phoneNumbers;
    }
    public boolean addPhoneNumber(Customer customer, String phoneNumber) {
        int customerId = customer.getUserID();
        final String sql = "INSERT INTO PhoneNumber (customer_id, phone_number) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, phoneNumber);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add phone number to customer");
            return false;
        }
    }
    public ArrayList<Order> fetchCustomerOrders(Customer customer, ArrayList<Restaurant> restaurants) {
        int customerId = customer.getUserID();
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_date, o.status, r.restaurant_id as restaurant_id, \n" +
                "                       a.address_id,\n" +
                "                       rt.rating, rt.comment\n" +
                "                FROM Order o\n" +
                "                JOIN Restaurant r ON o.restaurant_id = r.restaurant_id\n" +
                "                JOIN Address a ON o.delivery_address_id = a.address_id\n" +
                "                LEFT JOIN Rating rt ON o.order_id = rt.cart_id\n" +
                "                WHERE o.customer_id = ?\n" +
                "                GROUP BY o.order_id, o.order_date, o.status, r.name, rt.rating, rt.comment\n" +
                "                ORDER BY o.order_date DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int order_id = resultSet.getInt("order_id");
                    Date date = resultSet.getDate("order_date");
                    OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
                    int restaurantId = resultSet.getInt("restaurant_id");
                    Rating rating = new Rating(resultSet.getObject("rating") != null ? resultSet.getInt("rating") : null, resultSet.getString("comment"));
                    int addressId = resultSet.getInt("address_id");
                    Address deliveryAddress = new Address();
                    for (Address address : customer.getAddresses()) {
                        if (address.getAddressID() == addressId) {
                            deliveryAddress = address;
                            break;
                        }
                    }
                    String restaurantName = "";
                    ArrayList<CartItem> items = new ArrayList<>();
                    sql = "SELECT mi.menu_item_id, oi.quantity" +
                            "FROM OrderItem oi " +
                            "JOIN MenuItem mi ON oi.menu_item_id = mi.menu_item_id " +
                            "WHERE oi.order_id = ?";
                    try (PreparedStatement itemStatement = connection.prepareStatement(sql)) {
                        itemStatement.setInt(1, order_id);
                        try (ResultSet itemResultSet = itemStatement.executeQuery()) {
                            int menuItemId = itemResultSet.getInt("menu_item_id");
                            for (Restaurant restaurant : restaurants) {
                                if (restaurant.getRestaurantID() == restaurantId) {
                                    restaurantName = restaurant.getRestaurantName();
                                    for (MenuItem menuItem : restaurant.getMenu()) {
                                        if (menuItem.getMenuItemID() == menuItemId) {
                                            int quantity = itemResultSet.getInt("quantity");
                                            items.add(new CartItem(menuItem, quantity));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        orders.add(new Order(deliveryAddress, items, date, status, restaurantName, order_id, rating));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Database failed to fetch customer orders");
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean insertCustomerOrder(Customer customer, Order order, Restaurant restaurant) {
        int customerId = customer.getUserID();
        int restaurantId = restaurant.getRestaurantID();
        int deliveryAddressId = order.getDeliveryAddress().getAddressID();
        final String sql = "INSERT INTO Orders (customer_id, order_date, restaurant_id, delivery_address_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setDate(2, new Date(order.getCreationDate().getTime()));
            statement.setInt(3, restaurantId);
            statement.setInt(4, deliveryAddressId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        order.setOrderID(orderId);
                        customer.getOrders().add(order);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database failed to add order to customer");
        }
        return false;
    }

}
