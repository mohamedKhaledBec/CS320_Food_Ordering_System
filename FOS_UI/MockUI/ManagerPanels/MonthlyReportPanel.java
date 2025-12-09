package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.IManagerService;
import FOS_CORE.Manager;
import FOS_CORE.Restaurant;
import FOS_UI.DialogUtils;
import FOS_UI.ServiceContext;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class MonthlyReportPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant restaurant;

    private JComboBox<String> monthDropdown;
    private JComboBox<String> yearDropdown;
    private JButton generateBtn;
    private JTextArea reportArea;

    public MonthlyReportPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainPanel.showManageRestaurant(mainPanel.getCurrentRestaurant()));
        topPanel.add(backButton);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthDropdown = new JComboBox<>(new String[]{
                "01 - January", "02 - February", "03 - March",
                "04 - April", "05 - May", "06 - June",
                "07 - July", "08 - August", "09 - September",
                "10 - October", "11 - November", "12 - December"
        });

        yearDropdown = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y >= currentYear - 5; y--) {
            yearDropdown.addItem(String.valueOf(y));
        }

        generateBtn = new JButton("Generate Report");
        generateBtn.addActionListener(e -> onGenerateReport());

        controlsPanel.add(new JLabel("Month:"));
        controlsPanel.add(monthDropdown);
        controlsPanel.add(new JLabel("Year:"));
        controlsPanel.add(yearDropdown);
        controlsPanel.add(generateBtn);

        topPanel.add(controlsPanel);
        add(topPanel, BorderLayout.NORTH);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void onGenerateReport() {
        Manager manager = mainPanel.getCurrentManager();
        if (manager == null) {
            DialogUtils.showError(this, "Only managers can view monthly reports.");
            return;
        }

        try {
            IManagerService ms = ServiceContext.getManagerService();

            if (restaurant == null) {
                DialogUtils.showError(this, "Restaurant not found.");
                return;
            }

            String monthString = (String) monthDropdown.getSelectedItem();
            String yearString = (String) yearDropdown.getSelectedItem();
            if (monthString == null || yearString == null) {
                DialogUtils.showError(this, "Please select both month and year.");
                return;
            }

            int month = Integer.parseInt(monthString.substring(0, 2));
            int year = Integer.parseInt(yearString);

            LocalDate date = LocalDate.of(year, month, 1);
            Date sqlDate = Date.valueOf(date);

            String report = ms.generateMonthlyReport(manager, restaurant, sqlDate);

            if (report == null || report.trim().isEmpty()) {
                reportArea.setText("No report available for the selected month.");
            } else {
                reportArea.setText(report);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtils.showError(this, "Failed to generate monthly report.");
        }
    }
}

