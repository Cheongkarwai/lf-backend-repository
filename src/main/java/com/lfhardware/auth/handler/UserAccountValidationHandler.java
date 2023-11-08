package com.lfhardware.auth.handler;

import com.lfhardware.auth.dto.UserAccountDTO;
import com.lfhardware.shared.AbstractValidationHandler;
import jakarta.validation.Validator;

public class UserAccountValidationHandler extends AbstractValidationHandler<UserAccountDTO, Validator> {
    protected UserAccountValidationHandler(Class<UserAccountDTO> clazz, Validator validator) {
        super(clazz, validator);
    }

}
