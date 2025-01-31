package com.hackathon.note_taker.models;

import jakarta.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Summary {
    private String id;
    private String summaryData;
    private String fileName;
    private String fileId;

    public Summary(String summaryData, String fileName) {
        this.summaryData = summaryData;
        this.fileName = fileName;
    }
}
