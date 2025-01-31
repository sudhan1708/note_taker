package com.hackathon.note_taker.repository;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class FileMetaDataRepository {

    @Autowired
    private ElasticBaseRepository elasticBaseRepository;

    private static final String FILE_METADATA_INDEX = "file_metadata";

    public String getFileId(String fileName) {
        BoolQueryBuilder filter = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("fileName.keyword", fileName)); // Use must() for exact match

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(filter);

        List<Map<String, Object>> fileMetaDataDoc = elasticBaseRepository.getResultsByQuery(
                FILE_METADATA_INDEX, sourceBuilder, Integer.MAX_VALUE
        );

        if (!fileMetaDataDoc.isEmpty() && fileMetaDataDoc.get(0).containsKey("fileId")) {
            return fileMetaDataDoc.get(0).get("fileId").toString();
        }
        return null;
    }
}
