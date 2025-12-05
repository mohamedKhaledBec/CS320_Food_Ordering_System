package FOS_DATA;

import FOS_CORE.*;

import java.util.ArrayList;

public interface IRestaurantData {
    public ArrayList<Discount> fetchMenuItemDiscounts(MenuItem menuItem);
}
