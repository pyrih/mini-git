package io.github.pyrih.minigit.logger;

enum LogLevel {
    ERROR("\033[31m"),
    WARN("\033[33m"),
    INFO("\033[32m");

    private final String colorCode;

    LogLevel(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return colorCode;
    }
}
