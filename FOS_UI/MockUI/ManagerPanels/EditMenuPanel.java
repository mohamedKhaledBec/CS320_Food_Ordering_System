package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.IManagerService;
import FOS_CORE.IRestaurantService;
import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;
import FOS_UI.DialogUtils;
import FOS_UI.InputValidator;
import FOS_UI.ServiceContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditMenuPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant restaurant;
    private final IRestaurantService restaurantService;
    private final IManagerService managerService;

    private JTable menuTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField priceField;
    private JTextArea descriptionArea;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    private List<MenuItem> menuItems = new ArrayList<>();

    public EditMenuPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.restaurantService = ServiceContext.getRestaurantService();
        this.managerService = ServiceContext.getManagerService();
        initComponents();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        if (restaurant != null) {
            loadMenu();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        ((JComponent) this).setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainPanel.showManageRestaurant(mainPanel.getCurrentRestaurant()));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Name", "Description", "Price"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        menuTable = new JTable(tableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.getSelectionModel().addListSelectionListener(e -> onTableSelectionChanged());

        JScrollPane tableScroll = new JScrollPane(menuTable);
        add(tableScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(25);
        priceField = new JTextField(10);
        descriptionArea = new JTextArea(3, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> handleAdd());
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMenu() {
        try {
            menuItems = restaurantService.fetchRestaurantMenu(restaurant);
            if (menuItems == null) {
                menuItems = new ArrayList<>();
            }
            reloadTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to load menu: " + ex.getMessage());
        }
    }

    private void reloadTable() {
        tableModel.setRowCount(0);
        for (MenuItem item : menuItems) {
            tableModel.addRow(new Object[]{
                    item.getItemName(),
                    item.getDescription(),
                    item.getPrice()
            });
        }
    }

    private void onTableSelectionChanged() {
        int row = menuTable.getSelectedRow();
        if (row < 0 || row >= menuItems.size()) {
            clearForm();
            return;
        }
        MenuItem item = menuItems.get(row);
        nameField.setText(item.getItemName());
        descriptionArea.setText(item.getDescription());
        priceField.setText(String.valueOf(item.getPrice()));
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        priceField.setText("");
    }

    private void handleAdd() {
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        String priceText = priceField.getText().trim();

        if (!InputValidator.isNonEmpty(name) ||
                !InputValidator.isNonEmpty(desc) ||
                !InputValidator.isNonEmpty(priceText)) {

            DialogUtils.showError(this, "Please fill in all fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            DialogUtils.showError(this, "Invalid price. Please enter a numeric value.");
            return;
        }

        try {
            MenuItem item = new MenuItem();
            item.setItemName(name);
            item.setDescription(desc);
            item.setPrice(price);

            managerService.addMenuItem(restaurant, item);

            menuItems.add(item);
            tableModel.addRow(new Object[]{
                    item.getItemName(),
                    item.getDescription(),
                    item.getPrice()
            });
            clearForm();

            DialogUtils.showInfo(this, "Menu item added successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to add menu item: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        int row = menuTable.getSelectedRow();
        if (row < 0 || row >= menuItems.size()) {
            DialogUtils.showError(this, "Please select an item to update.");
            return;
        }

        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        String priceText = priceField.getText().trim();

        if (!InputValidator.isNonEmpty(name) ||
                !InputValidator.isNonEmpty(desc) ||
                !InputValidator.isNonEmpty(priceText)) {

            DialogUtils.showError(this, "Please fill in all fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            DialogUtils.showError(this, "Invalid price. Please enter a numeric value.");
            return;
        }

        MenuItem item = menuItems.get(row);
        item.setItemName(name);
        item.setDescription(desc);
        item.setPrice(price);

        try {
            managerService.editMenuItem(restaurant, item);

            tableModel.setValueAt(name, row, 0);
            tableModel.setValueAt(desc, row, 1);
            tableModel.setValueAt(price, row, 2);

            DialogUtils.showInfo(this, "Menu item updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to update menu item: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        int row = menuTable.getSelectedRow();
        if (row < 0 || row >= menuItems.size()) {
            DialogUtils.showError(this, "Please select an item to delete.");
            return;
        }

        if (DialogUtils.confirm(this, "Are you sure you want to delete this menu item?") == 1) {
            return;
        }

        MenuItem item = menuItems.get(row);

        try {
            managerService.removeMenuItem(restaurant, item);

            menuItems.remove(row);
            tableModel.removeRow(row);
            clearForm();

            DialogUtils.showInfo(this, "Menu item deleted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to delete menu item: " + ex.getMessage());
        }
    }
}

