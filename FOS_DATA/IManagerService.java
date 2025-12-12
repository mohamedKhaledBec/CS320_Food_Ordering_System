package FOS_DATA;

import java.util.ArrayList;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

public interface IManagerService {

    public boolean saveRestaurantInfo(Restaurant restaurant);

    public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant);
    public boolean updateMenuItem(MenuItem menuItem);
    public boolean removeMenuItem(MenuItem menuItem);

    public boolean createDiscount(Discount discount, MenuItem menuItem);

    public ArrayList<Restaurant> fetchManagerRestaurants(Manager manager);

}