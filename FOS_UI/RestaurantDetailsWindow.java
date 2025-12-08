package FOS_UI;

import FOS_CORE.CartItem;
import FOS_CORE.Customer;
import FOS_CORE.ICartService;
import FOS_CORE.IRestaurantService;
import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//Worked on by Umair Ahmad
public class RestaurantDetailsWindow extends JFrame{

    private final Restaurant restaurant;
    private final IRestaurantService restaurantService;
    private final ICartService cartService;

    private JLabel nameLabel;
    private JLabel cuisineLabel;
    private JLabel cityLabel;
    private JPanel menuPanel;

    public RestaurantDetailsWindow(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantService = ServiceContext.getRestaurantService();
        this.cartService = ServiceContext.getCartService();

        setTitle("Restaurant Details");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadRestaurantDetails();
        loadMenuItems();
    }


    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("Name:"));
        nameLabel = new JLabel();
        infoPanel.add(nameLabel);

        infoPanel.add(new JLabel("Cuisine:"));
        cuisineLabel = new JLabel();
        infoPanel.add(cuisineLabel);

        infoPanel.add(new JLabel("City:"));
        cityLabel = new JLabel();
        infoPanel.add(cityLabel);

        add(infoPanel, BorderLayout.NORTH);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRestaurantDetails() {
        if (restaurant == null) {
            DialogUtils.showError(this, "No restaurant provided.");
            return;
        }
        nameLabel.setText(restaurant.getRestaurantName());
        cuisineLabel.setText(restaurant.getCuisineType());
        cityLabel.setText(restaurant.getCity());
    }


    private void loadMenuItems() {
        menuPanel.removeAll();
        if (restaurant == null) {
            return;
        }

        ArrayList<MenuItem> menuItems = restaurant.getMenu();
        if (menuItems == null || menuItems.isEmpty()) {
            menuItems = restaurantService.fetchRestaurantMenu(restaurant);
            if (menuItems != null) {
                restaurant.setMenu(new ArrayList<>(menuItems));
            }
        }

        if (menuItems == null || menuItems.isEmpty()) {
            menuPanel.add(new JLabel("No menu items available."));
        } else {
            for (MenuItem item : menuItems) {
                JPanel card = createMenuItemCard(item);
                menuPanel.add(card);
                menuPanel.add(Box.createVerticalStrut(8));
            }
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

}
