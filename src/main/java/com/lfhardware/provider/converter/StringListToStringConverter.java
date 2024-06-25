package com.lfhardware.provider.converter;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringListToStringConverter implements AttributeConverter<List<String>,String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute.stream().collect(Collectors.joining(","));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return Collections.emptyList();
        }

        return Arrays.asList(dbData.split(","));
    }
}
