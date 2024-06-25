package com.lfhardware.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shared.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormPageRequest extends PageInfo {

    @JsonProperty("service_provider_id")
    private Long serviceProviderId;

    @JsonProperty("service_id")
    private Long serviceId;
}
