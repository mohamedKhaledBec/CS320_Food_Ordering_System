package FOS_CORE;

import java.sql.*;
import java.util.ArrayList;

public class Order {
    private int orderID;
    private OrderStatus status;
    private Date creationDate;
    private Rating rating;
    private String deliveryAddress;
    private ArrayList<CartItem> items;
    private String restaurantName;


    public Order(String deliveryAddress, ArrayList<CartItem> items, String restaurantName) {
        this.deliveryAddress = deliveryAddress;
        this.creationDate = new Date(System.currentTimeMillis());
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.restaurantName = restaurantName;
    }
    public Order(String deliveryAddress, ArrayList<CartItem> items, Date date, OrderStatus status, String restaurantName, int orderID, Rating rating) {
        this.deliveryAddress = deliveryAddress;
        this.creationDate = date;
        this.items = items;
        this.status = status;
        this.restaurantName = restaurantName;
        this.orderID = orderID;
        this.rating = rating;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Rating getRating() {
        return rating;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setDate(Date date) {
        this.creationDate = date;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}
