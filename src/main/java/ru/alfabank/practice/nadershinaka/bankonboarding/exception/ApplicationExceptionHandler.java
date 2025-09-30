package ru.alfabank.practice.nadershinaka.bankonboarding.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.ErrorResponse;

@ControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getCode(), ex.getMetaData());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

}
