package ui;

import model.User;
import service.FinanceService;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Panel for displaying financial summary and charts
 */
public class SummaryPanel extends JPanel {
    private User currentUser;
    private FinanceService financeService;
    
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel savingsLabel;
    private ChartPanel chartPanel;

    public SummaryPanel(User user) {
        this.currentUser = user;
        this.financeService = new FinanceService();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 35, 35));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeComponents();
        loadSummaryData();
    }

    private void initializeComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(35, 35, 35));
        JLabel titleLabel = new JLabel("📈 Financial Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(35, 35, 35));

        // Summary info panel
        JPanel infoPanel = createInfoPanel();
        centerPanel.add(infoPanel, BorderLayout.NORTH);

        // Chart panel
        chartPanel = new ChartPanel();
        centerPanel.add(chartPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(new Color(35, 35, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Income card
        JPanel incomeCard = createInfoCard("Total Income", "₹0.00", new Color(60, 179, 113));
        totalIncomeLabel = (JLabel) ((JPanel) incomeCard.getComponent(1)).getComponent(0);
        panel.add(incomeCard);

        // Expense card
        JPanel expenseCard = createInfoCard("Total Expenses", "₹0.00", new Color(220, 53, 69));
        totalExpenseLabel = (JLabel) ((JPanel) expenseCard.getComponent(1)).getComponent(0);
        panel.add(expenseCard);

        // Savings card
        JPanel savingsCard = createInfoCard("Savings", "₹0.00", new Color(70, 130, 180));
        savingsLabel = (JLabel) ((JPanel) savingsCard.getComponent(1)).getComponent(0);
        panel.add(savingsCard);

        return panel;
    }

    private JPanel createInfoCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(45, 45, 45));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(180, 180, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanel.setBackground(new Color(45, 45, 45));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valuePanel);

        return card;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(35, 35, 35));

        JButton refreshButton = createStyledButton("Refresh Data", new Color(70, 130, 180));
        refreshButton.addActionListener(e -> loadSummaryData());
        panel.add(refreshButton);

        JButton exportButton = createStyledButton("Export Summary", new Color(60, 179, 113));
        exportButton.addActionListener(e -> exportSummary());
        panel.add(exportButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadSummaryData() {
        if (currentUser.getUserId() == -1) {
            // Guest mode - would need to access guest data from panels
            // For simplicity, showing placeholder
            totalIncomeLabel.setText("₹0.00");
            totalExpenseLabel.setText("₹0.00");
            savingsLabel.setText("₹0.00");
            chartPanel.updateChart(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            Map<String, BigDecimal> summary = financeService.getFinancialSummary(currentUser.getUserId());
            
            BigDecimal income = summary.getOrDefault("totalIncome", BigDecimal.ZERO);
            BigDecimal expenses = summary.getOrDefault("totalExpenses", BigDecimal.ZERO);
            BigDecimal savings = summary.getOrDefault("savings", BigDecimal.ZERO);

            totalIncomeLabel.setText("₹" + String.format("%,.2f", income));
            totalExpenseLabel.setText("₹" + String.format("%,.2f", expenses));
            savingsLabel.setText("₹" + String.format("%,.2f", savings));

            chartPanel.updateChart(income, expenses);
        }
    }

    private void exportSummary() {
        try {
            String filename = "Reports/summary_" + LocalDate.now() + ".txt";
            FileWriter writer = new FileWriter(filename);

            writer.write("==============================================\n");
            writer.write("     PERSONAL FINANCE TRACKER - SUMMARY\n");
            writer.write("==============================================\n\n");
            writer.write("User: " + currentUser.getUsername() + "\n");
            writer.write("Date: " + LocalDate.now() + "\n\n");
            writer.write("----------------------------------------------\n");
            writer.write("Total Income:     " + totalIncomeLabel.getText() + "\n");
            writer.write("Total Expenses:   " + totalExpenseLabel.getText() + "\n");
            writer.write("Savings:          " + savingsLabel.getText() + "\n");
            writer.write("----------------------------------------------\n\n");
            writer.write("Generated by Personal Finance Tracker\n");

            writer.close();

            JOptionPane.showMessageDialog(this, 
                "Summary exported successfully to:\n" + filename,
                "Export Successful",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error exporting summary: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inner class for rendering the bar chart
     */
    private class ChartPanel extends JPanel {
        private BigDecimal incomeValue = BigDecimal.ZERO;
        private BigDecimal expenseValue = BigDecimal.ZERO;

        public ChartPanel() {
            setBackground(new Color(45, 45, 45));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            setPreferredSize(new Dimension(600, 300));
        }

        public void updateChart(BigDecimal income, BigDecimal expense) {
            this.incomeValue = income;
            this.expenseValue = expense;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int chartHeight = height - 100;
            int barWidth = 100;
            int spacing = 150;

            // Title
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Income vs Expenses", width / 2 - 100, 30);

            // Calculate max value for scaling
            BigDecimal maxValue = incomeValue.max(expenseValue);
            if (maxValue.compareTo(BigDecimal.ZERO) == 0) {
                maxValue = BigDecimal.valueOf(100);
            }

            // Draw Income bar
            int incomeHeight = maxValue.compareTo(BigDecimal.ZERO) > 0 
                ? incomeValue.multiply(BigDecimal.valueOf(chartHeight))
                    .divide(maxValue, BigDecimal.ROUND_HALF_UP).intValue()
                : 0;
            
            int incomeX = width / 2 - spacing;
            int incomeY = height - 50 - incomeHeight;

            g2d.setColor(new Color(60, 179, 113));
            g2d.fillRect(incomeX, incomeY, barWidth, incomeHeight);
            g2d.setColor(new Color(80, 199, 133));
            g2d.drawRect(incomeX, incomeY, barWidth, incomeHeight);

            // Income label
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Income", incomeX + 20, height - 30);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("₹" + String.format("%,.0f", incomeValue), incomeX + 10, height - 10);

            // Draw Expense bar
            int expenseHeight = maxValue.compareTo(BigDecimal.ZERO) > 0
                ? expenseValue.multiply(BigDecimal.valueOf(chartHeight))
                    .divide(maxValue, BigDecimal.ROUND_HALF_UP).intValue()
                : 0;
            
            int expenseX = width / 2 + spacing - barWidth;
            int expenseY = height - 50 - expenseHeight;

            g2d.setColor(new Color(220, 53, 69));
            g2d.fillRect(expenseX, expenseY, barWidth, expenseHeight);
            g2d.setColor(new Color(240, 73, 89));
            g2d.drawRect(expenseX, expenseY, barWidth, expenseHeight);

            // Expense label
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Expenses", expenseX + 10, height - 30);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("₹" + String.format("%,.0f", expenseValue), expenseX + 10, height - 10);

            // Draw baseline
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(50, height - 50, width - 50, height - 50);
        }
    }
}
