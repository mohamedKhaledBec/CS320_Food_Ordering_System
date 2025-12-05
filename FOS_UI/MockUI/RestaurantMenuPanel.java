package FOS_UI.MockUI;

import FOS_CORE.*;
import FOS_CORE.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RestaurantMenuPanel extends JPanel {
    private MainFrame mainFrame;
    private Restaurant currentRestaurant;
    private JPanel menuPanel;
    private JLabel restaurantNameLabel;

    public RestaurantMenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        restaurantNameLabel = new JLabel();
        restaurantNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        topPanel.add(restaurantNameLabel);
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainFrame.showRestaurants());
        topPanel.add(backButton);
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

        List<MenuItem> menuItems = currentRestaurant.getMenu();
        if (menuItems == null || menuItems.isEmpty()) {
            RestaurantService service = mainFrame.getRestaurantService();
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

        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JLabel descLabel = new JLabel(item.getDescription() != null ? item.getDescription() : "");
        JLabel priceLabel = new JLabel(String.format("$%.2f", item.getPrice()));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(descLabel);
        infoPanel.add(priceLabel);

        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityPanel.add(new JLabel("Quantity:"));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantityPanel.add(quantitySpinner);
        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> {
            int quantity = (Integer) quantitySpinner.getValue();
            addToCart(item, quantity);
        });
        quantityPanel.add(addButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(quantityPanel, BorderLayout.SOUTH);
        return card;
    }

    private void addToCart(MenuItem item, int quantity) {
        Customer customer = mainFrame.getCurrentCustomer();
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            CartService cartService = mainFrame.getCartService();
            cartService.addToCart(customer, item, quantity);
            JOptionPane.showMessageDialog(this,
                    quantity + "x " + item.getItemName() + " added to cart!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add to cart: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

