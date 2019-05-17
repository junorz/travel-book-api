package com.junorz.travelbook.context.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    
    private Object data;
    private String message;
    private Status status;
    
    public static Response of(Object data, String message, Status status) {
        return new Response(data, message, status);
    }
    
}
