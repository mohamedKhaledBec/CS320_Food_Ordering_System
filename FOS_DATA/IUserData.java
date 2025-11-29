package FOS_DATA;
/*
* Comment By Mohamed Khaled Becetti
*Requested :
*   add address method
*   update phone number method
* */

import FOS_CORE.*;

import java.util.ArrayList;

public interface IUserData {
    public boolean addNewCustomer(User user);
    public User getUserByEmail(String email);
    public boolean changeUserPassword(User user, String newHashedPassword);
    public ArrayList<Card> fetchCustomerCards(Customer customer);
    public boolean addCardToCustomer(Customer customer, Card card);
    public ArrayList<Address> fetchCustomerAddresses(Customer customer);
    public boolean addAddress(Customer customer,Address address);
    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer);
    public boolean addPhoneNumber(Customer customer, String phoneNumber);
    public ArrayList<Order> fetchCustomerOrders(Customer customer, ArrayList<Restaurant> restaurants);
    public boolean insertCustomerOrder(Customer customer, Order order, Restaurant restaurant);
}
