package FOS_UI.MockUI.CustomerPanels;

import FOS_CORE.*;
import FOS_UI.MockUI.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AddressesPanel extends JPanel {
    private CustomerMainPanel mainPanel;
    private AccountDetailsPanel accountDetailsPanel;
    private JPanel addressesPanel;

    public AddressesPanel(CustomerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> mainPanel.showAccountDetails());
        topPanel.add(backButton, BorderLayout.WEST);
        JLabel titleLabel = new JLabel("Addresses");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton addButton = new JButton("Add Address");
        addButton.setBackground(Color.green);
        addButton.addActionListener(e -> addButtonAction());
        topPanel.add(addButton, BorderLayout.EAST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        addressesPanel = new JPanel();
        addressesPanel.setLayout(new BoxLayout(addressesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(addressesPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh(){
        addressesPanel.removeAll();
        Customer customer = mainPanel.getCurrentCustomer();
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

    private void addButtonAction(){
        AddAddressDialog dialog = new AddAddressDialog(mainPanel);
        dialog.setVisible(true);

        Address added = dialog.getAddedAddress();
        if(added != null){
            refresh();
        }
    }
    private void removeButtonAction(Address address){
       Customer customer = mainPanel.getCurrentCustomer();
       if(customer == null) return;

       mainPanel.getMainFrame().getAccountService().removeAddress(mainPanel.getCurrentCustomer(), address);

       if(customer.getAddresses() != null){
           customer.getAddresses().remove(address);
       }

       refresh();
    }
}
