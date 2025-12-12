package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.Discount;
import FOS_CORE.IManagerService;
import FOS_CORE.MenuItem;
import FOS_UI.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class AddDiscountDialog extends JDialog {
    private MenuItem menuItem;
    private IManagerService managerService;
    private Discount resultDiscount;

    private JTextField nameField;
    private JTextField descriptionField;
    private JSpinner percentageSpinner;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;

    public AddDiscountDialog(JDialog parent, MenuItem menuItem, IManagerService managerService) {
        super(parent, "Add Discount", true);
        this.menuItem = menuItem;
        this.managerService = managerService;
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = new JTextField();
        descriptionField = new JTextField();

        SpinnerNumberModel percentageModel = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
        percentageSpinner = new JSpinner(percentageModel);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(percentageSpinner, "0.0");
        percentageSpinner.setEditor(editor);

        startDateSpinner = createDateSpinner();
        endDateSpinner = createDateSpinner();

        formPanel.add(new JLabel("Discount Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Percentage:"));
        formPanel.add(percentageSpinner);
        formPanel.add(new JLabel("Start Date:"));
        formPanel.add(startDateSpinner);
        formPanel.add(new JLabel("End Date:"));
        formPanel.add(endDateSpinner);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> onSave());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JSpinner createDateSpinner() {
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(dateEditor);
        return spinner;
    }

    private void onSave() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        double percentage = (Double) percentageSpinner.getValue();
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();

        if (name.isEmpty() || description.isEmpty()) {
            DialogUtils.showError(this, "Please fill in all fields.");
            return;
        }

        if (endDate.before(startDate)) {
            DialogUtils.showError(this, "End date must be after start date.");
            return;
        }

        resultDiscount = new Discount();
        resultDiscount.setDiscountName(name);
        resultDiscount.setDescription(description);
        resultDiscount.setDiscountPercentage(percentage);
        resultDiscount.setStartDate(new Timestamp(startDate.getTime()));
        resultDiscount.setEndDate(new Timestamp(endDate.getTime()));

        try {
            managerService.createDiscount(null, menuItem, description, percentage,
                new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to add discount: " + ex.getMessage());
        }
    }

    public Discount getDiscount() {
        return resultDiscount;
    }
}
