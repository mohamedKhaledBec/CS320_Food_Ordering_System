// java
package FOS_UI.CustomerPanels;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RestaurantListPanel extends JPanel {
    private CustomerMainPanel mainPanel;
    private JPanel restaurantPanel;
    private JComboBox<String> cityDropdown;
    private JTextField searchField;
    private ArrayList<Restaurant> restaurants;
    private String selectedAddress;

    public RestaurantListPanel(CustomerMainPanel customerMainPanel) {
        this.mainPanel = customerMainPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        cityDropdown = new JComboBox<>(getCustomerAddresses());
        // safe initialization: only set selectedAddress if an item exists
        if (cityDropdown.getItemCount() > 0 && cityDropdown.getItemAt(0) != null) {
            selectedAddress = cityDropdown.getItemAt(0);
        } else {
            selectedAddress = null;
        }
        topPanel.add(cityDropdown);
        searchField = new JTextField(15);
        topPanel.add(searchField);
        JButton searchKeywordButton = new JButton("Search");
        topPanel.add(searchKeywordButton);
        add(topPanel, BorderLayout.NORTH);

        restaurantPanel = new JPanel();
        restaurantPanel.setLayout(new BoxLayout(restaurantPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(restaurantPanel);
        add(scrollPane, BorderLayout.CENTER);

        cityDropdown.addActionListener(e -> searchByCity());
        searchKeywordButton.addActionListener(e -> searchByKeyword());
    }

    public void refresh() {
        refreshAddressDropdown();
        restaurantPanel.removeAll();
        String city = extractCityFromAddress(selectedAddress);
        if (!city.isEmpty()) {
            loadRestaurantsByCity(city);
        }
        restaurantPanel.revalidate();
        restaurantPanel.repaint();
    }

    public void refreshAddressDropdown() {
        cityDropdown.removeAllItems();
        String[] addresses = getCustomerAddresses();
        for (String address : addresses) {
            cityDropdown.addItem(address);
        }
        if (cityDropdown.getItemCount() > 0 && cityDropdown.getItemAt(0) != null) {
            selectedAddress = cityDropdown.getItemAt(0);
        } else {
            selectedAddress = null;
        }
    }

    private void searchByCity() {
        Object sel = cityDropdown.getSelectedItem();
        selectedAddress = (sel != null) ? sel.toString() : null;
        String city = extractCityFromAddress(selectedAddress);
        loadRestaurantsByCity(city);
    }

    private void searchByKeyword() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            searchByCity();
            return;
        }
        loadRestaurantsByKeyword(keyword);
    }

    private void loadRestaurantsByCity(String city) {
        restaurantPanel.removeAll();
        RestaurantService service = mainPanel.getRestaurantService();
        this.restaurants = service.getRestaurantsByCity(city);

        if (restaurants == null || restaurants.isEmpty()) {
            restaurantPanel.add(new JLabel("No restaurants found in " + city));
        } else {
            for (Restaurant restaurant : restaurants) {
                JPanel card = createRestaurantCard(restaurant);
                restaurantPanel.add(card);
            }
        }
        restaurantPanel.revalidate();
        restaurantPanel.repaint();
    }

    private void loadRestaurantsByKeyword(String keyword) {
        restaurantPanel.removeAll();
        RestaurantService service = mainPanel.getRestaurantService();
        this.restaurants = service.searchRestaurantsByKeyword(keyword, restaurants);

        if (restaurants == null || restaurants.isEmpty()) {
            restaurantPanel.add(new JLabel("No restaurants found matching: " + keyword));
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
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        JLabel cuisineLabel = new JLabel("Cuisine: " + restaurant.getCuisineType());
        JLabel cityLabel = new JLabel("City: " + restaurant.getCity());
        JLabel ratingLabel = new JLabel("Rating: " + restaurant.calculateRestaurantRating(restaurant));

        JPanel infoPanel = new JPanel(new GridLayout(4, -1));
        infoPanel.add(nameLabel);
        infoPanel.add(cuisineLabel);
        infoPanel.add(cityLabel);
        infoPanel.add(ratingLabel);

        JButton viewMenuButton = new JButton("View Menu");
        viewMenuButton.addActionListener(e -> mainPanel.showMenu(restaurant));

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(viewMenuButton, BorderLayout.EAST);
        return card;
    }

    // Null-safe: returns empty string if address is null or doesn't contain a city part
    private String extractCityFromAddress(String address) {
        if (address == null) return "";
        String trimmed = address.trim();
        if (trimmed.isEmpty()) return "";
        String[] parts = trimmed.split(",\\s*");
        if (parts.length >= 2) {
            return parts[1].trim();
        }
        // If address has no comma, attempt to use the whole value as city if plausible
        return "";
    }

    // Null-safe: always returns at least one element for the combo to display
    private String[] getCustomerAddresses() {
        if (mainPanel.getMainFrame() == null || mainPanel.getCurrentCustomer() == null) {
            return new String[]{"No addresses available"};
        }
        ArrayList<Address> addresses = mainPanel.getCurrentCustomer().getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            return new String[]{"No addresses available"};
        }
        ArrayList<String> addressStrings = new ArrayList<>();
        for (Address a : addresses) {
            if (a == null) continue;
            String s = (a.toString() != null) ? a.toString().trim() : "";
            if (!s.isEmpty()) {
                addressStrings.add(s);
            }
        }
        if (addressStrings.isEmpty()) {
            return new String[]{"No addresses available"};
        }
        return addressStrings.toArray(new String[0]);
    }

    public String getSelectedAddress() {
        return selectedAddress;
    }
}
