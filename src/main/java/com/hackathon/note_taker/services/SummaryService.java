package com.hackathon.note_taker.services;

import com.hackathon.note_taker.constants.Prompts;
import com.hackathon.note_taker.factory.ModelServiceFactory;
import com.hackathon.note_taker.models.Summary;
import com.hackathon.note_taker.repository.SummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.note_taker.dto.WebRequestResponse;
import com.hackathon.note_taker.dto.api.TranscriptionApiResponse;
import com.hackathon.note_taker.services.contract.ISummaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.hackathon.note_taker.enums.VertexModelType.VERTEX_AI_GEMINI_PRO;

@Slf4j
@Service
public class SummaryService implements ISummaryService {
    @Autowired
    private ModelServiceFactory modelServiceFactory;

    @Autowired
    private ChatExtractorService chatExtractorService;

    @Autowired
    private FileMetadataService fileMetadataService;

    @Autowired
    private SummaryRepository summaryRepository;
    @Value("${external.api.transcription.url}")
    private String transcriptionApiUrl;
    @Value("${external.api.transcription.key}")
    private String getTranscriptionApiKey;
    @Value("${transcription.model}")
    private String transcriptionModel;
    private RestTemplate restTemplate;

    public SummaryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WebRequestResponse generateAudioSummary(String audioUrl, boolean diarizationRequired) throws IOException {
        TranscriptionApiResponse transcriptionApiResponse = getAudioTranscription(audioUrl, diarizationRequired);
        return transcriptionApiResponse;
    }

    private TranscriptionApiResponse getAudioTranscription(String audioUrl, boolean diarizationRequired) throws IOException {
        File file = downloadFile(audioUrl);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + audioUrl);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", getTranscriptionApiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        String payload = String.format("{\"audio_path_type\": \"file_upload\", \"diarization_required\": %b, \"model\": \"%s\"}", diarizationRequired, transcriptionModel);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        body.add("payload", payload);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                transcriptionApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        TranscriptionApiResponse apiResponse = deserializeTranscriptionApiResponse(response.getBody());

        return apiResponse;
    }

    public String getSummary(String fileName) {
        log.info("Generating summary for file {}", fileName);
        String fileId = fileMetadataService.getFileIdByName(fileName);
        List<String> chatMessages = chatExtractorService.getStoredChatMessagesByFile(fileId);
        String prompt = Prompts.BASE_PROMPT + "\n" + chatMessages;
        return modelServiceFactory.getModel(ModelServiceFactory.AI_MODEL.VERTEX_AI).predictChatResponse(prompt, VERTEX_AI_GEMINI_PRO);
    private File downloadFile(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        File tempFile = File.createTempFile("downloaded_audio", ".wav");
        try (InputStream in = url.openStream()) {
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    public void StoreSummary(Summary summary) {
        log.info("Storing summary data in DB");
        summaryRepository.storeSummary(summary);
    public TranscriptionApiResponse deserializeTranscriptionApiResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode dataNode = rootNode.get("data");

            return objectMapper.treeToValue(dataNode, TranscriptionApiResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
