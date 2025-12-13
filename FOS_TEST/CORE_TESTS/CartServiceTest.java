package FOS_TEST.CORE_TESTS;

import FOS_CORE.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

// Being done by Umair Ahmad
public class CartServiceTest {
    private CartService cartService;
    private AccountService accountService;
    private Address baseAddress;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
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

    private Customer createCustomer() {
        String email = uniqueEmail();
        String phone = uniquePhone();
        String password = "StrongPass123!";
        accountService.createCustomerAccount(email, phone, password, baseAddress);
        User user = accountService.login(email, password);
        assertNotNull(user);
        assertTrue(user instanceof Customer);
        return (Customer) user;
    }

    private MenuItem createMenuItem() {
        return new MenuItem();
    }

    @Test
    void testCartStartsEmpty() {
        Customer customer = createCustomer();
        assertNotNull(customer.getCart());
        assertTrue(customer.getCart().isEmpty());
    }

    @Test
    void testAddToCart() {
        Customer customer = createCustomer();
        MenuItem item = createMenuItem();
        int quantity = 2;
        double price = 10.0;

        cartService.addToCart(customer, item, quantity, price);

        assertEquals(1, customer.getCart().size());
        CartItem cartItem = customer.getCart().get(0);
        assertEquals(item, cartItem.getMenuItem());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(price, cartItem.getPrice());
        assertEquals(price * quantity, cartItem.calculateItemTotal(), 0.0001);
    }

    @Test
    void testUpdateQuantity() {
        Customer customer = createCustomer();
        MenuItem item = createMenuItem();

        cartService.addToCart(customer, item, 1, 5.0);
        cartService.updateCartItemQuantity(customer, item, 3);

        assertEquals(1, customer.getCart().size());
        CartItem cartItem = customer.getCart().get(0);
        assertEquals(3, cartItem.getQuantity());
        assertEquals(3 * 5.0, cartItem.calculateItemTotal(), 0.0001);
    }

    @Test
    void testAddMultipleItems() {
        Customer customer = createCustomer();
        MenuItem item = createMenuItem();

        cartService.addToCart(customer, item, 1, 7.5);
        cartService.addToCart(customer, item, 2, 7.5);

        assertEquals(1, customer.getCart().size());
        CartItem cartItem = customer.getCart().get(0);
        assertEquals(3, cartItem.getQuantity());
    }
    @Test
    void testRemoveFromCart() {
        Customer customer = createCustomer();
        MenuItem item = createMenuItem();

        cartService.addToCart(customer, item, 2, 10.0);
        assertFalse(customer.getCart().isEmpty());

        cartService.removeFromCart(customer, item);

        assertTrue(customer.getCart().isEmpty());
    }
}
