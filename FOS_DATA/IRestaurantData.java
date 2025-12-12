package FOS_DATA;

import FOS_CORE.*;

import java.util.ArrayList;

public interface IRestaurantData {
    public ArrayList<Discount> fetchMenuItemDiscounts(MenuItem menuItem);
    public ArrayList<Restaurant> fetchRestaurantsByCity(String city);
    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant);
    public double calculateRestaurantRating(Restaurant restaurant);
    public ArrayList<String> fetchRestaurantKeywords(Restaurant restaurant);
    public ArrayList<Order> fetchRestaurantOrdersForToday(Restaurant restaurant);
}
