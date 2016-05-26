package expensemanager;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Setting and configure a logging class with Logger and Handler
 * 
 * @version 1.00
 * @author Flo&Geo
 */
public class Logging {

	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	public void configure(String filename) {
		try {
			Handler fileHandler = configureFileLog(filename);
			Logger.getGlobal().addHandler(fileHandler);
			Handler consoleHandler = configureConsoleLog();
			Logger.getGlobal().addHandler(consoleHandler);
			Logger.getGlobal().setLevel(Level.ALL);
		} catch (SecurityException | IOException e) {
			LOGGER.log(Level.SEVERE, "cannot init logging system");
		}

		LOGGER.info("Logging configured properly for console and file: " + filename);
	}

	/**
	 * configure handler
	 * 
	 * @param filename
	 *            name of the file
	 * @return the FileHandler with a filename
	 * @throws IOException,
	 *             throws a checked exception
	 */
	private Handler configureFileLog(String filename) throws IOException {
		Handler handler = new FileHandler(filename);
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.INFO);
		LOGGER.info("FileHandler created for file: " + filename + " " + handler);
		return handler;
	}

	/**
	 * console handler
	 * 
	 * @return a ConsoleHandler handler
	 */
	private Handler configureConsoleLog() {
		Handler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		LOGGER.info("ConsoleHandler created for " + handler);
		return handler;
	}

}