package com.lfhardware.core.validation;

import com.lfhardware.core.repository.Search;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KeywordAndAttributeListValidator implements ConstraintValidator<ValidKeywordAndAttributeList, Search> {

    @Override
    public boolean isValid(Search search, ConstraintValidatorContext constraintValidatorContext) {
        if(search != null){
            if(search.getAttributes() != null && !search.getAttributes().isEmpty()){
                return search.getKeyword() != null && !search.getKeyword().isBlank();
            }
        }
        return true;
    }
}
