package expensemanager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import expensemanager.expenselist.Expense;
import expensemanager.expenselist.Expense.ExpenseType;

public class ExpenseTest {
	
	Expense dailyExpense;
	Expense weeklyExpense;
	Expense monthlyExpense;
	Expense yearlyExpense;
	Expense negativeExpense;

	@Before
	public void setup() {

		dailyExpense = new Expense("Bread", 3, "2016-05-11", ExpenseType.DAILY);
		weeklyExpense = new Expense("Tennis", 25, "2016-05-15", ExpenseType.WEEKLY);
		monthlyExpense = new Expense("Movie", 55, "2017-08-11", ExpenseType.MONTHLY);
		yearlyExpense = new Expense("Holiday", 3278, "2016-06-11", ExpenseType.YEARLY);
	}
	
	@Test
	public void testExpense() {
		
		assertEquals(3, dailyExpense.getValue(), 0.0);	
		assertEquals(25, weeklyExpense.getValue(), 0.0);	
		assertEquals(55, monthlyExpense.getValue(), 0.0);	
		assertEquals(3278, yearlyExpense.getValue(), 0.0);
		
		assertEquals(ExpenseType.DAILY, dailyExpense.getExpenseType());	
		assertEquals(ExpenseType.WEEKLY, weeklyExpense.getExpenseType());	
		assertEquals(ExpenseType.MONTHLY, monthlyExpense.getExpenseType());	
		assertEquals(ExpenseType.YEARLY, yearlyExpense.getExpenseType());

		assertEquals("Bread", dailyExpense.getName());	
		assertEquals("Tennis", weeklyExpense.getName());	
		assertEquals("Movie", monthlyExpense.getName());	
		assertEquals("Holiday", yearlyExpense.getName());

	}

}
