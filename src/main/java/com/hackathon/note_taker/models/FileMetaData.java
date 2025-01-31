package com.hackathon.note_taker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaData {
    private String id;
    private String fileId;
    private String fileName;
}
