package com.hackathon.note_taker.factory;


import com.hackathon.note_taker.services.ModelService;
import com.hackathon.note_taker.services.VertexAIModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceFactory {

    @Autowired
    VertexAIModelService vertexAIModelService;

    public enum AI_MODEL{
        VERTEX_AI,
        VERTEX_AI_CLAUDE
    }

    public ModelService getModel(AI_MODEL aiModel) {
        if (aiModel == AI_MODEL.VERTEX_AI) {
            return vertexAIModelService;
        }
        return null;
    }
}
