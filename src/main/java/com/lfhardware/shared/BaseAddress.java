package com.lfhardware.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseAddress {

    @JsonProperty("line_1")
    private String addressLine1;

    @JsonProperty("line_2")
    private String addressLine2;

    private String city;

    private String state;

    private String zipcode;
}
