package FOS_CORE;

import java.util.List;

public class OrderService implements IOrderService {

    @Override
    public Order placeOrder(Customer customer, Cart cart, Address address) {
        // TODO: Implementation
        return null;
    }

    @Override
    public Order trackOrder(String orderID) {
        // TODO: Implementation
        return null;
    }

    @Override
    public List<Order> getOrderHistory(Customer customer) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void rateOrder(Order order, int rating, String comment) {
        // TODO: Implementation
    }

    private void validateOrder(Order order) {
        // TODO: Implementation
    }

    private double calculateOrderTotal(Cart cart) {
        // TODO: Implementation
        return 0.0;
    }

    private void saveOrder(Order order) {
        // TODO: Implementation
    }
}