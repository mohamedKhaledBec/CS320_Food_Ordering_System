package FOS_CORE;

import java.awt.*;

public class MenuItem {

    private int menuItemID;
    private String itemName;
    private String description;
    private double price;

    public MenuItem(){ }

    public MenuItem(int menuItemID, String itemName, String description, double price){
        this.menuItemID = menuItemID;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
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
}
