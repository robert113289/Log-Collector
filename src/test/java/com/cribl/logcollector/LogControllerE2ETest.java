package com.cribl.logcollector;

import com.cribl.logcollector.testutils.LogTestBase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogControllerE2ETest extends LogTestBase {
    @LocalServerPort
    private int port;

    private static final String TEST_LOG = "test.log";

    List<String> logLines = List.of(
            "error log line 1",
            "info log line",
            "error log line 2",
            "debug log line"
    );

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
    }

    @BeforeEach
    public void setUp() throws Exception {
        createTestFile("test.log", logLines);
    }

    @Test
    public void whenGetLogsWithoutKeyword_thenReturnAllLogs() {
        given().port(port)
                .param("filename", TEST_LOG)
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", not(empty()))
                .body("logs", containsInRelativeOrder(logLines.toArray()));

    }

    @Test
    public void whenGetLogsWithoutNLast_thenReturnDefaultNLastLogs(TestInfo testInfo) throws IOException {
        String fileName = testInfo.getDisplayName() + ".log";
        // Generate a list of 25 log lines
        List<String> logLines = IntStream.range(0, 25)
                .mapToObj(i -> "log line " + i)
                .collect(Collectors.toList());

        createTestFile(fileName, logLines);
        given().port(port)
                .param("filename", fileName)
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", not(empty()))
                .body("logs", hasSize(20));
    }

    @Test
    public void whenGetLogsWithLastNWithoutKeyword_thenReturnLastNLogs() {
        given().port(port)
                .param("filename", TEST_LOG)
                .param("lastN", 2)
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", hasSize(2))
                .body("logs", contains("error log line 2", "debug log line"));
    }
}
