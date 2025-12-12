package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.Manager;
import FOS_CORE.ManagerService;
import FOS_CORE.Restaurant;
import FOS_CORE.RestaurantService;
import FOS_UI.MockUI.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ManagerMainPanel extends JPanel {

    private MainFrame mainFrame;
    private CardLayout cardLayout;
    private RestaurantListPanel restaurantListPanel;
    private ManageRestaurantPanel manageRestaurantPanel;
    private EditMenuPanel editMenuPanel;
    private MonthlyReportPanel monthlyReportPanel;
    private ChangeRestaurantInfoPanel changeRestaurantInfoPanel;
    private ManageKeywordsPanel manageKeywordsPanel;
    private ManageOrdersPanel manageOrdersPanel;

    private Manager currentManager;
    private Restaurant currentRestaurant;

    private ManagerService managerService;
    private RestaurantService restaurantService;


    public ManagerMainPanel(MainFrame mainFrame, Manager manager) {
        this.currentManager = manager;
        this.mainFrame = mainFrame;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        managerService = new ManagerService();
        restaurantService = new RestaurantService();
        initComponents();
    }
    private void initComponents() {

        restaurantListPanel = new RestaurantListPanel(this);
        manageRestaurantPanel = new ManageRestaurantPanel(this);
        editMenuPanel = new EditMenuPanel(this);
        monthlyReportPanel = new MonthlyReportPanel(this);
        changeRestaurantInfoPanel = new ChangeRestaurantInfoPanel(this);
        manageKeywordsPanel = new ManageKeywordsPanel(this);
        manageOrdersPanel = new ManageOrdersPanel(this);

        add(restaurantListPanel, "MANAGER RESTAURANTS");
        add(manageRestaurantPanel, "MANAGE RESTAURANT");
        add(editMenuPanel, "EDIT MENU");
        add(monthlyReportPanel, "MONTHLY REPORT");
        add(changeRestaurantInfoPanel, "CHANGE RESTAURANT INFO");
        add(manageKeywordsPanel, "MANAGE KEYWORDS");
        add(manageOrdersPanel, "MANAGE ORDERS");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem logoutItem = new JMenuItem("Logout");

        logoutItem.addActionListener(e -> mainFrame.logout());
        menu.add(logoutItem);
        menuBar.add(menu);
        mainFrame.setJMenuBar(menuBar);

        showRestaurants();
    }

    public void showRestaurants() {
        restaurantListPanel.refresh();
        cardLayout.show(this, "MANAGER RESTAURANTS");
    }

    public void showManageRestaurant(Restaurant restaurant) {
        this.currentRestaurant = restaurant;
        manageRestaurantPanel.setRestaurant(restaurant);
        cardLayout.show(this, "MANAGE RESTAURANT");
    }

    public void showEditMenu() {
        editMenuPanel.setRestaurant(currentRestaurant);
        cardLayout.show(this, "EDIT MENU");
    }

    public void showMonthlyReport() {
        monthlyReportPanel.setRestaurant(currentRestaurant);
        cardLayout.show(this, "MONTHLY REPORT");
    }

    public void showChangeRestaurantInfo() {
        changeRestaurantInfoPanel.setRestaurant(currentRestaurant);
        cardLayout.show(this, "CHANGE RESTAURANT INFO");
    }

    public void showManageKeywords() {
        manageKeywordsPanel.setRestaurant(currentRestaurant);
        cardLayout.show(this, "MANAGE KEYWORDS");
    }

    public void showManageOrders() {
        manageOrdersPanel.setRestaurant(currentRestaurant);
        cardLayout.show(this, "MANAGE ORDERS");
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public RestaurantListPanel getRestaurantListPanel() {
        return restaurantListPanel;
    }

    public ManageRestaurantPanel getManageRestaurantPanel() {
        return manageRestaurantPanel;
    }

    public EditMenuPanel getEditMenuPanel() {
        return editMenuPanel;
    }

    public MonthlyReportPanel getMonthlyReportPanel() {
        return monthlyReportPanel;
    }

    public ChangeRestaurantInfoPanel getChangeRestaurantInfoPanel() {
        return changeRestaurantInfoPanel;
    }

    public ManageKeywordsPanel getManageKeywordsPanel() {
        return manageKeywordsPanel;
    }

    public ManageOrdersPanel getManageOrdersPanel() {
        return manageOrdersPanel;
    }

    public Manager getCurrentManager() {
        return currentManager;
    }

    public Restaurant getCurrentRestaurant() {
        return currentRestaurant;
    }

    public ManagerService getManagerService() {
        return managerService;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }
}
