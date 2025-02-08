package com.lfhardware.auth.dto;

import com.lfhardware.user.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO extends UserDTO {

//    private String username;
//
//    private String password;
//
//    private Set<RoleDTO> roles = new HashSet<>();
//
//    private UserProfileDTO profile;

    private boolean disabled;

//    @JsonProperty("email_verified")
//    @Override
//    public Boolean isEmailVerified(){
//        return super.emailVerified;
//    }
}
