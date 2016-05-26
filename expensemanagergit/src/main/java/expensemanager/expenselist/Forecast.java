package expensemanager.expenselist;

import java.time.Month;
import java.util.logging.Logger;

/**
 * 
 * Forecast class is to predict how much money is needed in a month or per year.
 * The forecast considers current period and adds 5% to it for a future similar
 * period (same month or whole year).
 * 
 * @author Flo&Geo
 *
 */

public class Forecast {

	private ExpenseList expensesList;
	private float forecastAmount;

	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	/**
	 * constructor for the Forecast
	 * 
	 * @param expenses
	 *            keep the expensesList
	 */
	public Forecast(ExpenseList expenses) {
		this.expensesList = expenses;
	}

	/**
	 * calculate the forecast by year and month
	 * 
	 * @param year
	 *            this year will get calculated
	 * @param month
	 *            this month will get calculated
	 * 
	 * @return this calculated forecastAmount *1.05
	 */
	public float calculateForecastByYearAndMonth(int year, Month month) {
		forecastAmount = 0f;
		for (Expense exp : expensesList.getUniqueExpenses()) {
			if (exp.getMonth().equals(month) && exp.getYear() == year - 1) {
				forecastAmount += exp.getMonthlyValue();
				LOGGER.info("Returned forecast by year " + exp.getYear() + " " + "and month: " + exp.getMonth() + " "
						+ forecastAmount * 1.05f);
			}

		}

		return forecastAmount * 1.05f;
	}

	/**
	 * calculate the forecast only by year
	 * 
	 * @param year
	 *            that will get calculated
	 * 
	 * @return this calculated forecastAmount*1.05
	 */
	public float calculateForecastPerYear(int year) {
		forecastAmount = 0f;
		for (Expense exp : expensesList.getUniqueExpenses()) {
			if (exp.getYear() == year - 1) {
				forecastAmount += exp.getYearlyValue();
				LOGGER.info("Returned forecast by year: " + exp.getYear() + " " + forecastAmount * 1.05f);
			}

		}

		return forecastAmount * 1.05f;

	}

}
