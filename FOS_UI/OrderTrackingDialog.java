package FOS_UI;

// Being done by Hassan Askari

import FOS_CORE.Order;
import FOS_CORE.OrderStatus;
import FOS_CORE.Rating;

import javax.swing.*;
import java.awt.*;

public class OrderTrackingDialog extends JDialog {

    private final Order order;

    public OrderTrackingDialog(JFrame parent, Order order) {
        super(parent, "Order Tracking", true);
        this.order = order;

        setSize(450, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents()
    {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Order ID:"), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(order.getOrderID())), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(order.getStatus())), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Created On:"), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(order.getCreationDate().toString()), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Delivery Address:"), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(order.getDeliveryAddress()), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Restaurant:"), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(order.getRestaurantName()), gbc);

        Rating rating = order.getRating();
        if (rating != null) {

            gbc.gridx = 0; gbc.gridy = 5;
            panel.add(new JLabel("Rating:"), gbc);

            gbc.gridx = 1;
            panel.add(new JLabel(rating.getRatingValue() + " / 5"), gbc);

            gbc.gridx = 0; gbc.gridy = 6;
            panel.add(new JLabel("Review:"), gbc);

            gbc.gridx = 1;
            panel.add(new JLabel(rating.getReviewText()), gbc);
        }

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        gbc.gridx = 1; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(closeBtn, gbc);

        add(panel);
    }
}
