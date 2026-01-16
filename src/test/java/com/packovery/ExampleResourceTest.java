package com.packovery;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ExampleResourceTest {
    @Test
    void testAuthEndpointExists() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"test@example.com\", \"password\":\"password\"}")
                .when().post("/api/auth/login")
                .then()
                .statusCode(403);
    }

}