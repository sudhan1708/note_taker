package com.hackathon.note_taker.services;

import com.hackathon.note_taker.models.FileMetaData;
import com.hackathon.note_taker.repository.FileMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileMetadataService {
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    public String getFileIdByName(String fileName) {
       return fileMetaDataRepository.getFileId(fileName);
    }
}
