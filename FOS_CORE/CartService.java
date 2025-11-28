package FOS_CORE;

public class CartService implements ICartService {

    @Override
    public void addToCart(Cart cart, MenuItem item, int quantity) {
        // TODO: Implementation
    }

    @Override
    public void updateCartItem(Cart cart, MenuItem item, int newQuantity) {
        // TODO: Implementation
    }

    @Override
    public Cart getCart(Customer customer) {
        // TODO: Implementation
        return null;
    }

    private double calculateItemTotal(MenuItem item, int quantity) {
        // TODO: Implementation
        return 0.0;
    }

    private void validateCart(Cart cart) {
        // TODO: Implementation
    }
}