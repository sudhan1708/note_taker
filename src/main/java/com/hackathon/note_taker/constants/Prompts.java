package com.hackathon.note_taker.constants;

public class Prompts {
    public static final String BASE_PROMPT = "Your task is to analyze the WhatsApp chat conversation and provide a summary in the specified JSON format.\n" +
            "\n" +
            "Output format:\n" +
            "{\n" +
            "  \"chat_info\": {\n" +
            "    \"subject\": \"Brief description of the chat conversation\",\n" +
            "    \"date_range\": {\n" +
            "      \"start_date\": \"YYYY-MM-DD\",\n" +
            "      \"end_date\": \"YYYY-MM-DD\"\n" +
            "    },\n" +
            "    \"chat_type\": \"one_on_one|group\",\n" +
            "    \"participants\": [\"list of participants\"]\n" +
            "  },\n" +
            "  \"topics\": [\n" +
            "    {\n" +
            "      \"topic_name\": \"string\",\n" +
            "      \"discussion_points\": [\n" +
            "        {\n" +
            "          \"point\": \"string\",\n" +
            "          \"mentioned_by\": \"string\",\n" +
            "          \"date\": \"YYYY-MM-DD\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"customer_queries\": [\n" +
            "    {\n" +
            "      \"question\": \"string\",\n" +
            "      \"asked_by\": \"string\",\n" +
            "      \"date\": \"YYYY-MM-DD\",\n" +
            "      \"status\": \"answered|pending\",\n" +
            "      \"answer\": \"string (if provided)\",\n" +
            "      \"related_topic\": \"string\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"action_items\": [\n" +
            "    {\n" +
            "      \"task\": \"string\",\n" +
            "      \"assigned_to\": \"string\",\n" +
            "      \"assigned_by\": \"string\",\n" +
            "      \"due_date\": \"YYYY-MM-DD\",\n" +
            "      \"status\": \"pending|completed\",\n" +
            "      \"related_topic\": \"string\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"shared_resources\": [\n" +
            "    {\n" +
            "      \"type\": \"link|file|image|document\",\n" +
            "      \"content\": \"string\",\n" +
            "      \"shared_by\": \"string\",\n" +
            "      \"date_shared\": \"YYYY-MM-DD\",\n" +
            "      \"related_topic\": \"string\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n" +
            "\n" +
            "Guidelines:\n" +
            "\n" +
            "1. Derive all summary sections solely from what's explicitly mentioned in the WhatsApp conversation. If nothing is evident, leave the corresponding array EMPTY.\n" +
            "2. For discussion_points, include only the top 5 most significant talking points.\n" +
            "3. Include only relevant questions in customer_queries that require follow-up or represent significant information requests.\n" +
            "4. Action items should only include clearly defined tasks or commitments made in the conversation.\n" +
            "5. Shared resources should include any links, documents, or media files that were shared during the conversation.\n" +
            "6. Strictly adhere to the Output JSON format.\n" +
            "7. Exclude:\n" +
            "   - Small talk and greetings\n" +
            "   - Off-topic conversations\n" +
            "   - Emojis and reactions\n" +
            "   - Repeated information\n" +
            "\n" +
            "Please analyze the WhatsApp chat and provide a concise, accurate summary following these instructions.";
}
