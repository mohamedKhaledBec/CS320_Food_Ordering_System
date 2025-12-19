package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.*;
import FOS_CORE.MenuItem;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.Calendar;

public class AddDiscountDialog extends JDialog {
    private MenuItem menuItem;
    private IManagerService managerService;
    private Discount resultDiscount;

    private JTextField nameField;
    private JTextField descriptionField;
    private JSpinner percentageSpinner;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;

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

        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();

        formPanel.add(new JLabel("Discount Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Percentage:"));
        formPanel.add(percentageSpinner);
        formPanel.add(new JLabel("Start Date:"));
        formPanel.add(startDateChooser);
        formPanel.add(new JLabel("End Date:"));
        formPanel.add(endDateChooser);

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

    private void onSave() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        double percentage = (Double) percentageSpinner.getValue();


        Date startDate = new Date(startDateChooser.getDate().getTime());
        Date endDate = new Date(endDateChooser.getDate().getTime());

        if (name.isEmpty() || description.isEmpty()) {
           JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (endDate.before(startDate)) {
            JOptionPane.showMessageDialog(this, "End date must be after start date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        resultDiscount = new Discount(-1, name,description,percentage,startDate,endDate);

        try {
            managerService.createDiscount(menuItem, description, percentage, startDate, endDate);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create discount: " + ex.getMessage());
        }
    }

    public Discount getDiscount() {
        return resultDiscount;
    }
}
