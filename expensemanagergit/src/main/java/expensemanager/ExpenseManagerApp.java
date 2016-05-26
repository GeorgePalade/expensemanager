package expensemanager;

import java.awt.EventQueue;
import java.util.logging.Logger;

import expensemanager.gui.ExpenseManagerFrame;

/**
 * The main app runnable in GUI
 * 
 * @version 1.00
 * @since 2016-May
 * @author George
 *
 */

public class ExpenseManagerApp {
	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		new Logging().configure("expense-manager.log");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ExpenseManagerFrame window = new ExpenseManagerFrame();
				window.setVisible(true);
				LOGGER.info("ExpenseManagerApp GUI available");
			}
		});
		LOGGER.info("ExpenseManagerApp started");
	}
}