package com.automation.core.config;

public enum Environment {
    DEV("dev"),
    QA("qa"),
    STAGE("stage");

    private final String value;

    Environment(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Environment from(String value) {
        if (value == null || value.isBlank()) {
            return QA;
        }

        for (Environment environment : values()) {
            if (environment.value.equalsIgnoreCase(value)) {
                return environment;
            }
        }

        throw new IllegalArgumentException("Unsupported environment: " + value);
    }
}