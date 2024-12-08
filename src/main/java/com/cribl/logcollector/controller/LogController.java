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
                               @RequestParam(required = false) Integer lastN,
                               @RequestParam(required = false) String keyword) {
        return logService.getLogs(filename, lastN, keyword);
    }
}
