package FOS_CORE;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

public class PaymentDetails implements IPaymentDetails {

    private final Customer customer;
    private final Card card;
    private final double amount;

    public PaymentDetails(Customer customer, Card card, double amount) {
        this.customer = Objects.requireNonNull(customer, "customer must not be null");
        this.card = Objects.requireNonNull(card, "card must not be null");
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Card getCard() {
        return card;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void validate() {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate expiry = card.getExpiryDate().toLocalDate();
        if (expiry.isBefore(today)) {
            throw new IllegalArgumentException("Card has expired.");
        }
    }
}

