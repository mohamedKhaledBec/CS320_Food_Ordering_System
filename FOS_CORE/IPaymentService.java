package FOS_CORE;

public interface IPaymentService {
    TransactionRecord processPayment(IPaymentDetails cardDetails, double amount);
}