package com.hackathon.note_taker.enums;


public enum VertexModelType {
    VERTEX_AI_GEMINI_PRO("gemini-1.5-pro-002"),
    VERTEX_AI_GEMINI_PRO_EXPERIMENTAL("gemini-pro-experimental"),
    VERTEX_AI_GEMINI_2_EXPERIMENTAL("gemini-2.0-flash-exp"),
    VERTEX_AI_GEMINI_FLASH_002("gemini-1.5-flash-002"),
    VERTEX_AI_CLAUDE_3("claude-3-5-sonnet-v2");

    final String modelName;

    VertexModelType(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return this.modelName;
    }
}
