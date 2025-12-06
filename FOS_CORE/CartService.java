package FOS_CORE;

import java.util.ArrayList;

public class CartService implements ICartService {

    @Override
    public void addToCart(Customer customer, MenuItem item, int quantity, double price) {
        CartItem cartItem = new CartItem(item, quantity, price);
        ArrayList<CartItem> cart = customer.getCart();
        boolean itemExistsInCart = false;
        for(CartItem cartItemTemp: cart){
            if(cartItemTemp.getItem().getMenuItemID() == item.getMenuItemID()){
                int newQuantity = cartItemTemp.getQuantity() + quantity;
                if(newQuantity > 10) {
                    cartItemTemp.setQuantity(10);
                    throw new IllegalArgumentException("Cannot add more than 10 units of the same item to the cart.\n Current quantity is set to 10.");
                }
                cartItemTemp.setQuantity(newQuantity);
                itemExistsInCart = true;
                break;
            }
        }
        if(!itemExistsInCart) {
            cart.add(cartItem);
        }
    }

    public boolean removeFromCart(Customer customer, MenuItem item) {
        ArrayList<CartItem> cart = customer.getCart();
        for (CartItem cartItem : cart) {
            if (cartItem.getItem().getMenuItemID() == item.getMenuItemID()) {
                cart.remove(cartItem);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateCartItemQuantity(Customer cart, MenuItem item, int newQuantity) {
        ArrayList<CartItem> customerCart = cart.getCart();
        for (CartItem cartItem : customerCart) {
            if (cartItem.getItem().getMenuItemID() == item.getMenuItemID()) {
                cartItem.setQuantity(newQuantity);
                return;
            }
        }
    }


    private double calculateItemTotal(MenuItem item, int quantity) {
        // TODO: Implementation
        return 0.0;
    }

    private void validateCart(ArrayList<MenuItem> cart) {
        // TODO: Implementation
    }
}