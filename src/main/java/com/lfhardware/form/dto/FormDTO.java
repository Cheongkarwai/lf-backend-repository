package com.lfhardware.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.form.domain.FormConfiguration;

import java.util.Map;

public record FormDTO(Map<String, Object> configuration,
                      @JsonProperty("service_provider_id") String serviceProviderId,
                      @JsonProperty("service_id") String serviceId) {
}
