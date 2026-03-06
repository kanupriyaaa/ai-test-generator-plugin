package com.aiassistant.models;

public class TestCase {

    private String testId;
    private String action;
    private String input;
    private String expected;

    public TestCase() {
    }

    public TestCase(String testId, String action, String input, String expected) {
        this.testId = testId;
        this.action = action;
        this.input = input;
        this.expected = expected;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}