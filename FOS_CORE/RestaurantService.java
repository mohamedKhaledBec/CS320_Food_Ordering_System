package FOS_CORE;

import java.util.ArrayList;

public class RestaurantService implements IRestaurantService {

    @Override
    public ArrayList<Restaurant> getRestaurantsByCity(String city) {
        // TODO: Implementation
        return null;
    }

    @Override
    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant) {
        // TODO: Implementation
        return null;
    }

    @Override
    public ArrayList<Restaurant> searchRestaurantsByKeyword(String keyword) {
        // TODO: Implementation
        return null;
    }

    public ArrayList<String> fetchRestaurantKeywords(Restaurant restaurant){
        return new ArrayList<>();
    }

    private void loadRestaurantData(Restaurant restaurant) {
        // TODO: Implementation
    }
}