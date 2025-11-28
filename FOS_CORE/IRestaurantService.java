package FOS_CORE;

import java.util.List;

public interface IRestaurantService {
    List<Restaurant> getRestaurants(String city);
    Menu getMenu(String restaurantID);
    List<Restaurant> searchRestaurants(String keyword);
}