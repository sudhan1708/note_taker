package com.hackathon.note_taker.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateAudioSummaryRequestBody {
    private String audioUrl;
    private boolean diarizationRequired;
}
