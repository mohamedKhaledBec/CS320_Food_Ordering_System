package FOS_DATA;

import java.awt.*;
import java.util.ArrayList;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

import java.sql.*;

public interface IDataAccess {

    public boolean saveRestaurantInfo(Restaurant restaurant);
    public ArrayList<Restaurant> getManagerRestaurants(Manager manager);
    public ArrayList<Restaurant> getRestaurantByCity(String city);

    public boolean saveMenuItem(MenuItem menuItem);
    public ArrayList<MenuItem> getRestaurantMenuItems(Restaurant restaurant);
    public ArrayList<Order> findCustomerOrders(Customer customer);
    public ArrayList<Order> findRestaurantOrders(Restaurant restaurant);
    public boolean saveOrder(Order order);

}