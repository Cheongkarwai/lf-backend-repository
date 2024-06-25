package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shared.BaseAddress;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO extends BaseAddress {

    private String country;
//    @JsonProperty("address_line_1")
//    @Override
//    public String getAddressLine1(){
//        return super.getAddressLine1();
//    }
//
//    @JsonProperty("address_line_2")
//    @Override
//    public String getAddressLine2(){
//        return super.getAddressLine2();
//    }
}
