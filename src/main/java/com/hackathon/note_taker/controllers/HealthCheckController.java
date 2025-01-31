package com.hackathon.note_taker.controllers;

import com.hackathon.note_taker.dto.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rest")
public class HealthCheckController {
    @GetMapping("/health/check")
    public ResponseEntity<ResponseMessage<String>> healthCheck() {
        return ResponseEntity.ok().body(new ResponseMessage<>("health check success"));
    }
}
