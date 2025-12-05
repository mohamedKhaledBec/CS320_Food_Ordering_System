package FOS_UI.MockUI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Simple Swing dialog that mirrors the Python /confirm_cart flow at a high level:
 * - lets the customer review total amount,
 * - choose a stored card,
 * - invoke the payment service.
 *
 * The responsibility of actually creating/persisting the Order remains in
 * the service / data layer (OrderService, UserDataAccess, etc.).
 */
public class CheckoutDialog extends JDialog {

    private final Customer customer;
    private final List<CartItem> cartItems;
    private final Address deliveryAddress;
    private final Restaurant restaurant;

    private JComboBox<Card> cardComboBox;
    private JLabel totalLabel;
    private JButton payButton;
    private JButton cancelButton;

    private TransactionRecord lastTransaction;

    public CheckoutDialog(Frame owner,
                          Customer customer,
                          List<CartItem> cartItems,
                          Address deliveryAddress,
                          Restaurant restaurant) {
        super(owner, "Checkout", true);
        this.customer = customer;
        this.cartItems = cartItems;
        this.deliveryAddress = deliveryAddress;
        this.restaurant = restaurant;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        double total = calculateCartTotal();
        totalLabel = new JLabel("Total: " + String.format("%.2f", total));
        centerPanel.add(totalLabel);

        // Card selection
        cardComboBox = new JComboBox<>();
        for (Card card : customer.getCards()) {
            cardComboBox.addItem(card);
        }
        cardComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Card) {
                    Card card = (Card) value;
                    String number = String.valueOf(card.getCardNumber());
                    String last4 = number.length() > 4 ? number.substring(number.length() - 4) : number;
                    setText(card.getCardHolderName() + " - **** " + last4);
                }
                return this;
            }
        });
        centerPanel.add(new JLabel("Select card:"));
        centerPanel.add(cardComboBox);

        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        payButton = new JButton("Pay");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.add(payButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        cancelButton.addActionListener(e -> dispose());
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPay();
            }
        });
    }

    private double calculateCartTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getItem().getPrice() * item.getQuantity();
        }
        return total;
    }

    private void onPay() {
        if (cardComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "You do not have any saved cards. Please add a card first.",
                    "No Card Found",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Card selectedCard = (Card) cardComboBox.getSelectedItem();
        if (selectedCard == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a card.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double totalAmount = calculateCartTotal();

        IPaymentService paymentService = new PaymentService();
        IPaymentDetails paymentDetails = new PaymentDetails(customer, selectedCard, totalAmount);

        try {
            TransactionRecord record = paymentService.processPayment(paymentDetails, totalAmount);
            this.lastTransaction = record;

            if (record.getStatus() == PaymentStatus.SUCCESS) {
                JOptionPane.showMessageDialog(this,
                        "Payment successful.\nTransaction ID: " + record.getTransactionID(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                // In a full implementation, you would now:
                // 1. Create an Order instance from cartItems + deliveryAddress + restaurant
                // 2. Persist it via OrderService/UserDataAccess
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Payment failed: " + record.getMessage(),
                        "Payment Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Validation error: " + ex.getMessage(),
                    "Payment Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error during payment: " + ex.getMessage(),
                    "Payment Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public TransactionRecord getLastTransaction() {
        return lastTransaction;
    }
}


