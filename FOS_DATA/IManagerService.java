package FOS_DATA;

import java.util.ArrayList;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

public interface IManagerService {

    public boolean updateRestaurantInfo(Restaurant newRestaurantInfo);

    public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant);
    public boolean updateMenuItem(MenuItem menuItem);
    public boolean removeMenuItem(MenuItem menuItem);

    public boolean createDiscount(Discount discount, MenuItem menuItem);
    public boolean removeDiscount(Discount discount);

    public boolean updateOrderStatus(Order order, String status);

    public ArrayList<Restaurant> fetchManagerRestaurants(Manager manager);

}