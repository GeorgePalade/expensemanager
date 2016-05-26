package expensemanager;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import expensemanager.exceptions.ApproachingBudgetLimit;
import expensemanager.expenselist.Expense;
import expensemanager.expenselist.Expense.ExpenseType;
import expensemanager.expenselist.ExpenseList;

/**
 * 
 * @author Flo&Geo
 *
 */
public class ExpenseListTest {

	Expense dailyExpense;
	Expense weeklyExpense;
	Expense monthlyExpense;
	Expense yearlyExpense;
	Expense negativeExpense;
	List<Expense> setupExpenses = new ArrayList<Expense>();
	ExpenseList expenses;

	@Before
	public void setup() {

		dailyExpense = new Expense("Bread", 3, "2016-05-11", ExpenseType.DAILY);
		weeklyExpense = new Expense("Tennis", 25, "2016-05-15", ExpenseType.WEEKLY);
		monthlyExpense = new Expense("Movie", 55, "2017-08-11", ExpenseType.MONTHLY);
		yearlyExpense = new Expense("Holiday", 3278, "2016-06-11", ExpenseType.YEARLY);

		setupExpenses.add(dailyExpense);
		setupExpenses.add(weeklyExpense);
		setupExpenses.add(monthlyExpense);
		setupExpenses.add(yearlyExpense);

		negativeExpense = new Expense("Panie", -3, "2016-05-11", ExpenseType.DAILY);

		expenses = new ExpenseList();

	}

	@Test
	public void testAddExpenseAndVerifyBudget() throws ApproachingBudgetLimit {
		expenses.addExpenseAndVerifyBudget(dailyExpense);
		assertTrue(expenses.getUniqueExpenses().contains(dailyExpense));
		assertTrue(expenses.getDailyExpenses().contains(dailyExpense));
	}

	@Test
	public void testAddExpenseAndVerifyBudgetNegative() {
		try {
			expenses.addExpenseAndVerifyBudget(negativeExpense);
			fail("Expected exception");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().equals("Value can't be negative or '0'"));
		} catch (ApproachingBudgetLimit e) {
			assertTrue(e.getMessage().equals("You have exceeded 95% of your budget set for the year"));
		}
	}

	@Test
	public void testSetBudget() {
		expenses.setBudget(100000);
		assertTrue(expenses.budget == 100000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetBudgetNegative() {
		expenses.setBudget(-10000);
	}

	@Test
	public void testAddExpenseType() {
		try {
			for (Expense exp : setupExpenses) {
				expenses.addExpenseAndVerifyBudget(exp);
			}
		} catch (ApproachingBudgetLimit e) {
			assertTrue(e.getMessage().equals("You have exceeded 95% of your budget set for the year"));
		}
		assertTrue(expenses.getDailyExpenses().contains(dailyExpense));
		assertFalse(expenses.getWeeklyExpenses().contains(dailyExpense));
		assertFalse(expenses.getMonthlyExpenses().contains(dailyExpense));
		assertFalse(expenses.getYearlyExpenses().contains(dailyExpense));
		assertTrue(expenses.getUniqueExpenses().contains(dailyExpense));

		assertFalse(expenses.getDailyExpenses().contains(weeklyExpense));
		assertTrue(expenses.getWeeklyExpenses().contains(weeklyExpense));
		assertFalse(expenses.getMonthlyExpenses().contains(weeklyExpense));
		assertFalse(expenses.getYearlyExpenses().contains(weeklyExpense));
		assertTrue(expenses.getUniqueExpenses().contains(weeklyExpense));

		assertFalse(expenses.getDailyExpenses().contains(monthlyExpense));
		assertFalse(expenses.getWeeklyExpenses().contains(monthlyExpense));
		assertTrue(expenses.getMonthlyExpenses().contains(monthlyExpense));
		assertFalse(expenses.getYearlyExpenses().contains(monthlyExpense));
		assertTrue(expenses.getUniqueExpenses().contains(monthlyExpense));

		assertFalse(expenses.getDailyExpenses().contains(yearlyExpense));
		assertFalse(expenses.getWeeklyExpenses().contains(yearlyExpense));
		assertFalse(expenses.getMonthlyExpenses().contains(yearlyExpense));
		assertTrue(expenses.getYearlyExpenses().contains(yearlyExpense));
		assertTrue(expenses.getUniqueExpenses().contains(yearlyExpense));
	}

	@Test
	public void testMergingExpenses() throws ApproachingBudgetLimit {
		for (Expense exp : setupExpenses) {
			expenses.addExpenseAndVerifyBudget(exp);
		}
		for (Expense exp : setupExpenses) {
			assertTrue(expenses.getAllExpenses().contains(exp));
		}
	}
}
