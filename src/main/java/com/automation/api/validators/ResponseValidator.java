package com.automation.api.validators;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.restassured.response.Response;

public final class ResponseValidator {
    private ResponseValidator() {
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        if (response.statusCode() != expectedStatusCode) {
            throw new AssertionError("Expected status code " + expectedStatusCode + " but was " + response.statusCode());
        }
    }

    public static void assertJsonSchema(Response response, String schemaPath) {
        response.then().body(matchesJsonSchemaInClasspath(schemaPath));
    }
}