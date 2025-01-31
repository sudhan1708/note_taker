package com.hackathon.note_taker.services;


import com.google.api.client.util.Value;
import com.google.protobuf.ByteString;
import com.hackathon.note_taker.services.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

@Service
public class ASRService {

    private ManagedChannel channel;
    private RivaASRGrpc.RivaASRBlockingStub blockingStub;
    private RivaASRGrpc.RivaASRFutureStub futureStub;

    @PostConstruct
    private void init() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 9020)
                .usePlaintext()
                .build();

        blockingStub = RivaASRGrpc.newBlockingStub(channel);
        futureStub = RivaASRGrpc.newFutureStub(channel);
    }

    @PreDestroy
    private void shutdown() {
        if (channel != null) {
            channel.shutdown();
            try {
                channel.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String transcribeAudio(String audioPath) throws IOException {
        // Read audio file
        byte[] audioData = Files.readAllBytes(Path.of(audioPath));

        // Create recognition config
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setLanguageCode("en-US")
                .setMaxAlternatives(1)
                .setEnableAutomaticPunctuation(false)
                .build();

        // Create recognition request
        RecognizeRequest request = RecognizeRequest.newBuilder()
                .setConfig(config)
                .setAudio(AudioInput.newBuilder()
                        .setContent(ByteString.copyFrom(audioData).toByteArray())
                        .build())
                .build();

        // Get response using blocking stub
        RecognizeResponse response = blockingStub
                .withDeadlineAfter(30, TimeUnit.SECONDS)
                .recognize(request);

        // Process and return the transcript
        StringBuilder transcript = new StringBuilder();
        for (SpeechRecognitionResult result : response.getResultsList()) {
            for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {
                transcript.append(alternative.getTranscript());
            }
        }

        return transcript.toString();
    }

    // Async version using CompletableFuture
    public CompletableFuture<String> transcribeAudioAsync(String audioPath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return transcribeAudio(audioPath);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }
}
