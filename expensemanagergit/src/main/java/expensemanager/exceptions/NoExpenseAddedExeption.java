package expensemanager.exceptions;

/**
 * 
 * @author Flo&Geo
 *
 */
public class NoExpenseAddedExeption extends Exception {

	/**
	 * exception class that extends super class Exception
	 */
	private static final long serialVersionUID = 1L;

	public NoExpenseAddedExeption(String s) {
		super(s);
	}
}
