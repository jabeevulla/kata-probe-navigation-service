package com.codekata.oceanprobe.probenavigationservice.exception;

import org.springframework.stereotype.Component;

/**
 * Exception to throw when the intended data is not found in dataset.
 */
public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message) {
        super(message);
    }
}
