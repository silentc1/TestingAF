package com.testing.automation.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import org.testng.ITestResult;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;
import com.testing.automation.utils.TestConfig;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.testing.automation.utils.TestReport;

public class APITest {
    private TestReport report;
    
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestConfig.API_BASE_URL;
        report = new TestReport();
    }
    
    // GET Tests
    @Test(description = "Test getting all posts")
    public void testGetAllPosts() {
        Response response = given()
            .when()
            .get("/posts")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().response();
            
        List<Object> posts = response.jsonPath().getList("");
        assertFalse(posts.isEmpty(), "Posts list should not be empty");
        assertTrue(posts.size() > 0, "Should return multiple posts");
    }
    
    @Test(description = "Test getting a specific post")
    public void testGetPost() {
        int postId = 1;
        Response response = given()
            .pathParam("id", postId)
            .when()
            .get("/posts/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().response();
            
        assertEquals((int) response.path("id"), postId);
        assertNotNull(response.path("title"));
        assertNotNull(response.path("body"));
        assertNotNull(response.path("userId"));
    }
    
    @Test(description = "Test getting post comments")
    public void testGetPostComments() {
        int postId = 1;
        Response response = given()
            .pathParam("id", postId)
            .when()
            .get("/posts/{id}/comments")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().response();
            
        List<Object> comments = response.jsonPath().getList("");
        assertFalse(comments.isEmpty(), "Comments list should not be empty");
        
        // Verify first comment structure
        Map<String, Object> firstComment = (Map<String, Object>) comments.get(0);
        assertNotNull(firstComment.get("postId"));
        assertNotNull(firstComment.get("id"));
        assertNotNull(firstComment.get("name"));
        assertNotNull(firstComment.get("email"));
        assertNotNull(firstComment.get("body"));
    }

    // POST Tests
    @Test(description = "Test creating a new post")
    public void testCreatePost() {
        Map<String, Object> newPost = new HashMap<>();
        newPost.put("title", "Test Post");
        newPost.put("body", "This is a test post");
        newPost.put("userId", 1);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(newPost)
            .when()
            .post("/posts")
            .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract().response();

        assertNotNull(response.path("id"));
        assertEquals(response.path("title").toString(), "Test Post");
        assertEquals(response.path("body").toString(), "This is a test post");
    }

    // PUT Tests
    @Test(description = "Test updating a post")
    public void testUpdatePost() {
        Map<String, Object> updatedPost = new HashMap<>();
        updatedPost.put("id", 1);
        updatedPost.put("title", "Updated Title");
        updatedPost.put("body", "Updated body");
        updatedPost.put("userId", 1);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(updatedPost)
            .pathParam("id", 1)
            .when()
            .put("/posts/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().response();

        assertEquals(response.path("title").toString(), "Updated Title");
        assertEquals(response.path("body").toString(), "Updated body");
    }

    // PATCH Tests
    @Test(description = "Test partially updating a post")
    public void testPatchPost() {
        Map<String, Object> partialUpdate = new HashMap<>();
        partialUpdate.put("title", "Patched Title");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(partialUpdate)
            .pathParam("id", 1)
            .when()
            .patch("/posts/{id}")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract().response();

        assertEquals(response.path("title").toString(), "Patched Title");
    }

    // DELETE Tests
    @Test(description = "Test deleting a post")
    public void testDeletePost() {
        given()
            .pathParam("id", 1)
            .when()
            .delete("/posts/{id}")
            .then()
            .statusCode(200);
    }

    // Error Cases
    @Test(description = "Test getting non-existent post")
    public void testGetNonExistentPost() {
        given()
            .pathParam("id", 999999)
            .when()
            .get("/posts/{id}")
            .then()
            .statusCode(404);
    }

    @Test(description = "Test invalid request format")
    public void testInvalidRequest() {
        Map<String, Object> invalidPost = new HashMap<>();
        invalidPost.put("invalid_field", "value");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(invalidPost)
            .when()
            .post("/posts")
            .then()
            .statusCode(201) // JSONPlaceholder always returns 201 for POST
            .extract().response();

        assertNotNull(response.path("id"));
    }

    // Performance Tests
    @Test(description = "Test API response time")
    public void testAPIResponseTime() {
        Response response = given()
            .when()
            .get("/posts");
        
        long responseTime = response.time();
        assertTrue(responseTime < 2000, "API response time is greater than 2 seconds");
    }

    @DataProvider(name = "postIds")
    public Object[][] providePostIds() {
        return new Object[][] {
            {1}, {2}, {3}, {4}, {5}
        };
    }

    @Test(dataProvider = "postIds", description = "Test performance across multiple posts")
    public void testMultiplePostsPerformance(int postId) {
        Response response = given()
            .pathParam("id", postId)
            .when()
            .get("/posts/{id}");
        
        long responseTime = response.time();
        assertTrue(responseTime < 2000, 
            "API response time for post " + postId + " is greater than 2 seconds: " + responseTime + "ms");
        assertEquals(response.getStatusCode(), 200, 
            "Failed to get post " + postId);
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        boolean passed = result.getStatus() == ITestResult.SUCCESS;
        String error = result.getThrowable() != null ? result.getThrowable().getMessage() : null;
        report.addResult(testName, "API", passed, description, error);
    }
    
    @AfterClass
    public void tearDown() {
        report.generateReport();
    }
} 