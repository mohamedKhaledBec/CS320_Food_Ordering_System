package FOS_UI.MockUI;

import FOS_CORE.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CardsPanel extends JPanel {
    private MainFrame mainFrame;
    private AccountDetailsPanel accountDetailsPanel;
    private JPanel cardsPanel;

    public CardsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> mainFrame.showAccountDetails());
        topPanel.add(backButton, BorderLayout.WEST);
        JLabel titleLabel = new JLabel("Cards");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh(){
        cardsPanel.removeAll();
        Customer customer = mainFrame.getCurrentCustomer();
        if(customer == null){
            cardsPanel.add(new JLabel("Please log in to view cards."));
        } else {
            if(customer.getCards() == null || customer.getCards().isEmpty()){
                cardsPanel.add(new JLabel("No cards found."));
            } else {
                for(Card card : customer.getCards()){
                    JPanel cardsCard = createCardsCard(customer, card);
                    cardsPanel.add(cardsCard);
                }
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCardsCard(Customer customer, Card card){
        JPanel Jcard = new JPanel(new BorderLayout());
        Jcard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        Jcard.setPreferredSize(new Dimension(600, 50));
        Jcard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel cardLabel = new JLabel(card.getCardNumber());
        cardLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.red);
        removeButton.addActionListener(e -> removeButtonAction(card));


        Jcard.add(cardLabel, BorderLayout.CENTER);
        Jcard.add(removeButton, BorderLayout.EAST);

        return Jcard;
    }

    private void removeButtonAction(Card card){
        mainFrame.getAccountService().removeCard(mainFrame.getCurrentCustomer(), card);
        refresh();
    }
}
