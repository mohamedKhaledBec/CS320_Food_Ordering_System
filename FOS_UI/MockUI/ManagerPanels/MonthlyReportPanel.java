package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.IManagerService;
import FOS_CORE.Manager;
import FOS_CORE.Restaurant;
import FOS_UI.DialogUtils;
import FOS_UI.ServiceContext;
import com.toedter.calendar.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.Calendar;

public class MonthlyReportPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant restaurant;

    private JMonthChooser monthChooser;
    private JYearChooser yearChooser;
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

        monthChooser = new JMonthChooser();
        yearChooser = new JYearChooser();

        generateBtn = new JButton("Generate Report");
        generateBtn.addActionListener(e -> onGenerateReport());

        controlsPanel.add(new JLabel("Month:"));
        controlsPanel.add(monthChooser);
        controlsPanel.add(new JLabel("Year:"));
        controlsPanel.add(yearChooser);
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

            int month = monthChooser.getMonth() + 1;
            int year = yearChooser.getYear();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            Date sqlDate = new Date(cal.getTimeInMillis());

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
