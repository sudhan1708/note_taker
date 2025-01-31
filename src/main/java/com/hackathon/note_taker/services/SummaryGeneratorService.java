package com.hackathon.note_taker.services;

import com.hackathon.note_taker.factory.ModelServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hackathon.note_taker.enums.VertexModelType.VERTEX_AI_GEMINI_PRO;

@Service
public class SummaryGeneratorService {

    @Autowired
    ModelServiceFactory modelServiceFactory;

    public void getSummary(String fileName) {

        String prompt = "";
//        String prompt = String.format(BASE_PROMPT, userMessage, query, history);
        String response = modelServiceFactory.getModel(ModelServiceFactory.AI_MODEL.VERTEX_AI).predictChatResponse(prompt, VERTEX_AI_GEMINI_PRO);
    }
}
