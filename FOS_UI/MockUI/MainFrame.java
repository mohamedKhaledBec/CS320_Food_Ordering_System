package FOS_UI.MockUI;

import FOS_CORE.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private Customer currentCustomer;
    private RestaurantService restaurantService;
    private CartService cartService;
    private OrderService orderService;

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private RestaurantListPanel restaurantListPanel;
    private RestaurantMenuPanel menuPanel;
    private CartPanel cartPanel;
    private OrderHistoryPanel orderHistoryPanel;

    public MainFrame() {
        super("Food Ordering System");
        restaurantService = new RestaurantService();
        cartService = new CartService();
        orderService = new OrderService();
        initComponents();
        showLogin();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        restaurantListPanel = new RestaurantListPanel(this);
        menuPanel = new RestaurantMenuPanel(this);
        cartPanel = new CartPanel(this);
        orderHistoryPanel = new OrderHistoryPanel(this);

        mainPanel.add(restaurantListPanel, "RESTAURANTS");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(cartPanel, "CART");
        mainPanel.add(orderHistoryPanel, "ORDERS");

        add(mainPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem restaurantsItem = new JMenuItem("Restaurants");
        JMenuItem cartItem = new JMenuItem("Cart");
        JMenuItem ordersItem = new JMenuItem("Order History");
        JMenuItem logoutItem = new JMenuItem("Logout");

        restaurantsItem.addActionListener(e -> showRestaurants());
        cartItem.addActionListener(e -> showCart());
        ordersItem.addActionListener(e -> showOrderHistory());
        logoutItem.addActionListener(e -> logout());

        menu.add(restaurantsItem);
        menu.add(cartItem);
        menu.add(ordersItem);
        menu.addSeparator();
        menu.add(logoutItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public void showLogin() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);
        User user = loginDialog.getLoggedInUser();
        if (user instanceof Customer) {
            currentCustomer = (Customer) user;
            showRestaurants();
            setVisible(true);
        } else if (user != null) {
            JOptionPane.showMessageDialog(this, "Manager interface not yet implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else {
            System.exit(0);
        }
    }

    public void showRestaurants() {
        if (currentCustomer == null) {
            showLogin();
            return;
        }
        restaurantListPanel.refresh();
        cardLayout.show(mainPanel, "RESTAURANTS");
    }

    public void showMenu(Restaurant restaurant) {
        if (currentCustomer == null) {
            showLogin();
            return;
        }
        menuPanel.setRestaurant(restaurant);
        cardLayout.show(mainPanel, "MENU");
    }

    public void showCart() {
        if (currentCustomer == null) {
            showLogin();
            return;
        }
        cartPanel.refresh();
        cardLayout.show(mainPanel, "CART");
    }

    public void showOrderHistory() {
        if (currentCustomer == null) {
            showLogin();
            return;
        }
        orderHistoryPanel.refresh();
        cardLayout.show(mainPanel, "ORDERS");
    }

    public void logout() {
        currentCustomer = null;
        setVisible(false);
        showLogin();
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
}

