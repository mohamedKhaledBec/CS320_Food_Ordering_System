package FOS_CORE;

public interface IPaymentService {
    TransactionRecord processPayment(PaymentDetails cardDetails, double amount);
}