package com.automation.api.clients;

import com.automation.core.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ApiClient {
    private final RequestSpecification requestSpecification;

    public ApiClient() {
        ConfigManager config = ConfigManager.getInstance();
        int timeoutInMillis = config.getInt("api.timeout.seconds") * 1000;

        RestAssuredConfig restAssuredConfig = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeoutInMillis)
                        .setParam("http.socket.timeout", timeoutInMillis));

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(config.getRequired("api.base.url"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(restAssuredConfig)
                .build();
    }

    public Response get(String endpoint) {
        return RestAssured.given(requestSpecification)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response get(String endpoint, Map<String, ?> queryParams) {
        return RestAssured.given(requestSpecification)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response get(String endpoint, String pathParamName, Object pathParamValue) {
        return RestAssured.given(requestSpecification)
                .pathParam(pathParamName, pathParamValue)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response post(String endpoint, Object body) {
        return RestAssured.given(requestSpecification)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response put(String endpoint, Object body) {
        return RestAssured.given(requestSpecification)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response delete(String endpoint) {
        return RestAssured.given(requestSpecification)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}