package FOS_TEST.CORE_TESTS;


import FOS_CORE.Address;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Being done by Umair Ahmad
public class AddressTest {
    @Test
    void testCreateAddress() {
        Address address = new Address(-1, "Test Street 1", "Test City", "TS", "00000");
        assertNotNull(address);
    }
    @Test
    void testGetCity() {
        String city = "Test City";
        Address address = new Address(-1, "Test Street 1", city, "TS", "00000");
        assertEquals(city, address.getCity());
    }
    @Test
    void testToStringContainsStreetAndCity() {
        String street = "Test Street 1";
        String city = "Test City";
        Address address = new Address(-1, street, city, "TS", "00000");

        String text = address.toString();

        assertNotNull(text);
        assertTrue(text.contains(street));
        assertTrue(text.contains(city));
    }
}
