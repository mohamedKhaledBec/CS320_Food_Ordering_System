package FOS_CORE;

import FOS_DATA.CustomerService;
import FOS_DATA.ICustomerService;
import java.util.ArrayList;

public class DatabaseAPI {

    private final ICustomerService DB;


    public DatabaseAPI() {
        this.DB = new CustomerService();
    }

    public boolean addNewCustomer(User user) {

        return DB.addNewCustomer(user);
    }

    public User getUserByEmail(String email) {
        return DB.getUserByEmail(email);
    }

    public boolean changeUserPassword(User user, String newHashedPassword) {
        return DB.changeUserPassword(user, newHashedPassword);
    }

    public ArrayList<Card> fetchCustomerCards(Customer customer) {
        return DB.fetchCustomerCards(customer);
    }

    public void addCardToCustomer(Customer customer, Card card) {
        DB.addCardToCustomer(customer, card);
    }

    public ArrayList<Address> fetchCustomerAddresses(Customer customer) {
        return DB.fetchCustomerAddresses(customer);
    }

    public void addAddress(Customer customer, Address address) {
        DB.addAddressToCustomer(customer, address);
    }

    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer) {
        return DB.fetchCustomerPhoneNumbers(customer);
    }

    public void addPhoneNumber(Customer customer, String phoneNumber) {
        DB.addPhoneNumberToCustomer(customer, phoneNumber);
    }

    public ArrayList<Order> fetchCustomerOrders(Customer customer, ArrayList<Restaurant> restaurants) {
        return DB.fetchCustomerOrders(customer);
    }

    public boolean insertCustomerOrder(Customer customer, Address address, Order order, Restaurant restaurant) {
        return DB.insertCustomerOrder(customer,address, order, restaurant);
    }

}