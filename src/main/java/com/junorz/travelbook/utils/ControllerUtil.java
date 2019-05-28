package com.junorz.travelbook.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.junorz.travelbook.context.response.Response;

public class ControllerUtil {
    
    public static ResponseEntity<Response> ok(Response response) { 
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    public static ResponseEntity<Response> result(HttpStatus httpStatus, Response response) {
        return ResponseEntity.status(httpStatus).body(response);
    }
    
    public static ResponseEntity<Response> badRequest(Response response) {
        return ResponseEntity.badRequest().body(response);
    }
    
    public static ResponseEntity<Response> forbidden(Response response) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    public static ResponseEntity<Response> notFound(Response response) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    public static ResponseEntity<Response> unauthorized(Response response) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
}
