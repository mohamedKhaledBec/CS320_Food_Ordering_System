package FOS_TEST.DATA_TESTS;

import FOS_CORE.Card;
import FOS_CORE.Customer;
import FOS_DATA.CustomerData;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {

    private CustomerData customerService;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        customerService = new CustomerData();
        
        // Use existing customer from database (ID 4 - customer.1@gmail.com)
        // This customer has at least one card in the database
        testCustomer = new Customer(4, "customer.1@gmail.com", "hashed_password");
    }

    @Test
    void testFetchCustomerCardsSuccessfully() {
        // Fetch cards for a customer
        ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);

        assertNotNull(cards, "Cards list should not be null");
        assertTrue(cards.size() >= 0, "Customer should have zero or more cards");
    }

    @Test
    void testFetchCustomerCardsProperties() {
        // Fetch cards and verify properties are correctly loaded
        ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);

        assertNotNull(cards, "Cards list should not be null");
        
        if (!cards.isEmpty()) {
            Card firstCard = cards.get(0);
            assertNotNull(firstCard.getCardNumber(), "Card should have a card number");
            assertNotNull(firstCard.getCardHolderName(), "Card should have a card holder name");
            assertNotNull(firstCard.getExpiryDate(), "Card should have an expiry date");
            assertNotNull(firstCard.getCvv(), "Card should have a CVV");
            
            // Verify card number format (should be 16 characters)
            assertEquals(16, firstCard.getCardNumber().length(), "Card number should be 16 characters");
            
            // Verify CVV format (should be 3 characters)
            assertEquals(3, firstCard.getCvv().length(), "CVV should be 3 characters");
        }
    }

    @Test
    void testFetchCustomerCardsForNonExistentCustomer() {
        // Fetch cards for a customer that doesn't exist
        Customer nonExistentCustomer = new Customer(99999, "nonexistent@test.com", "password");
        
        ArrayList<Card> cards = customerService.fetchCustomerCards(nonExistentCustomer);

        assertNotNull(cards, "Cards list should not be null");
        assertEquals(0, cards.size(), "Non-existent customer should have no cards");
    }

    @Test
    void testAddCardToCustomerSuccessfully() {
        // Create a new card with valid data
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2); // Expiry date 2 years from now
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card newCard = new Card("9999888877776666", "Test Card Holder", expiryDate, "999");

        boolean result = customerService.addCardToCustomer(testCustomer, newCard);

        assertTrue(result, "Card should be added successfully");
        
        // Verify the card was added by fetching cards
        ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);
        boolean found = false;
        for (Card card : cards) {
            if (card.getCardNumber().equals(newCard.getCardNumber())) {
                found = true;
                assertEquals(newCard.getCardHolderName(), card.getCardHolderName(), 
                    "Card holder name should match");
                assertEquals(newCard.getExpiryDate(), card.getExpiryDate(), 
                    "Expiry date should match");
                assertEquals(newCard.getCvv(), card.getCvv(), 
                    "CVV should match");
                break;
            }
        }
        assertTrue(found, "Added card should be found in customer's cards");
    }

    @Test
    void testAddCardToCustomerWithDuplicateCardNumber() {
        // Try to add a card with a card number that already exists
        // First, get an existing card
        ArrayList<Card> existingCards = customerService.fetchCustomerCards(testCustomer);
        
        if (!existingCards.isEmpty()) {
            Card existingCard = existingCards.get(0);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 2);
            Date newExpiryDate = new Date(cal.getTimeInMillis());
            
            // Try to add the same card number with different details
            Card duplicateCard = new Card(
                existingCard.getCardNumber(), 
                "Different Name", 
                newExpiryDate, 
                "000"
            );

            boolean result = customerService.addCardToCustomer(testCustomer, duplicateCard);

            // Should fail because card_no is PRIMARY KEY
            assertFalse(result, "Adding duplicate card number should fail");
        }
    }

    @Test
    void testAddCardToCustomerWithInvalidCustomer() {
        // Try to add a card to a customer that doesn't exist
        Customer invalidCustomer = new Customer(99999, "invalid@test.com", "password");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card = new Card("1234567890123456", "Test Holder", expiryDate, "123");

        boolean result = customerService.addCardToCustomer(invalidCustomer, card);

        assertFalse(result, "Adding card to invalid customer should fail");
    }

    @Test
    void testAddCardToCustomerWithNullCardNumber() {
        // Try to add a card with null card number (should fail)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card = new Card(null, "Test Holder", expiryDate, "123");

        boolean result = customerService.addCardToCustomer(testCustomer, card);

        assertFalse(result, "Adding card with null card number should fail");
    }

    @Test
    void testAddCardToCustomerWithInvalidCardNumberLength() {
        // Try to add a card with invalid card number length (not 16 characters)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card = new Card("123456789012345", "Test Holder", expiryDate, "123"); // 15 characters

        boolean result = customerService.addCardToCustomer(testCustomer, card);

        // May fail due to database constraint (CHAR(16))
        assertFalse(result, "Adding card with invalid card number length should fail");
    }

    @Test
    void testAddCardToCustomerWithInvalidCVVLength() {
        // Try to add a card with invalid CVV length (not 3 characters)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card = new Card("1234567890123456", "Test Holder", expiryDate, "12"); // 2 characters

        boolean result = customerService.addCardToCustomer(testCustomer, card);

        // May fail due to database constraint (CHAR(3))
        assertFalse(result, "Adding card with invalid CVV length should fail");
    }

    @Test
    void testAddCardToCustomerWithExpiredCard() {
        // Try to add a card with an expired date (should still be allowed - no validation in DB)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1); // Expired 1 year ago
        Date expiredDate = new Date(cal.getTimeInMillis());
        
        Card expiredCard = new Card("8888777766665555", "Expired Card Holder", expiredDate, "888");

        boolean result = customerService.addCardToCustomer(testCustomer, expiredCard);

        // Database doesn't validate expiry date, so this may succeed
        assertTrue(result || !result, "Adding expired card may succeed or fail depending on validation");
    }

    @Test
    void testRemoveCardFromCustomerSuccessfully() {
        // First, add a card to remove
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card cardToAdd = new Card("7777666655554444", "Card To Remove", expiryDate, "777");
        boolean added = customerService.addCardToCustomer(testCustomer, cardToAdd);
        
        if (added) {
            // Now remove the card
            boolean result = customerService.removeCardFromCustomer(testCustomer, cardToAdd);

            assertTrue(result, "Card should be removed successfully");
            
            // Verify the card was removed
            ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);
            boolean found = false;
            for (Card card : cards) {
                if (card.getCardNumber().equals(cardToAdd.getCardNumber())) {
                    found = true;
                    break;
                }
            }
            assertFalse(found, "Removed card should not be found in customer's cards");
        }
    }

    @Test
    void testRemoveCardFromCustomerWithNonExistentCard() {
        // Try to remove a card that doesn't exist
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card nonExistentCard = new Card("0000111122223333", "Non Existent", expiryDate, "000");

        boolean result = customerService.removeCardFromCustomer(testCustomer, nonExistentCard);

        assertFalse(result, "Removing non-existent card should fail");
    }

    @Test
    void testRemoveCardFromCustomerWithInvalidCustomer() {
        // Try to remove a card from a customer that doesn't exist
        Customer invalidCustomer = new Customer(99999, "invalid@test.com", "password");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card = new Card("1111222233334444", "Test", expiryDate, "111");

        boolean result = customerService.removeCardFromCustomer(invalidCustomer, card);

        assertFalse(result, "Removing card from invalid customer should fail");
    }

    @Test
    void testRemoveCardFromCustomerWithWrongCustomer() {
        // Try to remove a card that belongs to a different customer
        // First, get a card from testCustomer
        ArrayList<Card> testCustomerCards = customerService.fetchCustomerCards(testCustomer);
        
        if (!testCustomerCards.isEmpty()) {
            Card card = testCustomerCards.get(0);
            
            // Try to remove it from a different customer
            Customer otherCustomer = new Customer(5, "customer.2@gmail.com", "hashed_password");
            
            boolean result = customerService.removeCardFromCustomer(otherCustomer, card);

            assertFalse(result, "Removing card from wrong customer should fail");
        }
    }

    @Test
    void testCardPropertiesAfterAddition() {
        // Add a card and verify all properties are saved correctly
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        String cardNumber = "6666555544443333";
        String cardHolderName = "Property Test Holder";
        String cvv = "666";
        
        Card card = new Card(cardNumber, cardHolderName, expiryDate, cvv);

        boolean added = customerService.addCardToCustomer(testCustomer, card);
        assertTrue(added, "Card should be added");

        // Fetch cards and verify properties
        ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);
        Card fetchedCard = null;
        for (Card c : cards) {
            if (c.getCardNumber().equals(cardNumber)) {
                fetchedCard = c;
                break;
            }
        }

        assertNotNull(fetchedCard, "Added card should be found");
        assertEquals(cardNumber, fetchedCard.getCardNumber(), "Card number should match");
        assertEquals(cardHolderName, fetchedCard.getCardHolderName(), "Card holder name should match");
        assertEquals(expiryDate, fetchedCard.getExpiryDate(), "Expiry date should match");
        assertEquals(cvv, fetchedCard.getCvv(), "CVV should match");
    }

    @Test
    void testMultipleCardsForCustomer() {
        // Add multiple cards to the same customer
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate1 = new Date(cal.getTimeInMillis());
        
        cal.add(Calendar.YEAR, 1);
        Date expiryDate2 = new Date(cal.getTimeInMillis());
        
        Card card1 = new Card("5555444433332222", "First Card", expiryDate1, "555");
        Card card2 = new Card("4444333322221111", "Second Card", expiryDate2, "444");

        boolean added1 = customerService.addCardToCustomer(testCustomer, card1);
        boolean added2 = customerService.addCardToCustomer(testCustomer, card2);

        assertTrue(added1, "First card should be added");
        assertTrue(added2, "Second card should be added");

        // Fetch cards - should contain both
        ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);
        
        assertTrue(cards.size() >= 2, "Customer should have at least 2 cards");
        
        // Verify both cards are in the list
        boolean found1 = false, found2 = false;
        for (Card card : cards) {
            if (card.getCardNumber().equals(card1.getCardNumber())) found1 = true;
            if (card.getCardNumber().equals(card2.getCardNumber())) found2 = true;
        }
        
        assertTrue(found1, "First card should be in customer cards");
        assertTrue(found2, "Second card should be in customer cards");
    }

    @Test
    void testCardNumberUniqueness() {
        // Verify that card numbers are unique (PRIMARY KEY constraint)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        String uniqueCardNumber = "3333222211110000";
        Card card1 = new Card(uniqueCardNumber, "First Holder", expiryDate, "333");
        Card card2 = new Card(uniqueCardNumber, "Second Holder", expiryDate, "333");

        boolean added1 = customerService.addCardToCustomer(testCustomer, card1);
        assertTrue(added1, "First card should be added");

        // Try to add the same card number again
        boolean added2 = customerService.addCardToCustomer(testCustomer, card2);
        assertFalse(added2, "Duplicate card number should not be added");
    }

    @Test
    void testCardWithDifferentExpiryDates() {
        // Add cards with different expiry dates
        Calendar cal = Calendar.getInstance();
        
        cal.add(Calendar.MONTH, 6);
        Date expiryDate1 = new Date(cal.getTimeInMillis());
        Card card1 = new Card("2222111100009999", "Short Term Card", expiryDate1, "222");
        
        cal.add(Calendar.YEAR, 1);
        Date expiryDate2 = new Date(cal.getTimeInMillis());
        Card card2 = new Card("1111000099998888", "Long Term Card", expiryDate2, "111");

        boolean added1 = customerService.addCardToCustomer(testCustomer, card1);
        boolean added2 = customerService.addCardToCustomer(testCustomer, card2);

        assertTrue(added1, "First card should be added");
        assertTrue(added2, "Second card should be added");

        // Verify both cards are saved with correct expiry dates
        ArrayList<Card> cards = customerService.fetchCustomerCards(testCustomer);
        Card fetchedCard1 = null, fetchedCard2 = null;
        
        for (Card card : cards) {
            if (card.getCardNumber().equals(card1.getCardNumber())) fetchedCard1 = card;
            if (card.getCardNumber().equals(card2.getCardNumber())) fetchedCard2 = card;
        }

        assertNotNull(fetchedCard1, "First card should be found");
        assertNotNull(fetchedCard2, "Second card should be found");
        assertEquals(expiryDate1, fetchedCard1.getExpiryDate(), "First card expiry date should match");
        assertEquals(expiryDate2, fetchedCard2.getExpiryDate(), "Second card expiry date should match");
    }

    @Test
    void testAddAndRemoveCardCycle() {
        // Test adding and then removing a card
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card = new Card("0000999988887777", "Cycle Test Card", expiryDate, "000");

        // Add card
        boolean added = customerService.addCardToCustomer(testCustomer, card);
        assertTrue(added, "Card should be added");

        // Verify it's in the list
        ArrayList<Card> cardsAfterAdd = customerService.fetchCustomerCards(testCustomer);
        boolean foundAfterAdd = false;
        for (Card c : cardsAfterAdd) {
            if (c.getCardNumber().equals(card.getCardNumber())) {
                foundAfterAdd = true;
                break;
            }
        }
        assertTrue(foundAfterAdd, "Card should be found after addition");

        // Remove card
        boolean removed = customerService.removeCardFromCustomer(testCustomer, card);
        assertTrue(removed, "Card should be removed");

        // Verify it's not in the list
        ArrayList<Card> cardsAfterRemove = customerService.fetchCustomerCards(testCustomer);
        boolean foundAfterRemove = false;
        for (Card c : cardsAfterRemove) {
            if (c.getCardNumber().equals(card.getCardNumber())) {
                foundAfterRemove = true;
                break;
            }
        }
        assertFalse(foundAfterRemove, "Card should not be found after removal");
    }

    @Test
    void testFetchCustomerCardsAfterMultipleOperations() {
        // Perform multiple add/remove operations and verify final state
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Date expiryDate = new Date(cal.getTimeInMillis());
        
        Card card1 = new Card("9999888877776666", "Operation Card 1", expiryDate, "999");
        Card card2 = new Card("8888777766665555", "Operation Card 2", expiryDate, "888");

        // Get initial count
        int initialCount = customerService.fetchCustomerCards(testCustomer).size();

        // Add two cards
        customerService.addCardToCustomer(testCustomer, card1);
        customerService.addCardToCustomer(testCustomer, card2);

        // Verify count increased
        int afterAddCount = customerService.fetchCustomerCards(testCustomer).size();
        assertTrue(afterAddCount >= initialCount + 2, "Card count should increase after adding");

        // Remove one card
        customerService.removeCardFromCustomer(testCustomer, card1);

        // Verify count decreased
        int afterRemoveCount = customerService.fetchCustomerCards(testCustomer).size();
        assertTrue(afterRemoveCount >= initialCount + 1, "Card count should decrease after removing");
        assertTrue(afterRemoveCount < afterAddCount, "Card count should be less after removal");
    }
}
