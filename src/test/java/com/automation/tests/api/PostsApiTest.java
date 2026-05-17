package com.automation.tests.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.api.endpoints.Endpoint;
import com.automation.api.validators.ResponseValidator;
import com.automation.tests.hooks.BaseApiTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class PostsApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke"})
    public void shouldGetPostById() {
        Response response = apiClient.get(Endpoint.POST_BY_ID, "postId", 1);

        ResponseValidator.assertStatusCode(response, 200);
        ResponseValidator.assertJsonSchema(response, "schemas/post-schema.json");
        assertThat(response.jsonPath().getInt("id")).isEqualTo(1);
        assertThat(response.jsonPath().getString("title")).isNotBlank();
    }
}