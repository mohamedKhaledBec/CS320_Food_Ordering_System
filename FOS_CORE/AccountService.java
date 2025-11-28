package FOS_CORE;

import java.util.List;

public class AccountService implements IAccountService {

    @Override
    public Customer createCustomerAccount(String email, String phone, String password, String address) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void changePassword(User user, String newPassword) {
        // TODO: Implementation
    }

    @Override
    public void updateContactInfo(Customer customer, String phone) {
        // TODO: Implementation
    }

    @Override
    public Address addAddress(Customer customer, String address) {
        // TODO: Implementation
        return null;
    }

    @Override
    public List<Address> getAddresses(Customer customer) {
        // TODO: Implementation
        return null;
    }

    private boolean validateEmailFormat(String email) {
        // TODO: Implementation
        return false;
    }

    private void saveCustomer(Customer customer) {
        // TODO: Implementation
    }
}