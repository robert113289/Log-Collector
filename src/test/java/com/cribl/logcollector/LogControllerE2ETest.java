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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<String> reversedLogLines = new ArrayList<>(logLines);
        Collections.reverse(reversedLogLines);
        given().port(port)
                .param("filename", TEST_LOG)
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", not(empty()))
                .body("logs", containsInRelativeOrder(reversedLogLines.toArray()));

    }

    @Test
    public void whenGetLogsWithoutNLast_thenReturnDefaultNLastLogs(TestInfo testInfo) throws IOException {
        String fileName = testInfo.getDisplayName() + ".log";
        List<String> logLines = TestData.generateNumberedLines(TestData.defaultLastN + 5);

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
                .body("logs", contains("debug log line", "error log line 2"));
    }

    @Test
    public void whenGetLogsWithKeyword_thenReturnLinesContainingKeyword() {
        given().port(port)
                .param("filename", TEST_LOG)
                .param("keyword", "error")
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", hasSize(2))
                .body("logs", contains("error log line 2", "error log line 1"));
    }

    @Test
    public void whenTooManyKeywordMatches_thenReturnNLinesContainingKeyword(TestInfo testInfo) throws IOException {
        Integer lastN = 5;
        String fileName = testInfo.getDisplayName() + ".log";
        createTestFile(fileName, TestData.generateNumberedLines(20));
        given().port(port)
                .param("filename", fileName)
                .param("lastN", lastN)
                .param("keyword", "1")
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", hasSize(lastN))
                .body("logs", everyItem(containsString("1")));

    }
}
