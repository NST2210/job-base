package com.gtel.api.utils;

import lombok.Getter;

@Getter
public enum ERROR_CODE {
    INVALID_FORMAT("ER100", "Invalid format"),
    BAD_REQUEST("ER400", "Bad request"),
    UNAUTHORIZED("ER401", "Unauthorized"),
    INTERNAL_SERVER_ERROR("ER500", "Internal error server");

    private final String errorCode;
    private final String message;

    ERROR_CODE(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
