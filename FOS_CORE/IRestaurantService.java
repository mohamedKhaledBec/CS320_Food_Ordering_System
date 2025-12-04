package FOS_CORE;

import java.util.ArrayList;

public interface IRestaurantService {
    public ArrayList<Restaurant> getRestaurantsByCity(String city) ;
    public ArrayList<MenuItem> getMenu(Restaurant restaurant);
    public ArrayList<MenuItem> getRestaurantMenu(Restaurant restaurant);
    public ArrayList<Restaurant> searchRestaurantsByKeyword(String keyword);
}