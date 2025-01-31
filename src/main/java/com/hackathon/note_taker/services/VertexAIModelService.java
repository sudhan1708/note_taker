package com.hackathon.note_taker.services;

import com.hackathon.note_taker.exceptions.NoteTakerException;
import com.hackathon.note_taker.enums.ExceptionReason;
import com.hackathon.note_taker.enums.VertexModelType;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class VertexAIModelService implements ModelService {
    private static final Logger log = LoggerFactory.getLogger(VertexAIModelService.class);
    private final List<String> regionCodes = List.of(
            "asia-southeast1", // Singapore
            "asia-northeast3", // Seoul, Korea
            "asia-east1", // Taiwan
            "asia-east2", // Hong Kong, China
            "asia-south1", // Mumbai, India
            "us-central1" // Specific for VERTEX_AI_GEMINI_2_EXPERIMENTAL
    );

    private static final String VERTEX_AI_PROJECT = "callzen";

    private static final int MAX_RETRY = 3;

    private static final Map<String, VertexAI> vertexAIMap = new HashMap<>();

    @PostConstruct
    public void init(){
        regionCodes.forEach(region -> {
            vertexAIMap.put(region, new VertexAI(VERTEX_AI_PROJECT, region));
        });
    }

    @Override
    public String predictChatResponse(String prompt, VertexModelType vertexAiModel) {
        return predictChatResponse(prompt, vertexAiModel, 0);
    }
    public String predictChatResponse(String prompt, VertexModelType vertexAiModel, int retryCount) {
        try {
            log.info(String.format("Start Vertex AI call %s", prompt));
            long startTime = System.currentTimeMillis();
            String regionCode = vertexAiModel == VertexModelType.VERTEX_AI_GEMINI_2_EXPERIMENTAL
                    ? "us-central1"
                    : regionCodes.get((new Random()).nextInt(regionCodes.size()));
            VertexAI vertexAi = vertexAIMap.get(regionCode);

            SafetySetting harmCategoryDcSetting =
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build();

            SafetySetting harmCategoryHarassmentSetting =
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build();

            SafetySetting harmCategoryHateSpeechSetting =
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build();

            SafetySetting harmCategorySexuallyExplicitSetting =
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_NONE)
                            .build();

            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder()
                            .setMaxOutputTokens(1024 * 8)
                            .setCandidateCount(1)
                            .setTemperature(0.0f)
                            .setTopP(1)
                            .build();

            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName(vertexAiModel.getModelName())
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .setSafetySettings(List.of(harmCategoryHateSpeechSetting, harmCategoryDcSetting, harmCategoryHarassmentSetting, harmCategorySexuallyExplicitSetting))
                            .build();
            GenerateContentResponse response = model.generateContent(prompt);
            String textResponse = ResponseHandler.getText(response);
            log.info(String.format("Time taken by Vertex AI call is %s", System.currentTimeMillis() - startTime));
            return textResponse;
        }catch (Exception e){
            log.error(String.format("Error while calling vertex AI : %s", e.getMessage()));
            if(retryCount < MAX_RETRY){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignore) {
                }
                return predictChatResponse(prompt, vertexAiModel, retryCount + 1);
            }
            throw new NoteTakerException(ExceptionReason.FAILED, "Error while calling vertex AI : %s", e.getMessage());
        }

    }

}
