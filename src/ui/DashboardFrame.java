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
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(35, 35, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top section - Welcome message and quick stats
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(new Color(35, 35, 35));

        // Welcome header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(35, 35, 35));
        
        JLabel welcomeLabel = new JLabel("<html><h1 style='color: white;'>Welcome back, " + 
            (currentUser.getUserId() == -1 ? "Guest" : currentUser.getUsername()) + "!</h1></html>");
        headerPanel.add(welcomeLabel);
        topSection.add(headerPanel, BorderLayout.NORTH);

        // Quick stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(35, 35, 35));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        if (currentUser.getUserId() != -1) {
            // Get financial summary
            service.FinanceService financeService = new service.FinanceService();
            java.util.Map<String, java.math.BigDecimal> summary = financeService.getFinancialSummary(currentUser.getUserId());
            
            java.math.BigDecimal income = summary.getOrDefault("totalIncome", java.math.BigDecimal.ZERO);
            java.math.BigDecimal expenses = summary.getOrDefault("totalExpenses", java.math.BigDecimal.ZERO);
            java.math.BigDecimal savings = summary.getOrDefault("savings", java.math.BigDecimal.ZERO);

            statsPanel.add(createQuickStatCard("Total Income", "₹" + String.format("%,.2f", income), new Color(60, 179, 113), "💰"));
            statsPanel.add(createQuickStatCard("Total Expenses", "₹" + String.format("%,.2f", expenses), new Color(220, 53, 69), "💸"));
            statsPanel.add(createQuickStatCard("Savings", "₹" + String.format("%,.2f", savings), new Color(70, 130, 180), "💵"));
        } else {
            statsPanel.add(createQuickStatCard("Guest Mode", "Data not saved", new Color(128, 128, 128), "👤"));
            statsPanel.add(createQuickStatCard("Quick Start", "Add Income/Expenses", new Color(70, 130, 180), "⚡"));
            statsPanel.add(createQuickStatCard("Try it Out", "Explore features", new Color(60, 179, 113), "🚀"));
        }

        topSection.add(statsPanel, BorderLayout.CENTER);
        panel.add(topSection, BorderLayout.NORTH);

        // Center section - Recent activity and quick actions
        JPanel centerSection = new JPanel(new GridLayout(1, 2, 20, 0));
        centerSection.setBackground(new Color(35, 35, 35));

        // Recent transactions panel
        JPanel recentPanel = createRecentTransactionsPanel();
        centerSection.add(recentPanel);

        // Quick actions panel
        JPanel actionsPanel = createQuickActionsPanel();
        centerSection.add(actionsPanel);

        panel.add(centerSection, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createQuickStatCard(String title, String value, Color color, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(45, 45, 45));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(180, 180, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(emojiLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueLabel);

        return card;
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("📊 Recent Activity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea activityArea = new JTextArea();
        activityArea.setEditable(false);
        activityArea.setBackground(new Color(35, 35, 35));
        activityArea.setForeground(new Color(200, 200, 200));
        activityArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        activityArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (currentUser.getUserId() != -1) {
            dao.IncomeDAO incomeDAO = new dao.IncomeDAO();
            dao.ExpenseDAO expenseDAO = new dao.ExpenseDAO();
            
            java.util.List<model.Income> recentIncomes = incomeDAO.getIncomesByUserId(currentUser.getUserId());
            java.util.List<model.Expense> recentExpenses = expenseDAO.getExpensesByUserId(currentUser.getUserId());

            StringBuilder activity = new StringBuilder();
            activity.append("💰 Recent Income:\n");
            int incomeCount = Math.min(3, recentIncomes.size());
            for (int i = 0; i < incomeCount; i++) {
                model.Income inc = recentIncomes.get(i);
                activity.append(String.format("  • %s - ₹%,.2f (%s)\n", 
                    inc.getCategory(), inc.getAmount(), inc.getDate()));
            }
            
            activity.append("\n💸 Recent Expenses:\n");
            int expenseCount = Math.min(3, recentExpenses.size());
            for (int i = 0; i < expenseCount; i++) {
                model.Expense exp = recentExpenses.get(i);
                activity.append(String.format("  • %s - ₹%,.2f (%s)\n", 
                    exp.getCategory(), exp.getAmount(), exp.getDate()));
            }

            if (incomeCount == 0 && expenseCount == 0) {
                activity.append("\nNo transactions yet.\nStart by adding income or expenses!");
            }

            activityArea.setText(activity.toString());
        } else {
            activityArea.setText("Guest Mode - Add transactions to see activity here!");
        }

        JScrollPane scrollPane = new JScrollPane(activityArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("⚡ Quick Actions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(new Color(45, 45, 45));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Quick action buttons
        JButton addIncomeBtn = createQuickActionButton("➕ Add Income", new Color(60, 179, 113));
        addIncomeBtn.addActionListener(e -> cardLayout.show(contentPanel, "Income"));
        buttonsPanel.add(addIncomeBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton addExpenseBtn = createQuickActionButton("➕ Add Expense", new Color(220, 53, 69));
        addExpenseBtn.addActionListener(e -> cardLayout.show(contentPanel, "Expense"));
        buttonsPanel.add(addExpenseBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton viewSummaryBtn = createQuickActionButton("📊 View Summary", new Color(70, 130, 180));
        viewSummaryBtn.addActionListener(e -> cardLayout.show(contentPanel, "Summary"));
        buttonsPanel.add(viewSummaryBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton refreshBtn = createQuickActionButton("🔄 Refresh Dashboard", new Color(100, 100, 100));
        refreshBtn.addActionListener(e -> refreshDashboard());
        buttonsPanel.add(refreshBtn);

        // Tips section
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel tipsLabel = new JLabel("<html><div style='color: #B0B0B0; padding: 10px;'>" +
            "<b>💡 Tips:</b><br>" +
            "• Track every expense<br>" +
            "• Review monthly summaries<br>" +
            "• Export reports regularly<br>" +
            "• Set savings goals</div></html>");
        tipsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.add(tipsLabel);

        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createQuickActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void refreshDashboard() {
        // Refresh the welcome panel by recreating it
        contentPanel.remove(0); // Remove old welcome panel
        contentPanel.add(createWelcomePanel(), "Welcome", 0);
        cardLayout.show(contentPanel, "Welcome");
        JOptionPane.showMessageDialog(this, "Dashboard refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
