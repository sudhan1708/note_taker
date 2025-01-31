package com.hackathon.note_taker.repository;

import com.hackathon.note_taker.models.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SummaryRepository {
    @Autowired
    private ElasticBaseRepository elasticBaseRepository;

    private static final String SUMMARY_INDEX = "summaries";

    public void storeSummary(Summary summary) {
        elasticBaseRepository.saveDoc(summary, SUMMARY_INDEX);
    }
}
