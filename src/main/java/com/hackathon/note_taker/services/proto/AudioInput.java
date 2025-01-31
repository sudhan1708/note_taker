package com.hackathon.note_taker.services.proto;

public class AudioInput {
    private byte[] content;
    private String uri;

    private AudioInput() {}

    public static class Builder {
        private final AudioInput instance;

        public Builder() {
            instance = new AudioInput();
        }

        public Builder setContent(byte[] content) {
            instance.content = content;
            return this;
        }

        public Builder setUri(String uri) {
            instance.uri = uri;
            return this;
        }

        public AudioInput build() {
            return instance;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public byte[] getContent() { return content; }
    public String getUri() { return uri; }
}
