package FOS_UI;

import FOS_CORE.IManagerService;
import FOS_CORE.Manager;
import FOS_CORE.Restaurant;
import javax.swing.*;
import java.awt.*;

//worked on by Umair Ahmad
public class RestaurantProfileWindow extends JFrame {
    private JLabel idValueLabel;
    private JTextField nameField;
    private JTextField cityField;
    private JTextField cuisineField;
    private JButton saveButton;
    private JButton refreshButton;
    private JButton closeButton;

    // keep current restaurant in memory
    private Restaurant currentRestaurant;

    public RestaurantProfileWindow() {
        setTitle("Food Ordering System - Restaurant Profile");
        setSize(450, 260);
        setLocationRelativeTo(null); // center
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadRestaurantDetails();
    }

    private void initComponents() {
        JLabel idLabel = new JLabel("Restaurant ID:");
        idValueLabel = new JLabel("-");

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField(20);

        JLabel cuisineLabel = new JLabel("Cuisine Type:");
        cuisineField = new JTextField(20);

        saveButton = new JButton("Save");
        refreshButton = new JButton("Refresh");
        closeButton = new JButton("Close");

        saveButton.addActionListener(e -> handleSave());
        refreshButton.addActionListener(e -> loadRestaurantDetails());
        closeButton.addActionListener(e -> dispose());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: ID (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(idValueLabel, gbc);

        // Row 1: Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Row 2: City
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(cityLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(cityField, gbc);

        // Row 3: Cuisine
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(cuisineLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(cuisineField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    // Load restaurant details for the currently logged-in manager
    // and fill the form fields.


    private void loadRestaurantDetails() {
        Manager manager = Session.getCurrentManager();
        if (manager == null) {
            DialogUtils.showError(this, "No manager is logged in.");
            return;
        }

        IManagerService managerService = ServiceContext.getManagerService();

        try {
            Restaurant restaurant = managerService.getRestaurantDetails(manager);
            if (restaurant == null) {
                DialogUtils.showError(this, "No restaurant profile found for this manager.");
                return;
            }

            this.currentRestaurant = restaurant;

            // Assuming typical getters in Restaurant: getRestaurantID(), getRestaurantName(), getCity(), getCuisineType()
            idValueLabel.setText(String.valueOf(restaurant.getRestaurantID()));
            nameField.setText(restaurant.getRestaurantName());
            cityField.setText(restaurant.getCity());
            cuisineField.setText(restaurant.getCuisineType());

        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to load restaurant details: " + ex.getMessage());
        }
    }


    // Validating inputs
    // updating the Restaurant object
    // calling managerService to update Restaurant Info via updateRestaurantInfo(...)

    private void handleSave() {
        if (currentRestaurant == null) {
            DialogUtils.showError(this, "No restaurant loaded to update.");
            return;
        }

        String name = nameField.getText().trim();
        String city = cityField.getText().trim();
        String cuisine = cuisineField.getText().trim();

        if (!InputValidator.isNonEmpty(name) ||
                !InputValidator.isNonEmpty(city) ||
                !InputValidator.isNonEmpty(cuisine)) {

            DialogUtils.showError(this, "Please fill in all fields.");
            return;
        }

        currentRestaurant.setRestaurantName(name);
        currentRestaurant.setCity(city);
        currentRestaurant.setCuisineType(cuisine);

        Manager manager = Session.getCurrentManager();

        if (manager == null) {
            DialogUtils.showError(this, "No manager is logged in.");
            return;
        }

        IManagerService managerService = ServiceContext.getManagerService();

        try {
            managerService.updateRestaurantInfo(currentRestaurant);
            DialogUtils.showInfo(this, "Restaurant profile updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to update restaurant profile: " + ex.getMessage());
        }
    }
}
