package com.hackathon.note_taker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GenericDocumentProcessor {

    /**
     * Converts extracted data (List of Maps) into a list of any class type (T).
     *
     * @param extractedData List of maps containing raw data
     * @param mapper        Function to convert Map<String, Object> to T
     * @param <T>           Type of document (e.g., ChatDocument, UserDocument, etc.)
     * @return List of converted documents
     */
    public static <T> List<T> convertToDocuments(List<Map<String, Object>> extractedData, Function<Map<String, Object>, T> mapper) {
        List<T> documents = new ArrayList<>();
        for (Map<String, Object> data : extractedData) {
            documents.add(mapper.apply(data));
        }
        return documents;
    }
}
