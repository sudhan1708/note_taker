package com.hackathon.note_taker.enums;


import org.springframework.http.HttpStatus;

public enum ExceptionReason {
    NO_ORG_EXISTS(HttpStatus.BAD_REQUEST, "Org doesn't exists"),
    ORG_ID_EMPTY(HttpStatus.BAD_REQUEST, "Org Id is empty"),
    USER_ID_EMPTY(HttpStatus.BAD_REQUEST, "User Id is empty or User doesn't exists"),
    PROJECT_ID_EMPTY(HttpStatus.BAD_REQUEST, "Project Id is empty or Project doesn't exists"),
    MEDIA_TYPE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "Media Type is empty"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "%s"),
    EXTERNAL_API_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "%s"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "%s"),
    FAILED(HttpStatus.BAD_REQUEST, "%s"),
    SOMETHING_WENT_WRONG(HttpStatus.BAD_REQUEST, "%s"),
    TICKET_NOT_EXISTS(HttpStatus.BAD_REQUEST, "Ticket doesn't exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "%s"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Not Authorized"),
    FIELD_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "%s is not %s"),
    STRING_TOO_LONG(HttpStatus.BAD_REQUEST, "String size is too long, Expected length is %s. KEY : %s, SIZE : %s"),
    CREATE_TICKET_VALIDATION(HttpStatus.BAD_REQUEST, "Create ticket validation failed :- "),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Item does not exist" );

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ExceptionReason(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
