package com.hackathon.note_taker.config;

import com.google.api.client.util.Value;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.server.host:localhost}")
    private String host;

    @Value("${grpc.server.port:9090}")
    private int port;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext() // Disable TLS for testing
                .build();
    }
}
