package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ManageOrdersPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant restaurant;
    private JPanel ordersListPanel;
    private ArrayList<Order> orders;

    public ManageOrdersPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        if (restaurant != null) {
            loadOrders();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainPanel.showManageRestaurant(mainPanel.getCurrentRestaurant()));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Manage Orders");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadOrders() {
        try {
            orders = mainPanel.getRestaurantService().fetchRestaurantOrdersForToday(restaurant);
            if (orders == null) {
                orders = new ArrayList<>();
            }
            refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load orders: " + ex.getMessage());
        }
    }

    public void refresh() {
        ordersListPanel.removeAll();

        if (orders == null || orders.isEmpty()) {
            ordersListPanel.add(new JLabel("No orders available."));
        } else {
            for (Order order : orders) {
                JPanel orderCard = createOrderCard(order);
                ordersListPanel.add(orderCard);
                ordersListPanel.add(Box.createVerticalStrut(10));
            }
        }

        ordersListPanel.revalidate();
        ordersListPanel.repaint();
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel orderIdLabel = new JLabel("Order #" + order.getOrderID());
        orderIdLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JLabel dateLabel = new JLabel("Date: " + order.getCreationDate());
        JLabel restaurantLabel = new JLabel("Restaurant: " + order.getRestaurantName());
        JLabel cardNumberLabel = new JLabel("Paid with Card: **** **** **** " + order.getCardNumber().substring(order.getCardNumber().length() - 4));
        JLabel phoneLabel = new JLabel("Contact Phone: " + order.getPhoneNumber());

        JTextArea itemsArea = new JTextArea(3, 30);
        itemsArea.setEditable(false);
        StringBuilder itemsText = new StringBuilder();
        float total = 0;
        for (CartItem item : order.getItems()) {
            itemsText.append(String.format("    %d %s                item price: %.2f \n",  item.getQuantity(), item.getMenuItem().getItemName(), item.getPrice()));
            total += item.getQuantity() * item.getPrice();
        }
        itemsArea.setText(itemsText.toString());
        JLabel totalPriceLabel = new JLabel("Total Price: " + String.format("%.2f", total));

        JPanel infoPanel = new JPanel(new GridLayout(-1, 1));
        infoPanel.add(orderIdLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(restaurantLabel);
        infoPanel.add(totalPriceLabel);
        infoPanel.add(cardNumberLabel);
        infoPanel.add(phoneLabel);

        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.add(new JLabel("Items:"), BorderLayout.NORTH);
        itemsPanel.add(new JScrollPane(itemsArea), BorderLayout.CENTER);
        JLabel ratingLabel = new JLabel();
        if (order.getRating() == null && order.getStatus() == OrderStatus.DELIVERED) {
            ratingLabel.setText("Order Not Yet Rated");
        } else if (order.getRating() != null) {
            ratingLabel.setText("Rating: " + order.getRating().getRatingValue() + "/5" +
                    (order.getRating().getReviewText().isEmpty() ? "" : " - \"" + order.getRating().getReviewText() + "\""));
        }
        itemsPanel.add(ratingLabel, BorderLayout.SOUTH);

        JPanel statusPanel = new JPanel(new GridLayout(5, 1));
        statusPanel.add(new JLabel("Status:"));

        JComboBox<OrderStatus> statusDropdown = new JComboBox<>(OrderStatus.values());
        statusDropdown.setSelectedItem(order.getStatus());
        statusDropdown.addActionListener(e -> {
            OrderStatus newStatus = (OrderStatus) statusDropdown.getSelectedItem();
            if (newStatus != order.getStatus()) {
                updateOrderStatus(order, newStatus);
            }
        });

        statusPanel.add(statusDropdown);
        card.add(infoPanel, BorderLayout.WEST);
        card.add(itemsPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }

    private void updateOrderStatus(Order order, OrderStatus newStatus) {
        try {
            IManagerService managerService = mainPanel.getManagerService();
            managerService.updateOrderStatus(order, newStatus.toString());
            JOptionPane.showMessageDialog(this, "Order status updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update order status: " + ex.getMessage());
            refresh();
        }
    }
}
