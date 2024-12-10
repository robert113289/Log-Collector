package com.cribl.logcollector.controller;

import com.cribl.logcollector.model.LogResponse;
import com.cribl.logcollector.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    public LogResponse getLogs(@RequestParam String filename,
                               @RequestParam(required = false, defaultValue = "20") Integer lastN,
                               @RequestParam(required = false) String keyword) {
        // todo: validate file exists
        // todo: validate fileName is not malicious
        // todo: validate lastN is positive
        // todo: validate lastN is not too large
        return logService.getLogs(filename, lastN, keyword);
    }
}
