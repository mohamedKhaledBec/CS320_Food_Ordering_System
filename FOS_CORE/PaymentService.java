package FOS_CORE;

public class PaymentService implements IPaymentService {

    @Override
    public TransactionRecord processPayment(IPaymentDetails cardDetails, double amount) {
        // TODO: Implementation
        return null;
    }

    private void validatePaymentDetails(IPaymentDetails cardDetails) {
        // TODO: Implementation
    }

    private void saveTransaction(TransactionRecord record) {
        // TODO: Implementation
    }
}