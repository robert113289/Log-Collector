package com.cribl.logcollector.model;

import java.util.List;

public class LogResponse {
    private List<String> logs;

    public LogResponse(List<String> logs) {
        this.logs = logs;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
