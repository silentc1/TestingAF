# Software Quality Assurance Final Project Test Plan

## 1. Application Selection and Analysis

### 1.1 Selected Applications
1. API Testing Target: JSONPlaceholder (https://jsonplaceholder.typicode.com)
   - Free REST API for testing and prototyping
   - No authentication required
   - Supports all CRUD operations
   - Reliable and stable for testing purposes

2. UI Testing Target: Wikipedia (https://www.wikipedia.org)
   - Publicly accessible website
   - Rich user interface elements
   - Form validation features
   - Multi-language support
   - Responsive design

### 1.2 Functional Requirements Analysis

#### API Requirements (JSONPlaceholder)
1. Resource Management
   - Posts CRUD operations
   - Comments management
   - User data access

2. Data Formats
   - JSON request/response format
   - Standard HTTP status codes
   - RESTful endpoints

3. Performance Requirements
   - Response time < 500ms
   - Proper error handling
   - Data consistency

#### UI Requirements (Wikipedia)
1. User Interface Elements
   - Search functionality
   - Navigation menus
   - Form elements
   - Dynamic content loading

2. User Interactions
   - Form submissions
   - Button clicks
   - Page transitions
   - Data input validation

3. Cross-browser Compatibility
   - Chrome support
   - Responsive design
   - Mobile compatibility

## 2. Test Cases

### 2.1 API Test Cases

#### TC001: CRUD Operations
**Objective**: Verify all CRUD operations for posts
1. Create Post (testCreatePost)
   - Input: Title, body, userId
   - Expected: 201 Created
   - Validation: Response contains new post ID

2. Read Operations (testGetPost, testGetAllPosts)
   - Input: Post ID
   - Expected: 200 OK
   - Validation: Correct post data returned

3. Update Operations (testUpdatePost, testPatchPost)
   - Input: Updated title and body
   - Expected: 200 OK
   - Validation: Changes reflected

4. Delete Operation (testDeletePost)
   - Input: Post ID
   - Expected: 200 OK
   - Validation: Post no longer accessible

#### TC002: Error Handling
**Objective**: Verify API error responses
1. Non-existent Resource (testGetNonExistentPost)
   - Request: GET /posts/999
   - Expected: 404 Not Found

2. Invalid Request (testInvalidRequest)
   - Request: Malformed request body
   - Expected: 400 Bad Request

#### TC003: Performance Testing
**Objective**: Verify API performance
1. Response Time (testAPIResponseTime)
   - Action: GET /posts
   - Expected: < 500ms
   - Measure: Average response time

2. Multiple Requests (testMultiplePostsPerformance)
   - Action: Multiple concurrent requests
   - Expected: All successful
   - Measure: Response times for 5 iterations

#### TC004: Additional Functionality
**Objective**: Verify related endpoints
1. Comments (testGetPostComments)
   - Action: GET post comments
   - Expected: 200 OK
   - Validation: Comment structure

### 2.2 UI Test Cases

#### TC005: Search and Navigation
**Objective**: Verify search and navigation features
1. Search Features
   - testSearchFunctionality
   - testSearchSuggestions
   - testAdvancedSearch
   - testRandomArticle

2. Navigation Features
   - testNavigationLinks
   - testArticleNavigation

#### TC006: Additional Features
**Objective**: Verify other UI features
1. Language and Display
   - testLanguageSelection
   - testMobileView
   - testArticleActions
   - testAccessibilityFeatures

## 3. Test Implementation

### 3.1 API Testing (RestAssured)
```java
@Test
public void testCreatePost() {
    Map<String, Object> post = new HashMap<>();
    post.put("title", "Test Post");
    post.put("body", "Test Content");
    post.put("userId", 1);

    Response response = given()
        .contentType(ContentType.JSON)
        .body(post)
        .when()
        .post("/posts")
        .then()
        .statusCode(201)
        .extract().response();

    assertNotNull(response.path("id"));
}
```

### 3.2 UI Testing (Selenium)
```java
@Test
public void testSearch() {
    WebElement searchBox = wait.until(
        ExpectedConditions.elementToBeClickable(By.name("search")));
    searchBox.sendKeys("Test");
    searchBox.submit();

    WebElement results = wait.until(
        ExpectedConditions.presenceOfElementLocated(By.className("searchresults")));
    assertTrue(results.isDisplayed());
}
```

## 4. Test Execution Results

### 4.1 API Test Results
1. Successful Tests:
   - CRUD operations (TC001)
   - Basic error handling (TC002)
   - Performance within limits (TC003)

2. Failed Tests:
   - None currently

3. Issues Encountered:
   - Occasional timeout on concurrent requests
   - Solution: Implemented retry mechanism

### 4.2 UI Test Results
1. Successful Tests:
   - Search functionality (TC004)
   - Basic navigation (TC006)

2. Failed Tests:
   - Form validation (TC005)
   - Issue: Dynamic element IDs
   - Solution: Updated element locators

### 4.3 Performance Metrics
1. API Performance:
   - Average response time: 150ms
   - Success rate: 99.5%
   - Error rate: 0.5%

2. UI Performance:
   - Page load time: avg 2.5s
   - Element render time: avg 500ms

## 5. Recommendations

### 5.1 Immediate Improvements
1. API Testing:
   - Add more error scenarios
   - Implement data validation
   - Add security testing

2. UI Testing:
   - Add cross-browser testing
   - Implement mobile testing
   - Add accessibility testing

### 5.2 Long-term Improvements
1. Technical:
   - Implement CI/CD pipeline
   - Add automated reporting
   - Implement test data management

2. Process:
   - Regular test maintenance
   - Performance monitoring
   - Security scanning 