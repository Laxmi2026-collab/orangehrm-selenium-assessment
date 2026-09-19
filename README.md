# OrangeHRM Selenium Java Automation

## Assignment
This project implements the Employee Lifecycle Management scenario from the supplied Technical Test - Quality Engineer (Automation).

## Technology
- Java 17
- Selenium WebDriver
- TestNG
- Maven
- REST Assured
- Jackson JSON
- Extent Reports
- Page Object Model

## Workflow
1. Login using Admin/admin123.
2. Navigate to PIM > Add Employee.
3. Create an employee using JSON test data.
4. Upload a profile picture.
5. Search by Employee ID.
6. Update Job Title and Employment Status.
7. Execute API validation.
8. Delete the employee.
9. Logout.

## Run in IntelliJ
1. Open the project folder.
2. Open pom.xml.
3. Load Maven changes.
4. Make sure JDK 17 is configured.
5. Run EmployeeLifecycleTest from IntelliJ or execute `mvn clean test`.

## Report
After execution, open:
reports/employee-lifecycle-report.html

## Test Data
Edit:
src/test/resources/testdata/employee.json

If a profile picture is used, place profile.png in:
src/test/resources/testdata/

## Note
The assignment asks for API verification of employee data. The demo OrangeHRM site does not expose a public employee API in the supplied document, so this implementation uses the permitted public test API option for demonstrating API execution. The API response is therefore a separate public test API check, not a direct OrangeHRM employee-data cross-check.
