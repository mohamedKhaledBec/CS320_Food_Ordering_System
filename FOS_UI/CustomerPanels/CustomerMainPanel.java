package FOS_UI.CustomerPanels;

import FOS_CORE.*;
import FOS_UI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CustomerMainPanel extends JPanel{

    private MainFrame mainFrame;
    private CardLayout cardLayout;
    private RestaurantListPanel restaurantListPanel;
    private RestaurantMenuPanel menuPanel;
    private CartPanel cartPanel;
    private OrderHistoryPanel orderHistoryPanel;
    private AccountDetailsPanel accountDetailsPanel;
    private PhoneNumbersPanel phoneNumbersPanel;
    private AddressesPanel addressesPanel;
    private CardsPanel cardsPanel;

    private Customer currentCustomer;

    private RestaurantService restaurantService;
    private CartService cartService;
    private OrderService orderService;

    public CustomerMainPanel(MainFrame mainFrame, Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
        this.mainFrame = mainFrame;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        restaurantService = new RestaurantService();
        cartService = new CartService();
        orderService = new OrderService();
        initComponents();
    }

    private void initComponents() {

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

        add(restaurantListPanel, "RESTAURANTS");
        add(menuPanel, "MENU");
        add(cartPanel, "CART");
        add(orderHistoryPanel, "ORDERS");
        add(accountDetailsPanel, "ACCOUNT DETAILS");
        add(phoneNumbersPanel, "PHONE NUMBERS");
        add(addressesPanel, "ADDRESSES");
        add(cardsPanel, "CARDS");


        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem restaurantsItem = new JMenuItem("Restaurants");
        JMenuItem cartItem = new JMenuItem("Cart");
        JMenuItem accountItem = new JMenuItem("Account Details");
        JMenuItem logoutItem = new JMenuItem("Logout");

        restaurantsItem.addActionListener(e -> showRestaurants());
        cartItem.addActionListener(e -> showCart());
        accountItem.addActionListener(e -> showAccountDetails());
        logoutItem.addActionListener(e -> mainFrame.logout());

        menu.add(restaurantsItem);
        menu.add(cartItem);
        menu.add(accountItem);
        menu.addSeparator();
        menu.add(logoutItem);
        menuBar.add(menu);
        mainFrame.setJMenuBar(menuBar);

        showRestaurants();
    }
    public void showRestaurants() {
        restaurantListPanel.refresh();
        cardLayout.show(this, "RESTAURANTS");
    }

    public void showMenu(Restaurant restaurant) {
        menuPanel.setRestaurant(restaurant);
        cardLayout.show(this, "MENU");
    }

    public void showCart() {
        cartPanel.refresh();
        cardLayout.show(this, "CART");
    }

    public void showOrderHistory() {
        orderHistoryPanel.refresh();
        cardLayout.show(this, "ORDERS");
    }

    public void showAccountDetails() {
        accountDetailsPanel.refresh();
        cardLayout.show(this, "ACCOUNT DETAILS");
    }

    public void showPhoneNumbers() {
        phoneNumbersPanel.refresh();
        cardLayout.show(this, "PHONE NUMBERS");
    }

    public void showAddresses() {
        addressesPanel.refresh();
        cardLayout.show(this, "ADDRESSES");
    }

    public void showCards() {
        cardsPanel.refresh();
        cardLayout.show(this, "CARDS");
    }

    public void showManagerRestaurants() {
        restaurantListPanel.refresh();
        cardLayout.show(this, "MANAGER RESTAURANTS");
    }
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public RestaurantListPanel getRestaurantListPanel() {
        return restaurantListPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public RestaurantMenuPanel getMenuPanel() {
        return menuPanel;
    }

    public CartPanel getCartPanel() {
        return cartPanel;
    }

    public OrderHistoryPanel getOrderHistoryPanel() {
        return orderHistoryPanel;
    }

    public AccountDetailsPanel getAccountDetailsPanel() {
        return accountDetailsPanel;
    }

    public PhoneNumbersPanel getPhoneNumbersPanel() {
        return phoneNumbersPanel;
    }

    public AddressesPanel getAddressesPanel() {
        return addressesPanel;
    }

    public CardsPanel getCardsPanel() {
        return cardsPanel;
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
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
