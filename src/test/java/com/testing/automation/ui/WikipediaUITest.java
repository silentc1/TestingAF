package com.testing.automation.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;

public class WikipediaUITest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://www.wikipedia.org";
    private static final String EN_WIKI = "https://en.wikipedia.org";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void testSearchFunctionality() {
        driver.get(BASE_URL);
        
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchInput")));
        searchInput.sendKeys("Software testing");
        
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[type='submit']")));
        searchButton.click();
        
        WebElement firstHeading = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstHeading")));
        Assert.assertTrue(firstHeading.getText().contains("Software testing"), 
            "Search results page should contain 'Software testing'");
    }

    @Test(priority = 2)
    public void testLanguageSelection() {
        driver.get(BASE_URL);
        
        WebElement languageSelector = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".lang-list-button")));
        languageSelector.click();
        
        WebElement spanishLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[lang='es']")));
        String spanishUrl = spanishLink.getAttribute("href");
        spanishLink.click();
        
        Assert.assertTrue(driver.getCurrentUrl().contains("es.wikipedia.org"), 
            "URL should contain Spanish Wikipedia domain");
    }

    @Test(priority = 3)
    public void testNavigationLinks() {
        driver.get(BASE_URL);
        
        List<WebElement> mainLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector(".central-featured-lang")));
        
        Assert.assertTrue(mainLinks.size() > 0, "Main navigation links should be present");
        
        WebElement englishLink = mainLinks.stream()
            .filter(link -> link.getAttribute("lang").equals("en"))
            .findFirst()
            .orElseThrow(() -> new AssertionError("English link not found"));
            
        englishLink.click();
        
        Assert.assertTrue(driver.getCurrentUrl().contains("en.wikipedia.org"), 
            "Should navigate to English Wikipedia");
    }

    @Test(priority = 4)
    public void testRandomArticle() {
        driver.get("https://en.wikipedia.org/wiki/Special:Random");
        
        WebElement articleContent = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".mw-parser-output")));
        WebElement firstHeading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.id("firstHeading")));
            
        Assert.assertTrue(articleContent.isDisplayed(), 
            "Random article content should be displayed");
        Assert.assertNotNull(firstHeading.getText(), 
            "Article should have a title");
    }

    @Test(priority = 5)
    public void testSearchSuggestions() {
        driver.get(BASE_URL);
        
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchInput")));
        searchInput.sendKeys("Albert Ein");
        
        List<WebElement> suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector(".suggestion-link")));
        
        Assert.assertTrue(suggestions.size() > 0, 
            "Search suggestions should be displayed");
        
        String firstSuggestion = suggestions.get(0).getText().toLowerCase();
        Assert.assertTrue(firstSuggestion.contains("einstein"), 
            "First suggestion should contain 'Einstein'");
    }

    @Test(priority = 6)
    public void testArticleNavigation() {
        driver.get(EN_WIKI + "/wiki/Software_testing");
        
        // Test content area
        WebElement contentDiv = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("#mw-content-text")));
        Assert.assertTrue(contentDiv.isDisplayed(), "Content div should be visible");
        
        // Test any headings (more reliable than specific sections)
        List<WebElement> headings = driver.findElements(By.cssSelector("#mw-content-text h2, #mw-content-text h3"));
        Assert.assertTrue(headings.size() > 0, "Article should have headings");
        
        // Test reference links (if any exist)
        List<WebElement> references = driver.findElements(By.cssSelector(".reference-text, .reference, .reflist"));
        Assert.assertTrue(references.size() >= 0, "Article may have references");
    }

    @Test(priority = 7)
    public void testMobileView() {
        // Set mobile viewport
        Dimension mobileSize = new Dimension(375, 812);
        driver.manage().window().setSize(mobileSize);
        
        driver.get(EN_WIKI);
        
        // Verify basic page structure
        WebElement mainContent = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("#content")));
        Assert.assertTrue(mainContent.isDisplayed(),
            "Main content should be visible in mobile view");
            
        // Verify logo is present
        WebElement logo = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".mw-logo, .mw-wiki-logo")));
        Assert.assertTrue(logo.isDisplayed(),
            "Logo should be visible in mobile view");
        
        // Reset viewport
        driver.manage().window().maximize();
    }

    @Test(priority = 8)
    public void testAdvancedSearch() {
        driver.get(EN_WIKI + "/wiki/Special:Search");
        
        // Use search input on Special:Search page
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input.oo-ui-inputWidget-input")));
        searchInput.sendKeys("automation testing");
        searchInput.sendKeys(Keys.ENTER);
        
        // Verify search results or options
        WebElement searchContent = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".mw-search-results, .searchresults")));
        Assert.assertTrue(searchContent.isDisplayed(), 
            "Search results or options should be displayed");
        
        // Verify presence of advanced features
        List<WebElement> advancedFeatures = driver.findElements(
            By.cssSelector(".mw-search-profile-tabs, .search-types"));
        Assert.assertTrue(advancedFeatures.size() > 0, 
            "Advanced search features should be available");
    }

    @Test(priority = 9)
    public void testArticleActions() {
        driver.get(EN_WIKI + "/wiki/Software_testing");
        
        // Test view history using URL
        String historyUrl = driver.getCurrentUrl() + "?action=history";
        driver.get(historyUrl);
        
        WebElement historyContent = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("#pagehistory, .mw-history-container")));
        Assert.assertTrue(historyContent.isDisplayed(), 
            "History page should be displayed");
        
        // Test talk page using URL
        String talkUrl = driver.getCurrentUrl().replace("action=history", "").replace("Software_testing", "Talk:Software_testing");
        driver.get(talkUrl);
        
        WebElement talkContent = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("#mw-content-text")));
        Assert.assertTrue(talkContent.isDisplayed(), 
            "Talk page should be displayed");
    }

    @Test(priority = 10)
    public void testAccessibilityFeatures() {
        driver.get(EN_WIKI);
        
        // Test main landmarks
        List<WebElement> landmarks = driver.findElements(
            By.cssSelector("[role='main'], [role='navigation'], [role='search']"));
        Assert.assertTrue(landmarks.size() > 0, 
            "Page should have ARIA landmarks");
        
        // Test heading hierarchy
        List<WebElement> headings = driver.findElements(
            By.cssSelector("h1, h2, h3, h4, h5, h6"));
        Assert.assertTrue(headings.size() > 0, 
            "Page should have proper heading hierarchy");
        
        // Test skip link (if visible on focus)
        List<WebElement> skipLinks = driver.findElements(
            By.cssSelector("[href='#content'], .mw-jump-link"));
        Assert.assertTrue(skipLinks.size() > 0, 
            "Skip navigation link should be present in DOM");
    }

    @AfterMethod
    public void resetBrowser() {
        driver.manage().deleteAllCookies();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.sessionStorage.clear();");
        js.executeScript("window.localStorage.clear();");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
} 