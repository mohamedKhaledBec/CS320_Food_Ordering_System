package FOS_DATA;
/*
* Comment By Mohamed Khaled Becetti
*Requested :
*   add address method
*   update phone number method
* */

import FOS_CORE.*;

import java.util.ArrayList;

public interface ICustomerService extends IUserData {
    public boolean addNewCustomer(Customer customer, String PhoneNumber, Address address);
    public ArrayList<Card> fetchCustomerCards(Customer customer);
    public boolean addCardToCustomer(Customer customer, Card card);
    public boolean removeCardFromCustomer(Customer customer, Card card);
    public ArrayList<Address> fetchCustomerAddresses(Customer customer);
    public boolean addAddressToCustomer(Customer customer,Address address);
    public  boolean removeAddressFromCustomer(Customer customer, Address address);
    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer);
    public boolean addPhoneNumberToCustomer(Customer customer, String phoneNumber);
    public boolean removePhoneNumberFromCustomer(Customer customer, String phoneNumber);
    public ArrayList<Order> fetchCustomerOrders(Customer customer);
    public boolean insertCustomerOrder(Customer customer,Address address, Order order, Restaurant restaurant);
}
