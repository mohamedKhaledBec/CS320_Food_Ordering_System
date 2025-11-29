package FOS_CORE;

import FOS_DATA.IUserData;
import java.util.ArrayList;

public class DatabaseAPI {

    private final IUserData dao;


    public DatabaseAPI() {
        this.dao = new FOS_DATA.UserData();
    }

    public boolean addNewCustomer(User user) {


        return dao.addNewCustomer(user);
    }

    public User getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    public boolean changeUserPassword(User user, String newHashedPassword) {
        return dao.changeUserPassword(user, newHashedPassword);
    }

    public ArrayList<Card> fetchCustomerCards(Customer customer) {
        return dao.fetchCustomerCards(customer);
    }

    public boolean addCardToCustomer(Customer customer, Card card) {
        return dao.addCardToCustomer(customer, card);
    }

    public ArrayList<Address> fetchCustomerAddresses(Customer customer) {
        return dao.fetchCustomerAddresses(customer);
    }

    public boolean addAddress(Customer customer, Address address) {
        return dao.addAddress(customer, address);
    }

    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer) {
        return dao.fetchCustomerPhoneNumbers(customer);
    }

    public boolean addPhoneNumber(Customer customer, String phoneNumber) {
        return dao.addPhoneNumber(customer, phoneNumber);
    }

    public ArrayList<Order> fetchCustomerOrders(Customer customer, ArrayList<Restaurant> restaurants) {
        return dao.fetchCustomerOrders(customer, restaurants);
    }

    public boolean insertCustomerOrder(Customer customer, Order order, Restaurant restaurant) {
        return dao.insertCustomerOrder(customer, order, restaurant);
    }

}