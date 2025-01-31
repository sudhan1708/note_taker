package com.hackathon.note_taker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

    public static void main(String[] args) {
        String text = "Thought: I have analyzed the conversation by summarizing the key events, identifying the common keywords, understanding the overall tone, and extracting actionable tasks.\n" +
                "\n" +
                "```json\n" +
                "{\n" +
                "  \"summary\": \"The conversation revolves around...\",\n" +
                "  \"tone\": \"Positive and celebratory\",\n" +
                "  \"common_keywords\": [\"Happy Birthday\", \"Congratulations\", \"Happy Diwali\"],\n" +
                "  \"tasks\": [\"Try the new voice bot\", \"Engage in the post\"]\n" +
                "}\n";
        // Regular expression to detect a JSON object
        String jsonRegex = "\\{.*?\\}";
        Pattern pattern = Pattern.compile(jsonRegex);
        Matcher matcher = pattern.matcher(text);

        ObjectMapper objectMapper = new ObjectMapper();

        while (matcher.find()) {
            String potentialJson = matcher.group();
            try {
                // Validate by parsing it as JSON
                JsonNode jsonNode = objectMapper.readTree(potentialJson);
                System.out.println(jsonNode);
            } catch (Exception ignored) {
                // If parsing fails, it means this substring is not valid JSON
            }
        }
    }
}
