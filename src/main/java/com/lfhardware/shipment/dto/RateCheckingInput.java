package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateCheckingInput extends ApiInput{

    private List<BulkDTO> bulk;

    @JsonProperty("exclude_fields")
    private List<String> excludeFields;
}
