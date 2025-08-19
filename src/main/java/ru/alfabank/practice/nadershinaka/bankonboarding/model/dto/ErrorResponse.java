package ru.alfabank.practice.nadershinaka.bankonboarding.model.dto;

import java.util.Map;

public class ErrorResponse {

    private String message;
    private String code;
    private Map<String, Object> metaData;

    public ErrorResponse(String message, String code, Map<String, Object> metaData) {
        this.message = message;
        this.code = code;
        this.metaData = metaData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }
}
