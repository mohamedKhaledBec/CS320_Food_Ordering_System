package FOS_UI;

import FOS_CORE.Manager;
import FOS_CORE.Restaurant;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// Being Done by Umair Ahmad (updated)
public class ManagerDashboardWindow extends JFrame {

    private Manager manager;
    private final JPanel gridPanel;
    private int columns = 2;

    public ManagerDashboardWindow() {
        setTitle("Manager Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel header = new JLabel("Manager Dashboard");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        header.setBorder(new EmptyBorder(16, 20, 8, 20));

        gridPanel = new JPanel();
        gridPanel.setBackground(new Color(0, 0, 0, 0));
        gridPanel.setLayout(new GridLayout(0, columns, 20, 20));
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(
                gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.getViewport().setBackground(getContentPane().getBackground());

        getContentPane().setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getContentPane().getWidth();
                int newCols = Math.max(1, w / 360);
                if (newCols != columns) {
                    columns = newCols;
                    gridPanel.setLayout(new GridLayout(0, columns, 20, 20));
                    gridPanel.revalidate();
                }
            }
        });
    }

    public ManagerDashboardWindow(Manager manager) {
        this();
        this.manager = manager;
        loadRestaurants();
    }

    private void loadRestaurants() {
        if (manager == null) {
            setRestaurants(new ArrayList<>());
            return;
        }

        try {
            ArrayList<Restaurant> restaurants =
                    ServiceContext.getManagerService().getManagerRestaurants(manager);

            List<RestaurantCard> cards = new ArrayList<>();
            for (Restaurant restaurant : restaurants) {
                // CAN BE REMOVED IF NEEDED
                cards.add(new RestaurantCard(restaurant, null, null));
            }

            setRestaurants(cards);
        } catch (Exception e) {
            System.err.println("Error loading restaurants for manager: " + e.getMessage());
            e.printStackTrace();
            setRestaurants(new ArrayList<>());
        }
    }

    public void setRestaurants(List<RestaurantCard> restaurants) {
        gridPanel.removeAll();
        if (restaurants == null || restaurants.isEmpty()) {
            JLabel none = new JLabel("No restaurants found.", SwingConstants.CENTER);
            none.setBorder(new EmptyBorder(40, 0, 40, 0));
            gridPanel.setLayout(new GridLayout(1, 1));
            gridPanel.add(none);
        } else {
            gridPanel.setLayout(new GridLayout(0, columns, 20, 20));
            for (RestaurantCard r : restaurants) {
                gridPanel.add(makeCard(r));
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBackground(new Color(49, 131, 143));
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 12f));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 28));
        return btn;
    }

    private JPanel makeCard(RestaurantCard r) {
        JPanel card = new JPanel(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 160));
        card.setOpaque(true);

        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(72, 72));
        BufferedImage img = loadImage(r);
        if (img != null) {
            Image scaled = img.getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaled));
            imgLabel.setBorder(new LineBorder(new Color(255, 102, 0), 2, true));
        } else {
            JPanel placeholder = new JPanel(new BorderLayout());
            placeholder.setPreferredSize(new Dimension(72, 72));
            placeholder.setBackground(new Color(248, 248, 248));
            placeholder.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
            JLabel plus = new JLabel("+", SwingConstants.CENTER);
            plus.setFont(plus.getFont().deriveFont(Font.BOLD, 20f));
            plus.setForeground(new Color(180, 180, 180));
            placeholder.add(plus, BorderLayout.CENTER);
            imgLabel.setIcon(iconFromComponent(placeholder));
        }

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(imgLabel, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(r.name);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setFont(name.getFont().deriveFont(Font.BOLD, 16f));

        JLabel meta = new JLabel(String.format("Cuisine: %s    Location: %s", r.cuisineType, r.city));
        meta.setAlignmentX(Component.LEFT_ALIGNMENT);
        meta.setForeground(new Color(90, 90, 90));
        meta.setFont(meta.getFont().deriveFont(12f));

        main.add(name);
        main.add(Box.createVerticalStrut(4));
        main.add(meta);
        main.add(Box.createVerticalStrut(12));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonRow.setOpaque(false);

        JButton editMenuBtn = createActionButton("Edit Menu");
        JButton monthlyReportBtn = createActionButton("Monthly Report");
        JButton manageInfoBtn = createActionButton("Manage Info");

        editMenuBtn.addActionListener(e -> {
            if (manager == null || r.restaurant == null) {
                DialogUtils.showError(ManagerDashboardWindow.this,
                        "Manager or restaurant information is missing.");
                return;
            }
            MenuManagementWindow win = new MenuManagementWindow(manager, r.restaurant);
            win.setVisible(true);
        });

        monthlyReportBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        ManagerDashboardWindow.this,
                        "Monthly Report clicked for " + r.name
                )
        );
        manageInfoBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        ManagerDashboardWindow.this,
                        "Manage Info clicked for " + r.name
                )
        );

        buttonRow.add(editMenuBtn);
        buttonRow.add(monthlyReportBtn);
        buttonRow.add(manageInfoBtn);

        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        main.add(buttonRow);

        card.add(left, BorderLayout.WEST);
        card.add(main, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(0, 196, 255), 2, true),
                        new EmptyBorder(14, 16, 14, 16)
                ));
                card.setBackground(new Color(255, 252, 248));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(220, 220, 220), 1, true),
                        new EmptyBorder(14, 16, 14, 16)
                ));
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private BufferedImage loadImage(RestaurantCard r) {
        try {
            if (r.imageBase64 != null && !r.imageBase64.isBlank()) {
                byte[] bytes = Base64.getDecoder().decode(r.imageBase64);
                return ImageIO.read(new ByteArrayInputStream(bytes));
            } else if (r.imageUrl != null && !r.imageUrl.isBlank()) {
                return ImageIO.read(new URL(r.imageUrl));
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Icon iconFromComponent(JComponent comp) {
        comp.setSize(comp.getPreferredSize());
        BufferedImage img = new BufferedImage(
                comp.getWidth(),
                comp.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = img.createGraphics();
        comp.paint(g);
        g.dispose();
        return new ImageIcon(img);
    }

    public static class RestaurantCard {
        public final Restaurant restaurant;
        public final String name;
        public final String cuisineType;
        public final String city;
        public final String imageBase64;
        public final String imageUrl;

        public RestaurantCard(Restaurant restaurant, String imageBase64, String imageUrl) {
            this.restaurant = restaurant;
            this.name = restaurant.getRestaurantName();
            this.cuisineType = restaurant.getCuisineType();
            this.city = restaurant.getCity();
            this.imageBase64 = imageBase64;
            this.imageUrl = imageUrl;
        }
    }
}
