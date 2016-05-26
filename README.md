# expensemanager
final project siit

This is the final project that me and my colleague we had to made for graduating at the Scoala Informală de IT Timișoara.
Down below are the requirements that we have followed for the abstractization and creation of the project.

"Create a desktop GUI application to manage personal expenses. Expenses added in the system should be persisted for future reuse. The application should support:
-add expenses 
-lookup expenses by type, date
-lookup expenses by type, month [optional]
-lookup expenses by type, year [optional]
-All expenses will be displayed by default (before filtering/lookup)
A budget forecast is necessary to predict how much money is needed in a month or per year. The forecast considers current period and adds 5% to it for a future similar period (same month or whole year). 
The forecast is available 
-per year [optional]
-per month.
Additionally the application should support some statistics like biggest expense (could be gas for the car or a vacation in Maldive). The statistic is available:
-per year
-per month (year-month)  [optional]

The application should support attaching several information to an expense like name, value, date of expense and type of expense. The type of expenses are: one time/yearly, monthly, weekly, daily.
As an example, when we add an daily expense (eg. bread) this will be expanded for every day of the year without forcing the user to enter it daily (one time is enough).

For better maintainability implement a logging feature that logs user information into a file.
For better code quality, perform unit testing for some of the methods of your system.

Optional: consider a warning for expense level limit per month/year based on a amount configured by the user (eg. 3000ron or 95% of budget forecast) [optional]


Steps:
Extract main features of the system
Create a GUI mockup [can be even on paper!] to help with GUI design
Extract main abstractions in the system
Describe the relationship between them
Consider storing/loading mechanism to provide persistency
"
