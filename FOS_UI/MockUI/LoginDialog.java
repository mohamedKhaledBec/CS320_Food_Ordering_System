package FOS_UI.MockUI;

import FOS_CORE.*;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private User loggedInUser;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginDialog(Frame owner) {
        super(owner, "Food Ordering System - Login", true);
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
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> onLogin());
        registerButton.addActionListener(e -> onRegister());
    }

    private void onLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AccountService accountService = new AccountService();
            User user = accountService.login(email, password);

            if (user != null) {
                this.loggedInUser = user;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Login error: " + e.getMessage() + "\n\nPlease check:\n- Database is running\n- Database connection settings",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void onRegister() {
        RegisterDialog registerDialog = new RegisterDialog(this);
        registerDialog.setVisible(true);
        if (registerDialog.getRegisteredUser() != null) {
            this.loggedInUser = registerDialog.getRegisteredUser();
            dispose();
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}

