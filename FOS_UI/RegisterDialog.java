package FOS_UI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private Customer registeredUser;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;
    private JTextField addressLineField;
    private JComboBox cityDropdown;
    private JTextField stateField;
    private JTextField zipField;

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
        centerPanel.add(new JLabel("Address Line:"), gbc);
        gbc.gridx = 1;
        addressLineField = new JTextField(20);
        centerPanel.add(addressLineField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        cityDropdown = new JComboBox<>(getTurkishCities());
        centerPanel.add(cityDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(new JLabel("State:"), gbc);
        gbc.gridx = 1;
        stateField = new JTextField(20);
        centerPanel.add(stateField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        centerPanel.add(new JLabel("Zip:"), gbc);
        gbc.gridx = 1;
        zipField = new JTextField(20);
        centerPanel.add(zipField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
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
        String addressLine = addressLineField.getText().trim();
        String city = cityDropdown.getSelectedItem().toString();
        String state = stateField.getText().trim();
        String zip = zipField.getText().trim();
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
            Address address = new Address(-1,addressLine, city, state, zip);
            accountService.createCustomerAccount(email, phone, password, address);

            this.registeredUser = (Customer) accountService.login(email, password);
            JOptionPane.showMessageDialog(this, "Registration successful! We are signing you in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Customer getRegisteredUser() {
        return registeredUser;
    }
    private String[] getTurkishCities(){
        return new String[]{"Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
                "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
                "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum",
                "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin",
                "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli",
                "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir",
                "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
                "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt",
                "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük",
                "Kilis", "Osmaniye", "Düzce"};
    }

}

