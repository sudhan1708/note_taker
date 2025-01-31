package com.hackathon.note_taker.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transcript {
    private String id;
    private int channel;
    private int chunk;
    private int order;
    private String chunkId;
    private String transcript;
    private double start_stamp;
    private double end_stamp;
}
