package com.hackathon.note_taker.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    // Function to extract ZIP file
    public static void unzipFile(MultipartFile file, Path destinationDir) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path filePath = destinationDir.resolve(entry.getName());

                if (!entry.isDirectory()) {
                    // Create parent directories if they don't exist
                    Files.createDirectories(filePath.getParent());
                    // Write file content
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        zipInputStream.transferTo(os);
                    }
                }
            }
        }
    }

    // Function to read text files from extracted directory
    public static Map<String, String> readTextFiles(Path directory) throws IOException {
        Map<String, String> fileContents = new HashMap<>();

        Files.walk(directory)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt")) // Read only .txt files
                .forEach(path -> {
                    try {
                        String content = Files.readString(path);
                        fileContents.put(path.getFileName().toString(), content);
                    } catch (IOException e) {
                        fileContents.put(path.getFileName().toString(), "Error reading file: " + e.getMessage());
                    }
                });

        return fileContents;
    }

    // Function to delete extracted files (cleanup)
    public static void deleteTempDirectory(Path directory) throws IOException {
        Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
