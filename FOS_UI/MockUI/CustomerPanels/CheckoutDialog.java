package FOS_UI.MockUI.CustomerPanels;

import FOS_CORE.*;
import FOS_UI.MockUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CheckoutDialog extends JDialog {

    private final Address deliveryAddress;
    private final Restaurant restaurant;
    private final String selectedPhoneNumber;
    private final CustomerMainPanel mainPanel;

    private JComboBox<Card> cardComboBox;
    private JLabel totalLabel;
    private JButton payButton;
    private JButton cancelButton;
    private JButton addCardButton;

    public CheckoutDialog(CustomerMainPanel mainPanel,
                          Address deliveryAddress,
                          Restaurant restaurant,
                          String selectedPhoneNumber) {
        super(mainPanel.getMainFrame(), "Checkout", true);
        this.mainPanel = mainPanel;
        this.deliveryAddress = deliveryAddress;
        this.restaurant = restaurant;
        this.selectedPhoneNumber = selectedPhoneNumber;
        initComponents();
        pack();
        setLocationRelativeTo(mainPanel.getMainFrame());
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        double total = calculateCartTotal();
        totalLabel = new JLabel("Total: " + String.format("%.2f", total));
        centerPanel.add(totalLabel);

        cardComboBox = new JComboBox<>();
        for (Card card : mainPanel.getCurrentCustomer().getCards()) {
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addCardButton = new JButton("Add Card");
        addCardButton.addActionListener(e -> addCard());
        buttonPanel.add(addCardButton);
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        if(cardComboBox.getItemCount() != 0){
            centerPanel.add(new JLabel("Select card:"));
            centerPanel.add(cardComboBox);
            payButton = new JButton("Pay");
            buttonPanel.add(payButton);
        }

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        payButton.addActionListener(e -> onPay());
    }

    private double calculateCartTotal() {
        double total = 0.0;
        for (CartItem item : mainPanel.getCurrentCustomer().getCart()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private void onPay() {
        if (cardComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "You do not have any saved cards. Please add a card first.",
                    "No Card Found",
                    JOptionPane.WARNING_MESSAGE);
        }

        Card selectedCard = (Card) cardComboBox.getSelectedItem();
        if (selectedCard == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a card.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Order order = new Order(deliveryAddress.toString(), (ArrayList<CartItem>) mainPanel.getCurrentCustomer().getCart().clone(), restaurant.getRestaurantName(), selectedPhoneNumber, selectedCard.getCardNumber());
        try {
            mainPanel.getOrderService().placeOrder(
                    mainPanel.getCurrentCustomer(),
                    deliveryAddress,order,
                    restaurant
            );
            mainPanel.getCurrentCustomer().getCart().clear();
            dispose();
            mainPanel.showOrderHistory();
            JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to place order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCard() {
        AddCardDialog addCardDialog = new AddCardDialog(this);
        Card newCard = addCardDialog.getAddedCard();
        if (newCard != null) {
            cardComboBox.addItem(newCard);
            cardComboBox.setSelectedItem(newCard);
        }
        cardComboBox.revalidate();
        cardComboBox.repaint();
    }
}


