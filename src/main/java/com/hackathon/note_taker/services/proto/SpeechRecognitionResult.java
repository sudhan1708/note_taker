package com.hackathon.note_taker.services.proto;

import java.util.ArrayList;
import java.util.List;

// SpeechRecognitionResult.java
public class SpeechRecognitionResult {
    private final List<SpeechRecognitionAlternative> alternatives;

    private SpeechRecognitionResult(Builder builder) {
        this.alternatives = builder.alternatives;
    }

    public List<SpeechRecognitionAlternative> getAlternativesList() {
        return alternatives;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private final List<SpeechRecognitionAlternative> alternatives = new ArrayList<>();

        public Builder addAlternatives(SpeechRecognitionAlternative alternative) {
            alternatives.add(alternative);
            return this;
        }

        public SpeechRecognitionResult build() {
            return new SpeechRecognitionResult(this);
        }
    }
}