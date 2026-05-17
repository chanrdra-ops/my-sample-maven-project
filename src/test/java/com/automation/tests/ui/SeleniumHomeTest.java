package com.automation.tests.ui;

import static org.assertj.core.api.Assertions.assertThat;

import com.automation.core.config.ConfigManager;
import com.automation.tests.hooks.BaseUiTest;
import com.automation.ui.pages.SeleniumHomePage;
import org.testng.annotations.Test;

public class SeleniumHomeTest extends BaseUiTest {

    @Test(groups = {"ui", "smoke"})
    public void seleniumHomePageShouldLoad() {
        SeleniumHomePage seleniumHomePage = new SeleniumHomePage()
                .open(ConfigManager.getInstance().getRequired("app.url"));

        assertThat(seleniumHomePage.isLoaded()).isTrue();
        assertThat(seleniumHomePage.isDownloadsLinkVisible()).isTrue();
    }
}