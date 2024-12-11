package com.cribl.logcollector.exception;

public class LogFileReadException extends RuntimeException {

    public LogFileReadException(String s, Exception e) {
        super(s, e);
    }
}
