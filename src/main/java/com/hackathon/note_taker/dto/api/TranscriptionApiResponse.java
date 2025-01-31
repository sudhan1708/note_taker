package com.hackathon.note_taker.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackathon.note_taker.dto.WebRequestResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptionApiResponse extends WebRequestResponse {
    List<Transcription> transcriptions;



    @Data
    public static class Transcription {
        String speaker_label;
        String transcription;
    }
}

/*
* [{"speaker_label":"speaker_0","transcription":"No speech detected"},{"speaker_label":"speaker_0","transcription":"hello one to three hello one to three "}]
* */
