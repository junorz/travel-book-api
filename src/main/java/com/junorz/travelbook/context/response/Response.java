package com.junorz.travelbook.context.response;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {

    private Object data;
    private String message;
    private Status status;

    @JsonIgnore
    private MessageSource messageSource;

    public Response(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Response(Object data, String message, Status status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public Response of(Object data, String messageCode) {
        return of(data, messageCode, Status.SUCCESS);
    }

    public Response of(Object data, String messageCode, Status status) {
        return new Response(data, getMsgWithoutArgs(messageCode), status);
    }

    private String getMsgWithoutArgs(String messageCode) {
        return messageSource.getMessage(messageCode, null, Locale.getDefault());
    }

}
