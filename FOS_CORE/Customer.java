package FOS_CORE;

import FOS_DATA.IUserData;
import FOS_DATA.UserData;

import java.util.ArrayList;

public class Customer extends User {

    private ArrayList<String> phoneNumbers;
    private ArrayList<Address> addresses;
    private ArrayList<Order> orders;
    private ArrayList<Card> cards;
    private ArrayList<CartItem> cart;
    private static IUserData userData = new UserData();

    public Customer() { }

    public Customer(int userID, String email, String passwordHash) {
        super(userID, email, passwordHash);
        this.phoneNumbers = userData.fetchCustomerPhoneNumbers(this);
        this.addresses = userData.fetchCustomerAddresses(this);
        this.orders = userData.fetchCustomerOrders(this, FOS.getAllRestaurants());
        this.cards = userData.fetchCustomerCards(this);
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void addPhoneNumber(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }
}
