package com.cribl.logcollector;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogControllerE2ETest {
    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void whenGetLogsWithAllParams_thenReturnDummyData() {
        given()
                .port(port)
                .param("filename", "test.log")
                .param("lastN", 10)
                .param("keyword", "error")
                .when()
                .get("/logs")
                .then()
                .statusCode(200)
                .body("logs", not(empty()));
    }
}
