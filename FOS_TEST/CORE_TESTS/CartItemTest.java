package FOS_TEST.CORE_TESTS;


import FOS_CORE.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

// Being done by Umair Ahmad
public class CartItemTest {
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
        return (Customer) user;
    }

    private MenuItem createMenuItem() {
        return new MenuItem();
    }

    @Test
    void testItemTotal() {
        Customer customer = createCustomer();
        MenuItem item = createMenuItem();

        cartService.addToCart(customer, item, 2, 10.0);

        assertEquals(1, customer.getCart().size());
        CartItem cartItem = customer.getCart().get(0);

        assertEquals(20.0, cartItem.calculateItemTotal(), 0.0001);
    }

    @Test
    void testItemTotalAfterQuantityChange() {
        Customer customer = createCustomer();
        MenuItem item = createMenuItem();

        cartService.addToCart(customer, item, 1, 5.0);
        CartItem cartItem = customer.getCart().get(0);
        assertEquals(5.0, cartItem.calculateItemTotal(), 0.0001);

        cartService.updateCartItemQuantity(customer, item, 3);

        cartItem = customer.getCart().get(0);
        assertEquals(15.0, cartItem.calculateItemTotal(), 0.0001);
    }
}
