package com.lfhardware.core.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private String code;

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }
}
