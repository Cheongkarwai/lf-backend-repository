package com.lfhardware.auth.domain;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Profile{

    private String emailAddress;

    private String phoneNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="addressLine1",
                    column=@Column(name= "address_line_1")),
            @AttributeOverride(name="addressLine2",
                    column=@Column(name="address_line_2"))
    })
    private Address address;
}
