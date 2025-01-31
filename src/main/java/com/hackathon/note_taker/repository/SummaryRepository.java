package com.hackathon.note_taker.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.note_taker.models.Summary;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SummaryRepository {
    @Autowired
    private ElasticBaseRepository elasticBaseRepository;

    private static final String SUMMARY_INDEX = "summaries";

    public void storeSummary(Summary summary) {
        elasticBaseRepository.saveDoc(summary, SUMMARY_INDEX);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getAllSummarySubjects() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        List<Map<String, Object>> summaryDocs = elasticBaseRepository.getResultsBySize(SUMMARY_INDEX, searchSourceBuilder, Integer.MAX_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();

        return summaryDocs.stream()
                .map(summaryDoc -> {
                    Object summaryData = summaryDoc.get("summaryData");
                    if (summaryData instanceof String) {  // Ensure it's a JSON string before parsing
                        try {
                            Map<String, Object> summaryMap = objectMapper.readValue((String) summaryData, Map.class);
                            return summaryMap != null ? Map.of("summary", summaryMap.get("summary"), "fileId", summaryDoc.get("id")) : null; // Extract subject
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                    return null;
                })
                .filter(subject -> subject != null)  // Remove any null values from failed parsing
                .collect(Collectors.toList());
    }

    public Map<String, Object> getSummaryByFileId(String fileId) {
       return elasticBaseRepository.getResultById(SUMMARY_INDEX, fileId);
    }
}
