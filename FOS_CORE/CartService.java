package FOS_CORE;

import java.util.ArrayList;

public class CartService implements ICartService {

    @Override
    public void addToCart(Customer customer, MenuItem item, int quantity) {
        // TODO: Implemntation
    }

    @Override
    public void updateCartItem(Customer cart, MenuItem item, int newQuantity) {
        // TODO: Implemntation
    }


    private double calculateItemTotal(MenuItem item, int quantity) {
        // TODO: Implementation
        return 0.0;
    }

    private void validateCart(ArrayList<MenuItem> cart) {
        // TODO: Implementation
    }
}