package com.aiassistant.services;

import com.aiassistant.models.TestCase;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class AITestGenerationService {

    public String generateTestWithAI(TestCase testCase) {

        String prompt = String.format("""
You are a senior QA automation engineer.

Generate ONE Playwright Java test method using JUnit5.

STRICT RULES:
- Generate ONLY one test method
- DO NOT generate a full class
- DO NOT generate multiple examples
- DO NOT include explanations
- DO NOT include markdown or ```java
- Output ONLY valid Java code for the test method

Use Playwright Java API.

Test Case Details:

Test ID: %s
Action: %s
Input: %s
Expected Result: %s

The output must look like:

@Test
void <meaningful_test_name>() {
    // test steps
}

Return ONLY the test method.
""",
                testCase.getTestId(),
                testCase.getAction(),
                testCase.getInput(),
                testCase.getExpected()
        );

        return callAI(prompt);
    }

    public String generateTestFromPrompt(String prompt) {
        return callAI(prompt);
    }

    private String callAI(String prompt) {

        String apiKey = System.getenv("OPENAI_API_KEY");
                
        OkHttpClient client = new OkHttpClient();

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4.1");
        requestBody.put("temperature", 0.2);

        JSONArray messages = new JSONArray();

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        messages.put(message);

        requestBody.put("messages", messages);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                return "// OpenAI API error: " + response.code();
            }

            String responseBody = response.body().string();

            JSONObject json = new JSONObject(responseBody);

            JSONArray choices = json.getJSONArray("choices");

            JSONObject firstChoice = choices.getJSONObject(0);

            JSONObject messageObj = firstChoice.getJSONObject("message");

            return messageObj.getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "// AI generation failed";
        }
    }
}