package com.lfhardware.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.auth.domain.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Recipient {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressLine1", column = @Column(name = "address_line_1")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "address_line_2")),
    })
    private Address address;
}
