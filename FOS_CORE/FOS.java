package FOS_CORE;

import java.util.ArrayList;

public class FOS {
    private static ArrayList<Restaurant> allRestaurants = new ArrayList<>();
    private ArrayList<Customer> allCustomers;
    private ArrayList<Manager> allManagers;

    public static ArrayList<Restaurant> getAllRestaurants() {
        if (allRestaurants == null) {
            allRestaurants = new ArrayList<>();
        }
        return allRestaurants;
    }

    public ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public ArrayList<Manager> getAllManagers() {
        return allManagers;
    }
}
