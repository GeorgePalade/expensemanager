package expensemanager.expenselist;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Expense class allow to insert the name, value, date and type of the expense
 * in ExpenseManagerApp
 * 
 * @version 1.00
 * @author Flo&Geo
 *
 */
public class Expense implements Serializable, Comparable<Expense> {

	private static final long serialVersionUID = 1L;
	public final static Expense MIN_EXPENSE_VALUE = new Expense("minExpense", 0, "2000-01-01", ExpenseType.DAILY);
	/**
	 * logger for this class
	 */
	public final static Logger LOGGER = Logger.getGlobal();

	public enum ExpenseType {
		DAILY(365, 30), WEEKLY(52, 4), MONTHLY(12, 1), YEARLY(1, 1);

		private int yearlyMultiplicator;
		private int monthlyMultiplicator;

		/**
		 * Constructor for the enum ExpenseType,allowing to set in the
		 * paramaters a value for year and month in days, weeks and years.
		 * 
		 * @param yearlyMultiplicator
		 *            value for yearly multiplicator
		 * @param monthlyMultiplicator
		 *            value for monthly multiplicator
		 */
		ExpenseType(int yearlyMultiplicator, int monthlyMultiplicator) {
			this.yearlyMultiplicator = yearlyMultiplicator;
			this.monthlyMultiplicator = monthlyMultiplicator;
		}

		/**
		 * Get and return the yearly multiplicated value
		 * 
		 * @return yearlymMltimplicator
		 */
		public int getYearlyMultiplicator() {
			return this.yearlyMultiplicator;
		}

		/**
		 * Get and return the monthly multiplicated value
		 * 
		 * @return monthlyMultiplicator
		 */
		public int getMonthlyMultiplicator() {
			return this.monthlyMultiplicator;
		}
	}

	private String name;
	private float value;
	private LocalDate date;
	private ExpenseType type;

	/**
	 * Constructor for expense, allowing to add name, value, date and type of
	 * the expense
	 * 
	 * @param name
	 *            of the expense
	 * @param value
	 *            of the expense added
	 * @param date
	 *            the date when expense is inserted
	 * @param type
	 *            of the expense, DAYLY,MONTHLY,etc
	 */
	public Expense(String name, float value, LocalDate date, ExpenseType type) {
		this.name = name;
		this.value = value;
		this.date = date;
		this.type = type;
	}

	/**
	 * Overrloading the constructor to format the date
	 * 
	 * @param name
	 *            of the expense
	 * @param value
	 *            of the expense added
	 * @param date
	 *            the date when expense is inserted
	 * @param type
	 *            of the expense, DAYLY,MONTHLY,etc
	 */
	public Expense(String name, float value, String date, ExpenseType type) {
		final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.name = name;
		this.value = value;
		this.date = LocalDate.parse(date, DATE_FORMAT);
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expense other = (Expense) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + ", " + value + ", " + date + ", " + type;
	}

	/**
	 * get yearly value after switching the type
	 * 
	 * @return the yearlyValue for each type
	 */
	public float getYearlyValue() {
		switch (this.type) {
		case MONTHLY:
			return this.value * ExpenseType.MONTHLY.getYearlyMultiplicator();
		case WEEKLY:
			return this.value * ExpenseType.WEEKLY.getYearlyMultiplicator();
		case DAILY:
			return this.value * ExpenseType.DAILY.getYearlyMultiplicator();
		default:
			return this.value * ExpenseType.YEARLY.getYearlyMultiplicator();
		}
	}

	/**
	 * get monthly value after switching the type
	 * 
	 * @return the monthlyValue for each type
	 */
	public float getMonthlyValue() {
		switch (this.type) {
		case MONTHLY:
			return this.value * ExpenseType.MONTHLY.getMonthlyMultiplicator();
		case WEEKLY:
			return this.value * ExpenseType.WEEKLY.getMonthlyMultiplicator();
		case DAILY:
			return this.value * ExpenseType.DAILY.getMonthlyMultiplicator();
		default:
			return this.value * ExpenseType.YEARLY.getMonthlyMultiplicator();
		}
	}

	/**
	 * get the expense type
	 * 
	 * @return the expense type
	 */
	public ExpenseType getExpenseType() {
		return this.type;
	}

	/**
	 * get the month of the year
	 * 
	 * @return the month of the year
	 */
	public Month getMonth() {
		return this.date.getMonth();
	}

	/**
	 * get the day of the week
	 * 
	 * @return the day of the week
	 */
	public DayOfWeek getDay() {
		return this.date.getDayOfWeek();
	}

	/**
	 * get the year
	 * 
	 * @return the year
	 */
	public int getYear() {
		return this.date.getYear();
	}

	/**
	 * name of the expense
	 * 
	 * @return the name of the expense
	 */
	public String getName() {
		return name;
	}

	/**
	 * set the name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the value of the expense
	 * 
	 * @return the value of expense
	 */
	public float getValue() {
		return value;
	}

	/**
	 * setting the value
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * get the date for the expense
	 * 
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * set the date for the expense
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Expense e) {
		return this.getDate().compareTo(e.getDate());
	}

}
