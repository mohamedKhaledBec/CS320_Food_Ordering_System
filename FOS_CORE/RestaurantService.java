package FOS_CORE;

import FOS_DATA.IManagerService;
import FOS_DATA.IRestaurantData;
import FOS_DATA.ManagerService;
import FOS_DATA.RestaurantData;

import java.sql.Timestamp;
import java.util.ArrayList;

public class RestaurantService implements IRestaurantService {
    private final IRestaurantData DB = new RestaurantData();
    @Override
    public ArrayList<Restaurant> getRestaurantsByCity(String city) {
        if (city == null || city.isEmpty()) {
            return new ArrayList<>();
        }
        return DB.fetchRestaurantsByCity(city);
    }

    @Override
    public ArrayList<MenuItem> fetchRestaurantMenu(Restaurant restaurant) {
        return DB.fetchRestaurantMenu(restaurant);
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
        return DB.fetchRestaurantKeywords(restaurant);
    }

    public ArrayList<Discount> fetchMenuItemDiscounts(MenuItem item){
        return DB.fetchMenuItemDiscounts(item);
    }

    public double calculateMenuItemDiscount(MenuItem item) {
        if (item == null || item.getDiscounts() == null) return 0.0;
        java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
        for (Discount d : item.getDiscounts()) {
            if (d == null) continue;
            Timestamp start = d.getStartDate();
            Timestamp end = d.getEndDate();
            if (start != null && end != null && !now.before(start) && !now.after(end)) {
                double percentage = d.getDiscountPercentage();
                double originalPrice = item.getPrice();
                return originalPrice * (1-(percentage / 100.0));
            }
        }
        return item.getPrice();
    }

    @Override
    public double calculateRestaurantRating(Restaurant restaurant) {
        return DB.calculateRestaurantRating(restaurant);
    }

    public ArrayList<Order> fetchRestaurantOrdersForToday(Restaurant restaurant) {
        return DB.fetchRestaurantOrdersForToday(restaurant);
    }
}