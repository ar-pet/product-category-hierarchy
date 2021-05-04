package com.mediamarktsaturn.productdomain.payload;

import java.time.LocalDateTime;

public final class ErrorResponse {

    private final String message;
    private final LocalDateTime timeStamp;

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public ErrorResponse(String message, LocalDateTime timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
