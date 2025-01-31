package com.hackathon.note_taker.services;

import com.hackathon.note_taker.models.ChatDocument;
import com.hackathon.note_taker.models.FileMetaData;
import com.hackathon.note_taker.repository.ElasticBaseRepository;
import com.hackathon.note_taker.utils.GenericDocumentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ChatExtractorService {

    @Autowired
    private final ElasticBaseRepository elasticBaseRepository;

    private static final String CHAT_REGEX = "(\\d{2}/\\d{2}/\\d{4}, \\d{2}:\\d{2}) - ([^:]+): (.*)";

    private static final String CHAT_INDEX = "chats";

    private static final String FILE_METADATA_INDEX = "file_metadata";

    public ChatExtractorService(ElasticBaseRepository elasticBaseRepository) {
        this.elasticBaseRepository = elasticBaseRepository;
    }

    public void storeChats(String fileName, String chats) {
        List<Map<String, Object>> extractedChats = extractChats(chats);
        String fileId = UUID.randomUUID().toString();
        List<ChatDocument> chatDocs = GenericDocumentProcessor.convertToDocuments(
                extractedChats,
                chat -> new ChatDocument(
                        fileId,
                        fileName,
                        chat.get("timestamp").toString(),
                        chat.get("sender").toString(),
                        chat.get("message").toString()
                )
        );
        FileMetaData fileMetaData = new FileMetaData(fileId, fileId, fileName);
        elasticBaseRepository.saveDocs(chatDocs, CHAT_INDEX);
        elasticBaseRepository.saveDoc(fileMetaData, FILE_METADATA_INDEX);
        log.info("chats and file metadata stored successfully!");
    }

    private List<Map<String, Object>> extractChats(String chats) {
        List<Map<String, Object>> chatList = new ArrayList<>();
        Pattern pattern = Pattern.compile(CHAT_REGEX);
        Scanner scanner = new Scanner(chats);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                Map<String, Object> chat = new HashMap<>();
                chat.put("timestamp", matcher.group(1));
                chat.put("sender", matcher.group(2));
                chat.put("message", matcher.group(3));
                chatList.add(chat);
            }
        }
        scanner.close();
        return chatList;
    }

    public List<String> getStoredChatMessagesByFile(String fileId) {
        log.info("Fetching chat messages for file id {}", fileId);
        BoolQueryBuilder filter = new BoolQueryBuilder();
        filter.should(QueryBuilders.termQuery("fileId.keyword", fileId));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.sort(SortBuilders.fieldSort("timestamp.keyword").order(SortOrder.ASC));
        sourceBuilder.query(QueryBuilders.boolQuery().filter(filter));
        List<Map<String, Object>> chatDocuments = elasticBaseRepository.getResultsByQuery(CHAT_INDEX, sourceBuilder, Integer.MAX_VALUE);
        return chatDocuments.stream().map(chatDoc -> chatDoc.get("message").toString()).toList();
    }
}