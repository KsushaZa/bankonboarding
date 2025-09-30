package ru.alfabank.practice.nadershinaka.bankonboarding.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApplicationException extends RuntimeException {

    private String message;
    private String code;
    private Map<String, Object> metaData;
    private HttpStatus httpStatus;

    public ApplicationException(String message, String code, Map<String, Object> metaData, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
        this.metaData = metaData;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
