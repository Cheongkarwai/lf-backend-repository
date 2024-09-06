package com.lfhardware.core.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceNotFoundException extends CustomException{

    public ServiceNotFoundException(String code, String message) {
        super(code, message);
    }
}
