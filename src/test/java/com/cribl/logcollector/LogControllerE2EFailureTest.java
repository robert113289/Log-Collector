package com.cribl.logcollector;

import com.cribl.logcollector.testutils.LogTestBase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogControllerE2EFailureTest extends LogTestBase {
    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void whenGetLogsWithNonExistentFile_thenReturnNotFound() {
        given().port(port)
                .param("filename", "nonexistent.log")
                .when()
                .get("/logs")
                .then()
                .statusCode(404)
                .body(equalTo("Log file not found: nonexistent.log"));
    }

}