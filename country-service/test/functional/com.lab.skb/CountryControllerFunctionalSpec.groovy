package com.lab.skb

import com.jayway.restassured.http.ContentType
import spock.lang.Specification

import static com.jayway.restassured.RestAssured.given
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.nullValue

class CountryControllerFunctionalSpec extends Specification {

    def setup() {
    }

    void "verify find all countries API version 1"() {
        given: "API version 1"
        def apiVersion = "v1"

        when: "request is sent"
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/country-service/api/$apiVersion/countries")
                .then()
                .statusCode(200)
                .body("result[0].code", nullValue())
                .body("result[0].name", equalTo("Россия"))

        then: "ok then"
        1 == 1
    }

    void "verify find all countries API version 2"() {
        given: "API version 2"
        def apiVersion = "v2"

        when: "request is sent"
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/country-service/api/$apiVersion/countries")
                .then()
                .statusCode(200)
                .body("result[0].code", equalTo("RU"))
                .body("result[0].name", equalTo("Россия"))

        then: "ok then"
        1 == 1
    }
}
