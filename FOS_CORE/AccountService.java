package FOS_CORE;

import java.util.ArrayList;
import java.util.List;
import FOS_DATA.*;

public class AccountService implements IAccountService {
    private final IUserData userData = new UserData() ;

    private final ICustomerData DB = new CustomerData() ;
    // Working on it : Mohamed Khaled Becetti
    @Override
    public Customer createCustomerAccount(String email, String phone, String password, Address address) {
        if (!this.validateEmailFormat(email) && phone != null && !phone.isEmpty()) {
            throw new IllegalArgumentException("Invalid email format");
        } else if (password != null && !password.isEmpty()) {
            Customer customer = new Customer(-1, email, hashPassword(password));
            boolean saved = DB.addNewCustomer(customer, phone, address);
            if (!saved) {
                throw new IllegalStateException("Failed to create customer account");
            } else {
                return (Customer) DB.getUserByEmail(email);
            }
        } else {
            throw new IllegalArgumentException("Password must not be empty");
        }
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void changePassword(User user, String newPassword) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        } else if (newPassword != null && !newPassword.isEmpty()) {
            boolean updated = DB.changeUserPassword(user, newPassword);
            if (!updated) {
                throw new IllegalStateException("Failed to change password");
            }
        } else {
            throw new IllegalArgumentException("New password must not be empty");
        }
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void addPhoneNumber(Customer customer, String phone) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        } else if (phone != null && !phone.isEmpty()) {
            if(DB.addPhoneNumberToCustomer(customer, phone)){
                customer.addPhoneNumber(phone);
            }else{
                throw new IllegalStateException("Failed to add phone number to customer");
            }

        }
    }
    @Override
    public void removePhoneNumber(Customer customer, String phone){
        if(DB.removePhoneNumberFromCustomer(customer, phone)){
            customer.getPhoneNumbers().remove(phone);
        } else {
            throw new IllegalStateException("Failed to remove phone number from customer");
        }
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void addAddress(Customer customer, Address address) {
        if (DB.addAddressToCustomer(customer, address)) {
            customer.getAddresses().add(address);
        }else{
            throw new IllegalStateException("Failed to add address to customer");
        }
    }
    public void removeAddress(Customer customer, Address address){
        DB.removeAddressFromCustomer(customer, address);
    }
    public void removeCard(Customer customer, Card card){
        DB.removeCardFromCustomer(customer, card);
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public List<Address> getAddresses(Customer customer) {
        return DB.fetchCustomerAddresses(customer);
    }
    public ArrayList<String> fetchCustomerPhoneNumbers(Customer customer){
        return DB.fetchCustomerPhoneNumbers(customer);
    }
    public ArrayList<Address> fetchCustomerAddresses(Customer customer){
        return DB.fetchCustomerAddresses(customer);
    }
    public void addCardToCustomer(Customer customer, Card card) {
        if (DB.addCardToCustomer(customer, card)) {
            customer.getCards().add(card);
        } else {
            throw new IllegalStateException("Failed to add card to customer");
        }
    }

    @Override
    public User login(String email, String password) {
        if (!validateCredentials(email, password)) {
            return null;
        }
        User user = getUserByEmail(email);
        if (user == null || !verifyPassword(user, password)) {
            return null;
        }
        return user;
    }

    @Override
    public void logout() {
        // TODO: Implementation
    }

    private boolean validateCredentials(String email, String password) {
        return email != null && !email.isEmpty() && validateEmailFormat(email)
                && password != null && !password.isEmpty();
    }

    private User getUserByEmail(String email) {
        return userData.getUserByEmail(email);
    }
    private String hashPassword(String password) {
        return PasswordUtils.hashPassword(password);
    }
    private boolean verifyPassword(User user, String password) {
        return PasswordUtils.verifyPassword(password, user.getPasswordHash());
    }
    //Working on it : Mohamed Khaled Becetti
    private boolean validateEmailFormat(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public ArrayList<Order> fetchCustomerOrders(Customer customer) {
        return DB.fetchCustomerOrders(customer);
    }

    public ArrayList<Card> fetchCustomerCards(Customer customer) {
        return DB.fetchCustomerCards(customer);
    }

    // to String Return?



    //Working on it : Mohamed Khaled Becetti
    //private void saveCustomer(Customer customer) {
       // same as create new customer
   // }
}