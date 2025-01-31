package com.hackathon.note_taker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDocument {

    private String fileId;
    private String fileName;
    private String timestamp;
    private String sender;
    private String message;

    public ChatDocument(String fileName, String timestamp, String sender, String message) {
        this.fileName = fileName;
        this.timestamp = timestamp;
        this.sender = sender;
        this.message = message;
    }
}