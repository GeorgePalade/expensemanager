package expensemanager.gui;

import java.util.logging.Logger;

import javax.swing.RowFilter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import expensemanager.expenselist.Expense;
import expensemanager.expenselist.ExpenseList;

/**
 * 
 * @author Flo&Geo
 *
 */
public class FilterAndSubtotal {

	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	ExpenseManagerFrame frame;
	DefaultTableModel dataModel;

	/**
	 * constructor for this class
	 * 
	 * @param frame
	 * @param dataModel
	 */
	public FilterAndSubtotal(ExpenseManagerFrame frame, DefaultTableModel dataModel) {
		this.frame = frame;
		this.dataModel = dataModel;
	}

	/**
	 * filter method by query and type
	 * 
	 * @param query
	 */
	void filter(String query) {
		TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(dataModel);
		tr.addRowSorterListener(new RowSorterListener() {
			@Override
			public void sorterChanged(RowSorterEvent e) {
				frame.lblSubtotalValue.setText((subtotal()) + "");
			}
		});
		frame.table.setRowSorter(tr);
		tr.setRowFilter(RowFilter.regexFilter(query));
		LOGGER.info("Filtered by: " + query + " and type: " + frame.comboBoxExpType2.getSelectedItem().toString());
	}

	/**
	 * clear filter and show unique values
	 * 
	 * @param expenses
	 *            get unique values
	 */
	void clearFilters(ExpenseList expenses) {
		filter("");
		dataModel.setRowCount(0);
		for (Expense exp : expenses.getUniqueExpenses()) {
			dataModel.addRow(new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });

		}
		LOGGER.info("Filter cleaning to see the unique expense types.");
	}

	/**
	 * populate the table rows with the selected items
	 * 
	 * @param expenseType
	 * @param expenses
	 */
	void populateRowsWithSelectedExpenseType(String expenseType, ExpenseList expenses) {
		switch (expenseType) {
		case "ALL":
			dataModel.setRowCount(0);
			for (Expense exp : expenses.getAllExpenses()) {
				dataModel.addRow(new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
			}
			break;
		case "DAILY":
			dataModel.setRowCount(0);
			for (Expense exp : expenses.getDailyExpenses()) {
				dataModel.addRow(new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
			}
			break;
		case "WEEKLY":
			dataModel.setRowCount(0);
			for (Expense exp : expenses.getWeeklyExpenses()) {
				dataModel.addRow(new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
			}
			break;
		case "MONTHLY":
			dataModel.setRowCount(0);
			for (Expense exp : expenses.getMonthlyExpenses()) {
				dataModel.addRow(new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
			}
			break;
		case "YEARLY":
			dataModel.setRowCount(0);
			for (Expense exp : expenses.getYearlyExpenses()) {
				dataModel.addRow(new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
			}
			break;
		}
	}

	/**
	 * calculate the subtotal of the selected expense type
	 * 
	 * @return the subtotal float value
	 */
	float subtotal() {
		float subtotal = 0f;
		for (int row = 0; row < frame.table.getRowCount(); row++) {
			subtotal += Float.parseFloat(dataModel.getValueAt(frame.table.convertRowIndexToModel(row), 1).toString());

		}
		// LOGGER.info("Expenses subtotal = " + subtotal);
		return subtotal;
	}
}
