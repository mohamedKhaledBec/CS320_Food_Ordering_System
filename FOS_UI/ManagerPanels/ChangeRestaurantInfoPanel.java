package FOS_UI.ManagerPanels;

import FOS_CORE.IManagerService;
import FOS_CORE.Restaurant;

import javax.swing.*;
import java.awt.*;

public class ChangeRestaurantInfoPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant currentRestaurant;

    private JTextField nameField;
    private JComboBox<String> cityDropdown;
    private JTextField cuisineField;
    private JButton saveButton;

    public ChangeRestaurantInfoPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.currentRestaurant = restaurant;
        if (restaurant != null) {
            loadRestaurantDetails();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainPanel.showManageRestaurant(mainPanel.getCurrentRestaurant()));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel cityLabel = new JLabel("City:");
        cityDropdown = new JComboBox<>(getTurkishCities());

        JLabel cuisineLabel = new JLabel("Cuisine Type:");
        cuisineField = new JTextField(20);

        saveButton = new JButton("Save");

        saveButton.addActionListener(e -> handleSave());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(cityLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(cityDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(cuisineLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(cuisineField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadRestaurantDetails() {
        if (currentRestaurant == null) {
            return;
        }

        nameField.setText(currentRestaurant.getRestaurantName());
        cityDropdown.setSelectedItem(currentRestaurant.getCity());
        cuisineField.setText(currentRestaurant.getCuisineType());
    }

    private void handleSave() {
        if (currentRestaurant == null) {
            JOptionPane.showMessageDialog(this, "Please select a restaurant.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String city = cityDropdown.getSelectedItem().toString();
        String cuisine = cuisineField.getText().trim();

        if (name.isEmpty() ||
                city.isEmpty() ||
                cuisine.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Restaurant newRestaurantInfo = new Restaurant(currentRestaurant.getRestaurantID(), name, cuisine,city);

        IManagerService managerService = mainPanel.getManagerService();

        try {
            managerService.updateRestaurantInfo(newRestaurantInfo);
            currentRestaurant.setRestaurantName(name);
            currentRestaurant.setCity(city);
            currentRestaurant.setCuisineType(cuisine);
            JOptionPane.showMessageDialog(this, "Restaurant profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update restaurant profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] getTurkishCities(){
        return new String[]{"Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
                "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
                "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum",
                "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin",
                "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli",
                "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir",
                "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
                "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt",
                "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük",
                "Kilis", "Osmaniye", "Düzce"};
    }

}

