package com.junorz.travelbook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.context.validator.InvalidOperationException;
import com.junorz.travelbook.context.validator.ValidationException;
import com.junorz.travelbook.utils.ControllerUtil;

import lombok.Data;

@ControllerAdvice
public class ExceptionController {

    private final Response response;

    public ExceptionController(Response response) {
        this.response = response;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<Response> handleValidationException(ValidationException e) {
        return ControllerUtil.badRequest(response.of(null, e.getMessage(), Status.FAILED));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Response> handleParamsValidationException(MethodArgumentNotValidException e) {
        List<FieldError> errorList = e.getBindingResult().getFieldErrors();
        List<FieldErrorDetail> errorDetailList = new ArrayList<>();
        errorList.forEach(error -> {
            FieldErrorDetail errorDetail = new FieldErrorDetail();
            errorDetail.setFieldName(error.getField());
            errorDetail.setErrorMessage(error.getDefaultMessage());
            errorDetailList.add(errorDetail);
        });

        return ControllerUtil.badRequest(response.of(errorDetailList, Messages.BAD_REQUEST, Status.FAILED));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<Response> handleNullReqeustBodyException(HttpMessageNotReadableException e) {
        return ControllerUtil.badRequest(response.of(null, Messages.BAD_REQUEST, Status.FAILED));
    }
    
    @ExceptionHandler(InvalidOperationException.class)
    @ResponseBody
    public ResponseEntity<Response> handleInvalidOperationException(InvalidOperationException e) {
        return ControllerUtil.forbidden(response.of(null, e.getMessage(), Status.FAILED));
    }

    @Data
    public static class FieldErrorDetail {
        private String fieldName;
        private String errorMessage;
    }

}
