/**
 * This test class is written by Mohamed Khaled Becetti
 */

package FOS_TEST.CORE_TESTS;

import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class RestaurantTest {

	private Restaurant restaurant;

	@BeforeEach
	void setUp() {
		restaurant = allocateRestaurant();
		setField(restaurant, "Keywords", new ArrayList<String>());
		setField(restaurant, "menu", new ArrayList<MenuItem>());
		setField(restaurant, "restaurantService", null);
	}

	@Test
	/* @brief Verifies basic getters/setters work
	 * @tests Ensures setter/getter contract for id, name, cuisine, city. */
	void restaurantPropertiesAreReadableAndWritable() {
		restaurant.setRestaurantID(7);
		restaurant.setRestaurantName("Bella Italia");
		restaurant.setCuisineType("Italian");
		restaurant.setCity("New York");

		assertEquals(7, restaurant.getRestaurantID());
		assertEquals("Bella Italia", restaurant.getRestaurantName());
		assertEquals("Italian", restaurant.getCuisineType());
		assertEquals("New York", restaurant.getCity());
	}

	@Test
	/* @brief Confirms keywords list is what we injected
	 * @tests Validates that getKeywords() returns the injected list reference. */
	void getKeywordsReturnsInjectedList() {
		ArrayList<String> keywords = new ArrayList<>();
		keywords.add("fast-food");
		keywords.add("burgers");

		setField(restaurant, "Keywords", keywords);

		assertSame(keywords, restaurant.getKeywords());
		assertEquals(2, restaurant.getKeywords().size());
	}

	@Test
	/* @brief Replacing keywords list should take effect immediately
	 * @tests Verifies setKeywords() swaps the backing list as expected. */
	void setKeywordsReplacesList() {
		ArrayList<String> newKeywords = new ArrayList<>();
		newKeywords.add("fine-dining");
		newKeywords.add("reservations");

		restaurant.setKeywords(newKeywords);

		assertSame(newKeywords, restaurant.getKeywords());
		assertEquals(2, restaurant.getKeywords().size());
	}

	@Test
	/* @brief Confirms menu list is what we injected
	 * @tests Validates that getMenu() returns the injected list reference. */
	void getMenuReturnsInjectedList() {
		ArrayList<MenuItem> menu = new ArrayList<>();
		MenuItem item1 = new MenuItem();
		setField(item1, "itemName", "Pasta");
		setField(item1, "price", 12.99);
		menu.add(item1);

		setField(restaurant, "menu", menu);

		assertSame(menu, restaurant.getMenu());
		assertEquals(1, restaurant.getMenu().size());
	}

	@Test
	/* @brief Replacing menu list should take effect immediately
	 * @tests Verifies setMenu() swaps the backing list as expected. */
	void setMenuReplacesList() {
		ArrayList<MenuItem> newMenu = new ArrayList<>();
		MenuItem item2 = new MenuItem();
		setField(item2, "itemName", "Pizza");
		setField(item2, "price", 14.99);
		newMenu.add(item2);

		restaurant.setMenu(newMenu);

		assertSame(newMenu, restaurant.getMenu());
		assertEquals(1, restaurant.getMenu().size());
	}

	@Test
	/* @brief Restaurant ID can be negative
	 * @tests Documents storage behavior for invalid/negative identifiers. */
	void restaurantIDCanBeNegative() {
		restaurant.setRestaurantID(-1);
		assertEquals(-1, restaurant.getRestaurantID());
	}

	@Test
	/* @brief Restaurant name can be empty
	 * @tests Records present behavior where empty names are stored. */
	void restaurantNameCanBeEmpty() {
		restaurant.setRestaurantName("");
		assertEquals("", restaurant.getRestaurantName());
	}

	@Test
	/* @brief Restaurant cuisine can be null
	 * @tests Ensures null assignment is retained and safe to read back. */
	void restaurantCuisineCanBeNull() {
		restaurant.setCuisineType(null);
		assertNull(restaurant.getCuisineType());
	}

	@Test
	/* @brief City can be null
	 * @tests Documents nullable behavior for city field. */
	void cityCanBeNull() {
		restaurant.setCity(null);
		assertNull(restaurant.getCity());
	}

	@Test
	/* @brief Multiple menu items can coexist
	 * @tests Ensures the list accepts and retains multiple entries. */
	void multipleMenuItemsCanBeAdded() {
		ArrayList<MenuItem> menu = new ArrayList<>();
		MenuItem item1 = new MenuItem();
		setField(item1, "itemName", "Burger");
		MenuItem item2 = new MenuItem();
		setField(item2, "itemName", "Fries");
		menu.add(item1);
		menu.add(item2);

		restaurant.setMenu(menu);

		assertEquals(2, restaurant.getMenu().size());
	}

	@Test
	/* @brief Multiple keywords can coexist
	 * @tests Ensures the list accepts and retains multiple entries. */
	void multipleKeywordsCanBeAdded() {
		ArrayList<String> keywords = new ArrayList<>();
		keywords.add("casual");
		keywords.add("outdoor-seating");
		keywords.add("delivery");

		restaurant.setKeywords(keywords);

		assertEquals(3, restaurant.getKeywords().size());
		assertTrue(restaurant.getKeywords().contains("casual"));
	}

	@Test
	/* @brief Mutating returned menu list changes object state
	 * @tests Validates returned list is live (mutations reflect on the object). */
	void mutatingReturnedMenuListReflectsOnObject() {
		ArrayList<MenuItem> menu = new ArrayList<>();
		restaurant.setMenu(menu);
		assertSame(menu, restaurant.getMenu());

		MenuItem newItem = new MenuItem();
		setField(newItem, "itemName", "Salad");
		restaurant.getMenu().add(newItem);

		assertEquals(1, restaurant.getMenu().size());
	}

	@Test
	/* @brief Mutating returned keywords list changes object state
	 * @tests Validates returned list is live (mutations reflect on the object). */
	void mutatingReturnedKeywordsListReflectsOnObject() {
		ArrayList<String> keywords = new ArrayList<>();
		restaurant.setKeywords(keywords);
		assertSame(keywords, restaurant.getKeywords());

		restaurant.getKeywords().add("vegan-friendly");

		assertEquals(1, restaurant.getKeywords().size());
	}

	@Test
	/* @brief Restaurant name can be null
	 * @tests Documents nullable behavior for name field. */
	void restaurantNameCanBeNull() {
		restaurant.setRestaurantName(null);
		assertNull(restaurant.getRestaurantName());
	}



	private Restaurant allocateRestaurant() {

		return new Restaurant(0, "Temp", "Temp", "Temp");
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
