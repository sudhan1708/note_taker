package com.hackathon.note_taker.services.contract;

import com.hackathon.note_taker.dto.WebRequestResponse;
import com.hackathon.note_taker.dto.response.AudioSummaryResponse;

import java.io.IOException;

public interface ISummaryService {
    public WebRequestResponse generateAudioSummary(String audioUrl, boolean diarizationRequired) throws IOException;
}
