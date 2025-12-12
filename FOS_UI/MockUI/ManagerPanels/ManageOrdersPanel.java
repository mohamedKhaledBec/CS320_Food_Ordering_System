package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.*;
import FOS_UI.DialogUtils;

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
            DialogUtils.showError(this, "Failed to load orders: " + ex.getMessage());
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

        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.add(new JLabel("Order ID: " + order.getOrderID()));
        infoPanel.add(new JLabel("Date: " + order.getCreationDate()));
        infoPanel.add(new JLabel("Address: " + order.getDeliveryAddress()));
        infoPanel.add(new JLabel("Phone: " + order.getPhoneNumber()));

        double total = 0;
        for (CartItem item : order.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        infoPanel.add(new JLabel(String.format("Total: $%.2f", total)));

        card.add(infoPanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }

    private void updateOrderStatus(Order order, OrderStatus newStatus) {
        try {
            IManagerService managerService = mainPanel.getManagerService();
            managerService.updateOrderStatus(order, newStatus.toString());
            order.setStatus(newStatus);
            DialogUtils.showInfo(this, "Order status updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to update order status: " + ex.getMessage());
            refresh();
        }
    }
}
