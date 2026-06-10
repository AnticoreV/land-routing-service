package com.ivan.landroutingservice.exceptions;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path
) {
}
