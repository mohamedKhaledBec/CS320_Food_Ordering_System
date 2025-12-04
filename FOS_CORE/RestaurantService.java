package FOS_CORE;

import java.util.ArrayList;

public class RestaurantService implements IRestaurantService {

    @Override
    public ArrayList<Restaurant> getRestaurantsByCity(String city) {
        // TODO: Implementation
        return null;
    }

    @Override
    public ArrayList<MenuItem> getMenu(Restaurant restaurant) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<MenuItem> getRestaurantMenu(Restaurant restaurant) {
        // TODO: Implementation
        return null;
    }

    @Override
    public ArrayList<Restaurant> searchRestaurantsByKeyword(String keyword) {
        // TODO: Implementation
        return null;
    }

    public ArrayList<String> fetchKeywords(Restaurant restaurant){

    }

    private void loadRestaurantData(Restaurant restaurant) {
        // TODO: Implementation
    }
}