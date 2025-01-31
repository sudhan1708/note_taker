package com.hackathon.note_taker.services.proto;


public class WordInfo {
    private double startTime;
    private double endTime;
    private String word;
    private float confidence;

    public static class Builder {
        private final WordInfo instance;

        public Builder() {
            instance = new WordInfo();
        }

        public Builder setStartTime(double startTime) {
            instance.startTime = startTime;
            return this;
        }

        public Builder setEndTime(double endTime) {
            instance.endTime = endTime;
            return this;
        }

        public Builder setWord(String word) {
            instance.word = word;
            return this;
        }

        public Builder setConfidence(float confidence) {
            instance.confidence = confidence;
            return this;
        }

        public WordInfo build() {
            return instance;
        }
    }

    public double getStartTime() { return startTime; }
    public double getEndTime() { return endTime; }
    public String getWord() { return word; }
    public float getConfidence() { return confidence; }
}
