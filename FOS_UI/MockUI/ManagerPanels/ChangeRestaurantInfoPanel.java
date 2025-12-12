package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.IManagerService;
import FOS_CORE.Restaurant;
import FOS_UI.DialogUtils;
import FOS_UI.InputValidator;
import FOS_UI.ServiceContext;

import javax.swing.*;
import java.awt.*;

public class ChangeRestaurantInfoPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant currentRestaurant;

    private JLabel idValueLabel;
    private JTextField nameField;
    private JComboBox<String> cityField;
    private JTextField cuisineField;
    private JButton saveButton;
    private JButton refreshButton;

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

        JLabel idLabel = new JLabel("Restaurant ID:");
        idValueLabel = new JLabel("-");

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel cityLabel = new JLabel("City:");
        cityField = new JComboBox<>(getTurkishCities());

        JLabel cuisineLabel = new JLabel("Cuisine Type:");
        cuisineField = new JTextField(20);

        saveButton = new JButton("Save");
        refreshButton = new JButton("Refresh");

        saveButton.addActionListener(e -> handleSave());
        refreshButton.addActionListener(e -> loadRestaurantDetails());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(idValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(cityLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(cuisineLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(cuisineField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
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

        idValueLabel.setText(String.valueOf(currentRestaurant.getRestaurantID()));
        nameField.setText(currentRestaurant.getRestaurantName());
        cityField.setSelectedItem(currentRestaurant.getCity());
        cuisineField.setText(currentRestaurant.getCuisineType());
    }

    private void handleSave() {
        if (currentRestaurant == null) {
            DialogUtils.showError(this, "No restaurant loaded to update.");
            return;
        }

        String name = nameField.getText().trim();
        String city = cityField.getSelectedItem().toString();
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

        IManagerService managerService = ServiceContext.getManagerService();

        try {
            managerService.updateRestaurantInfo(currentRestaurant);
            DialogUtils.showInfo(this, "Restaurant profile updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to update restaurant profile: " + ex.getMessage());
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

