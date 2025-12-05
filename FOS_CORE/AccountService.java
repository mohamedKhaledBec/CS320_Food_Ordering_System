package FOS_CORE;

import java.util.ArrayList;
import java.util.List;
import FOS_DATA.*;

public class AccountService implements IAccountService {
    private final IUserData userData = new UserData() ;

    private final ICustomerService DB = new CustomerService() ;
    // Working on it : Mohamed Khaled Becetti
    @Override
    public boolean createCustomerAccount(String email, String phone, String password) {
        if (!this.validateEmailFormat(email)) {
            throw new IllegalArgumentException("Invalid email format");
        } else if (password != null && !password.isEmpty()) {
            Customer customer = new Customer(-1, email, password);
            boolean saved = DB.addNewCustomer(customer);
            if (!saved) {
                throw new IllegalStateException("Failed to create customer account");
            } else {
                User created = DB.getUserByEmail(email);
                if (created instanceof Customer) {
                    Customer createdCustomer = (Customer)created;
                    if (phone != null && !phone.isEmpty()) {
                        DB.addPhoneNumberToCustomer(createdCustomer, phone);
                    }
                    return true;
                } else {
                    throw new IllegalStateException("Account was created but could not be reloaded as Customer");
                }
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
    public void updateContactInfo(Customer customer, String phone) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        } else if (phone != null && !phone.isEmpty()) {
            DB.addPhoneNumberToCustomer(customer, phone);
            customer.addPhoneNumber(phone);
        }
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void addAddress(Customer customer, Address address) {
        boolean added = DB.addAddressToCustomer(customer, address);
        if (added) {
            customer.getAddresses().add(address);
        }else{
            throw new IllegalStateException("Failed to add address to customer");
        }
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