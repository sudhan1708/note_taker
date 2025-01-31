package com.hackathon.note_taker.exceptions;

import com.hackathon.note_taker.enums.ExceptionReason;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoteTakerException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public NoteTakerException(ExceptionReason type) {
        this.httpStatus = type.getHttpStatus();
        this.errorMessage = type.getErrorMessage();
    }

    public NoteTakerException(final ExceptionReason type, String... args) {
        this.httpStatus = type.getHttpStatus();
        this.errorMessage = String.format(type.getErrorMessage(), args);
    }
}
