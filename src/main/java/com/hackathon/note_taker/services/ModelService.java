package com.hackathon.note_taker.services;

import com.hackathon.note_taker.enums.VertexModelType;

public interface ModelService {
    String predictChatResponse(String prompt, VertexModelType vertexAiModel);
}