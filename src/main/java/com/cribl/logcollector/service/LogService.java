package com.cribl.logcollector.service;

import com.cribl.logcollector.model.LogResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {
    @Value("${log.base.path}")
    private String basePath;

    public LogResponse getLogs(String filename, Integer lastN, String keyword) {
        List<String> logs = new ArrayList<>();
        String filePath = Paths.get(basePath, filename).toString();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            logs = br.lines()
                    .filter(line -> keyword == null || line.contains(keyword))
                    .collect(Collectors.toList());
        } catch (
                IOException e) {
            // Handle exception or log it
        }

        if (lastN != null && logs.size() > lastN) {
            logs = logs.subList(logs.size() - lastN, logs.size());
        }

        Collections.reverse(logs);
        return new LogResponse(logs);
    }
}

