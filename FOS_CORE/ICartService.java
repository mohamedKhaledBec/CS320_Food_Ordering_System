package FOS_CORE;

public interface ICartService {
    void addToCart(Cart cart, MenuItem item, int quantity);
    void updateCartItem(Cart cart, MenuItem item, int newQuantity);
    Cart getCart(Customer customer);
}