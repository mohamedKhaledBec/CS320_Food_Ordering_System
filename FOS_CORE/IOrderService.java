package FOS_CORE;
import java.util.ArrayList;

public interface IOrderService {
    Order placeOrder(Customer customer, ArrayList<CartItem> cart, Address address,Restaurant restaurant);
    Order trackOrder(int orderID, Customer customer);
    ArrayList<Order> getOrderHistory(Customer customer);
    void rateOrder(Order order, int rating, String comment);
}