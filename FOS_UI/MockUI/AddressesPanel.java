package FOS_UI.MockUI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddressesPanel extends JPanel {
    private MainFrame mainFrame;
    private AccountDetailsPanel accountDetailsPanel;
    private JPanel addressesPanel;

    public AddressesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> mainFrame.showAccountDetails());
        topPanel.add(backButton, BorderLayout.WEST);
        JLabel titleLabel = new JLabel("Addresses");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        addressesPanel = new JPanel();
        addressesPanel.setLayout(new BoxLayout(addressesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(addressesPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh(){
        addressesPanel.removeAll();
        Customer customer = mainFrame.getCurrentCustomer();
        if(customer == null){
            addressesPanel.add(new JLabel("Please log in to view addresses."));
        } else {
            if(customer.getAddresses() == null || customer.getAddresses().isEmpty()){
                addressesPanel.add(new JLabel("No addresses found."));
            } else {
                for(Address address : customer.getAddresses()){
                    JPanel addressCard = createAddressCard(customer, address);
                    addressesPanel.add(addressCard);
                }
            }
        }
        addressesPanel.revalidate();
        addressesPanel.repaint();
    }

    private JPanel createAddressCard(Customer customer, Address address){
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setPreferredSize(new Dimension(600, 50));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel addressLabel = new JLabel(address.toString());
        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.red);
        removeButton.addActionListener(e -> removeButtonAction(address));


        card.add(addressLabel, BorderLayout.CENTER);
        card.add(removeButton, BorderLayout.EAST);

        return card;
    }

    private void removeButtonAction(Address address){
        mainFrame.getAccountService().removeAddress(mainFrame.getCurrentCustomer(), address);
        refresh();
    }
}
