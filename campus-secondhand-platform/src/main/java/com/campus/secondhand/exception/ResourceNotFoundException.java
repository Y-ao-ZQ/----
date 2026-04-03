package com.campus.secondhand.exception;

/**
 * 资源不存在异常
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException() {
        super("资源不存在");
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
