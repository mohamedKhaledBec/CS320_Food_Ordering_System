package FOS_UI;

import FOS_CORE.IRestaurantService;
import FOS_CORE.Manager;
import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Window for managers to view and edit a restaurant's menu.
 * UI-only for now; backend persistence can be wired via IManagerService.
 */
public class MenuManagementWindow extends JFrame {

    private final Manager manager;
    private final Restaurant restaurant;
    private final IRestaurantService restaurantService;

    private JTable menuTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField priceField;
    private JTextArea descriptionArea;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton closeButton;

    private List<MenuItem> menuItems = new ArrayList<>();

    public MenuManagementWindow(Manager manager, Restaurant restaurant) {
        if (manager == null || restaurant == null) {
            throw new IllegalArgumentException("Manager and Restaurant must not be null");
        }
        this.manager = manager;
        this.restaurant = restaurant;
        this.restaurantService = ServiceContext.getRestaurantService();

        setTitle("Edit Menu - " + restaurant.getRestaurantName());
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadMenu();
    }

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // TOP: restaurant info
        JLabel titleLabel = new JLabel(restaurant.getRestaurantName());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        JLabel metaLabel = new JLabel(String.format(
                "Cuisine: %s    City: %s",
                restaurant.getCuisineType(),
                restaurant.getCity()
        ));
        metaLabel.setForeground(new Color(90, 90, 90));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(4));
        topPanel.add(metaLabel);

        add(topPanel, BorderLayout.NORTH);

        // CENTER: table
        tableModel = new DefaultTableModel(
                new Object[]{"Name", "Description", "Price"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // We'll edit via the fields, not directly in the table.
                return false;
            }
        };

        menuTable = new JTable(tableModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuTable.getSelectionModel().addListSelectionListener(e -> onTableSelectionChanged());

        JScrollPane tableScroll = new JScrollPane(menuTable);
        add(tableScroll, BorderLayout.CENTER);

        // BOTTOM: editor + buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));

        // editor
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(25);
        priceField = new JTextField(10);
        descriptionArea = new JTextArea(3, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        // row 0: name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // row 1: price
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        // row 2: description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");

        addButton.addActionListener(e -> handleAdd());
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

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

        // Create a new MenuItem in memory.
        MenuItem item = new MenuItem();
        item.setItemName(name);
        item.setDescription(desc);
        item.setPrice(price);
        // If your MenuItem has restaurant_id field, set it here as well.

        // TODO: Persist via IManagerService (e.g., managerService.addMenuItem(restaurant, item))
        // For now, just add to local list & table.
        menuItems.add(item);
        tableModel.addRow(new Object[]{name, desc, price});
        clearForm();

        DialogUtils.showInfo(this, "Menu item added (UI only).\nWire this to IManagerService to save to DB.");
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

        // TODO: Persist changes via IManagerService (e.g., managerService.updateMenuItem(item))

        tableModel.setValueAt(name, row, 0);
        tableModel.setValueAt(desc, row, 1);
        tableModel.setValueAt(price, row, 2);

        DialogUtils.showInfo(this, "Menu item updated (UI only).\nWire this to IManagerService to save to DB.");
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

        // TODO: Call IManagerService to delete from DB (e.g., managerService.deleteMenuItem(item))

        menuItems.remove(row);
        tableModel.removeRow(row);
        clearForm();

        DialogUtils.showInfo(this, "Menu item deleted (UI only).\nWire this to IManagerService to delete from DB.");
    }
}
