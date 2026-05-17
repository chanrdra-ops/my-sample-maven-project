# Code Walkthrough

This guide explains what each important framework file does and how the pieces connect.

## Build and Execution Files

### `pom.xml`

Defines the Maven project, Java version, dependencies, and test execution plugin.

Important sections:

- `properties`: centralizes dependency versions and default runtime values.
- `dependencies`: adds Selenium, TestNG, Rest Assured, WebDriverManager, Extent Reports, Log4j2, Jackson, and AssertJ.
- `maven-compiler-plugin`: compiles the project with Java 17.
- `maven-surefire-plugin`: runs TestNG using `testng.xml`.

### `testng.xml`

Defines the default suite.

It runs:

- UI smoke tests
- API smoke tests

It also registers:

```xml
<listener class-name="com.automation.core.listeners.TestListener"/>
```

That listener creates reports, logs test results, and attaches screenshots for UI failures.

## Core Config Layer

### `Environment.java`

Defines supported environments:

- `DEV`
- `QA`
- `STAGE`

The `from` method converts command-line input like `-Denv=qa` into the matching enum value.

### `ConfigManager.java`

Loads environment configuration from:

```text
src/test/resources/config
```

Key behavior:

- Defaults to `qa`
- Loads `config/{env}.properties`
- Allows command-line overrides through `System.getProperty`
- Provides typed helpers like `getInt` and `getBoolean`

Example:

```java
ConfigManager.getInstance().getRequired("app.url")
```

## Driver Layer

### `BrowserType.java`

Defines supported browsers:

- `CHROME`
- `FIREFOX`
- `EDGE`

The `from` method converts text from config into the enum.

### `DriverFactory.java`

Creates browser instances.

Responsibilities:

- Reads browser and headless settings from config
- Uses WebDriverManager to prepare browser drivers
- Applies browser options
- Sets page load timeout
- Maximizes the browser window

### `DriverManager.java`

Stores WebDriver using `ThreadLocal`.

Why it matters:

- Parallel tests need separate browser instances.
- `ThreadLocal` prevents one test from reusing another test's browser.

Key methods:

- `setDriver`
- `getDriver`
- `hasDriver`
- `quitDriver`

## Utility Layer

### `WaitUtils.java`

Provides reusable explicit waits:

- Wait for visibility
- Wait for clickability
- Wait for page title text

### `ScreenshotUtils.java`

Captures screenshots into:

```text
target/screenshots
```

It is used by the TestNG listener when a UI test fails.

### `JsonUtils.java`

Reads JSON files from classpath resources using Jackson.

Useful for:

- API payloads
- Test data
- Expected data files

## Reporting Layer

### `ExtentReportManager.java`

Creates the suite-level Extent report at:

```text
target/reports/extent-report.html
```

### `ExtentTestManager.java`

Stores each test's report node using `ThreadLocal`.

This keeps parallel report logging clean.

## Listener Layer

### `TestListener.java`

Hooks into TestNG lifecycle events:

- `onTestStart`
- `onTestSuccess`
- `onTestFailure`
- `onTestSkipped`
- `onFinish`

Failure behavior:

- Logs the exception
- Captures a screenshot if WebDriver exists
- Attaches the screenshot to Extent report

API tests do not create WebDriver, so screenshot capture is skipped for API-only failures.

## UI Layer

### `ElementActions.java`

Wraps common Selenium actions.

Current actions:

- `click`
- `type`
- `textOf`
- `isDisplayed`
- `scrollIntoView`

Page objects should use this class instead of repeating raw Selenium logic.

### `SeleniumHomePage.java`

Sample page object for the Selenium website.

It demonstrates:

- Locators kept inside page object
- Page-open method
- Page-loaded check
- Business-readable methods

## API Layer

### `ApiClient.java`

Reusable Rest Assured wrapper.

It centralizes:

- Base URI
- JSON headers
- Timeout configuration
- GET, POST, PUT, DELETE methods

### `Endpoint.java`

Stores API endpoint paths in one place.

This avoids hardcoding API paths in tests.

### `ResponseValidator.java`

Stores reusable API assertions.

Current validators:

- Status code validation
- JSON schema validation

## Test Hooks

### `BaseUiTest.java`

Creates a browser before each UI test and closes it after each UI test.

All UI tests should extend this class.

### `BaseApiTest.java`

Creates an `ApiClient` before each API test.

All API tests should extend this class.

## Sample Tests

### `SeleniumHomeTest.java`

Sample UI smoke test.

Flow:

1. Opens configured app URL.
2. Verifies the page title contains Selenium.
3. Verifies the Downloads link is visible.

### `PostsApiTest.java`

Sample API smoke test.

Flow:

1. Calls `GET /posts/1`.
2. Verifies HTTP status code is `200`.
3. Validates response body against JSON schema.
4. Verifies response fields.

## Configuration Files

### `qa.properties`, `dev.properties`, `stage.properties`

Define environment-specific URLs, browser, wait timeout, page-load timeout, API timeout, and headless mode.

## Resource Files

### `post-schema.json`

JSON schema used by the sample API test.

### `log4j2.xml`

Controls console and file logging.

### `ui-smoke.xml` and `api-smoke.xml`

Optional suite files for running UI-only or API-only smoke tests.
