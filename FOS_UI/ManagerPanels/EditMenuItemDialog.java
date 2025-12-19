package FOS_UI.ManagerPanels;

import FOS_CORE.MenuItem;
import FOS_CORE.Restaurant;

import javax.swing.*;
import java.awt.*;

public class EditMenuItemDialog extends JDialog {
    private MenuItem menuItem;
    private ManagerMainPanel mainPanel;

    private JTextField nameField;
    private JTextField priceField;
    private JTextArea descriptionArea;

    private MenuItem resultMenuItem;

    public EditMenuItemDialog(ManagerMainPanel mainPanel, MenuItem menuItem, Restaurant restaurant) {
        super(mainPanel.getMainFrame(), menuItem == null ? "Add Menu Item" : "Edit Menu Item", true);
        this.mainPanel = mainPanel;
        this.menuItem = menuItem;
        initComponents();
        pack();
        setLocationRelativeTo(mainPanel.getMainFrame());
    }

    private void initComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(30);
        if (menuItem != null) {
            nameField.setText(menuItem.getItemName());
        }

        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField(30);
        if (menuItem != null) {
            priceField.setText(String.valueOf(menuItem.getPrice()));
        }

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        if (menuItem != null) {
            descriptionArea.setText(menuItem.getDescription());
        }

        add(nameLabel);
        add(nameField);
        add(Box.createVerticalStrut(5));
        add(priceLabel);
        add(priceField);
        add(Box.createVerticalStrut(5));
        add(descriptionLabel);
        add(new JScrollPane(descriptionArea));
        add(Box.createVerticalStrut(10));

        JPanel buttonsSection = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Add Menu Item");
        saveButton.addActionListener(e -> onSave());
        if(menuItem != null){
            saveButton.setText("Update Menu Item");
            JButton manageDiscountsButton = new JButton("Manage Discounts");
            manageDiscountsButton.addActionListener(e -> onManageDiscounts());
            buttonsSection.add(manageDiscountsButton);
        }
        buttonsSection.add(saveButton);
        add(buttonsSection);
    }

    private void onSave() {
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        String priceText = priceField.getText().trim();

        if (!name.isEmpty() && !desc.isEmpty() && !priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Price must be a number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (menuItem == null) {
            resultMenuItem = new MenuItem();
        } else {
            resultMenuItem = menuItem;
        }
        
        resultMenuItem.setItemName(name);
        resultMenuItem.setDescription(desc);
        resultMenuItem.setPrice(price);

        dispose();
    }
    private void onManageDiscounts(){
        ManageDiscountsPanel discountsPanel = new ManageDiscountsPanel(this, menuItem,mainPanel.getManagerService());
        discountsPanel.setVisible(true);
    }

    public MenuItem getMenuItem() {
        return resultMenuItem;
    }
}

