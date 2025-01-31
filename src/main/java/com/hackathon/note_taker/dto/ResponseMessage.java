package com.hackathon.note_taker.dto;
import lombok.Data;

@Data
public class ResponseMessage<T>{
    private String message;
    private int status=200;
    private T data;

    public ResponseMessage(String message) {
        this(message, 200);
    }
    public ResponseMessage(String message, int status) {
        this(message, status, null);
    }

    public ResponseMessage(String message, int status, T data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public ResponseMessage(String message , T data) {
        this.data = data;
        this.message = message;
    }
}

