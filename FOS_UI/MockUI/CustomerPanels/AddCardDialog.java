package FOS_UI.MockUI.CustomerPanels;

import FOS_CORE.*;
import FOS_UI.MockUI.MainFrame;

import javax.swing.*;
import java.sql.Date;

public class AddCardDialog extends JDialog {
    private JDialog owner;
    private JTextField cardNumber;
    private JTextField cardHolderName;
    private JFormattedTextField expiryDate;
    private JTextField cvv;
    private AccountService accountService = new AccountService();
    private Card addedCard;
    public AddCardDialog(JDialog owner) {
        super(owner, "Add New Card", true);
        this.owner = owner;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }
    private void initComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        cardNumber = new JTextField(16);
        cardHolderName = new JTextField(30);
        expiryDate = new JFormattedTextField("MM/YY");
        cvv = new JTextField(3);

        add(new JLabel("Card Number:"));
        add(cardNumber);
        add(new JLabel("Card Holder Name:"));
        add(cardHolderName);
        add(new JLabel("Expiry Date:"));
        add(expiryDate);
        add(new JLabel("CVV:"));
        add(cvv);

        JButton addButton = new JButton("Add Card");
        addButton.addActionListener(e -> {onAddCard();});
        add(addButton);
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);

    }
    private void onAddCard() {
        String number = cardNumber.getText().trim();
        String holderName = cardHolderName.getText().trim();
        String expiry = expiryDate.getText().trim();
        String cvvCode = cvv.getText().trim();
        Date expiryDate = getDateFromString(expiry) ;



        if (number.isEmpty() || holderName.isEmpty() || expiry.toString().isEmpty() || cvvCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        addedCard = new Card(number, holderName, expiryDate, cvvCode);
        Customer currentCustomer = ((MainFrame) owner.getOwner()).getCustomerMainPanel().getCurrentCustomer();
        accountService.addCardToCustomer(currentCustomer, addedCard);

        JOptionPane.showMessageDialog(this, "Card added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    public Card getAddedCard() {
        return addedCard;
    }
    private Date getDateFromString(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid date format. Use MM/YY.");
            }
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Month must be between 1 and 12.");
            }
            String formattedDate = String.format("%04d-%02d-01", year, month);
            return Date.valueOf(formattedDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Use MM/YY.");
        }
    }
}
