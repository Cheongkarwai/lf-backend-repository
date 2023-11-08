package com.lfhardware.auth.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address{

    private String addressLine1;

    private String addressLine2;

    private String state;

    private String city;

    private String zipcode;

}
