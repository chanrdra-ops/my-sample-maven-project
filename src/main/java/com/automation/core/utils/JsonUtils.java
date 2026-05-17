package com.automation.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T readResource(String resourcePath, Class<T> type) {
        try (InputStream inputStream = resourceAsStream(resourcePath)) {
            return OBJECT_MAPPER.readValue(inputStream, type);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read JSON resource: " + resourcePath, exception);
        }
    }

    public static <T> T readResource(String resourcePath, TypeReference<T> typeReference) {
        try (InputStream inputStream = resourceAsStream(resourcePath)) {
            return OBJECT_MAPPER.readValue(inputStream, typeReference);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read JSON resource: " + resourcePath, exception);
        }
    }

    private static InputStream resourceAsStream(String resourcePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return inputStream;
    }
}