package FOS_UI.MockUI;

import FOS_CORE.*;
import javax.swing.*;
import java.awt.*;

public class AccountDetailsPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel emailLabel;
    private JPanel orderHistoryPanel;
    private JPanel phoneNumbersPanel;

    public AccountDetailsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Restaurants");
        backButton.addActionListener(e -> mainFrame.showRestaurants());
        topPanel.add(backButton, BorderLayout.WEST);
        // Title
        JLabel titleLabel = new JLabel("Account Information");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4,1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 20)); // margin
        GridLayout gridLayout = new GridLayout(-1,2);
        gridLayout.setHgap(20);
        gridLayout.setVgap(20);
        JPanel centerPanel1 = new JPanel(gridLayout);




        emailLabel = new JLabel("N/A");
        emailLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 48));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(emailLabel);


        // Buttons
        JButton orderButton = new JButton("Order");
        JButton phoneButton = new JButton("Phone Numbers");
        JButton addressButton = new JButton("Address");
        JButton cardButton = new JButton("Cards");

        centerPanel1.add(orderButton);
        orderButton.addActionListener(e -> showOrderHistory());
        centerPanel1.add(phoneButton);
        phoneButton.addActionListener(e -> {showPhoneNumbers();});
        centerPanel1.add(addressButton);
        addressButton.addActionListener(e -> {showAddresses();});
        centerPanel1.add(cardButton);
        cardButton.addActionListener(e -> {showCards();});
        centerPanel.add(centerPanel1);


        add(centerPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        Customer customer = mainFrame.getCurrentCustomer();
        if (customer != null) {
            emailLabel.setText(customer.getEmail() != null ? customer.getEmail() : "N/A");
            String phone = "N/A";
            if (customer.getPhoneNumbers() != null && !customer.getPhoneNumbers().isEmpty()) {
                phone = customer.getPhoneNumbers().get(0);
            }
        } else {
            emailLabel.setText("N/A");
        }
    }
    public void showOrderHistory() {
        mainFrame.showOrderHistory();
    }
    public void showAddresses() {
        mainFrame.showAddresses();
    }
    public void showPhoneNumbers() {
        mainFrame.showPhoneNumbers();
    }
    public void showCards() {
        mainFrame.showCards();
    }
}
