package com.hackathon.note_taker.repository;


import com.hackathon.note_taker.commons.ElasticSearchClientService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.elasticsearch.action.support.WriteRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ElasticBaseRepository {

    @Autowired
    protected ElasticSearchClientService clientService;


    public <T> void saveDoc(T document, String indexName) {
        try {
            // Create an IndexRequest for the given index
            IndexRequest request = new IndexRequest(indexName);

            // Extract fields from the object
            Map<String, Object> fields = extractFieldsFromObject(document);

            // Get the ID if present, otherwise generate a UUID
            String id = (String) fields.getOrDefault("id", UUID.randomUUID().toString());
            request.id(id);

            // Set the document source dynamically
            request.source(fields);

            // Send the request to Elasticsearch
            clientService.getClient().index(request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace(); // Log and handle the error
        }
    }

    public <T> void saveDocs(List<T> documents, String indexName) {
        BulkRequest bulkRequest = new BulkRequest();

        for (T document : documents) {
            try {
                // Extract fields from object dynamically
                Map<String, Object> fields = extractFieldsFromObject(document);

                // Create an IndexRequest
                IndexRequest request = new IndexRequest(indexName);

                // Assign UUID if no ID is present
                String id = (String) fields.getOrDefault("id", UUID.randomUUID().toString());
                request.id(id);

                // Set document fields
                request.source(fields);

                // Add request to bulk request
                bulkRequest.add(request);
            } catch (Exception e) {
                e.printStackTrace(); // Handle error properly in production
            }
        }

        try {
            // Execute bulk request
            BulkResponse bulkResponse = clientService.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                System.err.println("Bulk indexing failed: " + bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log and handle the error
        }
    }

    // Helper method to extract fields from the object using reflection
    private <T> Map<String, Object> extractFieldsFromObject(T document) {
        Map<String, Object> fieldsMap = new java.util.HashMap<>();
        // Use reflection to get all fields and their values
        Field[] fields = document.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            try {
                fieldsMap.put(field.getName(), field.get(document));
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Log and handle the error
            }
        }
        return fieldsMap;
    }


    public List<Map<String, Object>> getResultsByQuery(String indexName, SearchSourceBuilder query, Integer size) {
        try{
            return getResultsByScroll(indexName, query, size);
        } catch (Exception e) {
            log.error("Error while executing query: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Map<String, Object> getResultById(String index, String id) {
        try {
            log.info(String.format("Getting data for Id : %s, Index : %s", id, index));
            GetRequest getRequest = new GetRequest(index, id);
            GetResponse getResponse = clientService.getClient().get(getRequest, RequestOptions.DEFAULT);
            return getResponse.getSourceAsMap();
        } catch (Exception e) {
            log.error("Error while fetching from index {} : {}", index, e.getMessage());
        }
        return null;
    }

    private List<Map<String, Object>> getResultsByScroll(String index, SearchSourceBuilder query, Integer size) throws IOException {
        try{
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.scroll(TimeValue.timeValueMinutes(1L));
            searchRequest.source(query);
            log.info("Scrolling index {} with query: {}", index, query.toString());
            Instant scrollStartTime = Instant.now();
            SearchResponse searchResponse = clientService.getClient().search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            List<Map<String, Object>> allHits = new ArrayList<>(Arrays.stream(searchResponse.getHits().getHits()).map(SearchHit::getSourceAsMap).toList());
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

            while (searchResponse.getHits().getHits() != null && searchResponse.getHits().getHits().length > 0 && allHits.size() < size) {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = clientService.getClient().scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                allHits.addAll(Arrays.stream(searchResponse.getHits().getHits()).map(SearchHit::getSourceAsMap).toList());
            }

            clearScroll(scrollId);
            Instant scrollEndTime = Instant.now();
            Duration scrollingDuration = Duration.between(scrollStartTime, scrollEndTime);
            log.info("Scrolling completed in {} seconds.", scrollingDuration.getSeconds());
            return allHits.size() <= size ? allHits : allHits.subList(0, size);
        } catch (Exception e){
            log.error("Error in processing message: {}", e.getMessage());
            log.error("Failed to run query in scroller for index {} {}", index, query);
            throw e;
        }
    }


    private void clearScroll(String scrollId) throws IOException {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        clientService.getClient().clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
    }

    public List<Map<String, Object>> getResultsBySize(String index, SearchSourceBuilder query, Integer size) {
        // Check if the requested size exceeds the limit and use scroll if necessary
        try {
            query.timeout(TimeValue.timeValueSeconds(60));
            if (size != null && size > 10000) {
                query.size(5000);
                return getResultsByScroll(index, query, size);
            } else query.size(Objects.requireNonNullElse(size, 10000));

            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(query);
            SearchResponse searchResponse = clientService.getClient().search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getHits().length == 10000) {
                return getResultsByScroll(index, query, Integer.MAX_VALUE);
            }

            return Arrays.stream(searchResponse.getHits().getHits())
                    .map(SearchHit::getSourceAsMap)
                    .collect(Collectors.toList());
        }
        catch (Exception ex){
            log.error("Error in processing message: {}", ex.getMessage());
            log.error("Failed to run query for index {} {}", index, query);
            return null;
        }
    }
}