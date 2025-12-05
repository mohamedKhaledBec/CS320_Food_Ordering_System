package FOS_UI.MockUI;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;

    public CartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainFrame.showRestaurants());
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.CENTER);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> onCheckout());
        bottomPanel.add(checkoutButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        cartItemsPanel.removeAll();
        Customer customer = mainFrame.getCurrentCustomer();
        if (customer == null || customer.getCart() == null || customer.getCart().isEmpty()) {
            cartItemsPanel.add(new JLabel("Your cart is empty."));
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

        MenuItem item = cartItem.getItem();
        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JLabel quantityLabel = new JLabel("Quantity: " + cartItem.getQuantity());
        JLabel priceLabel = new JLabel(String.format("$%.2f", cartItem.calculateItemTotal()));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(quantityLabel);
        infoPanel.add(priceLabel);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            CartService cartService = mainFrame.getCartService();
            cartService.updateCartItem(mainFrame.getCurrentCustomer(), item, 0);
            refresh();
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(removeButton, BorderLayout.EAST);
        return card;
    }

    private void onCheckout() {
        Customer customer = mainFrame.getCurrentCustomer();
        if (customer == null || customer.getCart() == null || customer.getCart().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (customer.getAddresses() == null || customer.getAddresses().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a delivery address first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (customer.getCards() == null || customer.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a payment card first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert cart to Cart object
        ArrayList<CartItem> cart = new ArrayList<>();
        cart.addAll(customer.getCart());

        Address selectedAddress = customer.getAddresses().getFirst(); // Use first address
        Restaurant restaurant = null; // Would need to track which restaurant
        if (!customer.getCart().isEmpty()) {
            // Get restaurant from first item (simplified)
            MenuItem firstItem = customer.getCart().getFirst().getItem();
            // Would need restaurant lookup here
        }

        CheckoutDialog checkoutDialog = new CheckoutDialog(
                mainFrame,
                customer,
                new ArrayList<>(customer.getCart()),
                selectedAddress,
                restaurant
        );
        checkoutDialog.setVisible(true);

        TransactionRecord transaction = checkoutDialog.getLastTransaction();
        if (transaction != null && transaction.getStatus() == PaymentStatus.SUCCESS) {
            // Place order
            try {
                OrderService orderService = mainFrame.getOrderService();
                Order order = orderService.placeOrder(customer, cart, selectedAddress);
                customer.getCart().clear();
                JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to place order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

