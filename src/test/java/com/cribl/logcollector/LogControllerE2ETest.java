package com.cribl.logcollector;

import com.cribl.logcollector.testutils.LogTestBase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogControllerE2ETest extends LogTestBase {
    @LocalServerPort
    private int port;

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
                .param("filename", "test.log")
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", not(empty()))
                .body("logs", containsInRelativeOrder(logLines.toArray()));

    }
}
