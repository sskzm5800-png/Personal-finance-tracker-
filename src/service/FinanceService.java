package service;

import dao.IncomeDAO;
import dao.ExpenseDAO;
import model.Income;
import model.Expense;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Service layer for finance calculations and business logic
 */
public class FinanceService {
    private final IncomeDAO incomeDAO;
    private final ExpenseDAO expenseDAO;

    public FinanceService() {
        this.incomeDAO = new IncomeDAO();
        this.expenseDAO = new ExpenseDAO();
    }

    /**
     * Calculate total income for a user
     * @param userId User ID
     * @return Total income amount
     */
    public BigDecimal calculateTotalIncome(int userId) {
        List<Income> incomes = incomeDAO.getIncomesByUserId(userId);
        BigDecimal total = BigDecimal.ZERO;
        
        for (Income income : incomes) {
            total = total.add(income.getAmount());
        }
        
        return total;
    }

    /**
     * Calculate total expenses for a user
     * @param userId User ID
     * @return Total expense amount
     */
    public BigDecimal calculateTotalExpenses(int userId) {
        List<Expense> expenses = expenseDAO.getExpensesByUserId(userId);
        BigDecimal total = BigDecimal.ZERO;
        
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        
        return total;
    }

    /**
     * Calculate savings (Income - Expenses)
     * @param userId User ID
     * @return Savings amount
     */
    public BigDecimal calculateSavings(int userId) {
        BigDecimal income = calculateTotalIncome(userId);
        BigDecimal expenses = calculateTotalExpenses(userId);
        return income.subtract(expenses);
    }

    /**
     * Calculate monthly income
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Total income for the month
     */
    public BigDecimal calculateMonthlyIncome(int userId, int month, int year) {
        List<Income> incomes = incomeDAO.getIncomesByMonth(userId, month, year);
        BigDecimal total = BigDecimal.ZERO;
        
        for (Income income : incomes) {
            total = total.add(income.getAmount());
        }
        
        return total;
    }

    /**
     * Calculate monthly expenses
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Total expenses for the month
     */
    public BigDecimal calculateMonthlyExpenses(int userId, int month, int year) {
        List<Expense> expenses = expenseDAO.getExpensesByMonth(userId, month, year);
        BigDecimal total = BigDecimal.ZERO;
        
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        
        return total;
    }

    /**
     * Calculate monthly savings
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Savings for the month
     */
    public BigDecimal calculateMonthlySavings(int userId, int month, int year) {
        BigDecimal income = calculateMonthlyIncome(userId, month, year);
        BigDecimal expenses = calculateMonthlyExpenses(userId, month, year);
        return income.subtract(expenses);
    }

    /**
     * Get income breakdown by category
     * @param userId User ID
     * @return Map of category to total amount
     */
    public Map<String, BigDecimal> getIncomeByCategoryBreakdown(int userId) {
        List<Income> incomes = incomeDAO.getIncomesByUserId(userId);
        Map<String, BigDecimal> breakdown = new HashMap<>();
        
        for (Income income : incomes) {
            String category = income.getCategory();
            BigDecimal amount = income.getAmount();
            breakdown.put(category, breakdown.getOrDefault(category, BigDecimal.ZERO).add(amount));
        }
        
        return breakdown;
    }

    /**
     * Get expense breakdown by category
     * @param userId User ID
     * @return Map of category to total amount
     */
    public Map<String, BigDecimal> getExpenseByCategoryBreakdown(int userId) {
        List<Expense> expenses = expenseDAO.getExpensesByUserId(userId);
        Map<String, BigDecimal> breakdown = new HashMap<>();
        
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            BigDecimal amount = expense.getAmount();
            breakdown.put(category, breakdown.getOrDefault(category, BigDecimal.ZERO).add(amount));
        }
        
        return breakdown;
    }

    /**
     * Get financial summary for a user
     * @param userId User ID
     * @return Map with totalIncome, totalExpenses, and savings
     */
    public Map<String, BigDecimal> getFinancialSummary(int userId) {
        Map<String, BigDecimal> summary = new HashMap<>();
        BigDecimal totalIncome = calculateTotalIncome(userId);
        BigDecimal totalExpenses = calculateTotalExpenses(userId);
        BigDecimal savings = totalIncome.subtract(totalExpenses);
        
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpenses", totalExpenses);
        summary.put("savings", savings);
        
        return summary;
    }

    /**
     * Get monthly financial summary
     * @param userId User ID
     * @param month Month (1-12)
     * @param year Year
     * @return Map with monthly totals
     */
    public Map<String, BigDecimal> getMonthlySummary(int userId, int month, int year) {
        Map<String, BigDecimal> summary = new HashMap<>();
        BigDecimal monthlyIncome = calculateMonthlyIncome(userId, month, year);
        BigDecimal monthlyExpenses = calculateMonthlyExpenses(userId, month, year);
        BigDecimal monthlySavings = monthlyIncome.subtract(monthlyExpenses);
        
        summary.put("monthlyIncome", monthlyIncome);
        summary.put("monthlyExpenses", monthlyExpenses);
        summary.put("monthlySavings", monthlySavings);
        
        return summary;
    }
}
