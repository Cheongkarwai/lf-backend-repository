package com.lfhardware.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.form.domain.FormConfiguration;
import com.lfhardware.form.domain.FormValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormInput {

    private Map<String,Object> form;

    @JsonProperty("service_provider_id")
    private Long serviceProviderId;

    @JsonProperty("service_id")
    private Long serviceId;
}
