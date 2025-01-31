package com.hackathon.note_taker.services;

import com.hackathon.note_taker.constants.Prompts;
import com.hackathon.note_taker.factory.ModelServiceFactory;
import com.hackathon.note_taker.models.Summary;
import com.hackathon.note_taker.repository.SummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.note_taker.enums.VertexModelType.VERTEX_AI_GEMINI_PRO;

@Slf4j
@Service
public class SummaryService {

    @Autowired
    private ModelServiceFactory modelServiceFactory;

    @Autowired
    private ChatExtractorService chatExtractorService;

    @Autowired
    private FileMetadataService fileMetadataService;

    @Autowired
    private SummaryRepository summaryRepository;

    public String getSummary(String fileName) {
        log.info("Generating summary for file {}", fileName);
        String fileId = fileMetadataService.getFileIdByName(fileName);
        List<String> chatMessages = chatExtractorService.getStoredChatMessagesByFile(fileId);
        String prompt = Prompts.BASE_PROMPT + "\n" + chatMessages;
        return modelServiceFactory.getModel(ModelServiceFactory.AI_MODEL.VERTEX_AI).predictChatResponse(prompt, VERTEX_AI_GEMINI_PRO);
    }

    public void StoreSummary(Summary summary) {
        log.info("Storing summary data in DB");
        summaryRepository.storeSummary(summary);
    }
}
