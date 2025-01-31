package com.hackathon.note_taker.services.proto;

import java.util.List;
import java.util.ArrayList;

// RecognizeResponse.java
public class RecognizeResponse {
    private final List<SpeechRecognitionResult> results;

    private RecognizeResponse(Builder builder) {
        this.results = builder.results;
    }

    public List<SpeechRecognitionResult> getResultsList() {
        return results;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final List<SpeechRecognitionResult> results = new ArrayList<>();

        public Builder addResults(SpeechRecognitionResult result) {
            results.add(result);
            return this;
        }

        public RecognizeResponse build() {
            return new RecognizeResponse(this);
        }
    }
}



