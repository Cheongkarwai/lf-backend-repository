package com.lfhardware.form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormPageRequest{

    private int pageSize;

    private int page;

    private Sort sort;

    private Search search;

    @JsonProperty("service_provider_id")
    private Long serviceProviderId;

    @JsonProperty("service_id")
    private Long serviceId;
}
