package com.cribl.logcollector.service;

import com.cribl.logcollector.model.LogResponse;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {
    @Value("${log.base.path}")
    private String basePath;

    public LogResponse getLogs(String filename, Integer lastN, String keyword) {
        List<String> logs = new ArrayList<>();
        String filePath = Paths.get(basePath, filename).toString();
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (keyword == null || line.contains(keyword)) {
                    logs.add(line);
                }
                if (lastN != null && logs.size() == lastN) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading log file", e);
        }

        return new LogResponse(logs);
    }
}

