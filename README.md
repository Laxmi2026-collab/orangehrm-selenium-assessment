# OrangeHRM Selenium Java Automation

## Repository status
This repository currently contains the Maven configuration, this README, and an API validation test. The UI employee lifecycle implementation, JSON fixture, and report generation described in the original README have **not been committed**. This repository is therefore not yet a complete employee lifecycle submission.

## API validation design
The previous check called an unrelated public test API. That response could not prove that OrangeHRM saved the employee. The new test signs in through the OrangeHRM UI, opens an existing employee's Personal Details page, reads the visible first name, last name and employee ID, then uses REST Assured with the **same browser session cookies** to GET `/web/index.php/api/v2/pim/employees/{empNumber}`. It checks HTTP 200, JSON content type, internal record number, employee ID and both names against the UI. A login redirect, missing record, wrong employee or stale data now fails the test. The verifier also exposes `assertDeleted` (expects 404) for use immediately after a lifecycle test deletes its own employee.

`empNumber` is OrangeHRM's internal numeric record key in the page URL and API path; it is **different** from the user-facing employee ID. Use the number from `.../viewPersonalDetails/empNumber/123`. In a complete UI lifecycle test, capture this number after creating the employee, call `OrangeHrmEmployeeApiVerifier.assertEmployee(...)` after creation and again after edits, then call `assertDeleted(...)` after deletion. Compare the edited fields separately if the assignment requires job title and employment status; this helper currently asserts personal identity fields only.

## Run
Requires Java 17, Maven, Chrome and a ChromeDriver available to Selenium Manager. Pick an existing employee that your account can view. Pass credentials and its internal record number as Maven properties (do not commit secrets):

```bash
mvn test -Dorangehrm.username=Admin -Dorangehrm.password=YOUR_PASSWORD -Dorangehrm.empNumber=123
```

Optionally set `-Dorangehrm.baseUrl=https://YOUR_ORANGEHRM_HOST`; the default is `https://opensource-demo.orangehrmlive.com`. The test fails fast when required properties are missing. It does not create or delete records.

## Limitation
OrangeHRM documents `GET /api/v2/pim/employees/{empNumber}`, but the demo's availability, login and session behavior may change. This test uses an authenticated application endpoint, not an independently provisioned external API contract. The API and UI may be backed by the same service, so matching them does not independently prove database persistence. The public demo is shared and records can be edited or removed by other users between UI and API reads. The repository still needs the missing UI lifecycle sources and updated-field assertions before it can claim full create/update/delete coverage. No unrelated public API is used.
