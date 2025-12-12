package FOS_UI.MockUI.CustomerPanels;

import FOS_CORE.AccountService;
import FOS_CORE.Customer;

import javax.swing.*;

public class AddPhoneNumberDialog extends JDialog{
    private CustomerMainPanel mainPanel;
    private JTextField phoneNumberField;

    private AccountService accountService = new AccountService();

    public AddPhoneNumberDialog(CustomerMainPanel mainPanel) {
        super(mainPanel.getMainFrame(), "Add New Phone Number", true);
        this.mainPanel = mainPanel;
        this.accountService = new AccountService();

        initComponents();
        pack();
        setLocationRelativeTo(mainPanel.getMainFrame());
    }

    public void initComponents(){
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        phoneNumberField = new JTextField(16);

        add(new JLabel("Phone Number:"));
        add(phoneNumberField);

        JButton addButton = new JButton("Add Phone Number");
        addButton.addActionListener(e -> {onAddPhone();});
        add(addButton);
        pack();
        setLocationRelativeTo(mainPanel.getMainFrame());
        setVisible(true);

    }

    public String getAddedPhoneNumber(){
        return phoneNumberField.getText().trim();
    }

    private void onAddPhone(){
        String phoneNumber = phoneNumberField.getText().trim();

        if(phoneNumber.length() < 7 || phoneNumber.length() > 13){
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer currentCustomer = mainPanel.getCurrentCustomer();
        accountService.addPhoneNumber(currentCustomer, phoneNumber);

        JOptionPane.showMessageDialog(this, "Phone number added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
