package FOS_UI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;

// Being Done by Umair Ahmad
public class LoginWindow extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JButton clearButton;
    private JButton exitButton;
    private JButton registerButton;

    public LoginWindow() {
        setTitle("Food Ordering System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 250);
        setLocationRelativeTo(null); // center on screen

        initComponents();
    }

    private void initComponents() {
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        showPasswordCheckBox = new JCheckBox("Show password");
        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");
        registerButton = new JButton("Register");

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // No masking to the input
            } else {
                passwordField.setEchoChar('•');
            }
        });

        loginButton.addActionListener(e -> handleLogin());

        clearButton.addActionListener(e -> {
            emailField.setText("");
            passwordField.setText("");
        });

        exitButton.addActionListener(e -> System.exit(0));

        registerButton.addActionListener(e -> {
            // TO BE WORKED ON
            DialogUtils.showInfo(this, "Registration screen to be implemented.");
        });

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email: "), gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(showPasswordCheckBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        getContentPane().setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (!InputValidator.isNonEmpty(email) || !InputValidator.isNonEmpty(password)) {
            DialogUtils.showError(this, "Please enter both email and password.");
            return;
        }
        if (!InputValidator.isValidEmail(email)) {
            DialogUtils.showError(this, "Please enter a valid email address.");
            return;
        }
        IAccountService accountService = ServiceContext.getAccountService();
        User user = accountService.login(email, password);

        if (user == null) {
            DialogUtils.showError(this, "Invalid email or password.");
            return;
        }

        Session.setCurrentUser(user);

        if (user instanceof Customer) {
            DialogUtils.showInfo(this, "Logged in as customer.");
            CustomerDashboardWindow dashboard = new CustomerDashboardWindow();
            dashboard.setVisible(true);
            dispose();
        } else if (user instanceof Manager) {
            DialogUtils.showInfo(this, "Logged in as manager.");
            ManagerDashboardWindow dashboard = new ManagerDashboardWindow();
            dashboard.setVisible(true);
            dispose();
        } else {
            DialogUtils.showError(this, "Unknown user type.");
            Session.clear();
        }
    }
}
