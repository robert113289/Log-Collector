package com.cribl.logcollector.service;

import com.cribl.logcollector.model.LogResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    public LogResponse getLogs(String filename, Integer lastN, String keyword) {
        return new LogResponse(List.of("hello this is line 1", "this is line 2", "this is line 3"));
    }
}
