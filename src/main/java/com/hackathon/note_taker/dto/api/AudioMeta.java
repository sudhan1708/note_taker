package com.hackathon.note_taker.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioMeta {
    private float audio_duration;
    private float silence_duration;
    private List<Float> channel_talktimes;
    private int switches;
}
