package FOS_UI.MockUI.ManagerPanels;

import FOS_CORE.Restaurant;
import FOS_UI.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ManageKeywordsPanel extends JPanel {
    private ManagerMainPanel mainPanel;
    private Restaurant restaurant;
    private JPanel keywordsListPanel;
    private ArrayList<String> keywords;

    public ManageKeywordsPanel(ManagerMainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initComponents();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        if (restaurant != null) {
            this.keywords = restaurant.getKeywords() != null ? restaurant.getKeywords() : new ArrayList<>();
            refresh();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainPanel.showManageRestaurant(mainPanel.getCurrentRestaurant()));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Manage Keywords");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Keyword");
        addButton.setBackground(Color.GREEN);
        addButton.addActionListener(e -> addKeyword());
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        keywordsListPanel = new JPanel();
        keywordsListPanel.setLayout(new BoxLayout(keywordsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(keywordsListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        keywordsListPanel.removeAll();

        if (keywords == null || keywords.isEmpty()) {
            keywordsListPanel.add(new JLabel("No keywords available."));
        } else {
            for (String keyword : keywords) {
                JPanel keywordCard = createKeywordCard(keyword);
                keywordsListPanel.add(keywordCard);
                keywordsListPanel.add(Box.createVerticalStrut(5));
            }
        }

        keywordsListPanel.revalidate();
        keywordsListPanel.repaint();
    }

    private JPanel createKeywordCard(String keyword) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel keywordLabel = new JLabel(keyword);
        keywordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        card.add(keywordLabel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> deleteKeyword(keyword));
        card.add(deleteButton, BorderLayout.EAST);

        return card;
    }

    private void addKeyword() {
        String keyword = JOptionPane.showInputDialog(this, "Enter keyword:", "Add Keyword", JOptionPane.PLAIN_MESSAGE);

        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            if (keywords.contains(keyword)) {
                DialogUtils.showError(this, "Keyword already exists.");
                return;
            }

            keywords.add(keyword);
            restaurant.setKeywords(keywords);
            refresh();
            DialogUtils.showInfo(this, "Keyword added successfully.");
        }
    }

    private void deleteKeyword(String keyword) {
        int confirm = DialogUtils.confirm(this, "Are you sure you want to delete this keyword?");
        if (confirm != 0) {
            return;
        }

        keywords.remove(keyword);
        restaurant.setKeywords(keywords);
        refresh();
        DialogUtils.showInfo(this, "Keyword deleted successfully.");
    }
}
