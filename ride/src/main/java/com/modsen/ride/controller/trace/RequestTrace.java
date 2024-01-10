package com.modsen.ride.controller.trace;

import java.util.Optional;

public class RequestTrace {

    public static final String TRACE_ID_HEADER = "traceId";

    private static final ThreadLocal<String> id = new ThreadLocal<>();

    public static Optional<String> getId() {
        return Optional.ofNullable(id.get());
    }

    public static void removeId() {
        id.remove();
    }

    public static void setId(String traceId) {
        id.set(traceId);
    }
}