package FOS_CORE;

import java.awt.*;
import java.util.ArrayList;

public class MenuItem {

    private int menuItemID;
    private String itemName;
    private String description;
    private double price;
    private ArrayList<Discount> discounts;

    public MenuItem(){ }

    public MenuItem(int menuItemID, String itemName, String description, double price, ArrayList<Discount> discounts){
        this.menuItemID = menuItemID;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.discounts = discounts;
    }

    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }
}
