package FOS_UI.CustomerPanels;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;

public class PhoneNumbersPanel extends JPanel {
    private CustomerMainPanel mainPanel;
    private AccountDetailsPanel accountDetailsPanel;
    private JPanel phoneNumbersPanel;

    public PhoneNumbersPanel(CustomerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> mainPanel.showAccountDetails());
        topPanel.add(backButton, BorderLayout.WEST);
        JLabel titleLabel = new JLabel("Phone Numbers");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton addButton = new JButton("Add Phone Number");
        addButton.setBackground(Color.green);
        addButton.addActionListener(e -> addButtonAction());
        topPanel.add(addButton, BorderLayout.EAST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        phoneNumbersPanel = new JPanel();
        phoneNumbersPanel.setLayout(new BoxLayout(phoneNumbersPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(phoneNumbersPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh(){
        phoneNumbersPanel.removeAll();
        Customer customer = mainPanel.getCurrentCustomer();
        if(customer == null){
            phoneNumbersPanel.add(new JLabel("Please log in to view phone numbers."));
        } else {
            if(customer.getPhoneNumbers() == null || customer.getPhoneNumbers().isEmpty()){
                phoneNumbersPanel.add(new JLabel("No phone numbers found."));
            } else {
                for(String phone : customer.getPhoneNumbers()){
                JPanel phoneNumberCard = createPhoneCard(customer, phone);
                phoneNumbersPanel.add(phoneNumberCard);
                }
            }
        }
        phoneNumbersPanel.revalidate();
        phoneNumbersPanel.repaint();
    }

    private JPanel createPhoneCard(Customer customer, String phoneNumber){
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setPreferredSize(new Dimension(600, 50));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel phoneLabel = new JLabel(phoneNumber);
        phoneLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.red);
        removeButton.addActionListener(e -> removeButtonAction(phoneNumber));


        card.add(phoneLabel, BorderLayout.CENTER);
        card.add(removeButton, BorderLayout.EAST);

        return card;
    }

    private void addButtonAction(){
        AddPhoneNumberDialog dialog = new AddPhoneNumberDialog(mainPanel);
        String added = dialog.getAddedPhoneNumber();
        if(added != null){
            refresh();
        }
    }

    private void removeButtonAction(String phoneNumber){
        Customer customer = mainPanel.getCurrentCustomer();
        if(customer == null) return;

        mainPanel.getMainFrame().getAccountService().removePhoneNumber(mainPanel.getCurrentCustomer(), phoneNumber);

        if(customer.getPhoneNumbers() != null){
            customer.getPhoneNumbers().remove(phoneNumber);
        }

        refresh();
    }
}
