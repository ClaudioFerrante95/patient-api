package org.cgmtest.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientResourceTest {

    static Integer patientId;

    @Test
    @Order(1)
    void testCreatePatient() {
        patientId =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                    {
                      "firstName": "John",
                      "lastName": "Snow",
                      "email": "john.snow@example.com",
                      "fiscalCode": "JHNSNW90A01H501U",
                      "phoneNumber": "3334567890"
                    }
                """)
                        .when()
                        .post("/patients")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("firstName", equalTo("John"))
                        .extract()
                        .path("id");
    }

    @Test
    @Order(2)
    void testGetPatientById() {
        given()
                .when().get("/patients/{id}", patientId)
                .then()
                .statusCode(200)
                .body("fiscalCode", equalTo("JHNSNW90A01H501U"));
    }

    @Test
    @Order(3)
    void testUpdatePatient() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "firstName": "Giulia",
                  "lastName": "Bianchi",
                  "email": "giulia.bianchi@example.com",
                  "fiscalCode": "BNCGLI90A01H501U",
                  "phoneNumber": "3334567890"
                }
            """)
                .when()
                .put("/patients/{id}", patientId)
                .then()
                .statusCode(200)
                .body("lastName", equalTo("Bianchi"));
    }

    @Test
    @Order(4)
    void testSearchPatientsWithFilter() {
        given()
                .queryParam("firstName", "Giulia")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/patients")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(5)
    void testInvalidEmailValidation() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "firstName": "",
                  "lastName": "Rossi",
                  "email": "not-an-email2",
                  "fiscalCode": "RSSPLA90A01H501U",
                  "phoneNumber": "333123411"
                }
            """)
                .when()
                .post("/patients")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(6)
    void testDeletePatient() {
        given()
                .when()
                .delete("/patients/{id}", patientId)
                .then()
                .statusCode(204);

        // Verifica che non esista più
        given()
                .when().get("/patients/{id}", patientId)
                .then()
                .statusCode(404);
    }
}

