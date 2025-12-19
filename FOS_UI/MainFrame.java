package FOS_UI;

import FOS_CORE.*;
import FOS_UI.CustomerPanels.*;
import FOS_UI.ManagerPanels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private AccountService accountService;
    private CustomerMainPanel customerMainPanel;
    private ManagerMainPanel managerMainPanel;



    public MainFrame() {
        super("Food Ordering System");
        accountService = new AccountService();
        showLogin();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    // java
    public void showLogin() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);
        User user = loginDialog.getLoggedInUser();
        if (user instanceof Customer) {
            Customer currentCustomer = (Customer) user;
            customerMainPanel = new CustomerMainPanel(this, currentCustomer);
            add(customerMainPanel, BorderLayout.CENTER);
            setVisible(true);
        } else if (user instanceof Manager) {
            Manager currentManager = (Manager) user;
            managerMainPanel = new ManagerMainPanel(this, currentManager);
            add(managerMainPanel, BorderLayout.CENTER);
            setVisible(true);
        } else {
            System.exit(0);
        }
    }

    public void logout() {
        this.setVisible(false);
        this.dispose();
        this.getContentPane().removeAll();
        showLogin();
    }
    public AccountService getAccountService() {
        return accountService;
    }
    public CustomerMainPanel getCustomerMainPanel() {
        return customerMainPanel;
    }

    public ManagerMainPanel getManagerMainPanel() {
        return managerMainPanel;
    }
}