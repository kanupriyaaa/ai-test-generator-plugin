package com.aiassistant.services;

import com.aiassistant.models.TestCase;

public class TestCodeGeneratorService {

    public String generateTest(TestCase testCase) {

        return """
                @Test
                void test_%s() {
                    // Action: %s
                    // Input: %s
                    // Expected: %s
                }
                """.formatted(
                testCase.getTestId(),
                testCase.getAction(),
                testCase.getInput(),
                testCase.getExpected()
        );
    }

}