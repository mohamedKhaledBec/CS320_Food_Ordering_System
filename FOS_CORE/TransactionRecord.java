package FOS_CORE;

import java.time.LocalDateTime;

public class TransactionRecord {

    private final int transactionID;
    private final int customerID;
    private final double amount;
    private final PaymentStatus status;
    private final String message;
    private final String maskedCardNumber;
    private final LocalDateTime createdAt;

    public TransactionRecord(
            int transactionID,
            int customerID,
            double amount,
            PaymentStatus status,
            String message,
            String maskedCardNumber,
            LocalDateTime createdAt) {
        this.transactionID = transactionID;
        this.customerID = customerID;
        this.amount = amount;
        this.status = status;
        this.message = message;
        this.maskedCardNumber = maskedCardNumber;
        this.createdAt = createdAt;
    }

    public int getTransactionID() { return transactionID; }
    public int getCustomerID() { return customerID; }
    public double getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getMaskedCardNumber() { return maskedCardNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
