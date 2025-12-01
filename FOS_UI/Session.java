package FOS_UI;

import FOS_CORE.Customer;
import FOS_CORE.Manager;
import FOS_CORE.User;

public final class Session {

    private static User currentUser;

    private Session(){}

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static Customer getCurrentCustomer(){
        return (currentUser instanceof Customer) ? (Customer) currentUser : null;
    }

    public static Manager getCurrentManager(){
        return (currentUser instanceof Manager) ? (Manager) currentUser : null;
    }

    public static boolean isLoggedIn(){
        return currentUser != null;
    }

    public static void clear(){
        currentUser = null;
        //More session state can be added (e.g,
        // selectedRestaurant, etc.) and rest them here.
    }


}
