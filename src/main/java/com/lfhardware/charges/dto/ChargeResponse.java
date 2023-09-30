package com.lfhardware.charges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeResponse {


    @JsonProperty("authorize_uri")
    private String authorizeUri;
}
