# API and UI Test Automation Project

This project demonstrates automated testing of both API and UI using RestAssured and Selenium WebDriver. The project tests the JSONPlaceholder API and Wikipedia website.

## Prerequisites

- Java 11 or higher
- Maven
- Chrome browser (for UI tests)

## Project Structure

```
src/test/java/com/testing/automation/
├── api/
│   └── APITest.java         # API tests using RestAssured
├── ui/
│   └── WikipediaUITest.java # UI tests using Selenium
└── utils/
    ├── TestConfig.java      # Configuration properties
    └── TestReport.java      # Test reporting utility
```

## Setup

1. Clone the repository
2. Install dependencies:
   ```bash
   mvn clean install
   ```

## Running Tests

To run all tests:
```bash
mvn test
```

To run only API tests:
```bash
mvn test -Dtest=APITest
```

To run only UI tests:
```bash
mvn test -Dtest=WikipediaUITest
```

## Test Cases

### API Tests (JSONPlaceholder API)
1. GET Operations:
   - Get all posts
   - Get specific post
   - Get post comments

2. POST Operations:
   - Create new post with validation

3. PUT/PATCH Operations:
   - Full update of post
   - Partial update of post

4. DELETE Operations:
   - Delete post

5. Error Cases:
   - Non-existent resources (404)
   - Invalid request formats
   - Response validation

6. Performance Tests:
   - Response time verification
   - Multiple requests testing

### UI Tests (Wikipedia Website)
1. Search Functionality:
   - Search with valid input
   - Empty search validation
   - Search results verification

2. Navigation and Layout:
   - Language selection
   - Random article navigation
   - Responsive design testing
   - Mobile and desktop view verification

3. Form Validation:
   - Create account form validation
   - Required field validation

4. Performance:
   - Page load time measurement
   - Navigation response time
   - Element loading verification

## Test Coverage

The test suite covers:
- All major HTTP methods (GET, POST, PUT, PATCH, DELETE)
- Both positive and negative test scenarios
- Performance metrics
- Cross-browser compatibility
- Responsive design
- Form validations
- Navigation flows
- Error handling

## Reports

Test reports can be found in the `test-output/reports` directory after test execution. The reports include:
- Test execution summary
- Detailed test case results
- Performance metrics
- Error logs and screenshots (for UI test failures)

## Best Practices Implemented

1. API Testing:
   - Proper error handling
   - Response validation
   - Performance monitoring
   - Data-driven testing
   - All HTTP methods coverage

2. UI Testing:
   - Page Object Model
   - Explicit waits
   - Cross-browser testing
   - Responsive design verification
   - Error handling and recovery

3. General:
   - Clean code practices
   - Proper documentation
   - Maintainable test structure
   - Reusable components

## Notes

- The UI tests are configured to run on Chrome browser
- API tests use JSONPlaceholder (https://jsonplaceholder.typicode.com)
- UI tests use Wikipedia (https://www.wikipedia.org)
- Tests include proper wait mechanisms and error handling
- Performance thresholds are configurable
- Screenshots are captured on test failures
- Tests are designed to be environment-independent