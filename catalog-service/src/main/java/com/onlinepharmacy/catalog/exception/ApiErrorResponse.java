package com.onlinepharmacy.catalog.exception;

import java.time.Instant;

public record ApiErrorResponse(
    int status,
    String error,
    String message,
    Instant timestamp,
    String path
) {}
