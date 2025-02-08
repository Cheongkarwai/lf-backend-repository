package com.lfhardware.auth.dto;

import com.lfhardware.user.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO extends UserDTO {

//    @Valid
    protected ProfileDTO profile;
}
