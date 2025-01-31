package com.hackathon.note_taker.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class UploadService {

    private static final String DEFAULT_TEXT_FILE_NAME = "chats.txt";

    @Data
    @AllArgsConstructor
    private static class ProcessedZipContent {
        private File textFile;
        private File[] filesInFolder;
    }


    public String processZipFile(MultipartFile file) throws IOException {
        File tempDir = createTempDirectory();
        try {
            ProcessedZipContent content = extractZipContent(file, tempDir);
            processTextFile(content.getTextFile(), content.getFilesInFolder());
            return "Text extracted successfully";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private File createTempDirectory() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "uploadedZip/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return tempDir;
    }


    private ProcessedZipContent extractZipContent(MultipartFile file, File tempDir) throws IOException {
        File extractedFolder = null;
        File csvFile = null;

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.getBytes()))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File outputFile = new File(tempDir, zipEntry.getName());

                if (zipEntry.isDirectory()) {
                    outputFile.mkdirs();
                    if (extractedFolder == null) {
                        extractedFolder = outputFile;
                    }
                } else {
                    extractFile(zis, outputFile);
                    if (outputFile.getName().equals(DEFAULT_TEXT_FILE_NAME)) {
                        csvFile = outputFile;
                    }
                }
                zis.closeEntry();
            }
        }

        assert extractedFolder != null;
        return new ProcessedZipContent(csvFile, extractedFolder.listFiles());
    }


    private void extractFile(ZipInputStream zis, File outputFile) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = zis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
        }
    }

    private void processTextFile(File textFile, File[] filesInFolder) {

    }

}