package FOS_UI.MockUI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderHistoryPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel ordersPanel;

    public OrderHistoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainFrame.showRestaurants());
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        ordersPanel.removeAll();
        Customer customer = mainFrame.getCurrentCustomer();
        if (customer == null) {
            ordersPanel.add(new JLabel("Please log in to view orders."));
        } else {
            List<Order> orders = customer.getOrders();
            if (orders == null || orders.isEmpty()) {
                ordersPanel.add(new JLabel("No orders found."));
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                for (Order order : orders) {
                    JPanel orderCard = createOrderCard(order, dateFormat);
                    ordersPanel.add(orderCard);
                }
            }
        }
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private JPanel createOrderCard(Order order, SimpleDateFormat dateFormat) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel orderIdLabel = new JLabel("Order #" + order.getOrderID());
        orderIdLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JLabel dateLabel = new JLabel("Date: " + dateFormat.format(order.getCreationDate()));
        JLabel statusLabel = new JLabel("Status: " + order.getStatus());
        JLabel restaurantLabel = new JLabel("Restaurant: " + order.getRestaurantName());

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(orderIdLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(restaurantLabel);

        JTextArea itemsArea = new JTextArea(3, 30);
        itemsArea.setEditable(false);
        StringBuilder itemsText = new StringBuilder("Items:\n");
        for (CartItem item : order.getItems()) {
            itemsText.append(String.format("  - %s x%d\n", item.getItem().getItemName(), item.getQuantity()));
        }
        itemsArea.setText(itemsText.toString());

        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.add(new JLabel("Items:"), BorderLayout.NORTH);
        itemsPanel.add(new JScrollPane(itemsArea), BorderLayout.CENTER);

        if (order.getRating() == null && order.getStatus() == OrderStatus.DELIVERED) {
            JButton rateButton = new JButton("Rate Order");
            rateButton.addActionListener(e -> rateOrder(order));
            itemsPanel.add(rateButton, BorderLayout.SOUTH);
        } else if (order.getRating() != null) {
            JLabel ratingLabel = new JLabel("Rating: " + order.getRating().getRatingValue() + "/5");
            itemsPanel.add(ratingLabel, BorderLayout.SOUTH);
        }

        card.add(infoPanel, BorderLayout.WEST);
        card.add(itemsPanel, BorderLayout.CENTER);
        return card;
    }

    private void rateOrder(Order order) {
        String ratingStr = JOptionPane.showInputDialog(this, "Rate this order (1-5):", "Rate Order", JOptionPane.QUESTION_MESSAGE);
        if (ratingStr != null) {
            try {
                int rating = Integer.parseInt(ratingStr);
                if (rating < 1 || rating > 5) {
                    JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String comment = JOptionPane.showInputDialog(this, "Add a comment (optional):", "Comment", JOptionPane.QUESTION_MESSAGE);
                OrderService orderService = mainFrame.getOrderService();
                orderService.rateOrder(order, rating, comment != null ? comment : "");
                JOptionPane.showMessageDialog(this, "Thank you for your rating!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid rating value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

