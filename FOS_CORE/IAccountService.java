package FOS_CORE;

import java.util.List;

public interface IAccountService {
    Customer createCustomerAccount(String email, String phone, String password, String address);
    void changePassword(User user, String newPassword);
    void updateContactInfo(Customer customer, String phone);
    Address addAddress(Customer customer, String address);
    List<Address> getAddresses(Customer customer);
}