package FOS_CORE;

import java.util.List;
import java.util.ArrayList;


public interface IAccountService {
    Customer createCustomerAccount(String email, String phone, String password);
    void changePassword(User user, String newPassword);
    void updateContactInfo(Customer customer, String phone);
    boolean addAddress(Customer customer, Address address);
    List<Address> getAddresses(Customer customer);

    ArrayList<String> fetchCustomerPhoneNumbers(Customer customer);
    ArrayList<Address> fetchCustomerAddresses(Customer customer);
    ArrayList<Order> fetchCustomerOrders(Customer customer, ArrayList<Restaurant> allRestaurants);
    ArrayList<Card> fetchCustomerCards(Customer customer);



}