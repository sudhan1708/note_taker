package com.hackathon.note_taker.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hackathon.note_taker.dto.WebRequestResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranscriptionApiResponse extends WebRequestResponse {
    private String id;
    private String transcription_status;
    private List<Transcript> transcripts;
    private AudioMeta audio_meta;
}
