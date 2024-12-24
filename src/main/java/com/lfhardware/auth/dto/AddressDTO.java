package com.lfhardware.auth.dto;

import com.lfhardware.core.dto.BaseAddress;
import lombok.*;

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
