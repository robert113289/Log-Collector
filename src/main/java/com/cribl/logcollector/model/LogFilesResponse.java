package com.cribl.logcollector.model;

import java.util.List;

public class LogFilesResponse {
    private List<String> files;

    public LogFilesResponse(List<String> files) {
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
