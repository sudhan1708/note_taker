package com.hackathon.note_taker.controllers;

import com.hackathon.note_taker.dto.ResponseMessage;
import com.hackathon.note_taker.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class ZipUploadController {

    @PostMapping("/upload-zip")
    public ResponseEntity<ResponseMessage<String>> uploadZipFile(@RequestBody MultipartFile file) {
        Map<String, String> extractedTexts = new HashMap<>();

        // Check if the file is empty
        if (file.isEmpty()) {
            extractedTexts.put("error", "File upload failed: No file provided!");
            return ResponseEntity.badRequest().body(new ResponseMessage<>("No file provided"));
        }

        try {
            // Create a temporary directory to store extracted files
            Path tempDir = Files.createTempDirectory("unzipped");

            // Unzip the uploaded file
            FileUtils.unzipFile(file, tempDir);

            // Read all extracted text files and store content in a Map
            extractedTexts = FileUtils.readTextFiles(tempDir);

            // Cleanup: Delete extracted files (Optional)
            FileUtils.deleteTempDirectory(tempDir);

            log.info("Extracted text : {}", extractedTexts);
            extractedTexts.forEach((fileName, chats) -> {

            });
        } catch (IOException e) {
            extractedTexts.put("error", "Error processing ZIP file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseMessage<>("File texts extraction failed"));
        }

        return ResponseEntity.ok().body(new ResponseMessage<>("File texts extracted successfully"));
    }
}
