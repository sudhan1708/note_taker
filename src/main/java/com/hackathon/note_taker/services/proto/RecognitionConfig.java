package com.hackathon.note_taker.services.proto;

public class RecognitionConfig {
    private String languageCode;
    private int maxAlternatives;
    private boolean enableAutomaticPunctuation;
    private boolean enableWordTimeOffsets;
    private int sampleRateHertz;
    private AudioEncoding encoding;

    private RecognitionConfig() {}

    public static class Builder {
        private final RecognitionConfig instance;

        public Builder() {
            instance = new RecognitionConfig();
        }

        public Builder setLanguageCode(String languageCode) {
            instance.languageCode = languageCode;
            return this;
        }

        public Builder setMaxAlternatives(int maxAlternatives) {
            instance.maxAlternatives = maxAlternatives;
            return this;
        }

        public Builder setEnableAutomaticPunctuation(boolean enable) {
            instance.enableAutomaticPunctuation = enable;
            return this;
        }

        public Builder setEnableWordTimeOffsets(boolean enable) {
            instance.enableWordTimeOffsets = enable;
            return this;
        }

        public Builder setSampleRateHertz(int sampleRateHertz) {
            instance.sampleRateHertz = sampleRateHertz;
            return this;
        }

        public Builder setEncoding(AudioEncoding encoding) {
            instance.encoding = encoding;
            return this;
        }

        public RecognitionConfig build() {
            return instance;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    // Getters
    public String getLanguageCode() { return languageCode; }
    public int getMaxAlternatives() { return maxAlternatives; }
    public boolean getEnableAutomaticPunctuation() { return enableAutomaticPunctuation; }
    public boolean getEnableWordTimeOffsets() { return enableWordTimeOffsets; }
    public int getSampleRateHertz() { return sampleRateHertz; }
    public AudioEncoding getEncoding() { return encoding; }
}