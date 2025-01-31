package com.hackathon.note_taker.controllers;

import com.hackathon.note_taker.dto.WebRequestResponse;
import com.hackathon.note_taker.dto.request.GenerateAudioSummaryRequestBody;
import com.hackathon.note_taker.dto.response.AudioSummaryResponse;
import com.hackathon.note_taker.models.Summary;
import com.hackathon.note_taker.services.contract.ISummaryService;
import jakarta.json.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.hackathon.note_taker.utils.JsonExtractor.extractJsonString;

@Slf4j
@RestController
@RequestMapping("/api/v1/rest/summary")
public class SummaryController {
    @Autowired
    private ISummaryService summaryService;

    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> generateAudioSummary(@RequestBody @ModelAttribute GenerateAudioSummaryRequestBody requestBody) throws IOException {
        String res = summaryService.generateAudioSummary(requestBody.getFile());
        log.info("Transcripts : {}", res);
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("summery", res);
        String jsonPart = extractJsonString(res);
        String fileId = UUID.randomUUID().toString();
        summaryService.StoreSummary(new Summary(fileId, jsonPart, requestBody.getFile().getName(), fileId));
        return serviceResponse;
    }

    @GetMapping("/chat/subjects")
    public List<Map<String, Object>> getAllChatSummary() {
        return summaryService.getAllChatSummary();
    }

    @GetMapping("/chat/{fileId}")
    public Map<String, Object> getSummaryByFileId(@PathVariable String fileId) {
        return summaryService.getSummaryByFileId(fileId);
    }
}
