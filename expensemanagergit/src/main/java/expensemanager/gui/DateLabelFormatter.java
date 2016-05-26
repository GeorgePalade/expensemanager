package expensemanager.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * Date formatter class to set a custom date format
 * 
 * @author Flo&Geo
 *
 */

public class DateLabelFormatter extends AbstractFormatter {

	private static final long serialVersionUID = 1L;

	private String datePattern = "yyyy-MM-dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.JFormattedTextField.AbstractFormatter#stringToValue(java.lang
	 * .String)
	 */
	@Override
	public Object stringToValue(String text) throws ParseException {
		return dateFormatter.parseObject(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.JFormattedTextField.AbstractFormatter#valueToString(java.lang
	 * .Object)
	 */
	@Override
	public String valueToString(Object value) throws ParseException {
		if (value != null) {
			Calendar cal = (Calendar) value;
			return dateFormatter.format(cal.getTime());
		}

		return "";
	}

}
