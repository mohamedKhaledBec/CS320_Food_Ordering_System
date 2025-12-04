package FOS_DATA;

import java.util.ArrayList;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

public interface IRestaurantData {

    public boolean saveRestaurantInfo(Restaurant restaurant);
    public ArrayList<Restaurant> fetchRestaurantsByCity(String city);

    public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant);
    public boolean updateMenuItem(MenuItem menuItem);
    public boolean removeMenuItem(MenuItem menuItem);
    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant);
    public ArrayList<Order> fetchRestaurantOrders(Restaurant restaurant);
    public ArrayList<String> fetchRestaurantKeywords(Restaurant restaurant);

}