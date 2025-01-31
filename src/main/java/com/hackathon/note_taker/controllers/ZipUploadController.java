package com.hackathon.note_taker.controllers;

import com.hackathon.note_taker.dto.ResponseMessage;
import com.hackathon.note_taker.models.Summary;
import com.hackathon.note_taker.services.ChatExtractorService;
import com.hackathon.note_taker.services.FileMetadataService;
import com.hackathon.note_taker.services.SummaryService;
import com.hackathon.note_taker.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static com.hackathon.note_taker.utils.JsonExtractor.extractJsonString;
import static com.hackathon.note_taker.utils.JsonExtractor.extractSummaryJson;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class ZipUploadController {

    @Autowired
    private final ChatExtractorService chatExtractorService;

    @Autowired
    private final SummaryService summaryService;

    @Autowired
    private final FileMetadataService fileMetadataService;

    public ZipUploadController(ChatExtractorService chatExtractorService, SummaryService summaryService, FileMetadataService fileMetadataService) {
        this.chatExtractorService = chatExtractorService;
        this.summaryService = summaryService;
        this.fileMetadataService = fileMetadataService;
    }

    @PostMapping("/upload-zip")
    public ResponseEntity<ResponseMessage<String>> uploadZipFile(@RequestBody MultipartFile file) {
        Map<String, String> extractedTexts = new HashMap<>();

        if (file.isEmpty()) {
            extractedTexts.put("error", "File upload failed: No file provided!");
            return ResponseEntity.badRequest().body(new ResponseMessage<>("No file provided"));
        }

        try {
            Path tempDir = Files.createTempDirectory("unzipped");
            FileUtils.unzipFile(file, tempDir);
            extractedTexts = FileUtils.readTextFiles(tempDir);
            FileUtils.deleteTempDirectory(tempDir);
            log.info("Extracted text : {}", extractedTexts);
            List<String> summaries = new ArrayList<>();
            extractedTexts.forEach(chatExtractorService::storeChats);
            extractedTexts.forEach((fileName, chats) -> {
                String summary = summaryService.getSummary(fileName);
                log.info("Summary : {}", summary);
                String jsonPart = extractJsonString(summary);
                String fileId = fileMetadataService.getFileIdByName(fileName);
                summaries.add(jsonPart);
                summaryService.StoreSummary(new Summary(fileId, jsonPart, fileName, fileId));
            });
            log.info("Summaries : {}", summaries);
            return ResponseEntity.ok().body(new ResponseMessage<>(summaries.get(0)));
        } catch (IOException e) {
            extractedTexts.put("error", "Error processing ZIP file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseMessage<>("File texts extraction failed"));
        }
    }
}
