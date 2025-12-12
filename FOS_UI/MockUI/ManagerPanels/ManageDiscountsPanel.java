package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.*;
import FOS_CORE.MenuItem;
import FOS_UI.DialogUtils;
import FOS_UI.ServiceContext;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ManageDiscountsPanel extends JDialog {
    private MenuItem menuItem;
    private ArrayList<Discount> discounts;
    private JPanel discountsListPanel;
    private final IManagerService managerService;

    public ManageDiscountsPanel(JDialog parent, MenuItem menuItem) {
        super(parent, "Manage Discounts - " + menuItem.getItemName(), true);
        this.menuItem = menuItem;
        this.managerService = ServiceContext.getManagerService();
        this.discounts = menuItem.getDiscounts() != null ? menuItem.getDiscounts() : new ArrayList<>();
        initComponents();
        pack();
        setSize(700, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Discounts for: " + menuItem.getItemName());
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton addButton = new JButton("Add Discount");
        addButton.setBackground(Color.GREEN);
        addButton.addActionListener(e -> addDiscount());
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        discountsListPanel = new JPanel();
        discountsListPanel.setLayout(new BoxLayout(discountsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(discountsListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshDiscounts();
    }

    private void refreshDiscounts() {
        discountsListPanel.removeAll();

        if (discounts.isEmpty()) {
            discountsListPanel.add(new JLabel("No discounts available."));
        } else {
            for (Discount discount : discounts) {
                JPanel discountCard = createDiscountCard(discount);
                discountsListPanel.add(discountCard);
                discountsListPanel.add(Box.createVerticalStrut(5));
            }
        }

        discountsListPanel.revalidate();
        discountsListPanel.repaint();
    }

    private JPanel createDiscountCard(Discount discount) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(new JLabel("Name: " + discount.getDiscountName()));
        infoPanel.add(new JLabel("Description: " + discount.getDescription()));
        infoPanel.add(new JLabel("Percentage: " + discount.getDiscountPercentage() + "%"));
        infoPanel.add(new JLabel("Period: " + discount.getStartDate() + " to " + discount.getEndDate()));

        card.add(infoPanel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> deleteDiscount(discount));
        card.add(deleteButton, BorderLayout.EAST);

        return card;
    }

    private void addDiscount() {
        AddDiscountDialog dialog = new AddDiscountDialog(this, menuItem, managerService);
        dialog.setVisible(true);

        Discount newDiscount = dialog.getDiscount();
        if (newDiscount != null) {
            discounts.add(newDiscount);
            refreshDiscounts();
            DialogUtils.showInfo(this, "Discount added successfully.");
        }
    }

    private void deleteDiscount(Discount discount) {
        int confirm = DialogUtils.confirm(this, "Are you sure you want to delete this discount?");
        if (confirm != 0) {
            return;
        }

        discounts.remove(discount);
        refreshDiscounts();
        DialogUtils.showInfo(this, "Discount deleted successfully.");
    }
}
