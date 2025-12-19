package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.IManagerService;
import FOS_CORE.IRestaurantService;
import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditMenuPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant restaurant;
    private final IRestaurantService restaurantService;
    private final IManagerService managerService;

    private JPanel menuItemsPanel;
    private List<MenuItem> menuItems = new ArrayList<>();

    public EditMenuPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.restaurantService = mainPanel.getRestaurantService();
        this.managerService = mainPanel.getManagerService();
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

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainPanel.showManageRestaurant(mainPanel.getCurrentRestaurant()));
        topPanel.add(backButton, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Menu Items");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton addButton = new JButton("Add Menu Item");
        addButton.setBackground(Color.green);
        addButton.addActionListener(e -> addButtonAction());
        topPanel.add(addButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        menuItemsPanel = new JPanel();
        menuItemsPanel.setLayout(new BoxLayout(menuItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(menuItemsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadMenu() {
        try {
            menuItems = restaurantService.fetchRestaurantMenu(restaurant);
            if (menuItems == null) {
                menuItems = new ArrayList<>();
            }
            refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load menu items: " + ex.getMessage());
        }
    }

    public void refresh() {
        menuItemsPanel.removeAll();
        if (restaurant == null) {
            menuItemsPanel.add(new JLabel("Please select a restaurant."));
        } else {
            if (menuItems == null || menuItems.isEmpty()) {
                menuItemsPanel.add(new JLabel("No menu items found."));
            } else {
                for (MenuItem item : menuItems) {
                    JPanel itemCard = createMenuItemCard(item);
                    menuItemsPanel.add(itemCard);
                }
            }
        }
        menuItemsPanel.revalidate();
        menuItemsPanel.repaint();
    }

    private JPanel createMenuItemCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setPreferredSize(new Dimension(600, 50));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        editButton.setBackground(Color.yellow);
        editButton.addActionListener(e -> editButtonAction(item));
        
        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.red);
        removeButton.addActionListener(e -> removeButtonAction(item));

        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        card.add(nameLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void addButtonAction() {
        EditMenuItemDialog dialog = new EditMenuItemDialog(mainPanel, null, restaurant);
        dialog.setVisible(true);

        MenuItem added = dialog.getMenuItem();
        if (added != null) {
            try {
                managerService.addMenuItem(restaurant, added);
                menuItems.add(added);
                refresh();
                JOptionPane.showMessageDialog(this, "Menu item added successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add menu item: " + ex.getMessage());
            }
        }
    }

    private void editButtonAction(MenuItem item) {
        EditMenuItemDialog dialog = new EditMenuItemDialog(mainPanel, item, restaurant);
        dialog.setVisible(true);

        MenuItem updated = dialog.getMenuItem();
        if (updated != null) {
            try {
                item.setItemName(updated.getItemName());
                item.setDescription(updated.getDescription());
                item.setPrice(updated.getPrice());
                
                managerService.editMenuItem(restaurant, item);
                refresh();
               JOptionPane.showMessageDialog(this, "Menu item updated successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update menu item: " + ex.getMessage());
            }
        }
    }

    private void removeButtonAction(MenuItem item) {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this menu item?") == 1) {
            return;
        }

        try {
            managerService.removeMenuItem(restaurant, item);
            menuItems.remove(item);
            refresh();
            JOptionPane.showMessageDialog(this, "Menu item deleted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete menu item: " + ex.getMessage());
        }
    }
}
