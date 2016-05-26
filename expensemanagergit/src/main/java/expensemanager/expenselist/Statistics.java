package expensemanager.expenselist;

import java.time.Month;
import java.util.logging.Logger;

import expensemanager.exceptions.NoExpenseAddedExeption;

/**
 * Statistics class this support statistics like highestExpense that are
 * available per year, per month and per year and month together
 * 
 * @author Flo&Geo
 *
 */
public class Statistics {

	private ExpenseList expensesList;
	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	/**
	 * constuctor of the class
	 * 
	 * @param expenses
	 *            type for statistics
	 */

	public Statistics(ExpenseList expenses) {
		this.expensesList = expenses;
	}

	/**
	 * method to get the highest expense of the selected year
	 * 
	 * @param year
	 *            the year with highestExpenses
	 * @return the highest expense in a year
	 * @throws NoExpenseAddedExeption
	 */
	public Expense getHighestExpenseInYear(int year) throws NoExpenseAddedExeption {
		expensesList.mergeExpenses();
		Expense highestExpense = Expense.MIN_EXPENSE_VALUE;
		for (Expense exp : expensesList.getAllExpenses()) {
			if (exp.getYear() == year && exp.getYearlyValue() > highestExpense.getYearlyValue()) {
				highestExpense = exp;
			}
		}
		if (highestExpense.equals(Expense.MIN_EXPENSE_VALUE)) {
			LOGGER.warning("No expense with value higher than 0 found in expense list");
			throw new NoExpenseAddedExeption("No expense added in year: " + year);
		}
		LOGGER.info("Returned highest expense in year: " + highestExpense);
		return highestExpense;
	}

	/**
	 * method to get the highest expense of the selected month
	 * 
	 * @param month
	 *            the month woth the highest expenses
	 * @return highestExpenses in a month
	 * @throws NoExpenseAddedExeption
	 */
	public Expense getHighestExpenseInMonth(Month month) throws NoExpenseAddedExeption {
		expensesList.mergeExpenses();
		Expense highestExpense = Expense.MIN_EXPENSE_VALUE;
		for (Expense exp : expensesList.getAllExpenses()) {
			if (exp.getMonth().equals(month) && exp.getMonthlyValue() > highestExpense.getMonthlyValue()) {
				highestExpense = exp;
			}
		}
		if (highestExpense.equals(Expense.MIN_EXPENSE_VALUE)) {
			LOGGER.warning("No expense with value higher than 0 found in expense list");
			throw new NoExpenseAddedExeption("No expense added in month: " + month);
		}
		LOGGER.info("Returned highest expense in month: " + highestExpense);
		return highestExpense;
	}

	/**
	 * method to get the highest expense of the selected year and month
	 * 
	 * @param year
	 *            the year with highestExpenses
	 * @param month
	 *            the month woth the highest expenses
	 * @return the highest expense
	 * @throws NoExpenseAddedExeption
	 */
	public Expense getHighestExpenseByYearAndMonth(int year, Month month) throws NoExpenseAddedExeption {
		expensesList.mergeExpenses();
		Expense highestExpense = Expense.MIN_EXPENSE_VALUE;
		for (Expense exp : expensesList.getAllExpenses()) {
			if (exp.getYear() == year && exp.getMonth().equals(month)
					&& exp.getMonthlyValue() > highestExpense.getMonthlyValue()) {
				highestExpense = exp;
			}
		}
		if (highestExpense.equals(Expense.MIN_EXPENSE_VALUE)) {
			LOGGER.warning("No expense with value higher than 0 found in expense list");
			throw new NoExpenseAddedExeption("No expense added in month: " + month);
		}
		LOGGER.info("Returned highest expense in month: " + highestExpense);
		return highestExpense;
	}

}