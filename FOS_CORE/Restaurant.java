package FOS_CORE;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Restaurant {

    private int restaurantID;
    private String restaurantName;
    private Manager managerID;
    private String cuisineType;
    private String city;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    public Restaurant(){ }

    public Restaurant(int restaurantID, String restaurantName, String cuisineType, String city){
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.cuisineType = cuisineType;
        this.city = city;
    }

    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Manager getManagerID() {
        return managerID;
    }

    public void setManagerID(Manager managerID) {
        this.managerID = managerID;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
