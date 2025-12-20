package FOS_CORE;

import FOS_DATA.*;

import java.util.ArrayList;

public class OrderService implements IOrderService {
    private final CustomerData DB = new CustomerData();

    @Override
    public void placeOrder(Customer customer,Address address, Order order, Restaurant restaurant) {
        if(DB.insertCustomerOrder(customer,address,order,restaurant)){
            customer.getOrders().add(0,order);
        }else{
            throw new IllegalStateException("Failed to place order");
        }
    }

    @Override
    public Order trackOrder(String orderID) {
        // TODO: Implementation
        return null;
    }

    @Override
    public ArrayList<Order> getOrderHistory(Customer customer) {
        // TODO: Implementation
        return null;
    }

    @Override
    public void rateOrder(Order order, int rating, String comment) {
        if(DB.rateCustomerOrder(order,rating,comment)){
            order.setRating(new Rating(rating,comment));
        }else {
            throw new IllegalStateException("Failed to rate order");
        }
    }

    private void validateOrder(Order order) {
        // TODO: Implementation
    }

    private double calculateOrderTotal(ArrayList<MenuItem> cart) {
        // TODO: Implementation
        return 0.0;
    }

    private void saveOrder(Order order) {
        // TODO: Implementation
    }
}