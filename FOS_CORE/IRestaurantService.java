package FOS_CORE;

import java.util.List;

public interface IRestaurantService {
    List<Restaurant> getRestaurants(String city);
    List<MenuItem> getMenu(int restaurantID);
    List<Restaurant> searchRestaurants(String keyword);
}