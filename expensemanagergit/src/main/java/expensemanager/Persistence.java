package expensemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import expensemanager.expenselist.ExpenseList;

/**
 * Persistance class for saving in a file the expense list, and than the option
 * to load that file to see the last expenses
 * 
 * @author Flo&Geo
 *
 */
public class Persistence {

	private Path file = Paths.get("Expenses.ser");
	/**
	 * Logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	/**
	 * saving the expense list method
	 * 
	 * @param expenses
	 *            the expenses list to be saved
	 */
	public void saveExpenseList(ExpenseList expenses) {

		try (OutputStream os = Files.newOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(os)) {
			oos.writeObject(expenses);
		} catch (IOException e) {
			System.out.println("problem saving expenses " + e.getMessage());
			LOGGER.warning("Could not save expenses! " + e.getMessage());

		}
	}

	/**
	 * load the expense list
	 * 
	 * @return the expense list that are going to be loaded
	 */
	public ExpenseList loadExpenses() {
		ExpenseList expenses = null;
		if (Files.exists(file)) {
			try (InputStream is = Files.newInputStream(file); ObjectInputStream ois = new ObjectInputStream(is)) {
				expenses = (ExpenseList) ois.readObject();
			} catch (IOException e) {
				System.out.println("problem loading expenses " + e.getMessage());
				LOGGER.warning("Could not load expenses! " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("problem loading expenses " + e.getMessage());
				LOGGER.warning("Could not load expenses! " + e.getMessage());
			}
		}
		return expenses;
	}

}
