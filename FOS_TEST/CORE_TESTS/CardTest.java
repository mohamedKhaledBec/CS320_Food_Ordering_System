package FOS_TEST.CORE_TESTS;

import FOS_CORE.Card;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

// Being done by Umair Ahmad
public class CardTest {
    @Test
    void testCreateCard() {
        String number = "4111111111111111";
        String name = "Test User";
        Date expiry = Date.valueOf("2030-01-01");
        String cvv = "123";

        Card card = new Card(number, name, expiry, cvv);

        assertNotNull(card);
    }

    @Test
    void testGetNumberAndName() {
        String number = "5555444433331111";
        String name = "Another User";
        Date expiry = Date.valueOf("2031-06-01");
        String cvv = "456";

        Card card = new Card(number, name, expiry, cvv);

        assertEquals(number, card.getCardNumber());
        assertEquals(name, card.getCardHolderName());
    }
}
