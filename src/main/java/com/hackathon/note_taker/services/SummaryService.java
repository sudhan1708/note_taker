package com.hackathon.note_taker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackathon.note_taker.config.MultipartFileResource;
import com.hackathon.note_taker.constants.Prompts;
import com.hackathon.note_taker.dto.api.Transcript;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String generateAudioSummary(MultipartFile audioFile) throws IOException {
        String transcriptionApiResponse = getAudioTranscription(audioFile);
        return transcriptionApiResponse;
    }

    private String getAudioTranscription(MultipartFile audioFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartFileResource(audioFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                transcriptionApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        TranscriptionApiResponse apiResponse = deserializeTranscriptionApiResponse(response.getBody());
        return getSummaryFromScript(apiResponse);
    }

    public String getSummary(String fileName) {
        log.info("Generating summary for file {}", fileName);
        String fileId = fileMetadataService.getFileIdByName(fileName);
        List<String> chatMessages = chatExtractorService.getStoredChatMessagesByFile(fileId);
        String prompt = Prompts.BASE_PROMPT + "\n" + chatMessages;
        return modelServiceFactory.getModel(ModelServiceFactory.AI_MODEL.VERTEX_AI).predictChatResponse(prompt, VERTEX_AI_GEMINI_PRO);
    }

    public String getSummaryFromScript(TranscriptionApiResponse transcriptionApiResponse) {
        //log.info("Generating summary form script {}", fileName);
        List<String> chatMessages = new ArrayList<>();
        for(TranscriptionApiResponse.Transcription message : transcriptionApiResponse.getTranscriptions()){
            chatMessages.add(message.getSpeaker_label() + " : " + message.getTranscription());
        }
        String prompt = Prompts.BASE_PROMPT + "\n" + chatMessages;
        return modelServiceFactory.getModel(ModelServiceFactory.AI_MODEL.VERTEX_AI).predictChatResponse(prompt, VERTEX_AI_GEMINI_PRO);
    }
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
    }

    public TranscriptionApiResponse deserializeTranscriptionApiResponse(String jsonResponse) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TranscriptionApiResponse.Transcription>>() {}.getType();
            List<TranscriptionApiResponse.Transcription> myList = gson.fromJson(jsonResponse, listType);

            return new TranscriptionApiResponse(myList);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getAllChatSummary() {
        return summaryRepository.getAllSummarySubjects();
    }

    @Override
    public Map<String, Object> getSummaryByFileId(String fileId) {
        Map<String, Object> summaryDoc = summaryRepository.getSummaryByFileId(fileId);
        Object summaryData = summaryDoc.get("summaryData");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue((String) summaryData, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Error while reading summary docs");
        }
        return null;
    }
}
