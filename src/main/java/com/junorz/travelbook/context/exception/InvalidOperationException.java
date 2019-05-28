package com.junorz.travelbook.context.exception;

public class InvalidOperationException extends RuntimeException {

    private static final long serialVersionUID = -5321656640128552212L;
    
    public InvalidOperationException(String message) {
        super(message);
    }
    
}
