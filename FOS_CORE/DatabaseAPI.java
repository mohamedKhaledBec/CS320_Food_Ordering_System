package FOS_CORE;

import FOS_DATA.UserData;

import java.util.ArrayList;

public class DatabaseAPI {

    private final UserData dao;

    public DatabaseAPI() {
        this.dao = new FOS_DATA.UserData();
    }


    public boolean saveRestaurantInfo(Restaurant restaurant) {
        if (restaurant == null) return false;
        return dao.saveRestaurantInfo(restaurant);
    }

    public ArrayList<Restaurant> getManagerRestaurants(Manager manager) {
        return dao.getManagerRestaurants(manager);
    }

    public ArrayList<Restaurant> getRestaurantByCity(String city) {
        return dao.getRestaurantByCity(city);
    }

    public boolean saveMenuItem(MenuItem menuItem) {
        return dao.saveMenuItem(menuItem);
    }

    public ArrayList<MenuItem> getRestaurantMenuItems(Restaurant restaurant) {
        return dao.getRestaurantMenuItems(restaurant);
    }

    public ArrayList<Order> findCustomerOrders(Customer customer) {
        return dao.findCustomerOrders(customer);
    }

    public ArrayList<Order> findRestaurantOrders(Restaurant restaurant) {
        return dao.findRestaurantOrders(restaurant);
    }

    public boolean saveOrder(Order order) {
        return dao.saveOrder(order);
    }

}