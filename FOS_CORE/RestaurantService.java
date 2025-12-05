package FOS_CORE;

import FOS_DATA.IManagerService;
import FOS_DATA.ManagerService;

import java.util.ArrayList;

public class RestaurantService implements IRestaurantService {
    private final IManagerService DB = new ManagerService();
    @Override
    public ArrayList<Restaurant> getRestaurantsByCity(String city) {
        if (city == null || city.isEmpty()) {
            return new ArrayList<>();
        }
        return DB.fetchRestaurantsByCity(city);
    }

    @Override
    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant) {
        // TODO: Implementation
        return null;
    }

    @Override
    public ArrayList<Restaurant> searchRestaurantsByKeyword(String keyword, ArrayList<Restaurant> inRestaurants) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Restaurant> results = new ArrayList<>();
            for (Restaurant r : inRestaurants) {
                if (r.getRestaurantName().toLowerCase().contains(keyword.toLowerCase()) ||
                        r.getCuisineType().toLowerCase().contains(keyword.toLowerCase())||
                        r.getKeywords().contains(keyword.toLowerCase())) {
                    results.add(r);
                }
        }
        return results;
    }

    public ArrayList<String> fetchRestaurantKeywords(Restaurant restaurant){
        return new ArrayList<>();
    }

    private void loadRestaurantData(Restaurant restaurant) {
        // TODO: Implementation
    }
}