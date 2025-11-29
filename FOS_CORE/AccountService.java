package FOS_CORE;

import java.util.List;
import FOS_DATA.IUserData;

public class AccountService implements IAccountService {

    private final IUserData DB = new FOS_DATA.UserData() ;
    static int userID = 0;
    // Working on it : Mohamed Khaled Becetti
    @Override
    public Customer createCustomerAccount(String email, String phone, String password) {
        Customer user = new Customer(userID, email, password);
        DB.addNewCustomer(user);
        DB.addPhoneNumber(user,phone);
        return null;
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void changePassword(User user, String newPassword) {
        DB.changeUserPassword(user,newPassword);
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public void updateContactInfo(Customer customer, String phone) {
        DB.addPhoneNumber(customer,phone);
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public boolean addAddress(Customer customer, Address address) {
        DB.addAddress(customer,address);
        return false;
    }
    //Working on it : Mohamed Khaled Becetti
    @Override
    public List<Address> getAddresses(Customer customer) {
        return DB.fetchCustomerAddresses(customer);
    }
    //Working on it : Mohamed Khaled Becetti
    private boolean validateEmailFormat(String email) {
        if (email==null || email.trim().equals("")) {return false;}//empty string
        String prefix= email.substring(0, email.indexOf("@")).trim(); // prefix@suffix
        String suffix = email.substring(email.indexOf("@")+1).trim();
        if(prefix.length()==0 || suffix.length()==0) {return false;}//empty preffix or suffix
        if(prefix.contains(" ")||suffix.contains(" ")) {return false;}//space not allowed in between suffix or prefix
        if(prefix.contains("@#$%^%^&*()-=\\") ||suffix.contains("@#$%^%^&*()-=\\")) {return false;} // invalid characters for the email

        return true;
    }
    //Working on it : Mohamed Khaled Becetti
    //private void saveCustomer(Customer customer) {
       // same as create new customer
   // }
}