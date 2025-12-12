package FOS_UI.MockUI.CustomerPanels;

import FOS_CORE.AccountService;
import FOS_CORE.Address;
import FOS_CORE.Customer;

import javax.swing.*;

public class AddAddressDialog extends JDialog{

    private CustomerMainPanel mainPanel;
    private AccountService accountService;


    private JTextField address;
    private JComboBox<String> cityDropdown;
    private JTextField city;
    private JTextField state;
    private JTextField zipCode;

    private Address addedAddress;

    public AddAddressDialog(CustomerMainPanel mainPanel) {
        super(mainPanel.getMainFrame(), "Add New Address", true);
        this.mainPanel = mainPanel;
        this.accountService = mainPanel.getMainFrame().getAccountService();
        initComponents();
        pack();
        setLocationRelativeTo(mainPanel.getMainFrame());
    }
    private void initComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Add New Address");
        JLabel addressLabel = new JLabel("Address:");
        address = new JTextField(30);
        addressLabel.add(address);
        JLabel cityLabel = new JLabel("City:");
        cityDropdown = new JComboBox<>(getTurkishCities());
//      city = new JTextField(30);
        state = new JTextField(30);
        zipCode = new JTextField(16);

        add(new JLabel("Address Line:"));
        add(address);
        add(new JLabel("City:"));
        add(cityDropdown);
        add(new JLabel("State:"));
        add(state);
        add(new JLabel("Zip Code:"));
        add(zipCode);

        JButton addButton = new JButton("Add Address");
        addButton.addActionListener(e -> onAddAddress());
        add(addButton);
    }

    private void onAddAddress() {
        String address = this.address.getText().trim();
        String city = this.cityDropdown.getSelectedItem().toString();
        String state = this.state.getText().trim();
        String zipCode = this.zipCode.getText().trim();

        if(address.isEmpty() || city.isEmpty() || state.isEmpty() || zipCode.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(!isNumeric(zipCode) || !isStringOnlyLetters(address) || !isStringOnlyLetters(state)){
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        }

        addedAddress = new Address(-1, address, city, state, zipCode);

        Customer currentCustomer = mainPanel.getCurrentCustomer();
        accountService.addAddress(currentCustomer, addedAddress);

        JOptionPane.showMessageDialog(this, "Address added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();



    }

    public Address getAddedAddress() {
        return addedAddress;
    }

    private boolean isStringOnlyLetters(String text) {
        if (text == null || text.isEmpty()) return false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    private String[] getTurkishCities(){
        return new String[]{"Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
                "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
                "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum",
                "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin",
                "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli",
                "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir",
                "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
                "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt",
                "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük",
                "Kilis", "Osmaniye", "Düzce"};
    }

    private boolean isNumeric(String text) {
        if (text == null || text.isEmpty()) return false;

        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
