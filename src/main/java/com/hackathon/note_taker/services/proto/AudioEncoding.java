package com.hackathon.note_taker.services.proto;

public enum AudioEncoding {
    ENCODING_UNSPECIFIED(0),
    LINEAR_PCM(1),
    FLAC(2),
    WAV(3);

    private final int value;

    AudioEncoding(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
