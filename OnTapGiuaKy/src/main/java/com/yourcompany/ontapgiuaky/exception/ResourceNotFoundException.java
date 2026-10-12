package com.yourcompany.ontapgiuaky.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object fieldValue) {
        super(resourceName + " không tồn tại với giá trị: " + fieldValue);
    }
}

