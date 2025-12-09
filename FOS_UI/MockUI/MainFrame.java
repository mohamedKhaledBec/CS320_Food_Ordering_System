package FOS_UI.MockUI;

import FOS_CORE.*;
import FOS_UI.ManagerDashboardWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private Customer currentCustomer;
    private Manager currentManager;
    private RestaurantService restaurantService;
    private CartService cartService;
    private OrderService orderService;
    private AccountService accountService;

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private RestaurantListPanel restaurantListPanel;
    private RestaurantMenuPanel menuPanel;
    private CartPanel cartPanel;
    private OrderHistoryPanel orderHistoryPanel;
    private AccountDetailsPanel accountDetailsPanel;
    private PhoneNumbersPanel phoneNumbersPanel;
    private AddressesPanel addressesPanel;
    private CardsPanel cardsPanel;


    public MainFrame() {
        super("Food Ordering System");
        restaurantService = new RestaurantService();
        cartService = new CartService();
        orderService = new OrderService();
        accountService = new AccountService();
        showLogin();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        ArrayList<String> customerAddresses = new ArrayList<>();
        ArrayList<String> customerCities = new ArrayList<>();
        for (Address address : currentCustomer.getAddresses()){
            customerAddresses.add(address.toString());
            customerCities.add(address.getCity());
        }
        restaurantListPanel = new RestaurantListPanel(this);
        menuPanel = new RestaurantMenuPanel(this);
        cartPanel = new CartPanel(this);
        orderHistoryPanel = new OrderHistoryPanel(this);
        accountDetailsPanel = new AccountDetailsPanel(this);
        phoneNumbersPanel = new PhoneNumbersPanel(this);
        addressesPanel = new AddressesPanel(this);
        cardsPanel = new CardsPanel(this);

        mainPanel.add(restaurantListPanel, "RESTAURANTS");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(cartPanel, "CART");
        mainPanel.add(orderHistoryPanel, "ORDERS");
        mainPanel.add(accountDetailsPanel, "ACCOUNT DETAILS");
        mainPanel.add(phoneNumbersPanel, "PHONE NUMBERS");
        mainPanel.add(addressesPanel, "ADDRESSES");
        mainPanel.add(cardsPanel, "CARDS");

        add(mainPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem restaurantsItem = new JMenuItem("Restaurants");
        JMenuItem cartItem = new JMenuItem("Cart");
        JMenuItem accountItem = new JMenuItem("Account Details");
        JMenuItem logoutItem = new JMenuItem("Logout");

        restaurantsItem.addActionListener(e -> showRestaurants());
        cartItem.addActionListener(e -> showCart());
        accountItem.addActionListener(e -> showAccountDetails());
        logoutItem.addActionListener(e -> logout());

        menu.add(restaurantsItem);
        menu.add(cartItem);
        menu.add(accountItem);
        menu.addSeparator();
        menu.add(logoutItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    // java
    public void showLogin() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);
        User user = loginDialog.getLoggedInUser();
        if (user instanceof Customer) {
            currentCustomer = (Customer) user;
            initComponents();
            showRestaurants();
            setVisible(true);
// java
// Replace the "else if (user instanceof Manager) { ... }" branch in showLogin()
        } else if (user instanceof Manager) {
            Manager manager = (Manager) user;
            currentManager = manager;

            ManagerDashboardWindow dashboard = new ManagerDashboardWindow(manager);
            dashboard.setVisible(true);
            this.dispose();
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

    public void showAccountDetails() {
        if (currentCustomer == null) {
            showLogin();
            return;
        }
        accountDetailsPanel.refresh();
        cardLayout.show(mainPanel, "ACCOUNT DETAILS");
    }

    public void showPhoneNumbers() {
        phoneNumbersPanel.refresh();
        cardLayout.show(mainPanel, "PHONE NUMBERS");
    }

    public void showAddresses() {
        addressesPanel.refresh();
        cardLayout.show(mainPanel, "ADDRESSES");
    }

    public void showCards() {
        cardsPanel.refresh();
        cardLayout.show(mainPanel, "CARDS");
    }

    public void logout() {
        this.setVisible(false);
        this.dispose();
        this.getContentPane().removeAll();
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
    public RestaurantMenuPanel getMenuPanel() {
        return menuPanel;
    }
    public CartPanel getCartPanel() {
        return cartPanel;
    }
    public RestaurantListPanel getRestaurantListPanel() {
        return restaurantListPanel;
    }
    public AccountService getAccountService() {
        return accountService;
    }
}