package FOS_CORE;

import java.util.List;

public class AccountService implements IAccountService {

    private final DatabaseAPI DB = new DatabaseAPI();
    static int userID = 0;
    // Working on it : Mohamed Khaled Becetti
    @Override
    public Customer createCustomerAccount(String email, String phone, String password) {
        User user = new Customer(userID, email, password);
        DB.addNewCustomer(user);
        DB.addPhoneNumber((Customer)user,phone);
        return null;
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void changePassword(User user, String newPassword) {
        // TODO: Implementation

    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void updateContactInfo(Customer customer, String phone) {
        // TODO: Implementation
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public boolean addAddress(Customer customer, Address address) {
        // TODO: Implementation
        return true;
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public List<Address> getAddresses(Customer customer) {
        // TODO: Implementation
        return null;
    }
    //Working on it : Mohamed Khaled Becetti
    private boolean validateEmailFormat(String email) {
        // TODO: Implementation
        return false;
    }
    //Working on it : Mohamed Khaled Becetti
    private void saveCustomer(Customer customer) {
        // TODO: Implementation
    }
}