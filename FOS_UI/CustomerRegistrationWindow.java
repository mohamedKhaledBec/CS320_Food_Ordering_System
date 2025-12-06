package FOS_UI;
//Being Implemented by Hassan Askari
import FOS_CORE.Address;
import FOS_CORE.Customer;
import FOS_CORE.IAccountService;

import javax.swing.*;
import java.awt.*;

public class CustomerRegistrationWindow extends JFrame {

    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton clearButton;
    private JButton cancelButton;

    public CustomerRegistrationWindow() {
        setTitle("Food Ordering System - Register");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents()
    {

        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        passwordField = new JPasswordField(20);

        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        cancelButton = new JButton("Cancel");

        registerButton.addActionListener(e -> handleRegistration());

        clearButton.addActionListener(e ->
        {
            emailField.setText("");
            phoneField.setText("");
            passwordField.setText("");
        });

        cancelButton.addActionListener(e -> dispose());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(registerButton);

        getContentPane().setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleRegistration()
    {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (!InputValidator.isNonEmpty(email) ||
                !InputValidator.isNonEmpty(password))
        {
            DialogUtils.showError(this, "Email and password are required.");
            return;
        }

        if (!InputValidator.isValidEmail(email))
        {
            DialogUtils.showError(this, "Please enter a valid email address.");
            return;
        }

        try
        {
            IAccountService accountService = ServiceContext.getAccountService();
            Address address = new Address(-1,"1","1","1","1"); // Placeholder as address is not collected in this form
            Customer created = accountService.createCustomerAccount(email, phone, password, address);

            if (created != null)
            {
                DialogUtils.showInfo(this, "Account created successfully!");

                SwingUtilities.invokeLater(() -> {
                    new LoginWindow().setVisible(true);
                });

                dispose();
            }
            else
            {
                DialogUtils.showError(this, "Failed to create account.");
            }

        }
        catch (Exception ex)
        {
            DialogUtils.showError(this, "Error: " + ex.getMessage());
        }
    }
}
