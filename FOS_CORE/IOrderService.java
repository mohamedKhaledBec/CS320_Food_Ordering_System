package FOS_CORE;
import java.util.List;

public interface IOrderService {
    Order placeOrder(Customer customer, Cart cart, Address address);
    Order trackOrder(String orderID);
    List<Order> getOrderHistory(Customer customer);
    void rateOrder(Order order, int rating, String comment);
}