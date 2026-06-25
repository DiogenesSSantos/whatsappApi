package com.github.dio.mensageria.infra.controller.advice;

import java.time.OffsetDateTime;
import java.util.Map;

public class ErrorBodyResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;


    public ErrorBodyResponse(){}

    public ErrorBodyResponse(OffsetDateTime timestamp, int status, String message) {
        this(timestamp, status, message, null);
    }

    public ErrorBodyResponse(OffsetDateTime timestamp, int status, String message, Map<String, String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }


    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
