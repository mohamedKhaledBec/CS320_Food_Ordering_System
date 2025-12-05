package FOS_UI;

// Being done by Hassan Askari

import FOS_CORE.CartItem;
import FOS_CORE.MenuItem;
import FOS_CORE.Order;
import FOS_CORE.OrderStatus;
import FOS_CORE.IManagerService;
import FOS_CORE.Manager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderManagementWindow extends JFrame {

    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private Manager loggedManager;

    public OrderManagementWindow(Manager manager)
    {
        this.loggedManager = manager;

        setTitle("Order Management");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadIncomingOrders();
    }

    private void initComponents()
    {

        orderTableModel = new DefaultTableModel(
                new Object[]{"Order ID", "Restaurant", "Created On", "Status"}, 0
        );

        orderTable = new JTable(orderTableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(orderTable);

        JButton btnMarkPreparing = new JButton("Mark Preparing");
        JButton btnMarkCompleted = new JButton("Mark Delivered");
        JButton btnViewItems = new JButton("View Order Items");

        btnMarkPreparing.addActionListener(e -> updateOrderStatus(OrderStatus.PREPARING));
        btnMarkCompleted.addActionListener(e -> updateOrderStatus(OrderStatus.DELIVERED));
        btnViewItems.addActionListener(e -> viewSelectedOrderItems());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnMarkPreparing);
        buttonPanel.add(btnMarkCompleted);
        buttonPanel.add(btnViewItems);

        getContentPane().setLayout(new BorderLayout());
        add(tableScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadIncomingOrders()
    {
        try
        {
            IManagerService ms = ServiceContext.getManagerService();
            ArrayList<Order> orders = ms.viewIncomingOrders(loggedManager);

            orderTableModel.setRowCount(0);

            if (orders != null)
            {
                for (Order o : orders)
                {
                    orderTableModel.addRow(new Object[]{
                            o.getOrderID(),
                            o.getRestaurantName(),
                            o.getCreationDate(),
                            o.getStatus()
                    });
                }
            }

        }
        catch (Exception ex)
        {
            DialogUtils.showError(this, "Failed to load incoming orders.");
        }
    }

    private Order getSelectedOrder() {
        int row = orderTable.getSelectedRow();
        if (row == -1)
        {
            DialogUtils.showError(this, "Please select an order.");
            return null;
        }

        int orderID = (int) orderTableModel.getValueAt(row, 0);

        try
        {
            IManagerService ms = ServiceContext.getManagerService();
            ArrayList<Order> orders = ms.viewIncomingOrders(loggedManager);

            for (Order o : orders)
            {
                if (o.getOrderID() == orderID) return o;
            }

        }
        catch (Exception ex)
        {
            DialogUtils.showError(this, "Error retrieving selected order.");
        }

        return null;
    }

    private void updateOrderStatus(OrderStatus newStatus) {
        Order selected = getSelectedOrder();
        if (selected == null) return;

        try
        {
            IManagerService ms = ServiceContext.getManagerService();
            ms.updateOrderStatus(selected, newStatus.toString());
            DialogUtils.showInfo(this, "Order updated successfully.");

            loadIncomingOrders(); // refresh

        }
        catch (Exception ex)
        {
            DialogUtils.showError(this, "Failed to update order.");
        }
    }

    private void viewSelectedOrderItems() {
        Order selected = getSelectedOrder();
        if (selected == null) return;

        StringBuilder sb = new StringBuilder("Items in this order:\n\n");

        for (CartItem item : selected.getItems())
        {
            MenuItem mi = item.getItem();

            String name = (mi != null) ? mi.getItemName() : "Unknown Item";

            sb.append(name)
                    .append("  x ")
                    .append(item.getQuantity())
                    .append("  @ ")
                    .append(String.format("%.2f", item.getPrice()))
                    .append("\n");
        }

        DialogUtils.showInfo(this, sb.toString());
    }
}
