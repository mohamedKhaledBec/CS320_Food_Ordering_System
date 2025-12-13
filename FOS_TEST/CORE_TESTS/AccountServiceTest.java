package FOS_TEST.CORE_TESTS;

import FOS_CORE.AccountService;
import FOS_CORE.Address;
import FOS_CORE.Card;
import FOS_CORE.Customer;
import FOS_CORE.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        return "user_" + UUID.randomUUID() + "@test.com";
    }
    private String uniquePhone() {
        String digits = UUID.randomUUID().toString().replaceAll("\\D", "");
        String last7 = digits.substring(digits.length() - 7);
        return "555" + last7;
    }

    @Test
    void testRegisterAndLogin() {
        String email = uniqueEmail();
        String phone = uniquePhone();
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
        String phone = uniquePhone();
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);

        assertThrows(IllegalStateException.class, () ->
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
    void testPhoneNumbersInitialAndRemove() {
        String email = uniqueEmail();
        String phone = "5556666666";
        String password = "StrongPass123!";

        accountService.createCustomerAccount(email, phone, password, baseAddress);
        Customer customer = (Customer) accountService.login(email, password);
        assertNotNull(customer);

        assertNotNull(customer.getPhoneNumbers());
        assertTrue(customer.getPhoneNumbers().contains(phone));

        accountService.removePhoneNumber(customer, phone);

        assertFalse(customer.getPhoneNumbers().contains(phone));
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
