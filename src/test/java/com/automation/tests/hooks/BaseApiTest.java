package com.automation.tests.hooks;

import com.automation.api.clients.ApiClient;
import org.testng.annotations.BeforeMethod;

public abstract class BaseApiTest {

    protected ApiClient apiClient;

    @BeforeMethod(alwaysRun = true)
    public void setUpApi() {
        apiClient = new ApiClient();
    }
}