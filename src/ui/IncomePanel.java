package ui;

import dao.IncomeDAO;
import model.Income;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing income entries
 */
public class IncomePanel extends JPanel {
    private User currentUser;
    private IncomeDAO incomeDAO;
    private List<Income> guestIncomes; // For guest mode
    
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField notesField;
    private JTable incomeTable;
    private DefaultTableModel tableModel;

    public IncomePanel(User user) {
        this.currentUser = user;
        this.incomeDAO = new IncomeDAO();
        this.guestIncomes = new ArrayList<>();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 35, 35));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeComponents();
        loadIncomeData();
    }

    private void initializeComponents() {
        // Top panel for title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(35, 35, 35));
        JLabel titleLabel = new JLabel("💰 Income Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Center panel - split into form and table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(35, 35, 35));

        // Form panel
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = createTablePanel();
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Category
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createLabel("Category:"), gbc);

        String[] categories = {"Salary", "Freelance", "Business", "Investment", "Bonus", "Gift", "Other"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryCombo.setBackground(new Color(60, 60, 60));
        categoryCombo.setForeground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createLabel("Amount:"), gbc);

        amountField = createTextField();
        gbc.gridx = 1;
        panel.add(amountField, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createLabel("Date (YYYY-MM-DD):"), gbc);

        dateField = createTextField();
        dateField.setText(LocalDate.now().toString());
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        // Notes
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(createLabel("Notes:"), gbc);

        notesField = createTextField();
        gbc.gridx = 1;
        panel.add(notesField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(45, 45, 45));

        JButton addButton = createStyledButton("Add Income", new Color(60, 179, 113));
        addButton.addActionListener(e -> handleAddIncome());
        buttonPanel.add(addButton);

        JButton clearButton = createStyledButton("Clear", new Color(128, 128, 128));
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title
        JLabel tableTitle = new JLabel("Income Records");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(Color.WHITE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Category", "Amount (₹)", "Date", "Notes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        incomeTable = new JTable(tableModel);
        incomeTable.setFont(new Font("Arial", Font.PLAIN, 13));
        incomeTable.setRowHeight(25);
        incomeTable.setBackground(new Color(50, 50, 50));
        incomeTable.setForeground(Color.WHITE);
        incomeTable.setGridColor(new Color(70, 70, 70));
        incomeTable.getTableHeader().setBackground(new Color(70, 130, 180));
        incomeTable.getTableHeader().setForeground(Color.WHITE);
        incomeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(incomeTable);
        scrollPane.getViewport().setBackground(new Color(50, 50, 50));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Delete button
        JButton deleteButton = createStyledButton("Delete Selected", new Color(220, 53, 69));
        deleteButton.addActionListener(e -> handleDeleteIncome());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(45, 45, 45));
        bottomPanel.add(deleteButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleAddIncome() {
        try {
            String category = (String) categoryCombo.getSelectedItem();
            String amountStr = amountField.getText().trim();
            String dateStr = dateField.getText().trim();
            String notes = notesField.getText().trim();

            // Validation
            if (amountStr.isEmpty()) {
                showError("Please enter an amount");
                return;
            }

            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Amount must be greater than zero");
                return;
            }

            Date date = Date.valueOf(dateStr);

            // Show loading cursor
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (currentUser.getUserId() == -1) {
                // Guest mode
                Income income = new Income(-1, category, amount, date, notes);
                income.setIncomeId(guestIncomes.size() + 1);
                guestIncomes.add(income);
                JOptionPane.showMessageDialog(this, "✓ Income added successfully!\n(Guest Mode - Not saved to database)", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Regular user
                Income income = new Income(currentUser.getUserId(), category, amount, date, notes);
                if (incomeDAO.addIncome(income)) {
                    JOptionPane.showMessageDialog(this, "✓ Income added successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("Failed to add income");
                    return;
                }
            }

            clearForm();
            loadIncomeData();

        } catch (NumberFormatException e) {
            showError("Invalid amount format");
        } catch (IllegalArgumentException e) {
            showError("Invalid date format. Use YYYY-MM-DD");
        } catch (Exception e) {
            showError("Error adding income: " + e.getMessage());
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void handleDeleteIncome() {
        int selectedRow = incomeTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an income entry to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this income entry?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int incomeId = (int) tableModel.getValueAt(selectedRow, 0);

            if (currentUser.getUserId() == -1) {
                // Guest mode
                guestIncomes.removeIf(inc -> inc.getIncomeId() == incomeId);
                JOptionPane.showMessageDialog(this, "Income deleted (Guest Mode)");
            } else {
                // Regular user
                if (incomeDAO.deleteIncome(incomeId)) {
                    JOptionPane.showMessageDialog(this, "Income deleted successfully!");
                } else {
                    showError("Failed to delete income");
                    return;
                }
            }

            loadIncomeData();
        }
    }

    private void loadIncomeData() {
        tableModel.setRowCount(0);

        if (currentUser.getUserId() == -1) {
            // Guest mode
            for (Income income : guestIncomes) {
                Object[] row = {
                    income.getIncomeId(),
                    income.getCategory(),
                    income.getAmount(),
                    income.getDate(),
                    income.getNotes()
                };
                tableModel.addRow(row);
            }
        } else {
            // Regular user
            List<Income> incomes = incomeDAO.getIncomesByUserId(currentUser.getUserId());
            for (Income income : incomes) {
                Object[] row = {
                    income.getIncomeId(),
                    income.getCategory(),
                    income.getAmount(),
                    income.getDate(),
                    income.getNotes()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void clearForm() {
        categoryCombo.setSelectedIndex(0);
        amountField.setText("");
        dateField.setText(LocalDate.now().toString());
        notesField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getter for guest incomes (used by SummaryPanel)
    public List<Income> getGuestIncomes() {
        return guestIncomes;
    }
}
