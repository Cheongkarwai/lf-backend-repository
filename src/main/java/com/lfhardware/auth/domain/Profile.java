package com.lfhardware.auth.domain;

import com.lfhardware.shared.CommonConstant;
import com.lfhardware.auth.dto.ProfileDTO;
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
                    column=@Column(name= CommonConstant.COL_ADDRESS_LINE_1)),
            @AttributeOverride(name="addressLine2",
                    column=@Column(name=CommonConstant.COL_ADDRESS_LINE_2))
    })
    private Address address;
}
