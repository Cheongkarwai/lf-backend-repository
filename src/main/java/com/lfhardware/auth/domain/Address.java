package com.lfhardware.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shared.BaseAddress;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address extends BaseAddress{

}
