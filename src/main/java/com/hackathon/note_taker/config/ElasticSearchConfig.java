//package com.hackathon.note_taker.config;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.RestHighLevelClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.Resource;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//
//import java.time.Duration;
//
//@Configuration
//public class ElasticSearchConfig {
//
//    @Value("classpath:elastic/http_ca.crt")
//    private Resource caCertificate;
//
//    @Value("${elasticsearch.default.host}")
//    private String elasticsearchHost;
//
//    @Value("${elasticsearch.default.username}")
//    private String elasticsearchUsername;
//
//    @Value("${elasticsearch.default.password}")
//    private String elasticsearchPassword;
//
//    @Primary
//    @Bean(name = "defaultClient")
//    public RestHighLevelClient defaultClient() {
//        try {
//            ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                    .connectedTo(elasticsearchHost)
//                    .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
//                    .withConnectTimeout(Duration.ofSeconds(60))
//                    .withSocketTimeout(Duration.ofSeconds(60))
//                    .build();
//
//            return new RestHighLevelClientBuilder(RestClients.create(clientConfiguration)
//                    .lowLevelRest())
//                    .setApiCompatibilityMode(true).build();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to connect to elastic host " + elasticsearchHost + ": " + e.getMessage());
//        }
//    }
//}
