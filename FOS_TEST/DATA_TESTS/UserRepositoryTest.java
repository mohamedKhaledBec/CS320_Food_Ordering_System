package FOS_TEST.DATA_TESTS;

import FOS_CORE.Customer;
import FOS_CORE.Manager;
import FOS_CORE.User;
import FOS_DATA.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserRepositoryTest {

    private final UserData userData = new UserData();

    @Test
    void testGetUserByEmail_Manager() {
        User user = userData.getUserByEmail("manager.1@gmail.com");
        Assertions.assertNotNull(user, "Manager should exist");
        Assertions.assertTrue(user instanceof Manager, "Manager must be returned");
        Assertions.assertEquals("manager.1@gmail.com", user.getEmail());
    }

    @Test
    void testGetUserByEmail_Customer() {
        User user = userData.getUserByEmail("customer.1@gmail.com");
        Assertions.assertNotNull(user, "Customer should exist");
        Assertions.assertTrue(user instanceof Customer, "Customer must be returned");
        Assertions.assertEquals("customer.1@gmail.com", user.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        User user = userData.getUserByEmail("doesnotexist@example.com");
        Assertions.assertNull(user, "User should be null for non-existing email");
    }

    @Test
    void testChangeUserPassword() {
        // Arrange
        User user = userData.getUserByEmail("customer.2@gmail.com");
        Assertions.assertNotNull(user, "User must exist before test");

        String newPassword = "testpassword123";

        // Act
        boolean updated = userData.changeUserPassword(user, newPassword);

        // Assert
        Assertions.assertTrue(updated, "Password update should succeed");

        // Verify update
        User updatedUser = userData.getUserByEmail("customer.2@gmail.com");
        Assertions.assertEquals(newPassword, updatedUser.getPassword(),
                "Password must match newly updated password");
    }
}
