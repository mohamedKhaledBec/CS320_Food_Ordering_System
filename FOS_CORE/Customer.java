package FOS_CORE;



import FOS_DATA.IUserData;

import java.util.ArrayList;

public class Customer extends User {

    private ArrayList<String> phoneNumbers;
    private ArrayList<Address> addresses;
    private ArrayList<Order> orders;
    private ArrayList<Card> cards;
    private ArrayList<CartItem> cart;

    public Customer() { }
// Mohamed Khaled Becetti changed the fetching to be from the service class other than the UI class
    public Customer(int userID, String email, String passwordHash) {
        super(userID, email, passwordHash);
        AccountService accountService = new AccountService();
        this.phoneNumbers = accountService.fetchCustomerPhoneNumbers(this);
        this.addresses = accountService.fetchCustomerAddresses(this);
        this.orders = accountService.fetchCustomerOrders(this);
        this.cards = accountService.fetchCustomerCards(this);
    }

    public Customer(String email, String password) {

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
