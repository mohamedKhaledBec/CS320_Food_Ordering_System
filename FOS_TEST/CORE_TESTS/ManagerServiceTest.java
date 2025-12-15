/**
 * This test class is written by Mohamed Khaled Becetti
 */

package FOS_TEST.CORE_TESTS;

import FOS_CORE.Discount;
import FOS_CORE.Manager;
import FOS_CORE.ManagerService;
import FOS_CORE.MenuItem;
import FOS_CORE.Order;
import FOS_CORE.OrderStatus;
import FOS_CORE.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the CORE ManagerService (data layer stubbed)
public class ManagerServiceTest {

	private ManagerService service;
	private Manager manager;
	private DbStub dbStub;// This object simulates the Database (I used it since i Don't want to deal with database related errors in my device)

	@BeforeEach
	void setUp() {
		service = new ManagerService();
		manager = new Manager();
		dbStub = new DbStub();
		injectDb(service, dbStub);
	}

	@Test
	/* @brief Returns restaurants fetched from DB stub
	 * @tests Verifies getManagerRestaurants() delegates to data layer and returns its result. */
	void getManagerRestaurantsReturnsDbResult() {
		Restaurant r = newRestaurant(1, "R1", "Italian", "City");
		dbStub.restaurantsResult.add(r);

		ArrayList<Restaurant> result = service.getManagerRestaurants(manager);

		assertSame(dbStub.restaurantsResult, result);
	}

	@Test
	/* @brief Rejects null manager inputs
	 * @tests Ensures validation throws IllegalArgumentException for null manager. */
	void getManagerRestaurantsRejectsNullManager() {
		assertThrows(IllegalArgumentException.class, () -> service.getManagerRestaurants(null));
	}

	@Test
	/* @brief Saves restaurant info successfully
	 * @tests Confirms updateRestaurantInfo() calls save twice (direct + internal). */
	void updateRestaurantInfoValidSavesTwice() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");

		service.updateRestaurantInfo(r);

		assertEquals(2, dbStub.saveRestaurantInfoCalls); // called directly + via saveRestaurantChanges
	}

	@Test
	/* @brief Null restaurant is invalid
	 * @tests Validates null input throws IllegalArgumentException. */
	void updateRestaurantInfoRejectsNullRestaurant() {
		assertThrows(IllegalArgumentException.class, () -> service.updateRestaurantInfo(null));
	}

	@Test
	/* @brief Empty restaurant name is rejected
	 * @tests Ensures trimmed-empty names cause IllegalArgumentException. */
	void updateRestaurantInfoRejectsEmptyName() {
		Restaurant r = newRestaurant(1, " ", "Cuisine", "City");
		assertThrows(IllegalArgumentException.class, () -> service.updateRestaurantInfo(r));
	}

	@Test
	/* @brief Propagates DB failure on save
	 * @tests Confirms updateRestaurantInfo() throws when DB returns false. */
	void updateRestaurantInfoFailsWhenDbReturnsFalse() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		dbStub.saveRestaurantInfoResult = false;
		assertThrows(RuntimeException.class, () -> service.updateRestaurantInfo(r));
	}

	@Test
	/* @brief Add menu item delegates to DB
	 * @tests Verifies addMenuItem() passes to data layer and succeeds. */
	void addMenuItemValidDelegatesToDb() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem item = newMenuItem("Burger", "desc", 9.5);

		assertDoesNotThrow(() -> service.addMenuItem(r, item));
		assertEquals(1, dbStub.addMenuItemCalls);
	}

	@Test
	/* @brief Add menu item rejects nulls
	 * @tests Ensures null restaurant or item throws IllegalArgumentException. */
	void addMenuItemRejectsNulls() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		assertThrows(IllegalArgumentException.class, () -> service.addMenuItem(null, item));
		assertThrows(IllegalArgumentException.class, () -> service.addMenuItem(r, null));
	}

	@Test
	/* @brief Add menu item validates fields
	 * @tests Confirms negative price or blank name triggers IllegalArgumentException. */
	void addMenuItemRejectsInvalidItem() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem badPrice = newMenuItem("Burger", "desc", -1);
		MenuItem badName = newMenuItem(" ", "desc", 1);
		assertThrows(IllegalArgumentException.class, () -> service.addMenuItem(r, badPrice));
		assertThrows(IllegalArgumentException.class, () -> service.addMenuItem(r, badName));
	}

	@Test
	/* @brief Add menu item failure bubbles up
	 * @tests Verifies RuntimeException is thrown when DB add fails. */
	void addMenuItemPropagatesDbFailure() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		dbStub.addMenuItemResult = false;
		assertThrows(RuntimeException.class, () -> service.addMenuItem(r, item));
	}

	@Test
	/* @brief Edit menu item delegates to DB
	 * @tests Confirms editMenuItem() invokes update in data layer. */
	void editMenuItemValidDelegates() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem item = newMenuItem("Burger", "desc", 9.5);

		assertDoesNotThrow(() -> service.editMenuItem(r, item));
		assertEquals(1, dbStub.updateMenuItemCalls);
	}

	@Test
	/* @brief Edit menu item failure bubbles up
	 * @tests Ensures RuntimeException when DB update fails. */
	void editMenuItemPropagatesDbFailure() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		dbStub.updateMenuItemResult = false;
		assertThrows(RuntimeException.class, () -> service.editMenuItem(r, item));
	}

	@Test
	/* @brief Remove menu item delegates to DB
	 * @tests Verifies removeMenuItem() invokes data layer removal. */
	void removeMenuItemValidDelegates() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem item = newMenuItem("Burger", "desc", 9.5);

		assertDoesNotThrow(() -> service.removeMenuItem(r, item));
		assertEquals(1, dbStub.removeMenuItemCalls);
	}

	@Test
	/* @brief Remove menu item rejects nulls
	 * @tests Ensures null restaurant or item throws IllegalArgumentException. */
	void removeMenuItemRejectsNulls() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		assertThrows(IllegalArgumentException.class, () -> service.removeMenuItem(null, item));
		assertThrows(IllegalArgumentException.class, () -> service.removeMenuItem(r, null));
	}

	@Test
	/* @brief Remove menu item failure bubbles up
	 * @tests Confirms RuntimeException when DB remove fails. */
	void removeMenuItemPropagatesDbFailure() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		dbStub.removeMenuItemResult = false;
		assertThrows(RuntimeException.class, () -> service.removeMenuItem(r, item));
	}

	@Test
	/* @brief Order status updates with valid value
	 * @tests Ensures string status maps to enum and sets on order. */
	void updateOrderStatusSetsValidEnum() {
		Order order = new Order("addr", new ArrayList<>(), "rest", "phone", "card");
		service.updateOrderStatus(order, "delivered");
		assertEquals(OrderStatus.DELIVERED, order.getStatus());
	}

	@Test
	/* @brief Order status is case-insensitive
	 * @tests Mixed-case input should still map to the correct enum value. */
	void updateOrderStatusAcceptsMixedCase() {
		Order order = new Order("addr", new ArrayList<>(), "rest", "phone", "card");
		service.updateOrderStatus(order, "PrEpArInG");
		assertEquals(OrderStatus.PREPARING, order.getStatus());
	}

	@Test
	/* @brief Invalid order status rejected
	 * @tests Validates IllegalArgumentException for unknown status string. */
	void updateOrderStatusRejectsInvalidValue() {
		Order order = new Order("addr", new ArrayList<>(), "rest", "phone", "card");
		assertThrows(IllegalArgumentException.class, () -> service.updateOrderStatus(order, "unknown"));
	}

	@Test
	/* @brief Null order or status rejected
	 * @tests Ensures IllegalArgumentException on null order or null status. */
	void updateOrderStatusRejectsNulls() {
		assertThrows(IllegalArgumentException.class, () -> service.updateOrderStatus(null, "pending"));
		Order order = new Order("addr", new ArrayList<>(), "rest", "phone", "card");
		assertThrows(IllegalArgumentException.class, () -> service.updateOrderStatus(order, null));
	}

	@Test
	/* @brief Discount creation validates inputs
	 * @tests Ensures manager/item/percentage/date range are validated. */
	void createDiscountRejectsInvalidInputs() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Timestamp start = Timestamp.valueOf("2025-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2025-01-02 00:00:00");

		assertThrows(IllegalArgumentException.class, () -> service.createDiscount(null, item, "d", 10, start, end));
		assertThrows(IllegalArgumentException.class, () -> service.createDiscount(manager, null, "d", 10, start, end));
		assertThrows(IllegalArgumentException.class, () -> service.createDiscount(manager, item, "d", -1, start, end));
		assertThrows(IllegalArgumentException.class, () -> service.createDiscount(manager, item, "d", 101, start, end));
		assertThrows(IllegalArgumentException.class, () -> service.createDiscount(manager, item, "d", 10, end, start));
	}

	@Test
	/* @brief Detects overlapping discount periods
	 * @tests Verifies new discount overlaps cause IllegalArgumentException. */
	void createDiscountDetectsOverlap() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Timestamp existingStart = Timestamp.valueOf("2025-01-01 00:00:00");
		Timestamp existingEnd = Timestamp.valueOf("2025-01-10 00:00:00");
		item.getDiscounts().add(new Discount(1, "d1", "", 10, existingStart, existingEnd));

		Timestamp newStart = Timestamp.valueOf("2025-01-05 00:00:00");
		Timestamp newEnd = Timestamp.valueOf("2025-01-06 00:00:00");

		assertThrows(IllegalArgumentException.class,
				() -> service.createDiscount(manager, item, "new", 5, newStart, newEnd));
	}

	@Test
	/* @brief Boundary equality counts as overlap
	 * @tests New discount starting at existing end (or ending at existing start) is rejected. */
	void createDiscountBoundaryEqualityIsOverlap() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Timestamp existingStart = Timestamp.valueOf("2025-01-01 00:00:00");
		Timestamp existingEnd = Timestamp.valueOf("2025-01-10 00:00:00");
		item.getDiscounts().add(new Discount(1, "d1", "", 10, existingStart, existingEnd));

		Timestamp newStart = existingEnd; // equal boundary
		Timestamp newEnd = Timestamp.valueOf("2025-01-12 00:00:00");
		assertThrows(IllegalArgumentException.class,
				() -> service.createDiscount(manager, item, "boundary", 5, newStart, newEnd));

		Timestamp newStart2 = Timestamp.valueOf("2024-12-20 00:00:00");
		Timestamp newEnd2 = existingStart; // equal boundary
		assertThrows(IllegalArgumentException.class,
				() -> service.createDiscount(manager, item, "boundary2", 5, newStart2, newEnd2));
	}

	@Test
	/* @brief Exact same discount window is rejected
	 * @tests New discount matching existing start/end triggers overlap rejection. */
	void createDiscountExactWindowIsOverlap() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Timestamp existingStart = Timestamp.valueOf("2025-01-01 00:00:00");
		Timestamp existingEnd = Timestamp.valueOf("2025-01-10 00:00:00");
		item.getDiscounts().add(new Discount(1, "d1", "", 10, existingStart, existingEnd));

		assertThrows(IllegalArgumentException.class,
				() -> service.createDiscount(manager, item, "dup", 5, existingStart, existingEnd));
	}

	@Test
	/* @brief Successful discount creation appends to item
	 * @tests Confirms item gains discount and DB layer is invoked. */
	void createDiscountAppendsOnSuccess() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Timestamp start = Timestamp.valueOf("2025-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2025-01-02 00:00:00");

		service.createDiscount(manager, item, "new", 5, start, end);

		assertEquals(1, item.getDiscounts().size());
		assertEquals(1, dbStub.createDiscountCalls);
	}

	@Test
	/* @brief Discount DB failure propagates
	 * @tests Ensures RuntimeException when DB createDiscount returns false. */
	void createDiscountPropagatesDbFailure() {
		MenuItem item = newMenuItem("Burger", "desc", 9.5);
		Timestamp start = Timestamp.valueOf("2025-01-01 00:00:00");
		Timestamp end = Timestamp.valueOf("2025-01-02 00:00:00");
		dbStub.createDiscountResult = false;

		assertThrows(RuntimeException.class, () -> service.createDiscount(manager, item, "new", 5, start, end));
	}

	@Test
	/* @brief Monthly report returns DB content
	 * @tests Verifies generateMonthlyReport delegates and returns stubbed string. */
	void generateMonthlyReportReturnsDbResult() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		Date date = Date.valueOf("2025-02-01");
		dbStub.monthlyReport = "REPORT";

		String report = service.generateMonthlyReport(manager, r, date);

		assertEquals("REPORT", report);
		assertEquals(1, dbStub.generateMonthlyReportCalls);
		assertEquals(date, dbStub.lastPassedDate);
	}

	@Test
	/* @brief Monthly report rejects null inputs
	 * @tests Validates manager/restaurant/date cannot be null. */
	void generateMonthlyReportRejectsNulls() {
		Restaurant r = newRestaurant(1, "Valid", "Cuisine", "City");
		Date date = Date.valueOf("2025-02-01");
		assertThrows(IllegalArgumentException.class, () -> service.generateMonthlyReport(null, r, date));
		assertThrows(IllegalArgumentException.class, () -> service.generateMonthlyReport(manager, null, date));
		assertThrows(IllegalArgumentException.class, () -> service.generateMonthlyReport(manager, r, null));
	}

	// ---------- Helpers ----------

	private MenuItem newMenuItem(String name, String desc, double price) {
		MenuItem item = new MenuItem();
		setField(item, "itemName", name);
		setField(item, "description", desc);
		setField(item, "price", price);
		setField(item, "discounts", new ArrayList<Discount>());
		return item;
	}

	private Restaurant newRestaurant(int id, String name, String cuisine, String city) {
		Restaurant r = allocateRestaurant();
		setField(r, "restaurantID", id);
		setField(r, "restaurantName", name);
		setField(r, "cuisineType", cuisine);
		setField(r, "city", city);
		setField(r, "Keywords", new ArrayList<String>());
		setField(r, "menu", new ArrayList<>());
		return r;
	}

	private Restaurant allocateRestaurant() {
		try {
			Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
			return (Restaurant) unsafe.allocateInstance(Restaurant.class);
		} catch (Exception e) {
			fail("Unable to allocate Restaurant without constructor: " + e.getMessage());
			return null;
		}
	}

	private void injectDb(ManagerService target, FOS_DATA.ManagerService replacement) {
		setFinalField(target, "DB", replacement);
	}

	private void setField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception e) {
			fail("Failed to set field '" + fieldName + "': " + e.getMessage());
		}
	}

	private void setFinalField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(target, value);
		} catch (Exception e) {
			fail("Failed to inject field '" + fieldName + "': " + e.getMessage());
		}
	}

	// ---------- Stub ----------

	private static class DbStub extends FOS_DATA.ManagerService {
		boolean saveRestaurantInfoResult = true;
		boolean addMenuItemResult = true;
		boolean updateMenuItemResult = true;
		boolean removeMenuItemResult = true;
		boolean createDiscountResult = true;
		String monthlyReport = "";

		ArrayList<Restaurant> restaurantsResult = new ArrayList<>();

		int saveRestaurantInfoCalls = 0;
		int addMenuItemCalls = 0;
		int updateMenuItemCalls = 0;
		int removeMenuItemCalls = 0;
		int createDiscountCalls = 0;
		int generateMonthlyReportCalls = 0;
		Date lastPassedDate = null;

		@Override
		public ArrayList<Restaurant> fetchManagerRestaurants(Manager manager) {
			return restaurantsResult;
		}

		@Override
		public boolean saveRestaurantInfo(Restaurant restaurant) {
			saveRestaurantInfoCalls++;
			return saveRestaurantInfoResult;
		}

		@Override
		public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant) {
			addMenuItemCalls++;
			return addMenuItemResult;
		}

		@Override
		public boolean updateMenuItem(MenuItem menuItem) {
			updateMenuItemCalls++;
			return updateMenuItemResult;
		}

		@Override
		public boolean removeMenuItem(MenuItem menuItem) {
			removeMenuItemCalls++;
			return removeMenuItemResult;
		}

		@Override
		public boolean createDiscount(Discount discount, MenuItem menuItem) {
			createDiscountCalls++;
			return createDiscountResult;
		}

		@Override
		public String generateMonthlyReport(Restaurant restaurant, Date date) {
			generateMonthlyReportCalls++;
			lastPassedDate = date;
			return monthlyReport;
		}
	}
}
