package com.example.dashboardapi.dto;

import java.time.Instant;

public class GetResponse<T> {

    private boolean success;
    private T data;
    private Integer resultsSize;

    public GetResponse() {}

    public GetResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.resultsSize = computeResultsSize(data);
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
        this.resultsSize = computeResultsSize(data);
    }

    private Integer computeResultsSize(T data) {
        if (data == null) return 0;
        if (data instanceof java.util.Collection) {
            return ((java.util.Collection<?>) data).size();
        }
        return 1;
    }

    public Integer getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(Integer resultsSize) {
        this.resultsSize = resultsSize;
    }
}
