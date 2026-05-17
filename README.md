# Modular Automation Framework

Selenium + Java + TestNG framework for UI and API automation.

## Stack

- Java 17+
- Maven
- Selenium WebDriver
- TestNG
- Rest Assured
- WebDriverManager
- Extent Reports
- Log4j2

## Documentation

- [User Manual](docs/USER_MANUAL.md)
- [Code Walkthrough](docs/CODE_WALKTHROUGH.md)

## Project Structure

```text
src/main/java/com/automation
  core/config      Environment and property management
  core/driver      Browser creation and ThreadLocal driver lifecycle
  core/listeners   TestNG listener for logging, screenshots, and reports
  core/reporting   Extent report setup
  core/utils       Reusable utility classes
  api              API client, endpoints, and validators
  ui               UI actions, components, and page objects

src/test/java/com/automation
  tests/ui         UI tests
  tests/api        API tests
  tests/e2e        End-to-end tests
  tests/hooks      Test base classes

src/test/resources
  config           Environment properties
  schemas          JSON schemas
  suites           Additional TestNG suites
  testdata         Test data
```

## Run Tests

Install Java 17+ and Maven, then run:

```bash
mvn clean test
```

Run against a specific environment:

```bash
mvn clean test -Denv=qa
```

Run a specific browser:

```bash
mvn clean test -Dbrowser=chrome
```

Run headless:

```bash
mvn clean test -Dheadless=true
```

Run by group:

```bash
mvn clean test -Dgroups=smoke
```

## Reports

After execution, open:

```text
target/reports/extent-report.html
```

Screenshots for UI failures are stored in:

```text
target/screenshots
```

## Pipelines

GitHub Actions is configured in:

```text
.github/workflows/maven-test.yml
```

It runs on pushes and pull requests to `main` or `master`, and can also be started manually with environment, browser, and headless inputs.

Jenkins is configured in:

```text
Jenkinsfile
```

Create a Jenkins Pipeline job from SCM and point it at this repository. The Jenkins agent needs Java 17+ and Maven available on `PATH`. The pipeline runs:

```bash
mvn -B clean test -Denv=<ENVIRONMENT> -Dbrowser=<BROWSER> -Dheadless=<HEADLESS>
```

## Configuration

Default environment is `qa`.

Config files live under:

```text
src/test/resources/config
```

Available examples:

- `qa.properties`
- `dev.properties`
- `stage.properties`

System properties override file properties:

```bash
mvn clean test -Denv=qa -Dbrowser=edge -Dheadless=true
```

## Extension Guide

- Add new UI pages under `src/main/java/com/automation/ui/pages`
- Add reusable page fragments under `src/main/java/com/automation/ui/components`
- Add UI tests under `src/test/java/com/automation/tests/ui`
- Add API clients under `src/main/java/com/automation/api/clients`
- Add API endpoints under `src/main/java/com/automation/api/endpoints`
- Add API validators under `src/main/java/com/automation/api/validators`
- Add API tests under `src/test/java/com/automation/tests/api`
- Add schema files under `src/test/resources/schemas`
