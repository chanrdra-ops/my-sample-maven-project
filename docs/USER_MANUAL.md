# User Manual

This manual explains how to set up, run, and extend the modular automation framework.

## 1. Purpose

This framework automates:

- UI tests using Selenium WebDriver
- API tests using Rest Assured
- Test execution using TestNG
- Reporting using Extent Reports
- Logging using Log4j2
- Browser driver setup using WebDriverManager

## 2. Prerequisites

Install and configure:

- Java 17 or later
- Maven 3.8 or later
- Chrome, Firefox, or Microsoft Edge
- Git, optional but recommended

Verify installation:

```bash
java -version
mvn -version
```

Both commands should print version details. If they are not recognized, add Java and Maven to your system `PATH`.

## 3. Project Structure

```text
pom.xml
testng.xml
README.md
docs/
  USER_MANUAL.md
  CODE_WALKTHROUGH.md

src/main/java/com/automation/
  api/
    clients/
    endpoints/
    validators/
  core/
    config/
    driver/
    listeners/
    reporting/
    utils/
  ui/
    actions/
    components/
    pages/

src/test/java/com/automation/
  tests/
    api/
    e2e/
    hooks/
    ui/

src/test/resources/
  config/
  schemas/
  suites/
  testdata/
```

## 4. Configuration

Environment files live here:

```text
src/test/resources/config
```

Current files:

- `qa.properties`
- `dev.properties`
- `stage.properties`

Important keys:

```properties
app.url=https://www.selenium.dev
api.base.url=https://jsonplaceholder.typicode.com
browser=chrome
headless=false
explicit.wait.seconds=15
page.load.timeout.seconds=30
api.timeout.seconds=30
```

Command-line values override properties file values:

```bash
mvn clean test -Denv=qa -Dbrowser=edge -Dheadless=true
```

## 5. Running Tests

Run the default suite:

```bash
mvn clean test
```

Run in a specific environment:

```bash
mvn clean test -Denv=stage
```

Run with a specific browser:

```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

Run in headless mode:

```bash
mvn clean test -Dheadless=true
```

Run only smoke tests:

```bash
mvn clean test -Dgroups=smoke
```

Run a specific TestNG suite file:

```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/api-smoke.xml
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/ui-smoke.xml
```

## 6. Reports and Logs

Extent report:

```text
target/reports/extent-report.html
```

Screenshots for failed UI tests:

```text
target/screenshots
```

Log file:

```text
target/logs/automation.log
```

## 7. How to Add a UI Test

1. Add a page object in:

```text
src/main/java/com/automation/ui/pages
```

2. Add locators as private fields.

3. Add business-readable methods such as:

```java
public LoginPage enterUsername(String username)
public LoginPage enterPassword(String password)
public HomePage submitLogin()
```

4. Add the test class in:

```text
src/test/java/com/automation/tests/ui
```

5. Extend `BaseUiTest`.

6. Add TestNG groups:

```java
@Test(groups = {"ui", "smoke"})
```

7. Add the test class to `testng.xml` or a suite file.

## 8. How to Add an API Test

1. Add endpoint constants in:

```text
src/main/java/com/automation/api/endpoints
```

2. Use `ApiClient` from:

```text
src/main/java/com/automation/api/clients
```

3. Add response validations in:

```text
src/main/java/com/automation/api/validators
```

4. Add schema files in:

```text
src/test/resources/schemas
```

5. Add the test class in:

```text
src/test/java/com/automation/tests/api
```

6. Extend `BaseApiTest`.

7. Add TestNG groups:

```java
@Test(groups = {"api", "smoke"})
```

## 9. Test Data

Recommended locations:

```text
src/test/resources/testdata/ui
src/test/resources/testdata/api
```

Recommended formats:

- JSON for structured payloads
- CSV for simple tabular data
- Properties for small key-value config

Use `JsonUtils` to read JSON resources.

## 10. Parallel Execution

The framework supports parallel execution through:

- TestNG suite configuration
- `ThreadLocal<WebDriver>` in `DriverManager`
- `ThreadLocal<ExtentTest>` in `ExtentTestManager`

The default `testng.xml` uses:

```xml
<suite name="Automation Framework Suite" parallel="tests" thread-count="2">
```

Increase `thread-count` carefully after confirming your application and test data can support parallel runs.

## 11. Troubleshooting

### Java is not recognized

Install Java 17+ and add it to `PATH`.

### Maven is not recognized

Install Maven and add Maven `bin` directory to `PATH`.

### Browser does not launch

Check:

- Browser is installed
- `browser` value is one of `chrome`, `firefox`, or `edge`
- Corporate proxy or security tooling is not blocking WebDriverManager downloads

### UI test fails in headless mode only

Try:

```bash
mvn clean test -Dheadless=false
```

Then inspect screenshots and logs.

### API test times out

Increase:

```properties
api.timeout.seconds=60
```

Or pass:

```bash
mvn clean test -Dapi.timeout.seconds=60
```

### Report is not generated

Confirm the TestNG listener is present in the suite XML:

```xml
<listener class-name="com.automation.core.listeners.TestListener"/>
```

## 12. Best Practices

- Keep Selenium locators inside page objects.
- Keep test classes readable and business-focused.
- Do not create WebDriver directly in tests.
- Do not hardcode URLs in test classes.
- Put API paths in endpoint classes.
- Put reusable assertions in validators.
- Use TestNG groups consistently.
- Keep test data outside Java code.
- Prefer explicit waits over sleeps.
- Review screenshots and logs for failure analysis.
