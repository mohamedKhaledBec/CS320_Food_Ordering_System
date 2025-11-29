package FOS_DATA;

import FOS_CORE.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public abstract class UserData {
    public abstract boolean addNewCustomer(User user);
    public abstract User getUserByEmail(String email);
    public abstract boolean changeUserPassword(User user, String newHashedPassword);
    public abstract ArrayList<Card> fetchCustomerCards(Customer customer);
    public abstract boolean addCardToCustomer(Customer customer, Card card);
    public abstract ArrayList<Address> fetchCustomerAddresses(Customer customer);
    public abstract boolean addAddress(Customer customer,Address address);
    public abstract ArrayList<String> fetchCustomerPhoneNumbers(Customer customer);
    public abstract boolean addPhoneNumber(Customer customer, String phoneNumber);
    public abstract ArrayList<Order> fetchCustomerOrders(Customer customer, ArrayList<Restaurant> restaurants);
    public abstract boolean insertCustomerOrder(Customer customer, Order order, Restaurant restaurant);
}
