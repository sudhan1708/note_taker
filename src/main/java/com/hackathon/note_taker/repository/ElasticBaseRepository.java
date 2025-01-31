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
}