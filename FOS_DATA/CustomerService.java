package FOS_DATA;

import FOS_CORE.*;
import java.sql.*;
import java.util.ArrayList;

public class CustomerService extends UserData implements ICustomerService {
    public boolean addNewCustomer(Customer customer, String phoneNumber, Address address) {
        String sql = "INSERT INTO User (email, password, user_type) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, customer.getEmail());
                statement.setString(2, customer.getPasswordHash());
                statement.setString(3, "customer");
                statement.executeUpdate();
                ResultSet resultset = statement.getGeneratedKeys();
                if(!resultset.next()){
                    connection.rollback();
                    System.out.println("Failed to retrieve generated user ID");
                    return false;
                }
                customer.setUserID(resultset.getInt(1));
                if(!addPhoneNumberToCustomerOnLogin(connection, customer, phoneNumber)){
                    System.out.println("Failed to add phone number to new customer");
                    connection.rollback();
                    return false;
                }
                if(!addAddressToCustomerOnLogin(connection, customer, address)){
                    System.out.println("Failed to add address to new customer");
                    connection.rollback();
                    return false;
                }
                System.out.println("Customer phone number added successfully");
                connection.commit();
                System.out.println("New customer added successfully with ID: " + customer.getUserID());
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database failed to save user" + e.getMessage());
        }
        return false;
    }
    public ArrayList<Card> fetchCustomerCards(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<Card> cards = new ArrayList<>();
        final String sql = "SELECT card_no, expiry_date, card_holder_name, cvv FROM Card WHERE customer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String cardNumber = resultSet.getString("card_no");
                    Date expiryDate = resultSet.getDate("expiry_date");
                    String cardholderName = resultSet.getString("card_holder_name");
                    String cvv = resultSet.getString("cvv");
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
        final String sql = "INSERT INTO Card (customer_id, card_no, expiry_date, card_holder_name, cvv) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, card.getCardNumber());
            statement.setDate(3, card.getExpiryDate());
            statement.setString(4, card.getCardHolderName());
            statement.setString(5, card.getCvv());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add card to customer" + e.getMessage());
            return false;
        }
    }
    public boolean removeCardFromCustomer(Customer customer, Card card) {
        int customerId = customer.getUserID();
        String cardNumber = card.getCardNumber();
        final String sql = "DELETE FROM Card WHERE customer_id = ? AND card_no = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, cardNumber);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to remove card from customer");
            return false;
        }
    }
    public ArrayList<Address> fetchCustomerAddresses(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<Address> addresses = new ArrayList<>();
        final String sql = "SELECT address_id, address_line, city, state, zip FROM Address WHERE customer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int addressId = resultSet.getInt("address_id");
                    String addressLine = resultSet.getString("address_line");
                    String city = resultSet.getString("city");
                    String state = resultSet.getString("state");
                    String zipCode = resultSet.getString("zip");
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
    public boolean addAddressToCustomer(Customer customer, Address address) {
        int customerId = customer.getUserID();
        String sql = "INSERT INTO Address (customer_id, address_line, city, state, zip) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, customerId);
            statement.setString(2, address.getAddressLine());
            statement.setString(3, address.getCity());
            statement.setString(4, address.getState());
            statement.setString(5, address.getZipCode());
            statement.executeUpdate();
            ResultSet resultset = statement.getGeneratedKeys();
            if(!resultset.next()){
                connection.rollback();
                System.out.println("Failed to retrieve generated user ID");
                return false;
            }
            address.setAddressID(resultset.getInt(1));
            return true;
        } catch (SQLException e) {
            System.out.println("Database failed to add address to customer");
            return false;
        }
    }

    @Override
    public boolean removeAddressFromCustomer(Customer customer,Address address) {
        int addressId = address.getAddressID();
        final String sql = "DELETE FROM Address WHERE address_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, addressId);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database failed to remove address from customer");
        }
        return false;
    }

    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        final String sql = "SELECT phone_number FROM Phone WHERE customer_id = ?";
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
    public boolean addPhoneNumberToCustomer(Customer customer, String phoneNumber) {
        int customerId = customer.getUserID();
        final String sql = "INSERT INTO Phone (customer_id, phone_number) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, phoneNumber);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add phone number to customer" + e.getMessage());
        }
        return false;
    }
    public boolean removePhoneNumberFromCustomer(Customer customer, String phoneNumber) {
        int customerId = customer.getUserID();
        final String sql = "DELETE FROM Phone WHERE customer_id = ? AND phone_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, phoneNumber);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                customer.getPhoneNumbers().remove(phoneNumber);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database failed to remove phone number from customer");
        }
        return false;
    }
    public ArrayList<Order> fetchCustomerOrders(Customer customer) {
        int customerId = customer.getUserID();
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_date, o.order_status, o.phone_number, o.card_no, " +
                    "r.name AS restaurant_name, a.address_id, " +
                    "rt.rating_value, rt.rating_comment " +
                    "FROM `Order` o " +
                    "JOIN Restaurant r ON o.restaurant_id = r.restaurant_id " +
                    "JOIN Address a ON o.delivery_address_id = a.address_id " +
                    "LEFT JOIN Rating rt ON o.order_id = rt.order_id " +
                    "WHERE o.customer_id = ? " +
                    "ORDER BY o.order_date DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int order_id = resultSet.getInt("order_id");
                    Timestamp date = resultSet.getTimestamp("order_date");
                    OrderStatus status = OrderStatus.valueOf(resultSet.getString("order_status").toUpperCase());
                    int ratingValue = resultSet.getInt("rating_value");
                    String ratingComment = resultSet.getString("rating_comment");
                    Rating rating = (ratingValue == 0 && ratingComment == null)? null : new Rating(ratingValue, ratingComment);
                    int addressId = resultSet.getInt("address_id");
                    String addressDetails = fetchAddressDetails(addressId);
                    String restaurantName = resultSet.getString("restaurant_name");
                    String phoneNumber = resultSet.getString("phone_number");
                    String cardNumber = resultSet.getString("card_no");
                    ArrayList<CartItem> items = fetchOrderItemsByOrderID(order_id);
                    Order order = new Order(addressDetails, items, date, status, restaurantName, order_id, rating, phoneNumber, cardNumber);
                    orders.add(order);
                }
            } catch (SQLException e){
                System.out.println("No orders found for the customer");
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch customer orders");
        }
        return orders;
    }
    public boolean insertCustomerOrder(Customer customer,Address address, Order order, Restaurant restaurant) {
        int customerId = customer.getUserID();
        String phoneNumber = order.getPhoneNumber();
        String cardNumber = order.getCardNumber();
        int restaurantId = restaurant.getRestaurantID();
        int deliveryAddressId = address.getAddressID();
        final String sql = "INSERT INTO `Order` (customer_id, order_date, restaurant_id, delivery_address_id, phone_number, card_no) VALUES (?, ?, ?, ?, ?, ?)";
        final String insertCartItemSQL = "INSERT INTO CartItem (order_id, menu_item_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()){
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, customerId);
                statement.setTimestamp(2, new Timestamp(order.getCreationDate().getTime()));
                statement.setInt(3, restaurantId);
                statement.setInt(4, deliveryAddressId);
                statement.setString(5, phoneNumber);
                statement.setString(6, cardNumber);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int orderId = generatedKeys.getInt(1);
                            order.setOrderID(orderId);
                            try (PreparedStatement cartItemStatement = connection.prepareStatement(insertCartItemSQL)) {
                                for (CartItem item : order.getItems()) {
                                    cartItemStatement.setInt(1, orderId);
                                    cartItemStatement.setInt(2, item.getMenuItem().getMenuItemID());
                                    cartItemStatement.setInt(3, item.getQuantity());
                                    cartItemStatement.setDouble(4, item.getPrice());
                                    cartItemStatement.addBatch();
                                }
                                cartItemStatement.executeBatch();
                            }
                            connection.commit();
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Database failed to add order to customer" + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database failed to add order to customer" + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean rateCustomerOrder(Order  order, int rating, String comment) {
        final String sql = "INSERT INTO Rating (order_id, rating_value, rating_comment) VALUES (?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, order.getOrderID());
            statement.setInt(2, rating);
            statement.setString(3, comment);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e){
            System.out.println("Database failed to rate customer customer");
        }
        return false;
    }

    //private functions start here
    private String fetchAddressDetails(int addressId){
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
                return street + ", " + city + ", " + state + " " + zipCode;
            }
        }catch (SQLException e) {
            System.out.println("Database failed to fetch address details: " + e.getMessage());
        }
        return null;
    }

    private ArrayList<CartItem> fetchOrderItemsByOrderID(int orderID) {
        ArrayList<CartItem> items = new ArrayList<>();
        final String sql = "SELECT mi.menu_item_id, mi.item_name, mi.description, mi.price, ci.quantity , ci.price as cart_price " +
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
                items.add(new CartItem(menuItem, quantity,cartPrice));
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch order items: " + e.getMessage());
        }
        return items;
    }
    private ArrayList<Discount> fetchDiscountsByMenuItemID(int menuItemID) {
        ArrayList<Discount> discounts = new ArrayList<>();
        final String sql = "SELECT discount_id, discount_name, discount_description, discount_percentage, start_date, end_date FROM Discount WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItemID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int discountId = resultSet.getInt("discount_id");
                String discountName = resultSet.getString("discount_name");
                String discountDescription = resultSet.getString("discount_description");
                double percentage = resultSet.getDouble("discount_percentage");
                Timestamp startDate = resultSet.getTimestamp("start_date");
                Timestamp endDate = resultSet.getTimestamp("end_date");
                discounts.add(new Discount(discountId,discountName, discountDescription, percentage, startDate, endDate));
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch discounts: " + e.getMessage());
        }
        return discounts;
    }
    private boolean addPhoneNumberToCustomerOnLogin(Connection connection, Customer customer, String phoneNumber) {
        int customerId = customer.getUserID();
        final String sql = "INSERT INTO Phone (customer_id, phone_number) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setString(2, phoneNumber);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add phone number to customer" + e.getMessage());
        }
        return false;
    }
    private boolean addAddressToCustomerOnLogin(Connection connection, Customer customer, Address address) {
        int customerId = customer.getUserID();
        String sql = "INSERT INTO Address (customer_id, address_line, city, state, zip) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, customerId);
            statement.setString(2, address.getAddressLine());
            statement.setString(3, address.getCity());
            statement.setString(4, address.getState());
            statement.setString(5, address.getZipCode());
            statement.executeUpdate();
            ResultSet resultset = statement.getGeneratedKeys();
            if(!resultset.next()){
                connection.rollback();
                System.out.println("Failed to retrieve generated user ID");
                return false;
            }
            address.setAddressID(resultset.getInt(1));
            return true;
        } catch (SQLException e) {
            System.out.println("Database failed to add address to customer");
            return false;
        }
    }
}
