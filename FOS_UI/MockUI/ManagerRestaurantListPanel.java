// java
package FOS_UI.MockUI;

import FOS_CORE.*;
import FOS_UI.MockUI.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ManagerRestaurantListPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel restaurantPanel;
    private ArrayList<Restaurant> restaurants;

    public ManagerRestaurantListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }
    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel header = new JLabel("Manager Dashboard");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        add (topPanel, BorderLayout.NORTH);
        restaurantPanel = new JPanel();
        restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(restaurantPanel);
        add(scrollPane, BorderLayout.CENTER);

    }
    public void refresh() {
        loadRestaurants(mainFrame.getCurrentManager());
        restaurantPanel.revalidate();
        restaurantPanel.repaint();
    }

    private void loadRestaurants(Manager manager) {
        restaurantPanel.removeAll();
        if (manager == null) {
            restaurants = new ArrayList<>();
            return;
        }
        ManagerService service = mainFrame.getManagerService();
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
        card.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setOpaque(false);

        JLabel name = new JLabel(restaurant.getRestaurantName());
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        JLabel cuisine = new JLabel("Cuisine: " + restaurant.getCuisineType());
        cuisine.setForeground(new Color(80, 80, 80));

        JLabel city = new JLabel("City: " + restaurant.getCity());
        city.setForeground(new Color(80, 80, 80));

        infoPanel.add(name);
        infoPanel.add(cuisine);
        infoPanel.add(city);

        card.add(infoPanel, BorderLayout.CENTER);


        JLabel arrow = new JLabel("View →");
        arrow.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        JPanel arrowPanel = new JPanel(new GridBagLayout());
        arrowPanel.setOpaque(false);
        arrowPanel.add(arrow);

        card.add(arrowPanel, BorderLayout.EAST);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        return card;

    }
}
