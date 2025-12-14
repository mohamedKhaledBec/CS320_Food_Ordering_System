package FOS_TEST.DATA_TESTS;

import FOS_CORE.Customer;
import FOS_CORE.User;
import FOS_DATA.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest
{

    private UserData userData;
    private User testCustomer;

    @BeforeEach
    void setUp()
    {
        userData = new UserData();
        testCustomer = new Customer(4, "customer.1@gmail.com", "$2a$10$QYjRugyWC3sHYEqJCljG8OsPiCEMFNb4IPdtj/o9MPyW6zFbWrAP6");
    }

    @Test
    void testGetUserByEmail_ExistingCustomer()
    {
        User fetchedUser = userData.getUserByEmail("customer.1@gmail.com");

        assertNotNull(fetchedUser, "User should be found");
        assertTrue(fetchedUser instanceof Customer, "User should be an instance of Customer");
        assertEquals(4, fetchedUser.getUserID(), "User ID should match existing record");
    }

    @Test
    void testGetUserByEmail_NonExistent()
    {
        User fetchedUser = userData.getUserByEmail("nonexistent.ghost@gmail.com");
        assertNull(fetchedUser, "Non-existent user should return null");
    }

    @Test
    void testChangeUserPassword()
    {
        String newPassHash = "TEMP_TEST_HASH_123";
        boolean result = userData.changeUserPassword(testCustomer, newPassHash);

        assertTrue(result, "Password update should return true");
        User updatedUser = userData.getUserByEmail(testCustomer.getEmail());
        assertEquals(newPassHash, updatedUser.getPasswordHash(), "Password hash in DB should be updated");
        userData.changeUserPassword(testCustomer, testCustomer.getPasswordHash());
    }
}