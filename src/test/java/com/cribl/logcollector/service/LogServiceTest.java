package com.cribl.logcollector.service;

import com.cribl.logcollector.model.LogResponse;
import com.cribl.logcollector.testutils.LogTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LogServiceTest extends LogTestBase {

    public static final String TEST_LOG = "test.log";

    @Autowired
    private LogService logService;

    private List<String> logLines = List.of(
            "error log line 1",
            "info log line",
            "error log line 2",
            "debug log line"
    );

    @BeforeEach
    public void setUp() throws Exception {
        createTestFile(TEST_LOG, logLines);
    }


    @Test
    public void whenNoKeywordIsSpecified_thenReturnAllLogs() {
        List<String> reversedLogLines = new ArrayList<>(logLines);
        Collections.reverse(reversedLogLines);
        LogResponse response = logService.getLogs(TEST_LOG, null, null);
        List<String> logs = response.getLogs();
        assertThat(logs).containsExactlyElementsOf(reversedLogLines);
    }

    @Test
    public void whenKeywordIsSpecified_thenReturnAllLogsContainingKeyword() {
        LogResponse response = logService.getLogs(TEST_LOG, null, "error");
        List<String> logs = response.getLogs();
        assertThat(logs).containsExactly("error log line 2", "error log line 1");
    }

    @Test
    public void whenTooManyMatches_thenReturnLatestLogsContainingKeyword() {
        LogResponse response = logService.getLogs(TEST_LOG, 1, "error");
        List<String> logs = response.getLogs();
        assertThat(logs).containsExactly("error log line 2");
    }


    @Test
    public void whenLastNIsSpecified_thenReturnLastNLogs() {
        LogResponse response = logService.getLogs(TEST_LOG, 2, null);
        List<String> logs = response.getLogs();
        assertThat(logs).containsExactly("debug log line", "error log line 2");
    }

    @Test
    public void whenGetFiles_thenReturnAllLogFiles() {
        List<String> files = logService.getFiles().getFiles();
        assertThat(files).containsExactly(TEST_LOG);
    }


}
