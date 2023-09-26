package com.dblokhin.persons.model;

import java.util.Map;

public record ValidationErrorResponse(
        String message,
        Map<String, String> errors
) implements ErrorWithMessage {
}
