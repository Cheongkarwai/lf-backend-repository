package com.lfhardware.auth.mapper;

import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.domain.Profile;
import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.dto.UserAccountDTO;
import com.lfhardware.auth.dto.UserRoleDTO;

public class UserMapper{

    public static User map(UserAccountDTO userAccountDTO){
        return User.builder()
                .username(userAccountDTO.getUsername())
                .profile(Profile.builder()
                        .emailAddress(userAccountDTO.getProfileDTO().getEmailAddress())
                        .phoneNumber(userAccountDTO.getProfileDTO().getPhoneNumber())
                        .address(Address.builder().addressLine1(userAccountDTO.getProfileDTO().getAddressDTO().getAddressLine1())
                                .addressLine2(userAccountDTO.getProfileDTO().getAddressDTO().getAddressLine2())
                                .zipcode(userAccountDTO.getProfileDTO().getAddressDTO().getZipcode())
                                .state(userAccountDTO.getProfileDTO().getAddressDTO().getState())
                                .city(userAccountDTO.getProfileDTO().getAddressDTO().getCity())
                                .build())
                        .build())
                .build();
    }


}
