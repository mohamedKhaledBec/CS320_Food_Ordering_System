package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.Restaurant;

import javax.swing.*;
import java.awt.*;

public class ManageRestaurantPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant currentRestaurant;
    private JLabel metaLabel;
    private JLabel restaurantNameLabel;

    public ManageRestaurantPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainPanel.showRestaurants());
        topPanel.add(backButton);
        
        restaurantNameLabel = new JLabel();
        restaurantNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        topPanel.add(restaurantNameLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        metaLabel = new JLabel();
        metaLabel.setForeground(new Color(90, 90, 90));
        metaLabel.setFont(metaLabel.getFont().deriveFont(14f));
        metaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton editMenuButton = createActionButton("Edit Menu");
        JButton monthlyReportButton = createActionButton("Monthly Report");
        JButton changeInfoButton = createActionButton("Change Restaurant Info");
        JButton manageKeywordsButton = createActionButton("Manage Keywords");
        JButton manageOrdersButton = createActionButton("Manage Orders");

        editMenuButton.addActionListener(e -> {
            if (currentRestaurant == null) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant information is missing.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainPanel.showEditMenu();
        });

        monthlyReportButton.addActionListener(e -> {
            if (currentRestaurant == null) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant information is missing.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainPanel.showMonthlyReport();
        });

        changeInfoButton.addActionListener(e -> {
            if (currentRestaurant == null) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant information is missing.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainPanel.showChangeRestaurantInfo();
        });

        manageKeywordsButton.addActionListener(e -> {
            if (currentRestaurant == null) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant information is missing.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainPanel.showManageKeywords();
        });

        manageOrdersButton.addActionListener(e -> {
            if (currentRestaurant == null) {
                JOptionPane.showMessageDialog(this,
                        "Restaurant information is missing.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainPanel.showManageOrders();
        });

        buttonPanel.add(editMenuButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(monthlyReportButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(changeInfoButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(manageKeywordsButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(manageOrdersButton);

        centerPanel.add(metaLabel);
        centerPanel.add(buttonPanel);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(centerPanel);
        add(wrapper, BorderLayout.CENTER);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.currentRestaurant = restaurant;
        refresh();
    }

    private void refresh() {
        if (currentRestaurant != null && metaLabel != null && restaurantNameLabel != null) {
            restaurantNameLabel.setText(currentRestaurant.getRestaurantName());
            metaLabel.setText(String.format(
                    "Cuisine: %s    City: %s",
                    currentRestaurant.getCuisineType(),
                    currentRestaurant.getCity()
            ));
        }
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(200, 30));
        btn.setMaximumSize(new Dimension(200, 30));
        return btn;
    }
}

