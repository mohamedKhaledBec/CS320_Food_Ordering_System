package FOS_CORE;

import FOS_DATA.CustomerService;
import FOS_DATA.ICustomerService;
import java.util.ArrayList;

public class OrderService implements IOrderService {
    final ICustomerService DB = new CustomerService();
    @Override
    public Order placeOrder(Customer customer, ArrayList<CartItem> cart, Address address,Restaurant restaurant) {
        Order order = new Order(address.getAddressLine(),cart,restaurant.getRestaurantName());
        DB.insertCustomerOrder(customer,address,order,restaurant);
        return order;
    }

    @Override
    public Order trackOrder(int orderID, Customer customer) {
        ArrayList<Order> orders=DB.fetchCustomerOrders(customer);
        for (Order order : orders) {
            if (order.getOrderID() == orderID)return order;
        }
        return null;
    }

    @Override
    public ArrayList<Order> getOrderHistory(Customer customer) {
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

    private double calculateOrderTotal(ArrayList<MenuItem> cart) {
        // TODO: Implementation
        return 0.0;
    }

    private void saveOrder(Order order) {
        // TODO: Implementation
    }
}