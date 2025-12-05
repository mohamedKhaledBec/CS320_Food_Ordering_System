package FOS_UI;

// Being done by Hassan Askari

import FOS_CORE.Order;
import FOS_CORE.IOrderService;
import FOS_CORE.Customer;
import FOS_CORE.Rating;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderHistoryWindow extends JFrame {

    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    public OrderHistoryWindow() {
        setTitle("Food Ordering System – Order History");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadOrderHistory();
    }

    private void initComponents()
    {

        orderTableModel = new DefaultTableModel(
                new Object[]{"Order ID", "Restaurant", "Date", "Status", "Rating"}, 0
        );
        orderTable = new JTable(orderTableModel);
        orderTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(orderTable);

        getContentPane().setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadOrderHistory()
    {
        Customer customer = Session.getCurrentCustomer();

        if (customer == null)
        {
            DialogUtils.showError(this, "You must be logged in as a customer.");
            return;
        }

        try
        {
            IOrderService orderService = ServiceContext.getOrderService();
            ArrayList<Order> orders = orderService.getOrderHistory(customer);

            orderTableModel.setRowCount(0);

            if (orders == null || orders.isEmpty())
            {
                DialogUtils.showInfo(this, "You have no previous orders.");
                return;
            }

            for (Order o : orders)
            {
                String ratingText = (o.getRating() == null)
                        ? "Not Rated"
                        : String.valueOf(o.getRating().getRatingValue());

                orderTableModel.addRow(new Object[]{
                        o.getOrderID(),
                        o.getRestaurantName(),
                        o.getCreationDate(),
                        o.getStatus(),
                        ratingText
                });
            }

        }
        catch (Exception ex)
        {
            DialogUtils.showError(this, "An error occurred while loading order history.");
        }
    }
}
