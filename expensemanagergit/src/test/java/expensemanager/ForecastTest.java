package expensemanager;

import static org.junit.Assert.assertEquals;

import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import expensemanager.exceptions.ApproachingBudgetLimit;
import expensemanager.expenselist.Expense;
import expensemanager.expenselist.Expense.ExpenseType;
import expensemanager.expenselist.ExpenseList;
import expensemanager.expenselist.Forecast;

public class ForecastTest {

	Expense dailyExpense;
	Expense weeklyExpense;
	Expense monthlyExpense;
	Expense yearlyExpense;
	Expense negativeExpense;
	ExpenseList expenses;
	Forecast forecast;

	@Before
	public void setup() {

		dailyExpense = new Expense("Bread", 3, "2016-05-11", ExpenseType.DAILY);
		weeklyExpense = new Expense("Tennis", 25, "2016-05-15", ExpenseType.WEEKLY);
		monthlyExpense = new Expense("Movie", 55, "2017-08-11", ExpenseType.MONTHLY);
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

		forecast = new Forecast(expenses);
	}

	@Test
	public void testCalculateForecastByYearAndMonth() {
		int year = 2017;
		Month month = Month.MAY;
		float forecastedAmount = (dailyExpense.getMonthlyValue() + weeklyExpense.getMonthlyValue()) * 1.05f;
		assertEquals(forecastedAmount, 190 * 1.05f, 0);
		assertEquals(forecastedAmount, forecast.calculateForecastByYearAndMonth(year, month), 0);
	}

	@Test
	public void testCalculateForecastPerYear() {
		int year = 2017;
		float forecastedAmount = (dailyExpense.getYearlyValue() + weeklyExpense.getYearlyValue()
				+ yearlyExpense.getYearlyValue()) * 1.05f;
		assertEquals(forecastedAmount, 4900 * 1.05f, 0);
		assertEquals(forecastedAmount, forecast.calculateForecastPerYear(year), 0f);
	}
	
}
