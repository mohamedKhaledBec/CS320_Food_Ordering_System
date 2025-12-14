package FOS_TEST.CORE_TESTS;

import FOS_CORE.Address;
import FOS_CORE.Card;
import FOS_CORE.CartItem;
import FOS_CORE.Customer;
import FOS_CORE.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Basic unit tests for the Customer domain model
public class CustomerTest {

	private Customer customer;

	@BeforeEach
	void setUp() {
		customer = new Customer();
		// Initialize collections so behavior can be tested without hitting the DB-backed constructor
		setField(customer, "phoneNumbers", new ArrayList<String>());
		setField(customer, "addresses", new ArrayList<Address>());
		setField(customer, "orders", new ArrayList<Order>());
		setField(customer, "cards", new ArrayList<Card>());
		setField(customer, "cart", new ArrayList<CartItem>());
	}

	@Test
	void userPropertiesAreReadableAndWritable() {
		customer.setUserID(42);
		customer.setEmail("alice@example.com");
		customer.setPasswordHash("hashed-password");

		assertEquals(42, customer.getUserID());
		assertEquals("alice@example.com", customer.getEmail());
		assertEquals("hashed-password", customer.getPasswordHash());
	}

	@Test
	void addPhoneNumberAppendsToList() {
		customer.addPhoneNumber("555-1234");

		assertEquals(1, customer.getPhoneNumbers().size());
		assertTrue(customer.getPhoneNumbers().contains("555-1234"));
	}

	@Test
	void gettersExposeInjectedCollections() {
		ArrayList<String> phones = new ArrayList<>();
		ArrayList<Address> addresses = new ArrayList<>();
		ArrayList<Order> orders = new ArrayList<>();
		ArrayList<Card> cards = new ArrayList<>();
		ArrayList<CartItem> cart = new ArrayList<>();

		setField(customer, "phoneNumbers", phones);
		setField(customer, "addresses", addresses);
		setField(customer, "orders", orders);
		setField(customer, "cards", cards);
		setField(customer, "cart", cart);

		assertSame(phones, customer.getPhoneNumbers());
		assertSame(addresses, customer.getAddresses());
		assertSame(orders, customer.getOrders());
		assertSame(cards, customer.getCards());
		assertSame(cart, customer.getCart());
	}

	private static void setField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			fail("Failed to inject field '" + fieldName + "': " + e.getMessage());
		}
	}
}
