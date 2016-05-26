package expensemanager;

import static org.junit.Assert.*;

import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import expensemanager.exceptions.ApproachingBudgetLimit;
import expensemanager.exceptions.NoExpenseAddedExeption;
import expensemanager.expenselist.Expense;
import expensemanager.expenselist.ExpenseList;
import expensemanager.expenselist.Statistics;
import expensemanager.expenselist.Expense.ExpenseType;

public class StatisticsTest {

	Expense dailyExpense;
	Expense weeklyExpense;
	Expense monthlyExpense;
	Expense yearlyExpense;
	Expense negativeExpense;
	ExpenseList expenses;
	Statistics statistics;

	@Before
	public void setup() {

		dailyExpense = new Expense("Bread", 3, "2016-05-11", ExpenseType.DAILY);
		weeklyExpense = new Expense("Tennis", 25, "2016-05-15", ExpenseType.WEEKLY);
		monthlyExpense = new Expense("Movie", 110, "2017-08-11", ExpenseType.MONTHLY);
		yearlyExpense = new Expense("Holiday", 2505, "2016-06-11", ExpenseType.YEARLY);

		expenses = new ExpenseList();
		try {
			expenses.addExpenseAndVerifyBudget(dailyExpense);
			expenses.addExpenseAndVerifyBudget(weeklyExpense);
			expenses.addExpenseAndVerifyBudget(monthlyExpense);
			expenses.addExpenseAndVerifyBudget(yearlyExpense);
		} catch (ApproachingBudgetLimit e) {
			System.out.println(e.getMessage());
		}

		statistics = new Statistics(expenses);
	}

	@Test
	public void testGetHighestExpenseInYear() throws NoExpenseAddedExeption {
		assertEquals(statistics.getHighestExpenseInYear(2016), yearlyExpense);
		assertEquals(statistics.getHighestExpenseInYear(2017), monthlyExpense);
	}

	@Test
	public void testGetHighestExpenseInYearIfNoExpenseAdded() {
		try {
			statistics.getHighestExpenseInYear(2018);
			fail("Expected exception");
		} catch (NoExpenseAddedExeption e) {
			assertTrue(e.getMessage().equals("No expense added in year: 2018"));
		}
	}

	@Test
	public void testGetHighestExpenseInMonth() throws NoExpenseAddedExeption {
		assertEquals(statistics.getHighestExpenseInMonth(Month.MAY), weeklyExpense);
		assertEquals(statistics.getHighestExpenseInMonth(Month.JUNE), yearlyExpense);
		assertEquals(statistics.getHighestExpenseInMonth(Month.AUGUST), monthlyExpense); 
	}

	@Test
	public void testGetHighestExpenseInMonthIfNoExpenseAdded() {
		try {
			statistics.getHighestExpenseInMonth(Month.APRIL);
			fail("Expected exception");
		} catch (NoExpenseAddedExeption e) {
			assertTrue(e.getMessage().equals("No expense added in month: APRIL"));
		}
	}

	@Test
	public void testGetHighestExpenseByYearAndMonth() throws NoExpenseAddedExeption {
		assertEquals(statistics.getHighestExpenseByYearAndMonth(2016, Month.MAY), weeklyExpense);
		assertEquals(statistics.getHighestExpenseByYearAndMonth(2016, Month.JUNE), yearlyExpense);
		assertEquals(statistics.getHighestExpenseByYearAndMonth(2017, Month.AUGUST), monthlyExpense); 
	}

	@Test
	public void testGetHighestExpenseByYearAndMonthIfNoExpenseAdded() {
		try {
			statistics.getHighestExpenseByYearAndMonth(2017, Month.APRIL);
			fail("Expected exception");
		} catch (NoExpenseAddedExeption e) {
			assertTrue(e.getMessage().equals("No expense added in month: APRIL"));
		}
	}
}
