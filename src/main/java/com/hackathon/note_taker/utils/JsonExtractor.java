package com.hackathon.note_taker.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonExtractor {

    // Function to extract only the JSON part from the string
    public static String extractJsonString(String input) {
        int startIndex = input.indexOf("{");  // Find first '{'
        int endIndex = input.lastIndexOf("}"); // Find last '}'

        if (startIndex != -1 && endIndex != -1) {
            return input.substring(startIndex, endIndex + 1);
        }
        return null;
    }

    public static String extractSummaryJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            if (rootNode.has("summary")) {
                String summaryValue = rootNode.get("summary").asText();
                return "{ \"summary\": \"" + summaryValue + "\" }";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
