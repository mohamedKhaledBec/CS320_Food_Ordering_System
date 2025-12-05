package FOS_DATA;

import FOS_CORE.Customer;
import FOS_CORE.Manager;
import FOS_CORE.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserData implements IUserData{
    public User getUserByEmail(String email) {
        final String sql = "SELECT user_id, password, user_type FROM User WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String password = resultSet.getString("password");
                    String userType = resultSet.getString("user_type");
                    if(userType.equals("manager")){
                        return new Manager(id, email, password);
                    }else{
                        return new Customer(id, email, password);
                    }

                }
            } catch (SQLException e) {
                System.out.println("No user found with the corresponding username");
            }

        } catch (SQLException e) {
            System.out.println("Database failed to fetch user by username");
        }
        return null;
    }

    public boolean changeUserPassword(User user, String newHashedPassword){
        int userId = user.getUserID();
        final String sql = "UPDATE User SET password = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newHashedPassword);
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to update user password");
            return false;
        }
    }
}
