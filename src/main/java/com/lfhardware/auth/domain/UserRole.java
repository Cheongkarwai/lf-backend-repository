package com.lfhardware.auth.domain;

import com.lfhardware.shared.CommonConstant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name=CommonConstant.TBL_USER_ROLE)
public class UserRole {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = CommonConstant.COL_USERNAME)
    private User user;

    @ManyToOne
    @JoinColumn(name = CommonConstant.COL_ROLE_ID)
    private Role role;
}
