package FOS_UI.MockUI.CustomerPanels;

import FOS_CORE.*;
import FOS_UI.MockUI.MainFrame;

import javax.swing.*;
import java.awt.*;

public class RateOrderDialog  extends JDialog{
    private CustomerMainPanel mainPanel;
    private Order order;
    private JSpinner ratingSpinner;
    private JTextArea ratingText;

    public RateOrderDialog(CustomerMainPanel mainPanel, Order order) {
        super(mainPanel.getMainFrame(), "Rate Order", true);
        this.order = order;
        this.mainPanel = mainPanel;
        initComponents();
        pack();
        setLocationRelativeTo(mainPanel);
    }

    public void initComponents(){
        setLayout(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new GridLayout(-1,1));
        ratingText = new JTextArea(5,10);
        ratingSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JPanel panel = new JPanel(new GridLayout(-1,1));
        panel.add(new JLabel());
        panel.add(new JLabel("Rating"));
        panel.add(ratingSpinner);
        panel.add(new JLabel("Write your thoughts here"));
        centerPanel.add(panel);
        centerPanel.add(ratingText);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit Rating");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        submitButton.addActionListener(e -> {
            onSubmit();
        });
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(mainPanel.getMainFrame());
        setVisible(true);
    }

    public void onSubmit(){
        try {
            int rating = (Integer) ratingSpinner.getValue();
            String comment = ratingText.getText();
            OrderService orderService = mainPanel.getOrderService();
            orderService.rateOrder(order, rating, comment != null ? comment : "");
            JOptionPane.showMessageDialog(this, "Thank you for your rating!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rating value.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
        mainPanel.getOrderHistoryPanel().refresh();
    }


}
