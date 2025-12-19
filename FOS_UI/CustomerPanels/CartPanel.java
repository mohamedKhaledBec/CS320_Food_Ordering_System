package FOS_UI.CustomerPanels;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel {
    private CustomerMainPanel mainPanel;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;
    private JLabel restaurantLabel = new JLabel("Your Cart is empty");
    private JLabel addressLabel;
    private Restaurant restaurant;
    private String selectedPhoneNumber;


    public CartPanel(CustomerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainPanel.showRestaurants());
        topPanel.add(backButton, BorderLayout.WEST);
        restaurantLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        restaurantLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(restaurantLabel, BorderLayout.CENTER);
        JButton emptyCart = new JButton("Empty Cart");
        emptyCart.addActionListener(e -> {mainPanel.getCurrentCustomer().getCart().clear();refresh();});
        topPanel.add(emptyCart, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel1 = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        bottomPanel1.add(totalLabel, BorderLayout.CENTER);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> onCheckout());
        bottomPanel1.add(checkoutButton, BorderLayout.EAST);
        JComboBox <String> phoneNumberDropdown = new JComboBox<>(getCustomerPhoneNumbers());

        JPanel bottomPanel2 = new JPanel(new BorderLayout());
        selectedPhoneNumber = phoneNumberDropdown.getItemAt(0);
        phoneNumberDropdown.addActionListener(e -> selectedPhoneNumber = (String) phoneNumberDropdown.getSelectedItem());
        bottomPanel2.add(phoneNumberDropdown, BorderLayout.SOUTH);
        addressLabel = new JLabel("Delivery Address: " + mainPanel.getRestaurantListPanel().getSelectedAddress());
        bottomPanel2.add(addressLabel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomPanel1, BorderLayout.SOUTH);
        bottomPanel.add(bottomPanel2, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        cartItemsPanel.removeAll();
        Customer customer = mainPanel.getCurrentCustomer();
        addressLabel.setText("Delivery Address: " + mainPanel.getRestaurantListPanel().getSelectedAddress());
        if (customer == null || customer.getCart() == null || customer.getCart().isEmpty()) {
            restaurantLabel.setText("Your Cart is empty");
            totalLabel.setText("Total: $0.00");
        } else {
            double total = 0.0;
            for (CartItem cartItem : customer.getCart()) {
                JPanel itemCard = createCartItemCard(cartItem);
                cartItemsPanel.add(itemCard);
                total += cartItem.calculateItemTotal();
            }
            totalLabel.setText(String.format("Total: $%.2f", total));
        }
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createCartItemCard(CartItem cartItem) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setPreferredSize(new Dimension(600, 150));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        MenuItem item = cartItem.getMenuItem();
        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityPanel.add(new JLabel("Quantity:"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(cartItem.getQuantity(), 1, 10, 1));
        quantitySpinner.addChangeListener(
                e -> {
                    int newQuantity = (int) quantitySpinner.getValue();
                    CartService cartService = mainPanel.getCartService();
                    cartService.updateCartItemQuantity(mainPanel.getCurrentCustomer(), item, newQuantity);
                    refresh();
                }
        );
        quantityPanel.add(quantitySpinner);
        JLabel perItemPriceLabel = new JLabel("Item Price:  " + String.format("$%.2f", cartItem.getPrice()));
        JLabel totalPriceLabel = new JLabel("Sub Total Price:  " + String.format("$%.2f", cartItem.calculateItemTotal()));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(quantityPanel);
        infoPanel.add(perItemPriceLabel);
        infoPanel.add(totalPriceLabel);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            CartService cartService = mainPanel.getCartService();
            cartService.removeFromCart(mainPanel.getCurrentCustomer(), item);
            refresh();
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(removeButton, BorderLayout.EAST);
        return card;
    }

    private void onCheckout() {
        Customer customer = mainPanel.getCurrentCustomer();
        if (customer == null || customer.getCart() == null || customer.getCart().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (customer.getCards() == null || customer.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a payment card first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Address selectedAddress = getSelectedAddress();
        CheckoutDialog checkoutDialog = new CheckoutDialog(
                mainPanel,
                selectedAddress,
                restaurant,
                selectedPhoneNumber
        );
        checkoutDialog.setVisible(true);
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurantLabel.setText("~ " + restaurant.getRestaurantName() + " ~");
    }
    private String[] getCustomerPhoneNumbers() {
        return mainPanel.getCurrentCustomer().getPhoneNumbers().toArray(new String[0]);
    }
    private Address getSelectedAddress() {
        String selectedAddressStr = mainPanel.getRestaurantListPanel().getSelectedAddress();
        for (Address address : mainPanel.getCurrentCustomer().getAddresses()) {
            if (address.toString().equals(selectedAddressStr)) {
                return address;
            }
        }
        return null;
    }
}

