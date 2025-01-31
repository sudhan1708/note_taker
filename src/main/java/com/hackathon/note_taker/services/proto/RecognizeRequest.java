package com.hackathon.note_taker.services.proto;

public class RecognizeRequest {
    private RecognitionConfig config;
    private AudioInput audio;

    private RecognizeRequest() {}

    public static class Builder {
        private final RecognizeRequest instance;

        public Builder() {
            instance = new RecognizeRequest();
        }

        public Builder setConfig(RecognitionConfig config) {
            instance.config = config;
            return this;
        }

        public Builder setAudio(AudioInput audio) {
            instance.audio = audio;
            return this;
        }

        public RecognizeRequest build() {
            return instance;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public RecognitionConfig getConfig() { return config; }
    public AudioInput getAudio() { return audio; }
}
