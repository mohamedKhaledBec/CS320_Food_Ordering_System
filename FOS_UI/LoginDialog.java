package FOS_UI;

import FOS_CORE.*;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private User loggedInUser;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

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
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> onLogin());
    }

    private void onLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            LoginService loginService = new LoginService();
            User user = loginService.login(email, password);

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
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}

