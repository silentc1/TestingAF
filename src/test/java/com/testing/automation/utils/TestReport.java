package com.testing.automation.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestReport {
    private static final String REPORT_DIR = "test-output/reports/";
    private List<TestResult> results;
    private long startTime;
    private long endTime;

    public TestReport() {
        results = new ArrayList<>();
        startTime = System.currentTimeMillis();
        new File(REPORT_DIR).mkdirs();
    }

    public void addResult(String testName, String category, boolean passed, String description, String error) {
        results.add(new TestResult(testName, category, passed, description, error));
    }

    public void generateReport() {
        endTime = System.currentTimeMillis();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = REPORT_DIR + "TestReport_" + timestamp + ".html";

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportPath))) {
            writer.println("<html><head><title>Test Execution Report</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("table { border-collapse: collapse; width: 100%; }");
            writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println(".pass { color: green; }");
            writer.println(".fail { color: red; }");
            writer.println("</style></head><body>");

            // Summary
            writer.println("<h1>Test Execution Report</h1>");
            writer.println("<h2>Summary</h2>");
            writer.println("<p>Execution Time: " + (endTime - startTime) / 1000.0 + " seconds</p>");
            
            long passedTests = results.stream().filter(TestResult::isPassed).count();
            writer.println("<p>Total Tests: " + results.size() + "</p>");
            writer.println("<p>Passed: " + passedTests + "</p>");
            writer.println("<p>Failed: " + (results.size() - passedTests) + "</p>");

            // Detailed Results
            writer.println("<h2>Detailed Results</h2>");
            writer.println("<table>");
            writer.println("<tr><th>Category</th><th>Test Name</th><th>Status</th><th>Description</th><th>Error</th></tr>");

            for (TestResult result : results) {
                writer.println("<tr>");
                writer.println("<td>" + result.getCategory() + "</td>");
                writer.println("<td>" + result.getTestName() + "</td>");
                writer.println("<td class='" + (result.isPassed() ? "pass'>PASS" : "fail'>FAIL") + "</td>");
                writer.println("<td>" + result.getDescription() + "</td>");
                writer.println("<td>" + (result.getError() != null ? result.getError() : "") + "</td>");
                writer.println("</tr>");
            }

            writer.println("</table>");
            writer.println("</body></html>");
        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private static class TestResult {
        private String testName;
        private String category;
        private boolean passed;
        private String description;
        private String error;

        public TestResult(String testName, String category, boolean passed, String description, String error) {
            this.testName = testName;
            this.category = category;
            this.passed = passed;
            this.description = description;
            this.error = error;
        }

        public String getTestName() { return testName; }
        public String getCategory() { return category; }
        public boolean isPassed() { return passed; }
        public String getDescription() { return description; }
        public String getError() { return error; }
    }
} 