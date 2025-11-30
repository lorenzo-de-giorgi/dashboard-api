package com.example.dashboardapi.dto;

import java.time.Instant;

public class UpdateResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private long timestamp;

    public UpdateResponse() {}

    public UpdateResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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
