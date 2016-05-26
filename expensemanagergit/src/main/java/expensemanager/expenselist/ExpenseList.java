/**
 * 
 */
package expensemanager.expenselist;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import expensemanager.exceptions.ApproachingBudgetLimit;
import expensemanager.expenselist.Expense.ExpenseType;

/**
 * ExpenseList where all type of expenses are keeped
 * 
 * @version 1.00
 * @author Florin
 *
 */
public class ExpenseList implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	/**
	 * lists of the expenses types
	 */
	private List<Expense> dailyExpenses = new ArrayList<Expense>();
	private List<Expense> weeklyExpenses = new ArrayList<Expense>();
	private List<Expense> monthlyExpenses = new ArrayList<Expense>();
	private List<Expense> yearlyExpenses = new ArrayList<Expense>();
	private List<Expense> allExpenses = new ArrayList<Expense>();
	private List<Expense> uniqueExpenses = new ArrayList<Expense>();

	public float budget = Float.MAX_VALUE;

	/**
	 * Add expense and verify budget limit
	 * 
	 * @param expense
	 *            adding expense
	 * @throws ApproachingBudgetLimit
	 *             the exception
	 */
	public void addExpenseAndVerifyBudget(Expense expense) throws ApproachingBudgetLimit {
		LOGGER.fine("Adding expense and verifying budget! ");
		if ((getTotalActualExpenseInYear() + expense.getYearlyValue()) > (budget * 0.95f) && expense.getValue() > 0) {
			addExpense(expense);
			LOGGER.info("Budget limit exceeded");
			throw new ApproachingBudgetLimit("You have exceeded 95% of your budget set for the year");
		} else if (expense.getValue() > 0) {
			addExpense(expense);
			LOGGER.info("Expense added: " + expense);
		} else {
			LOGGER.warning("Failed to add expense. inserted wrong value: " + expense.getValue());
			throw new IllegalArgumentException("Value can't be negative or '0'");
		}
	}

	/**
	 * After the merging it returns allExpenses
	 * 
	 * @return allExpense in the list
	 */
	public List<Expense> getAllExpenses() {
		mergeExpenses();
		LOGGER.info("Merged all expenses");
		Collections.sort(allExpenses);
		return allExpenses;
	}

	/**
	 * sorting unique expenses
	 * 
	 * @return the uniqueExpenses sorted by type
	 */
	public List<Expense> getUniqueExpenses() {
		Collections.sort(uniqueExpenses);
		return uniqueExpenses;
	}

	/**
	 * sorting expenses
	 * 
	 * @return daily expenses sorted by date
	 */
	public List<Expense> getDailyExpenses() {
		Collections.sort(dailyExpenses);
		return dailyExpenses;
	}

	/**
	 * sorting expenses
	 * 
	 * @return weekly expenses sorted by date
	 */
	public List<Expense> getWeeklyExpenses() {
		Collections.sort(weeklyExpenses);
		return weeklyExpenses;
	}

	/**
	 * sorting expenses
	 * 
	 * @return monthly expenses sorted by date
	 */
	public List<Expense> getMonthlyExpenses() {
		Collections.sort(monthlyExpenses);
		return monthlyExpenses;
	}

	/**
	 * sorting expenses
	 * 
	 * @return yearly expenses sorted by date
	 */
	public List<Expense> getYearlyExpenses() {
		Collections.sort(yearlyExpenses);
		return yearlyExpenses;
	}

	/**
	 * setting the budget
	 * 
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(float budget) throws IllegalArgumentException {
		if (budget >= 0f) {
			this.budget = budget;
			LOGGER.info("Budget limit set to: " + budget);
		} else {
			LOGGER.warning("Budget limit to be set was inserted as negative number: " + budget);
			throw new IllegalArgumentException("Budget limit can't be negative");
		}
	}

	/**
	 * merge all the expenses
	 */
	void mergeExpenses() {
		allExpenses.clear();
		allExpenses.addAll(dailyExpenses);
		allExpenses.addAll(weeklyExpenses);
		allExpenses.addAll(monthlyExpenses);
		allExpenses.addAll(yearlyExpenses);
	}

	/**
	 * after checking the type the expense is added in the right type list
	 * 
	 * @param expense
	 *            to be added
	 */
	private void addExpense(Expense expense) {
		LocalDate date;
		switch (expense.getExpenseType()) {
		case DAILY:
			uniqueExpenses.add(expense);
			date = expense.getDate();
			for (int d = date.getDayOfYear(); d <= date.lengthOfYear(); d++) {
				dailyExpenses.add(new Expense(expense.getName(), expense.getValue(), date, ExpenseType.DAILY));
				date = date.plusDays(1);
			}
			break;
		case WEEKLY:
			uniqueExpenses.add(expense);
			date = expense.getDate();
			LocalDate endOfYear = date.withDayOfYear(date.lengthOfYear());
			while (date.isBefore(endOfYear.plusDays(1))) {
				weeklyExpenses.add(new Expense(expense.getName(), expense.getValue(), date, ExpenseType.WEEKLY));
				date = date.plusWeeks(1);
			}
			break;
		case MONTHLY:
			uniqueExpenses.add(expense);
			date = expense.getDate();
			for (int d = date.getMonthValue(); d <= 12; d++) {
				monthlyExpenses.add(
						new Expense(expense.getName(), expense.getValue(), date.withMonth(d), ExpenseType.MONTHLY));
			}
			break;
		case YEARLY:
			uniqueExpenses.add(expense);
			yearlyExpenses.add(expense);
			break;
		}
	}

	/**
	 * actual expense in a year
	 * 
	 * @return total actual expense in a year
	 */
	private float getTotalActualExpenseInYear() {
		float total = 0f;
		mergeExpenses();
		for (Expense exp : allExpenses) {
			total += exp.getValue();
		}

		return total;
	}

}
