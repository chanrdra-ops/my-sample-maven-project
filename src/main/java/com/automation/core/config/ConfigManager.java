package com.automation.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final String DEFAULT_ENVIRONMENT = "qa";
    private static final Properties PROPERTIES = new Properties();
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Environment environment;

    private ConfigManager() {

        environment = Environment.from(System.getProperty("env", DEFAULT_ENVIRONMENT));
        loadProperties(environment);
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public String getEnvironmentName() {
        return environment.value();
    }

    public String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    public String getRequired(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required configuration key: " + key);
        }
        return value;
    }

    public int getInt(String key) {
        return Integer.parseInt(getRequired(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getRequired(key));
    }

    private void loadProperties(Environment environment) {
        String path = "config/" + environment.value() + ".properties";
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found: " + path);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration file: " + path, exception);
        }
    }
}