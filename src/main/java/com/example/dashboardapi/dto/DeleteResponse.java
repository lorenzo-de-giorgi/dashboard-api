package com.example.dashboardapi.dto;

import java.time.Instant;

public class DeleteResponse {

    private boolean success;
    private Object id;
    private String message;
    private long timestamp;

    public DeleteResponse() {}

    public DeleteResponse(boolean success, Object id, String message) {
        this.success = success;
        this.id = id;
        this.message = message;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
