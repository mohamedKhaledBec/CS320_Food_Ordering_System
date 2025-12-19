// java
package FOS_UI.ManagerPanels;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RestaurantListPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private JPanel restaurantPanel;
    private ArrayList<Restaurant> restaurants;

    public RestaurantListPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }
    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel header = new JLabel("Manager Dashboard");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        topPanel.add(header);
        add (topPanel, BorderLayout.NORTH);
        restaurantPanel = new JPanel();
        restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(restaurantPanel);
        add(scrollPane, BorderLayout.CENTER);

    }
    public void refresh() {
        loadRestaurants(mainPanel.getCurrentManager());
        restaurantPanel.revalidate();
        restaurantPanel.repaint();
    }

    private void loadRestaurants(Manager manager) {
        restaurantPanel.removeAll();
        if (manager == null) {
            restaurants = new ArrayList<>();
            return;
        }
        ManagerService service = mainPanel.getManagerService();
        this.restaurants = service.getManagerRestaurants(manager);

        if (restaurants == null || restaurants.isEmpty()) {
            restaurantPanel.add(new JLabel("No restaurants found for this manager."));
        } else {
            for (Restaurant restaurant : restaurants) {
                JPanel card = createRestaurantCard(restaurant);
                restaurantPanel.add(card);
            }
        }
        restaurantPanel.revalidate();
        restaurantPanel.repaint();
    }

    private JPanel createRestaurantCard(Restaurant restaurant) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(600, 150));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel nameLabel = new JLabel(restaurant.getRestaurantName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        JLabel cuisineLabel = new JLabel("Cuisine: " + restaurant.getCuisineType());
        JLabel cityLabel = new JLabel("City: " + restaurant.getCity());

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(cuisineLabel);
        infoPanel.add(cityLabel);

        JButton viewMenuButton = new JButton("Manage Restaurant");
        viewMenuButton.addActionListener(e -> {
            mainPanel.showManageRestaurant(restaurant);
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(viewMenuButton, BorderLayout.EAST);
        return card;

    }
}
