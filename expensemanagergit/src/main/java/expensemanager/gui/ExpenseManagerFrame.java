package expensemanager.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import expensemanager.Persistence;
import expensemanager.exceptions.ApproachingBudgetLimit;
import expensemanager.exceptions.NoExpenseAddedExeption;
import expensemanager.expenselist.Expense;
import expensemanager.expenselist.ExpenseList;
import expensemanager.expenselist.Forecast;
import expensemanager.expenselist.Statistics;
import expensemanager.expenselist.Expense.ExpenseType;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * The window frame GUI
 * 
 * @author Flo&Geo
 *
 */
public class ExpenseManagerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private ExpenseList expenses = new ExpenseList();
	private Statistics stats;
	private Forecast forecast;
	private Persistence persistence = new Persistence();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public static final Logger LOGGER = Logger.getGlobal();

	JTable table;
	JLabel lblSubtotalValue = new JLabel("0");
	JComboBox<String> comboBoxExpType2 = new JComboBox<String>();

	private JTextField textFieldName;
	private JTextField textFieldValue;
	private JLabel lblType;
	private JLabel lblDate;
	private JLabel lblBudgetActualValue = new JLabel("");
	private JScrollPane scrollPane;
	private JTextField textFieldYear1Stats;
	private JTextField textFieldYear2Stats;
	private JTextField textFieldBudgetValue;
	private JTextField textFieldYear1Fore;
	private JTextField textFieldYear2Fore;
	private JTextField textFieldYearMonthFilter;
	private JTextField textFieldYearFilter;
	private JComboBox<ExpenseType> comboBoxExpType1 = new JComboBox<ExpenseType>();

	private UtilDateModel model2 = new UtilDateModel();
	private JDatePanelImpl datePanelAdd = new JDatePanelImpl(model2);
	private JDatePickerImpl datePickerAdd = new JDatePickerImpl(datePanelAdd, new DateLabelFormatter());
	private DefaultTableModel dataModel = new DefaultTableModel(new Object[][] {},
			new String[] { "Name", "Value", "Date ", "Type" });

	private FilterAndSubtotal calculations = new FilterAndSubtotal(this, dataModel);

	public ExpenseManagerFrame() {
		super();

		LOGGER.fine("Loading saved expense list.");
		expenses = persistence.loadExpenses();

		if (expenses == null) {
			LOGGER.severe("no expenses list available");
			expenses = new ExpenseList();
			LOGGER.info("New expense list created!");
		}

		stats = new Statistics(expenses);
		forecast = new Forecast(expenses);

		setResizable(false);
		/**
		 * saving or not the list
		 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int PromptResult = JOptionPane.showConfirmDialog(null, "Do you want to save changes before exit?",
						"Expense Manager", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (PromptResult == JOptionPane.YES_OPTION) {
					persistence.saveExpenseList(expenses);
					LOGGER.info("expense list saved");
					System.exit(0);
				} else if (PromptResult == JOptionPane.NO_OPTION) {
					LOGGER.info("expense list not saved");
					System.exit(0);
				}
			}

			/**
			 * populate the table rows with unique types
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				for (Expense exp : expenses.getUniqueExpenses()) {
					dataModel.addRow(
							new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
				}
				LOGGER.info("Expense list loaded");
				lblSubtotalValue.setText(calculations.subtotal() + "");
				if (expenses.budget == Float.MAX_VALUE) {
					lblBudgetActualValue.setText("Not set yet");
					LOGGER.warning("Budget value not set");
				} else {
					lblBudgetActualValue.setText(expenses.budget + "");
					LOGGER.info("Budget limit loaded: " + expenses.budget);
				}
			}
		});
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);
		setSize(1200, 650);
		setTitle("Expense Manager ");
		setLocationRelativeTo(null);

		createGUI();

	}

	/**
	 * creating the GUI
	 */
	private void createGUI() {

		scrollPane = new JScrollPane();
		scrollPane.setBounds(619, 323, 525, 252);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		table.setModel(dataModel);

		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanelFilter = new JDatePanelImpl(model);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1194, 22);
		getContentPane().add(menuBar);
		/*
		 * creating jmneu file
		 */
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Tahoma", Font.BOLD, 13));
		menuBar.add(mnFile);

		final JDatePickerImpl datePickerFilter = new JDatePickerImpl(datePanelFilter, new DateLabelFormatter());
		datePickerFilter.getJFormattedTextField().setText("Select Date");
		datePickerFilter.setBounds(231, 420, 115, 28);
		getContentPane().add(datePickerFilter);

		datePickerAdd.getJFormattedTextField().setText("Select Date");
		datePickerAdd.setBounds(99, 170, 120, 28);
		getContentPane().add(datePickerAdd);

		/**
		 * creating add expense label
		 */

		JLabel lblAddExpense = new JLabel("Add Expense");
		lblAddExpense.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddExpense.setForeground(new Color(0, 0, 205));
		lblAddExpense.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAddExpense.setBounds(0, 25, 242, 22);
		getContentPane().add(lblAddExpense);

		/**
		 * creating statistics label
		 */
		JLabel lblStatistics = new JLabel("Statistics");
		lblStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatistics.setForeground(new Color(0, 0, 205));
		lblStatistics.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblStatistics.setBounds(239, 21, 383, 30);
		getContentPane().add(lblStatistics);
		/**
		 * creating budget label
		 */
		JLabel lblBudget = new JLabel("Budget");
		lblBudget.setHorizontalAlignment(SwingConstants.CENTER);
		lblBudget.setForeground(new Color(0, 0, 205));
		lblBudget.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblBudget.setBounds(619, 25, 227, 23);
		getContentPane().add(lblBudget);
		/**
		 * creating forecast label
		 */
		JLabel lblForecast = new JLabel("Forecast");
		lblForecast.setHorizontalAlignment(SwingConstants.CENTER);
		lblForecast.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblForecast.setForeground(new Color(0, 0, 205));
		lblForecast.setBounds(846, 29, 338, 14);
		getContentPane().add(lblForecast);
		/**
		 * creating filter expenses label
		 */
		JLabel lblFilterExpenses = new JLabel("Filter Expenses");
		lblFilterExpenses.setForeground(new Color(0, 0, 205));
		lblFilterExpenses.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblFilterExpenses.setBounds(260, 324, 133, 23);
		getContentPane().add(lblFilterExpenses);
		/**
		 * creating name label
		 */
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(43, 70, 46, 14);
		getContentPane().add(lblName);
		/**
		 * creating value label
		 */
		JLabel lblValue = new JLabel("Value");
		lblValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblValue.setBounds(43, 95, 46, 14);
		getContentPane().add(lblValue);
		/**
		 * creating type label
		 */
		lblType = new JLabel("Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblType.setBounds(43, 142, 46, 22);
		getContentPane().add(lblType);

		/**
		 * creating date label
		 */
		lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDate.setBounds(43, 175, 46, 14);
		getContentPane().add(lblDate);

		/**
		 * creating year label
		 */

		JLabel lblYear1 = new JLabel("Year");
		lblYear1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYear1.setBounds(309, 70, 37, 14);
		getContentPane().add(lblYear1);

		/**
		 * return highest expense in selected year in a label
		 */

		final JLabel lblHighestExpInYear = new JLabel("Shows Highest Expense In Selected Year");
		lblHighestExpInYear.setForeground(new Color(0, 128, 0));
		lblHighestExpInYear.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHighestExpInYear.setBounds(260, 99, 351, 22);
		getContentPane().add(lblHighestExpInYear);
		/**
		 * creating year label
		 */
		JLabel lblYear2 = new JLabel("Year");
		lblYear2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYear2.setBounds(309, 142, 46, 14);
		getContentPane().add(lblYear2);
		/**
		 * creating month label
		 */
		JLabel lblMonth1 = new JLabel("Month");
		lblMonth1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMonth1.setBounds(309, 172, 46, 14);
		getContentPane().add(lblMonth1);

		/**
		 * return highest expense in selected month or year&month in a label
		 */
		final JLabel lblHighestExpInMonth = new JLabel("Shows Highest Expense In Selected Month or Year&Month");
		lblHighestExpInMonth.setForeground(new Color(0, 128, 0));
		lblHighestExpInMonth.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHighestExpInMonth.setBounds(260, 206, 351, 18);
		getContentPane().add(lblHighestExpInMonth);
		/**
		 * creating value label
		 */
		JLabel lblBudgetValue = new JLabel("Value");
		lblBudgetValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBudgetValue.setBounds(659, 70, 46, 14);
		getContentPane().add(lblBudgetValue);
		/**
		 * creating curent budget label
		 */
		JLabel lblCurrentBudget = new JLabel("Current budget/year = ");
		lblCurrentBudget.setForeground(new Color(0, 128, 0));
		lblCurrentBudget.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCurrentBudget.setBounds(635, 147, 133, 17);
		getContentPane().add(lblCurrentBudget);

		lblBudgetActualValue.setForeground(new Color(0, 128, 0));
		lblBudgetActualValue.setBounds(769, 147, 67, 17);
		getContentPane().add(lblBudgetActualValue);

		JLabel lblYear3 = new JLabel("Year");
		lblYear3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYear3.setBounds(916, 70, 46, 14);
		getContentPane().add(lblYear3);

		final JLabel lblYearlyForecast = new JLabel("Forecast for selected year");
		lblYearlyForecast.setForeground(new Color(0, 128, 0));
		lblYearlyForecast.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYearlyForecast.setBounds(876, 95, 169, 14);
		getContentPane().add(lblYearlyForecast);

		JLabel lblYear4 = new JLabel("Year");
		lblYear4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYear4.setBounds(916, 142, 37, 14);
		getContentPane().add(lblYear4);

		JLabel lblMonth2 = new JLabel("Month");
		lblMonth2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMonth2.setBounds(916, 172, 46, 14);
		getContentPane().add(lblMonth2);

		final JLabel lblMonthlyForecast = new JLabel("Forecast for Selected Year&Month");
		lblMonthlyForecast.setForeground(new Color(0, 128, 0));
		lblMonthlyForecast.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMonthlyForecast.setBounds(876, 205, 267, 14);
		getContentPane().add(lblMonthlyForecast);

		JLabel lblTypeFilter = new JLabel("Type");
		lblTypeFilter.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTypeFilter.setBounds(147, 375, 46, 14);
		getContentPane().add(lblTypeFilter);

		JLabel lblDateFilter = new JLabel("Date");
		lblDateFilter.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDateFilter.setBounds(147, 420, 31, 14);
		getContentPane().add(lblDateFilter);

		JLabel lblYearMonthFilter = new JLabel("Year-Month");
		lblYearMonthFilter.setToolTipText("keep format");
		lblYearMonthFilter.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYearMonthFilter.setBounds(147, 472, 90, 34);
		getContentPane().add(lblYearMonthFilter);

		JLabel lblYearFilter = new JLabel("Year");
		lblYearFilter.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblYearFilter.setBounds(147, 530, 46, 14);
		getContentPane().add(lblYearFilter);

		JLabel lblClearFilters = new JLabel("Clear filter and show unique values");
		lblClearFilters.setForeground(Color.RED);
		lblClearFilters.setBounds(147, 572, 200, 14);
		getContentPane().add(lblClearFilters);

		JLabel lblSubtotal = new JLabel("Subtotal");
		lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSubtotal.setForeground(new Color(0, 128, 0));
		lblSubtotal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSubtotal.setBounds(643, 576, 90, 14);
		getContentPane().add(lblSubtotal);

		lblSubtotalValue.setForeground(new Color(0, 128, 0));
		lblSubtotalValue.setBounds(747, 577, 125, 14);
		getContentPane().add(lblSubtotalValue);
		/**
		 * creating text field for name
		 */
		textFieldName = new JTextField();
		textFieldName.setBounds(99, 68, 120, 20);
		getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		/**
		 * creating error dialogs
		 */
		final JDialog dialog = new JDialog();
		dialog.setResizable(false);
		dialog.setBounds(550, 400, 300, 100);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Error");

		final JTextPane textPane = new JTextPane();
		textPane.setSize(300, 100);
		textPane.setEditable(false);

		dialog.getContentPane().add(textPane);
		/**
		 * creating text field for value
		 */
		textFieldValue = new JTextField();
		textFieldValue.setBounds(99, 93, 120, 20);
		getContentPane().add(textFieldValue);
		textFieldValue.setColumns(10);

		textFieldYear1Stats = new JTextField();
		textFieldYear1Stats.setBounds(356, 68, 86, 20);
		getContentPane().add(textFieldYear1Stats);
		textFieldYear1Stats.setColumns(10);

		textFieldYear2Stats = new JTextField();
		textFieldYear2Stats.setToolTipText(
				"leave \"Just by month\" here if you want the highest expense in a month for all the years. Input the year in format \"YYYY\" if you want to restrain the statistic to a specific year.");
		textFieldYear2Stats.setText("Just By Month");
		textFieldYear2Stats.setBounds(356, 138, 86, 20);
		getContentPane().add(textFieldYear2Stats);
		textFieldYear2Stats.setColumns(10);

		textFieldBudgetValue = new JTextField();
		textFieldBudgetValue.setToolTipText("set budget limit");
		textFieldBudgetValue.setBounds(729, 68, 86, 20);
		getContentPane().add(textFieldBudgetValue);
		textFieldBudgetValue.setColumns(10);

		textFieldYear1Fore = new JTextField();
		textFieldYear1Fore.setBounds(959, 68, 86, 20);
		getContentPane().add(textFieldYear1Fore);
		textFieldYear1Fore.setColumns(10);

		textFieldYear2Fore = new JTextField();
		textFieldYear2Fore.setBounds(959, 138, 86, 20);
		getContentPane().add(textFieldYear2Fore);
		textFieldYear2Fore.setColumns(10);

		textFieldYearMonthFilter = new JTextField();
		textFieldYearMonthFilter.setToolTipText("keep format");
		textFieldYearMonthFilter.setText("YYYY");
		textFieldYearMonthFilter.setBounds(231, 463, 115, 20);
		getContentPane().add(textFieldYearMonthFilter);
		textFieldYearMonthFilter.setColumns(10);

		textFieldYearFilter = new JTextField();
		textFieldYearFilter.setText("YYYY");
		textFieldYearFilter.setBounds(231, 528, 115, 20);
		getContentPane().add(textFieldYearFilter);
		textFieldYearFilter.setColumns(10);

		comboBoxExpType1.setModel(new DefaultComboBoxModel<ExpenseType>(ExpenseType.values()));
		comboBoxExpType1.setBounds(99, 144, 120, 20);
		getContentPane().add(comboBoxExpType1);

		comboBoxExpType2.setModel(
				new DefaultComboBoxModel<String>(new String[] { "ALL", "DAILY", "WEEKLY", "MONTHLY", "YEARLY" }));
		comboBoxExpType2.setBounds(231, 375, 115, 20);
		getContentPane().add(comboBoxExpType2);

		final JComboBox<Month> comboBoxMonth1 = new JComboBox<Month>();
		comboBoxMonth1.setModel(new DefaultComboBoxModel<Month>(Month.values()));
		comboBoxMonth1.setBounds(356, 170, 86, 22);
		getContentPane().add(comboBoxMonth1);

		final JComboBox<Month> comboBoxMonth2 = new JComboBox<Month>();
		comboBoxMonth2.setModel(new DefaultComboBoxModel<Month>(Month.values()));
		comboBoxMonth2.setBounds(959, 170, 86, 22);
		getContentPane().add(comboBoxMonth2);
		/**
		 * adding the expense button and action listener
		 */
		JButton btnAddExpense = new JButton("Add");
		btnAddExpense.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: Expense added, button pressed.");
				try {

					checkIfAllFieldsAreCompleted();
					calculations.clearFilters(expenses);
					expenses.addExpenseAndVerifyBudget(
							new Expense(textFieldName.getText(), Float.parseFloat(textFieldValue.getText()),
									formatter.format(datePickerAdd.getModel().getValue()),
									(ExpenseType) (comboBoxExpType1.getSelectedItem())));
					dataModel.addRow(new Object[] { textFieldName.getText(), textFieldValue.getText(),
							formatter.format(datePickerAdd.getModel().getValue()),
							comboBoxExpType1.getSelectedItem() });
					lblSubtotalValue.setText(calculations.subtotal() + "");
				} catch (ApproachingBudgetLimit e1) {
					LOGGER.fine("Budget limit warning displayed.");
					dialog.setTitle("Info");
					textPane.setText(e1.getMessage());
					dialog.setVisible(true);
					dataModel.addRow(new Object[] { textFieldName.getText(), textFieldValue.getText(),
							formatter.format(datePickerAdd.getModel().getValue()),
							comboBoxExpType1.getSelectedItem() });
					lblSubtotalValue.setText(calculations.subtotal() + "");
				} catch (NumberFormatException e1) {
					LOGGER.warning("Value format incorect: " + textFieldValue.getText());
					textPane.setText("Please insert a corect number for value field");
					dialog.setVisible(true);
				} catch (IllegalArgumentException e1) {
					displayError(dialog, textPane, e1);
				} catch (IllegalStateException e1) {
					textPane.setText(e1.getMessage());
					dialog.setVisible(true);
				}
			}
		});

		final JComboBox<Month> comboBoxMonth3 = new JComboBox<Month>();
		comboBoxMonth3.setModel(new DefaultComboBoxModel<Month>(Month.values()));
		comboBoxMonth3.setBounds(231, 483, 115, 22);
		getContentPane().add(comboBoxMonth3);

		JMenuItem mntmNewFile = new JMenuItem("New Expense List");
		mntmNewFile.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mntmNewFile.setBounds(0, 0, 135, 22);
		mnFile.add(mntmNewFile);
		/**
		 * crating a new expense list buton and action listener
		 */
		mntmNewFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float temp = Float.parseFloat(lblBudgetActualValue.getText());
				expenses = new ExpenseList();
				LOGGER.fine("GUI MESSAGE : new expense list button pressed");
				try {
					expenses.setBudget(temp);
					lblBudgetActualValue.setText(temp + "");
				} catch (IllegalArgumentException e1) {
					displayError(dialog, textPane, e1);
				}
				dataModel.setRowCount(0);
				for (Expense exp : expenses.getUniqueExpenses()) {
					dataModel.addRow(
							new Object[] { exp.getName(), exp.getValue(), exp.getDate(), exp.getExpenseType() });
				}
			}
		});
		/**
		 * saving the expense list button and action listener
		 */
		JMenuItem mntmSaveExpenseList = new JMenuItem("Save Expense List");
		mntmSaveExpenseList.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mntmSaveExpenseList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: saved expense list buton pressed");
				persistence.saveExpenseList(expenses);
			}
		});
		mnFile.add(mntmSaveExpenseList);

		btnAddExpense.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddExpense.setBounds(101, 217, 86, 23);
		getContentPane().add(btnAddExpense);
		/**
		 * view statistics button and action listener
		 */
		JButton btnStatsPerYear = new JButton("View");
		btnStatsPerYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: view statistics year button pressed.");
				Expense highetExpense = null;
				try {
					highetExpense = stats.getHighestExpenseInYear(Integer.parseInt(textFieldYear1Stats.getText()));
					lblHighestExpInYear.setText(highetExpense + " - Total: " + highetExpense.getYearlyValue());
				} catch (NumberFormatException e1) {
					LOGGER.warning("Year not valid" + e1.getMessage());
					textPane.setText("Year not valid");
					dialog.setVisible(true);
				} catch (NoExpenseAddedExeption e1) {
					LOGGER.warning("No expense added " + e1.getMessage());
					textPane.setText(e1.getMessage());
					dialog.setVisible(true);

				}
			}
		});
		btnStatsPerYear.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStatsPerYear.setBounds(452, 67, 89, 23);
		getContentPane().add(btnStatsPerYear);
		/**
		 * statistics just by month field
		 */
		JButton btnStatsPerMonth = new JButton("View");
		btnStatsPerMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldYear2Stats.getText().equals("Just By Month")) {
					Expense highetExpense;
					LOGGER.fine("GUI MESSAGE: field set just by month");
					try {
						highetExpense = stats.getHighestExpenseInMonth((Month) comboBoxMonth1.getSelectedItem());
						lblHighestExpInMonth.setText(highetExpense + " - Total: " + highetExpense.getMonthlyValue());
					} catch (NoExpenseAddedExeption e1) {
						LOGGER.warning("No expense added " + e1.getMessage());
						textPane.setText(e1.getMessage());
						dialog.setVisible(true);
					}
				} else {
					Expense highetExpense;
					LOGGER.fine("GUI MESSAGE: view statistics year and month button pressed ");
					try {
						highetExpense = stats.getHighestExpenseByYearAndMonth(
								Integer.parseInt(textFieldYear2Stats.getText()),
								(Month) comboBoxMonth1.getSelectedItem());
						lblHighestExpInMonth.setText(highetExpense + " - Total: " + highetExpense.getMonthlyValue());
					} catch (NumberFormatException | NoExpenseAddedExeption e1) {
						LOGGER.warning("Bad format, no expense added or " + e1.getMessage());
						textPane.setText(e1.getMessage());
						dialog.setVisible(true);
					}
				}
			}
		});
		btnStatsPerMonth.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStatsPerMonth.setBounds(452, 154, 89, 23);
		getContentPane().add(btnStatsPerMonth);
		/**
		 * new budget button and action listener
		 */
		JButton btnSetBudget = new JButton("Set New Value");
		btnSetBudget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: New budget value added: " + textFieldBudgetValue.getText());
				try {
					expenses.setBudget(Float.parseFloat(textFieldBudgetValue.getText()));
					lblBudgetActualValue.setText(textFieldBudgetValue.getText());
				} catch (IllegalArgumentException e1) {
					LOGGER.warning("Bad,negative value or " + e1.getMessage());
					displayError(dialog, textPane, e1);
				}
			}
		});
		btnSetBudget.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSetBudget.setBounds(669, 104, 135, 23);
		getContentPane().add(btnSetBudget);
		/**
		 * view forecast of the year button and action listener
		 */
		JButton btnForecastPerYear = new JButton("View");
		btnForecastPerYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LOGGER.fine("GUI MESSAGE: Pressed the button to view the forecast of the year: "
						+ textFieldYear1Fore.getText());
				try {
					lblYearlyForecast.setText(
							forecast.calculateForecastPerYear(Integer.parseInt(textFieldYear1Fore.getText())) + "");
				} catch (NumberFormatException e) {
					LOGGER.warning("Year is not valid");
					textPane.setText("Year not valid");
					dialog.setVisible(true);
				}
			}
		});
		btnForecastPerYear.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnForecastPerYear.setBounds(1055, 67, 89, 23);
		getContentPane().add(btnForecastPerYear);
		/**
		 * view forecast of the year and month button and action listener
		 */
		JButton btnForecastPerMonth = new JButton("View");
		btnForecastPerMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("Pressed the button to view the forecast of the month: "
						+ (Month) comboBoxMonth2.getSelectedItem() + " of year: " + textFieldYear2Fore.getText());
				try {
					lblMonthlyForecast.setText(
							forecast.calculateForecastByYearAndMonth(Integer.parseInt(textFieldYear2Fore.getText()),
									(Month) comboBoxMonth2.getSelectedItem()) + "");
				} catch (NumberFormatException e1) {
					LOGGER.warning("Not a valid year");
					textPane.setText("Year not valid");
					dialog.setVisible(true);
				}
			}
		});
		btnForecastPerMonth.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnForecastPerMonth.setBounds(1055, 154, 89, 23);
		getContentPane().add(btnForecastPerMonth);
		/**
		 * selected date in filter, and action listener
		 */
		JButton btnFilterByDate = new JButton("View");
		btnFilterByDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("GUI MESSAGE: filter select date button pressed");
				try {
					formatter.format(datePickerFilter.getModel().getValue());
					dataModel.setRowCount(0);
					calculations.populateRowsWithSelectedExpenseType(comboBoxExpType2.getSelectedItem().toString(),
							expenses);
					calculations.filter(formatter.format(datePickerFilter.getModel().getValue()));
				} catch (IllegalArgumentException e1) {
					LOGGER.warning("Date not selected " + e1.getMessage());
					textPane.setText("Please select date");
					dialog.setVisible(true);
				}
			}
		});
		/**
		 * selected type button in filter and action listener
		 */
		JButton btnFilterByType = new JButton("View");
		btnFilterByType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: filter select type button pressed ");
				calculations.filter("");
				calculations.populateRowsWithSelectedExpenseType(comboBoxExpType2.getSelectedItem().toString(),
						expenses);
			}
		});
		btnFilterByType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnFilterByType.setBounds(373, 375, 89, 23);
		getContentPane().add(btnFilterByType);
		btnFilterByDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnFilterByDate.setBounds(373, 420, 89, 23);
		getContentPane().add(btnFilterByDate);
		/**
		 * view button for year-month in filter and action listener
		 */
		JButton btnFilterByMonth = new JButton("View");
		btnFilterByMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: view button filter by year-month selected");
				try {
					checkIfYearCompleted();
					calculations.populateRowsWithSelectedExpenseType(comboBoxExpType2.getSelectedItem().toString(),
							expenses);
					calculations.filter(textFieldYearMonthFilter.getText() + "-"
							+ String.format("%02d", comboBoxMonth3.getSelectedIndex() + 1));
				} catch (Exception e1) {
					LOGGER.warning(e1.getMessage());
					textPane.setText(e1.getMessage());
					dialog.setVisible(true);
				}
			}
		});
		btnFilterByMonth.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnFilterByMonth.setBounds(373, 478, 89, 23);
		getContentPane().add(btnFilterByMonth);
		/**
		 * filter year button and action listener
		 */
		JButton btnFilterByYear = new JButton("View");
		btnFilterByYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: filter year button selected");

				try {
					checkIfYearCompleted2();
					calculations.populateRowsWithSelectedExpenseType(comboBoxExpType2.getSelectedItem().toString(),
							expenses);
					calculations.filter(textFieldYearFilter.getText());
				} catch (Exception e1) {
					LOGGER.warning(e1.getMessage());
					textPane.setText(e1.getMessage());
					dialog.setVisible(true);
				}
			}
		});
		btnFilterByYear.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnFilterByYear.setBounds(373, 526, 89, 23);
		getContentPane().add(btnFilterByYear);
		/**
		 * clear filter button in filter
		 */
		JButton btnClearFilters = new JButton("Clear");
		btnClearFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.fine("GUI MESSAGE: clear filter button pressed");
				calculations.clearFilters(expenses);
			}
		});
		btnClearFilters.setBounds(373, 568, 89, 23);
		getContentPane().add(btnClearFilters);
		/**
		 * separators for window frame
		 */
		JSeparator separator = new JSeparator();
		separator.setBounds(73, 282, 1, 2);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 299, 1194, 14);
		getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(621, -19, 28, 317);
		getContentPane().add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(228, 299, 1, 2);
		getContentPane().add(separator_3);

		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setBounds(260, 0, -15, 301);
		getContentPane().add(separator_4);

		JSeparator separator_5 = new JSeparator();
		separator_5.setOrientation(SwingConstants.VERTICAL);
		separator_5.setBounds(239, 0, 14, 301);
		getContentPane().add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(846, 0, 20, 301);
		getContentPane().add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(61, 407, 513, 14);
		getContentPane().add(separator_7);

		JSeparator separator_8 = new JSeparator();
		separator_8.setBounds(61, 459, 513, 14);
		getContentPane().add(separator_8);

		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(61, 513, 513, 14);
		getContentPane().add(separator_9);

		JSeparator separator_10 = new JSeparator();
		separator_10.setBounds(61, 555, 513, 14);
		getContentPane().add(separator_10);
	}

	/**
	 * check all fields are completed
	 * 
	 * @throws IllegalStateException
	 */
	private void checkIfAllFieldsAreCompleted() throws IllegalStateException {
		if (textFieldName.getText().equals("") || textFieldValue.getText().equals("")
				|| datePickerAdd.getModel().getValue() == null) {
			throw new IllegalStateException("Please complete all fields to add a new expense");
		}
	}

	/**
	 * check if year and month is completed
	 * 
	 * @throws IllegalStateException
	 */
	private void checkIfYearCompleted() throws IllegalStateException {
		if (textFieldYearMonthFilter.getText().equals("YYYY")) {
			throw new IllegalStateException("Please complete year field");
		}
	}

	/**
	 * check if year is completed
	 * 
	 * @throws IllegalStateException
	 */
	private void checkIfYearCompleted2() throws IllegalStateException {
		if (textFieldYearFilter.getText().equals("YYYY")) {
			throw new IllegalStateException("Please complete year field");
		}
	}

	/**
	 * display errors
	 * 
	 * @param dialog
	 *            show errors
	 * @param textPane
	 * @param e1
	 */
	private void displayError(final JDialog dialog, final JTextPane textPane, IllegalArgumentException e1) {
		textPane.setText(e1.getMessage());
		dialog.setVisible(true);
	}

}
