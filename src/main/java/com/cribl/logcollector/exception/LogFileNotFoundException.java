package com.cribl.logcollector.exception;

public class LogFileNotFoundException extends RuntimeException {

    public LogFileNotFoundException(String s) {
        super(s);
    }
}
