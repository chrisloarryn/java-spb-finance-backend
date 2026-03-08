package accounttransaction.performance;

import java.time.Duration;

final class GatlingSettings {

    private GatlingSettings() {
    }

    static String baseUrl() {
        return System.getProperty("gatling.baseUrl", "http://127.0.0.1:1204");
    }

    static int users() {
        return positiveIntProperty("gatling.users", 1);
    }

    static Duration rampDuration() {
        return Duration.ofSeconds(positiveIntProperty("gatling.rampSeconds", 1));
    }

    static Duration holdDuration() {
        return Duration.ofSeconds(positiveIntProperty("gatling.holdSeconds", 1));
    }

    private static int positiveIntProperty(String propertyName, int defaultValue) {
        return Math.max(1, Integer.getInteger(propertyName, defaultValue));
    }
}
