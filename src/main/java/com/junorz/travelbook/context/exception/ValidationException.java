package com.junorz.travelbook.context.exception;

public class ValidationException extends RuntimeException {
    
    private static final long serialVersionUID = 3035160580195044865L;
    
    public ValidationException(String message) {
        super(message);
    }
    
}
