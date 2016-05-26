package expensemanager.exceptions;

/**
 * 
 * @author Flo&Geo
 *
 */
public class ApproachingBudgetLimit extends Exception {

	/**
	 * exception class that extends super class Exception
	 */
	private static final long serialVersionUID = 1L;

	public ApproachingBudgetLimit(String s) {
		super(s);
	}

}
