package FOS_CORE;

import java.util.ArrayList;

public interface IRestaurantService {
    public ArrayList<Restaurant> getRestaurantsByCity(String city) ;
    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant);
    public ArrayList<String> fetchRestaurantKeywords(Restaurant restaurant);
    public ArrayList<Restaurant> searchRestaurantsByKeyword(String keyword, ArrayList<Restaurant> inRestaurants);
    public double calculateMenuItemDiscount(MenuItem item);
    public double calculateRestaurantRating(Restaurant restaurant);
    public ArrayList<Order> fetchRestaurantOrdersForToday(Restaurant restaurant);
}