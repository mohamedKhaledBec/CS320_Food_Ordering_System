package FOS_UI.CustomerPanels;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderHistoryPanel extends JPanel {
    private CustomerMainPanel mainPanel;
    private JPanel ordersPanel;

    public OrderHistoryPanel(CustomerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> mainPanel.showAccountDetails());
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        ordersPanel.removeAll();
        Customer customer = mainPanel.getCurrentCustomer();
        if (customer == null) {
            ordersPanel.add(new JLabel("Please log in to view orders."));
        } else {
            ArrayList<Order> orders = customer.getOrders();
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
        card.setPreferredSize(new Dimension(600, 150));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel orderIdLabel = new JLabel("Order #" + order.getOrderID());
        orderIdLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JLabel dateLabel = new JLabel("Date: " + dateFormat.format(order.getCreationDate()));
        JLabel statusLabel = new JLabel("Status: " + order.getStatus());
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
        infoPanel.add(statusLabel);
        infoPanel.add(restaurantLabel);
        infoPanel.add(totalPriceLabel);
        infoPanel.add(cardNumberLabel);
        infoPanel.add(phoneLabel);

        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.add(new JLabel("Items:"), BorderLayout.NORTH);
        itemsPanel.add(new JScrollPane(itemsArea), BorderLayout.CENTER);

        if (order.getRating() == null && order.getStatus() == OrderStatus.DELIVERED) {
            JButton rateButton = new JButton("Rate Order");
            rateButton.addActionListener(e -> rateOrder(order));
            itemsPanel.add(rateButton, BorderLayout.SOUTH);
        } else if (order.getRating() != null) {
            JLabel ratingLabel = new JLabel("Rating: " + order.getRating().getRatingValue() + "/5" +
                    (order.getRating().getReviewText().isEmpty() ? "" : " - \"" + order.getRating().getReviewText() + "\""));
            itemsPanel.add(ratingLabel, BorderLayout.SOUTH);
        }

        card.add(infoPanel, BorderLayout.WEST);
        card.add(itemsPanel, BorderLayout.CENTER);
        return card;
    }

    private void rateOrder(Order order) {
        RateOrderDialog rateOrderDialog = new RateOrderDialog(this.mainPanel, order);
        initComponents();
    }
}

