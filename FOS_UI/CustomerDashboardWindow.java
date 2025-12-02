package FOS_UI;
import javax.swing.*;
import java.awt.*;
// Being Done by Umair Ahmad
public class CustomerDashboardWindow extends JFrame {

    public CustomerDashboardWindow(){
        setTitle("Food Ordering System - Customer Dashboard");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Customer Dashboard", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
