package com.cribl.logcollector.testutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@TestPropertySource(properties = "log.base.path=./test-logs")
public abstract class LogTestBase {

    public static final Path TEST_LOG_DIR = Paths.get("./test-logs");

    @BeforeEach
    public void setUpLogDirectory() throws IOException {
        Files.createDirectories(TEST_LOG_DIR);
    }

    @AfterEach
    public void tearDownLogDirectory() throws IOException {
        // Clean up the test log directory after each test
        Files.walk(TEST_LOG_DIR)
                .map(Path::toFile)
                .forEach(file -> {
                    if (!file.delete()) {
                        file.deleteOnExit();
                    }
                });
    }

    protected void createTestFile(String fileName, List<String> lines) throws IOException {
        Path logFilePath = TEST_LOG_DIR.resolve(fileName);
        Files.write(logFilePath, lines);
    }
}

