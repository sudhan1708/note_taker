package com.hackathon.note_taker.commons;

import jakarta.annotation.PostConstruct;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticSearchClientService {

    @Autowired
    @Qualifier("defaultClient")
    private RestHighLevelClient defaultClient;

    public RestHighLevelClient getClient() {
        return defaultClient;
    }
}
