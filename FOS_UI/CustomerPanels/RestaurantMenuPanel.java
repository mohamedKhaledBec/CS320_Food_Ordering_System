package FOS_UI.CustomerPanels;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuPanel extends JPanel {
    private CustomerMainPanel mainPanel;
    private Restaurant currentRestaurant;
    private JPanel menuPanel;
    private JLabel restaurantNameLabel;
    RestaurantService service = new RestaurantService();

    public RestaurantMenuPanel(CustomerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainPanel.showRestaurants());
        topPanel.add(backButton);
        restaurantNameLabel = new JLabel();
        restaurantNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        topPanel.add(restaurantNameLabel);
        add(topPanel, BorderLayout.NORTH);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.currentRestaurant = restaurant;
        if (restaurant != null) {
            restaurantNameLabel.setText(restaurant.getRestaurantName() + " - " + restaurant.getCuisineType());
            loadMenu();
        }
    }

    private void loadMenu() {
        menuPanel.removeAll();
        if (currentRestaurant == null) {
            return;
        }

        ArrayList<MenuItem> menuItems = currentRestaurant.getMenu();
        if (menuItems == null || menuItems.isEmpty()) {
            menuItems = service.fetchRestaurantMenu(currentRestaurant);
            if (menuItems != null) {
                currentRestaurant.setMenu(new java.util.ArrayList<>(menuItems));
            }
        }

        if (menuItems == null || menuItems.isEmpty()) {
            menuPanel.add(new JLabel("No menu items available."));
        } else {
            for (MenuItem item : menuItems) {
                JPanel itemCard = createMenuItemCard(item);
                menuPanel.add(itemCard);
            }
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JPanel createMenuItemCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setPreferredSize(new Dimension(600, 150));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JLabel descLabel = new JLabel(item.getDescription() != null ? item.getDescription() : "");
        double price = item.getPrice();
        JLabel priceLabel = new JLabel(String.format("  $%.2f", item.getPrice()));
        final double finalPrice = service.calculateMenuItemDiscount(item);
        JLabel discountedPriceLabel = new JLabel();
        if (finalPrice != price) {
            priceLabel.setText(String.format("Original Price:   " + String.format("$%.2f", item.getPrice())));
            discountedPriceLabel.setText(String.format("Discounted Price:   "+ String.format("$%.2f", finalPrice)));
        }

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(descLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(discountedPriceLabel);

        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityPanel.add(new JLabel("Quantity:"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantityPanel.add(quantitySpinner);
        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> {
            int quantity = (Integer) quantitySpinner.getValue();
            addToCart(item, quantity, finalPrice);
        });
        quantityPanel.add(addButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(quantityPanel, BorderLayout.SOUTH);
        return card;
    }

    private void addToCart(MenuItem item, int quantity, double price) {
        Customer customer = mainPanel.getCurrentCustomer();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!isSameRestaurant()) {
            JOptionPane.showMessageDialog(this, "You can only add items from one restaurant at a time. Please clear your cart first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            CartService cartService = mainPanel.getCartService();
            cartService.addToCart(customer, item, quantity, price);
            mainPanel.getCartPanel().setRestaurant(currentRestaurant);
            JOptionPane.showMessageDialog(this,
                    quantity + "x " + item.getItemName() + " added to cart!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add to cart: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public JLabel getRestaurantNameLabel() {
        return restaurantNameLabel;
    }

    private boolean isSameRestaurant() {
        Customer customer = mainPanel.getCurrentCustomer();
        ArrayList<CartItem> cartItems = customer.getCart();
        ArrayList<MenuItem> cartMenuItems = new ArrayList<>();
        List<MenuItem> menuItems = currentRestaurant.getMenu();
        for (CartItem cartItem : cartItems) {
            cartMenuItems.add(cartItem.getMenuItem());
        }
        for(MenuItem cartMenuItem : cartMenuItems) {
            int counter = 0;
            for (MenuItem menuItem : menuItems) {
                if(cartMenuItem.getMenuItemID() == menuItem.getMenuItemID()) {
                    counter++;
                    break;
                }
            }
            if(counter == 0) {
                return false;
            }
        }
        return true;
    }
}

