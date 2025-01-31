package com.hackathon.note_taker.controllers;

import com.hackathon.note_taker.dto.WebRequestResponse;
import com.hackathon.note_taker.dto.request.GenerateAudioSummaryRequestBody;
import com.hackathon.note_taker.dto.response.AudioSummaryResponse;
import com.hackathon.note_taker.services.contract.ISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SummaryController {
    @Autowired
    private ISummaryService summaryService;

    @PostMapping(value = "/transcription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebRequestResponse> generateAudioSummary(@RequestBody GenerateAudioSummaryRequestBody requestBody) throws IOException {
        WebRequestResponse serviceResponse = summaryService.generateAudioSummary(requestBody.getAudioUrl(), requestBody.isDiarizationRequired());
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }
}
