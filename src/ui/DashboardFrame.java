package ui;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main dashboard frame with navigation to different modules
 */
public class DashboardFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Personal Finance Tracker - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Top panel with welcome message and logout
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Navigation panel (left side)
        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.WEST);

        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(35, 35, 35));

        // Add panels
        contentPanel.add(createWelcomePanel(), "Welcome");
        contentPanel.add(new IncomePanel(currentUser), "Income");
        contentPanel.add(new ExpensePanel(currentUser), "Expense");
        contentPanel.add(new SummaryPanel(currentUser), "Summary");

        add(contentPanel, BorderLayout.CENTER);

        // Show welcome panel by default
        cardLayout.show(contentPanel, "Welcome");
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(70, 130, 180)));

        // Welcome label
        String userDisplay = currentUser.getUserId() == -1 ? "Guest" : currentUser.getUsername();
        JLabel welcomeLabel = new JLabel("  Welcome, " + userDisplay + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(100, 40));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(logoutBtn);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(40, 40, 40));
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(70, 130, 180)));

        // Add spacing at top
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation buttons
        navPanel.add(createNavButton("📊 Dashboard", "Welcome"));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("💰 Income", "Income"));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("💸 Expenses", "Expense"));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createNavButton("📈 Summary", "Summary"));

        // Add glue to push buttons to top
        navPanel.add(Box.createVerticalGlue());

        return navPanel;
    }

    private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 50));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> cardLayout.show(contentPanel, panelName));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 60, 60));
            }
        });

        return button;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(35, 35, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel welcomeMsg = new JLabel("<html><center>" +
            "<h1 style='color: white;'>Welcome to Personal Finance Tracker!</h1>" +
            "<p style='color: #B0B0B0; font-size: 16px;'>Select a module from the left menu to get started:</p>" +
            "<ul style='color: #B0B0B0; font-size: 14px; text-align: left;'>" +
            "<li><b>Income:</b> Track your earnings and sources of income</li>" +
            "<li><b>Expenses:</b> Monitor your spending and categorize costs</li>" +
            "<li><b>Summary:</b> View detailed financial reports and charts</li>" +
            "</ul>" +
            "<p style='color: #70A0FF; font-size: 14px; margin-top: 30px;'>" +
            "Start managing your finances smartly today!" +
            "</p>" +
            "</center></html>");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(welcomeMsg, gbc);

        return panel;
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            });
        }
    }
}
