package FOS_UI.MockUI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private Customer registeredUser;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;

    public RegisterDialog(LoginDialog owner) {
        super(owner, "Register New Account", true);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        centerPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        centerPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        centerPanel.add(confirmPasswordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> onRegister());
        cancelButton.addActionListener(e -> dispose());
    }

    private void onRegister() {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AccountService accountService = new AccountService();
            accountService.createCustomerAccount(email, phone, password);
            Customer customer = (Customer) accountService.login(email, password);

            this.registeredUser = customer;
            JOptionPane.showMessageDialog(this, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Customer getRegisteredUser() {
        return registeredUser;
    }
}

