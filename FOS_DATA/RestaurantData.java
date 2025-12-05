package FOS_DATA;

import FOS_CORE.Discount;
import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;

import java.sql.*;
import java.util.ArrayList;

public class RestaurantData implements IRestaurantData{

    public ArrayList<Discount> fetchMenuItemDiscounts(MenuItem menuItem) {
        int menuItemID = menuItem.getMenuItemID();
        ArrayList<Discount> discounts = new ArrayList<>();
        final String sql = "SELECT * FROM Discount WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItemID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int discountID = resultSet.getInt("discount_id");
                String discountName = resultSet.getString("discount_name");
                String discountDescription = resultSet.getString("discount_description");
                double percentage = resultSet.getDouble("discount_percentage");
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");
                long millis= System.currentTimeMillis();
                Date currentDate = new Date(millis);
                if (startDate.before(currentDate) && endDate.after(currentDate)) {
                    Discount discount = new Discount(discountID, discountName, discountDescription, percentage, startDate, endDate);
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database failed to fetch Menu Item Discounts: " + e.getMessage());
        }
        return discounts;
    }
}
