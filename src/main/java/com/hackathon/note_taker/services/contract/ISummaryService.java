package com.hackathon.note_taker.services.contract;

import com.hackathon.note_taker.dto.WebRequestResponse;
import com.hackathon.note_taker.dto.response.AudioSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ISummaryService {
    public WebRequestResponse generateAudioSummary(MultipartFile audioUrl) throws IOException;

    public List<Map<String, Object>> getAllChatSummary();

    public Map<String, Object> getSummaryByFileId(String fileId);
}
