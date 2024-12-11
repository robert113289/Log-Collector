package com.cribl.logcollector.service;

import com.cribl.logcollector.exception.LogFileNotFoundException;
import com.cribl.logcollector.exception.LogFileReadException;
import com.cribl.logcollector.model.LogFilesResponse;
import com.cribl.logcollector.model.LogResponse;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {
    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    @Value("${log.base.path}")
    private String basePath;

    public LogResponse getLogs(String filename, Integer lastN, String keyword) {
        List<String> logs = new ArrayList<>();
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(getFileSafely(filename))) {
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
            if (!logs.isEmpty()) {
                log.info("Last Line read: {}", logs.get(logs.size() - 1));
            }
            throw new LogFileReadException("Error reading log file: " + filename, e);
        }

        return new LogResponse(logs);
    }

    private File getFileSafely(String filename) {
        String filePath = Paths.get(basePath, filename).toString();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new LogFileNotFoundException("Log file not found: " + filename);
        }
        return file;
    }

    public LogFilesResponse getFiles() {
        File folder = new File(basePath);
        File[] files = folder.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".log")) {
                    fileNames.add(file.getName());
                }
            }
        }
        return new LogFilesResponse(fileNames);
    }
}

