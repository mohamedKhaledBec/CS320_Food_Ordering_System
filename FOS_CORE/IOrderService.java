package FOS_CORE;
import java.util.ArrayList;

public interface IOrderService {
    Order placeOrder(Customer customer, ArrayList<CartItem> cart, Address address);
    Order trackOrder(String orderID);
    ArrayList<Order> getOrderHistory(Customer customer);
    void rateOrder(Order order, int rating, String comment);
}