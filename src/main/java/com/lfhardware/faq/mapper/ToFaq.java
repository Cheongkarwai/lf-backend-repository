package com.lfhardware.faq.mapper;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "title", source = "title")
@Mapping(target = "description", source = "description")
public @interface ToFaq {
}
