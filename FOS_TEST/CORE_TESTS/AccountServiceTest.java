package FOS_TEST.CORE_TESTS;

import FOS_CORE.*;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
// Being done by Umair Ahmad
public class AccountServiceTest {

    private AccountService accountService;
    private Address baseAddress;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
        baseAddress = new Address(
                -1,
                "Test Street 1",
                "Test City",
                "TS",
                "00000"
        );
    }
    private String uniqueEmail() {
        return "user+" + UUID.randomUUID() + "@test.com";
    }

    @Test
    void testRegisterAndLogin() {
        String email = uniqueEmail();
        String phone = "5550000000";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);
        User loggedIn = accountService.login(email, password);

        assertNotNull(loggedIn);
        assertTrue(loggedIn instanceof Customer);
        Customer customer = (Customer) loggedIn;
        assertEquals(email, customer.getEmail());
    }

    @Test
    void testDuplicateEmail() {
        String email = uniqueEmail();
        String phone = "5551111111";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);

        assertThrows(IllegalArgumentException.class, () ->
                accountService.createCustomerAccount(email, phone, password, baseAddress));
    }

    @Test
    void testInvalidEmail() {
        String invalidEmail = "not-an-email";
        String phone = "5552222222";
        String password = "StrongPass123!";

        assertThrows(IllegalArgumentException.class, () ->
                accountService.createCustomerAccount(invalidEmail, phone, password, baseAddress));
    }

    @Test
    void testLoginValid() {
        String email = uniqueEmail();
        String phone = "5553333333";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);

        User loggedIn = accountService.login(email, password);

        assertNotNull(loggedIn);
        assertEquals(email, loggedIn.getEmail());
    }

    @Test
    void testLoginWrongPassword() {
        String email = uniqueEmail();
        String phone = "5554444444";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);

        User loggedIn = accountService.login(email, "WrongPassword!23");

        assertNull(loggedIn);
    }

    @Test
    void testLoginUnknownEmail() {
        User loggedIn = accountService.login(uniqueEmail(), "SomePassword123!");
        assertNull(loggedIn);
    }


    @Test
    void testChangePassword() {
        String email = uniqueEmail();
        String phone = "5555555555";
        String oldPassword = "OldPass123!";
        String newPassword = "NewPass456!";

        accountService.createCustomerAccount(email, phone, oldPassword, baseAddress);
        Customer customer = (Customer) accountService.login(email, oldPassword);
        assertNotNull(customer);

        accountService.changePassword(customer,newPassword);

        assertNull(accountService.login(email, oldPassword));
        assertNotNull(accountService.login(email, newPassword));
    }

    @Test
    void testPhoneNumbers() {
        String email = uniqueEmail();
        String phone = "5556666666";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);
        Customer customer = (Customer) accountService.login(email, password);
        assertNotNull(customer);

        String additionalPhone = "5557777777";

        accountService.addPhoneNumber(customer, additionalPhone);

        assertNotNull(customer.getPhoneNumbers());
        assertTrue(customer.getPhoneNumbers().contains(additionalPhone));

        accountService.removePhoneNumber(customer, additionalPhone);

        assertFalse(customer.getPhoneNumbers().contains(additionalPhone));
    }

    @Test
    void testAddCard() {
        String email = uniqueEmail();
        String phone = "5558888888";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);
        Customer customer = (Customer) accountService.login(email, password);
        assertNotNull(customer);

        Date expiryDate = Date.valueOf(LocalDate.now().plusYears(1).withDayOfMonth(1));
        Card card = new Card("4111111111111111", "Test User", expiryDate, "123");

        accountService.addCardToCustomer(customer, card);

        assertNotNull(customer.getCards());
        assertTrue(customer.getCards().contains(card));
    }
}
