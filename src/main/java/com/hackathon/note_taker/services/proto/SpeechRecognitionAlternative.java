package com.hackathon.note_taker.services.proto;

// SpeechRecognitionAlternative.java
public class SpeechRecognitionAlternative {
    private final String transcript;
    private final float confidence;

    private SpeechRecognitionAlternative(com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative.Builder builder) {
        this.transcript = builder.transcript;
        this.confidence = builder.confidence;
    }

    public String getTranscript() {
        return transcript;
    }

    public float getConfidence() {
        return confidence;
    }

    public static com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative.Builder newBuilder() {
        return new com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative.Builder();
    }

    public static class Builder {
        private String transcript;
        private float confidence;

        public com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative.Builder setTranscript(String transcript) {
            this.transcript = transcript;
            return this;
        }

        public com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative.Builder setConfidence(float confidence) {
            this.confidence = confidence;
            return this;
        }

        public com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative build() {
            return new com.hackathon.note_taker.services.proto.SpeechRecognitionAlternative(this);
        }
    }
}