package com.campus.secondhand.exception;

/**
 * 访问拒绝异常
 */
public class AccessDeniedException extends RuntimeException {
    
    public AccessDeniedException() {
        super("无权限访问");
    }
    
    public AccessDeniedException(String message) {
        super(message);
    }
    
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
