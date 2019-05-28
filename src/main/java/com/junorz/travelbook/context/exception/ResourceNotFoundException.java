package com.junorz.travelbook.context.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2831199536498222707L;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
